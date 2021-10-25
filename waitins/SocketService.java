package logicreat.waitins;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Aaron on 2016-05-28.
 */
public class SocketService extends Service {

    private static final String ACTION="android.intent.action.MAIN";

    private final String SOCKET_ADDR = "159.203.30.14";
    private final int SOCKET_PORT = 8080;
    private final String USERNAME = "username";
    private final String PASSWORD = "password";

    private NotificationManager notificationManager;
    private SharedPreferences preferences;
    private SharedPreferences.Editor prefEditor;
    public static Socket mySocket = null;
    private Scanner in;
    private String curMessage = "";
    private String curType = "";
    private Context context;
    private Intent intent;

    private static final String TAG = "SocketService";
    private MyApp myInstance;
    static int count = 0;

    protected BroadcastReceiver disconnectSocketReceiver;

    public SocketService(){}

    /*@Override
    protected void onHandleIntent(Intent intent){
        try {
            while (true) {
                Log.d(TAG, "On Handle Background");
                Thread.sleep(3000);
            }
        }catch(InterruptedException e){
            Log.d(TAG, "Problem");
        }
    }*/

    @Override
    public void onCreate(){
        Log.d(TAG, "On create");
        this.disconnectSocketReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "On Receive");
                try {
                    if (mySocket != null) {
                        mySocket.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };

        registerReceiver(disconnectSocketReceiver, new IntentFilter(ACTION));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "On start command");
        myInstance = MyApp.getOurInstance();
        this.intent = intent;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // setup
                    context = getApplicationContext();
                    preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    prefEditor = preferences.edit();
                    notificationManager = (NotificationManager)
                            getSystemService(NOTIFICATION_SERVICE);

                    mySocket = new Socket(SOCKET_ADDR, SOCKET_PORT);
                    Log.d(TAG, "Connecting...");
                    in = new Scanner(mySocket.getInputStream());
                    Log.d(TAG, "Got Input Stream");
                    backgroundLogin();
                    startReading();
                }catch(IOException e){
                    Log.d(TAG, "IO Exception");
                    e.printStackTrace();
                }finally {
                    Log.d(TAG, "Finally");
                }
            }
        });
        if (myInstance == null) {
            t.start();
        }
        return START_STICKY;
    }

    public void startReading(){
        try {
            while (myInstance == null && !mySocket.isClosed()) {
                String message = in.nextLine();
                Log.d(TAG, String.format("Message: %s", message));
                curMessage = message.split("&")[1];
                curType = message.split("&")[0];
                handleResponse(message);
            }
        }catch(NoSuchElementException e){
            Log.d(TAG, "NoSuchElementException");
            startReading();
        }
        finally {
            Log.d(TAG, "finally");
        }
    }

    public void handleResponse(String response){
        String[] info = response.split("&");

        if (info[0].equals("ResCall")){
            // the restaurant is calling this user
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.waitins)
                            .setContentTitle("Ticket Info")
                            .setContentText("Your are being called")
                            .setAutoCancel(true);

            // message disappear
            Intent notificationIntent = new Intent(this, MainActivity.class);

            notificationIntent.putExtra("Back", true);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(LoginActivity.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 ,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //mBuilder.setContentIntent(pendingIntent);
            mBuilder.setContentIntent(resultPendingIntent);

            notificationManager.notify(1, mBuilder.build());

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        }
    }

    public void backgroundLogin(){
        try {
            String username = preferences.getString(USERNAME, "");
            String password = preferences.getString(PASSWORD, "");

            Log.d(TAG, String.format("Got username %s and password %s", username, password));
            OutputStream o = mySocket.getOutputStream();
            String message = String.format("User Login %s %s", username, password);
            o.write(message.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            Log.d(TAG, "finally");
        }

    }

    @Override
    public IBinder onBind(Intent intent){
        Log.d(TAG, "on Bind");
        return null;
    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "on Destroy");
        try {
            if (mySocket != null) {
                mySocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            Log.d(TAG, "SocketService Problem");
        }
        super.onDestroy();
    }

}
