package logicreat.waitins;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aaron on 2016-05-16.
 */
public class CustomTicketDialog extends Dialog
        implements android.view.View.OnClickListener {

    private MyApp myInstance = MyApp.getOurInstance();

    private long startTime;
    private long interval;

    private Activity context;
    private Dialog dialog = this;
    private TextView tvTime, tvInfo;
    private Button bAccept, bReject, bDelay;
    private DialogCountDownTimer countDownTimer;
    private String resInfo;

    String curType;
    String socketResponse;

    public CustomTicketDialog(Activity context, String resInfo, int timeLeft){
        super(context);
        this.context = context;
        this.resInfo = resInfo;
        this.startTime = timeLeft * 1000;
        this.interval = 1 * 1000;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ticket_dialog);
        bAccept = (Button) findViewById(R.id.ticket_dialog_accept);
        bReject = (Button) findViewById(R.id.ticket_dialog_reject);
        bDelay = (Button) findViewById(R.id.ticket_dialog_delay);
        tvTime = (TextView) findViewById(R.id.ticket_dialog_time);
        tvInfo = (TextView) findViewById(R.id.ticket_dialog_text);

        tvInfo.setText(resInfo);
        countDownTimer = new DialogCountDownTimer(startTime, interval);
        countDownTimer.start();

        bAccept.setOnClickListener(this);
        bReject.setOnClickListener(this);
        bDelay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        MyApp.setDidResponse(true);
        switch(v.getId()){
            case R.id.ticket_dialog_accept:
                myInstance.writeSocket("User React Accept");
                curType = myInstance.getCurType();

                while (!curType.equals("UserReact") && !curType.equals(MyApp.SOCKET_NETWORK_ERROR)){
                    curType = myInstance.getCurType();
                }

                if (curType.equals(MyApp.SOCKET_NETWORK_ERROR)){
                    MyApp.getCurActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApp.getCurActivity(), "Network problem", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dismiss();
                    return;
                }

                socketResponse = myInstance.getCurMessage();
                if (socketResponse.equals("$")){
                    MyApp.setDidAccept(true);
                    MainActivity.intent.putExtra("goTo", 2);
                    InLineTab.setupInLineInfo();
                    Intent intent = new Intent(MyApp.getCurActivity(), MainActivity.class);
                    MyApp.getCurActivity().startActivity(intent);
                }else{
                    Toast.makeText(MyApp.getCurActivity(),
                            "Accept Failed", Toast.LENGTH_LONG).show();
                }
                dismiss();

                break;
            case R.id.ticket_dialog_reject:
                myInstance.writeSocket("User React Reject");
                curType = myInstance.getCurType();

                while (!curType.equals("UserReact") && !curType.equals(MyApp.SOCKET_NETWORK_ERROR)){
                    curType = myInstance.getCurType();
                }

                if (curType.equals(MyApp.SOCKET_NETWORK_ERROR)){
                    MyApp.getCurActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApp.getCurActivity(), "Network problem", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dismiss();
                    return;
                }

                socketResponse = myInstance.getCurMessage();
                if (socketResponse.equals("$")){
                    //Toast.makeText(MyApp.getCurActivity(),
                    //        "Reject Success", Toaawwst.LENGTH_LONG).show();
                    MyApp.setTicketNum("0");
                    InLineTab.setupInLineInfo();
                }else{
                    //Toast.makeText(MyApp.getCurActivity(),
                    //        "Reject Faled", Toast.LENGTH_LONG).show();
                }
                dismiss();
                break;
            case R.id.ticket_dialog_delay:
                myInstance.writeSocket("User React Delay");
                curType = myInstance.getCurType();

                while (!curType.equals("UserReact") && !curType.equals(MyApp.SOCKET_NETWORK_ERROR)){
                    curType = myInstance.getCurType();
                }

                if (curType.equals(MyApp.SOCKET_NETWORK_ERROR)){
                    MyApp.getCurActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApp.getCurActivity(), "Network problem", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dismiss();
                    return;
                }

                socketResponse = myInstance.getCurMessage();
                if (socketResponse.equals("$")){
                    //Toast.makeText(MyApp.getCurActivity(),
                    //        "Delay Success", Toast.LENGTH_LONG).show();
                }else{
                    //Toast.makeText(MyApp.getCurActivity(),
                    //        "Delay Fail", Toast.LENGTH_LONG).show();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public class DialogCountDownTimer extends CountDownTimer{
        public DialogCountDownTimer(long startTime, long interval){
            super(startTime, interval);
        }

        @Override
        public void onFinish(){
            try {
                MyApp.setDidResponse(true);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }catch(IllegalArgumentException e){
                e.printStackTrace();
            }finally {
            }
        }

        @Override
        public void onTick(long millisUntilFinished){
            //Log.d(TAG, "dialog is showing ");

            //Log.d(TAG, "parent activity null: " + context.isFinishing());
            //Log.d(TAG, "Activity different: " + (context.equals(MyApp.getCurActivity())));

            if (dialog != null && (!dialog.isShowing() || !context.equals(MyApp.getCurActivity()))) {
                this.cancel();
                try {
                    dialog.dismiss();
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
                //onFinish();
            }
            tvTime.setText(MyApp.getCurActivity().getString(R.string.time_remaining) + millisUntilFinished/1000);
        }
    }
}
