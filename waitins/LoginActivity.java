package logicreat.waitins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.RequestFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LoginActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;

    TextView tvRegister;
    Button button;
    TextView forgotButton;

    String username;
    String password;
    boolean newFrom;
    boolean fromBackground;
    String response;

    private MyApp myInstance;

    Activity activity = this;
    //RequestFuture<String> loginFuture;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login);
        setTitle("Login");
        usernameField = (EditText) findViewById(R.id.etUsername);
        passwordField = (EditText) findViewById(R.id.etPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        button = (Button) findViewById(R.id.bSignIn);
        forgotButton = (TextView) findViewById(R.id.tvForgotPassword);
        //checkBoxField = (CheckBox) findViewById(R.id.login_checkBox);
        myInstance = MyApp.getOurInstance(this.getApplicationContext());
        MyApp.setCurActivity(activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        newFrom = intent.getBooleanExtra("New", true);
        fromBackground = intent.getBooleanExtra("Back", false);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        MyApp.width = size.x;
        MyApp.height = size.y;

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ForgotPasswordActivity.class);
                activity.startActivity(intent);
            }
        });

        setLogin();
    }

    public void register(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // Provides the logIn function
    public void logIn(final View view) {
        tvRegister.setClickable(false);
        button.setClickable(false);
        username = usernameField.getText().toString();
        if (myInstance.getPassword().equals("")) {
            password = passwordField.getText().toString();
        }else{
            password = myInstance.getPassword();
        }

        Thread requestThread = new Thread(new Runnable() {
            @Override
            public void run() {
                myInstance.setSocket();
                myInstance.writeSocket(String.
                        format("User Login %s %s", username, password));

                String curType = myInstance.getCurType();

                while ((!curType.equals("UserLogin") || !MyApp.finishedDiscoveryLoading) && !curType.equals(MyApp.SOCKET_NETWORK_ERROR)){
                    curType = myInstance.getCurType();
                }

                if (curType.equals(MyApp.SOCKET_NETWORK_ERROR)){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, getString(R.string.toast_network_error_try_again), Toast.LENGTH_LONG).show();
                        }
                    });
                    button.setClickable(true);
                    tvRegister.setClickable(true);
                    myInstance.resetCurType();
                    return;
                }

                myInstance.resetCurType();

                String socketResponse = myInstance.getCurMessage();

                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                response = myInstance.sendSynchronousStringRequest(myInstance.LOGIN_URL, params);

                if (response.equals(MyApp.NETWORK_ERROR)){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, getString(R.string.toast_network_error_try_again), Toast.LENGTH_LONG).show();
                        }
                    });
                    button.setClickable(true);
                    tvRegister.setClickable(true);
                    myInstance.resetCurType();
                    return;
                }

                if (response.equals("@") || socketResponse.equals("@")){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(getString(R.string.dialog_invalid_username_password))
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                    });
                }else if (socketResponse.equals("!")){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,
                                    getString(R.string.toast_logged_in_another_place), Toast.LENGTH_LONG).show();

                            // a alert dialog asking user whether or not to force login
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(getString(R.string.toast_force_login_text))
                                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                        // what happens if the user clicked yes
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Thread t = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    myInstance.writeSocket(String.format("User ForceLogin %s %s", username, password));
                                                    String curType = myInstance.getCurType();

                                                    while (!curType.equals("UserLogin")){
                                                        curType = myInstance.getCurType();
                                                    }

                                                    myInstance.resetCurType();
                                                    String socketResponse = myInstance.getCurMessage();
                                                    if (socketResponse.equals("@")){
                                                    }else{
                                                        loadLoginInfo();
                                                    }
                                                }
                                            });
                                            t.start();
                                        }
                                    })
                                    .setNegativeButton(getString(R.string.cancel), null)
                                    .create()
                                    .show();
                        }
                    });
                }else if (socketResponse.equals("#")){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,
                                    getString(R.string.toast_multiple_login_same_device), Toast.LENGTH_LONG).show();
                        }
                    });
                } else{
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, getString(R.string.login_success), Toast.LENGTH_LONG).show();
                        }
                    });

                    loadLoginInfo();
                }
                button.setClickable(true);
                tvRegister.setClickable(true);
            }
        });
        if (myInstance.checkNetworkConnection()) {
            requestThread.start();
        }else{
            button.setClickable(true);
            tvRegister.setClickable(true);
        }
    }

    public void setLogin(){
        if (!myInstance.getUsername().equals("")) {
            usernameField.setText(myInstance.getUsername());

            if (myInstance.isRememberClick()) {
                if (!myInstance.getPassword().equals("")) {
                    passwordField.setText(myInstance.getPassword());
                }
                //checkBoxField.setChecked(true);
            } else {
                //checkBoxField.setChecked(false);
            }

            if (newFrom && !(myInstance.getPassword().equals(""))){
                logIn(findViewById(R.id.bSignIn));
            }else{
                findViewById(R.id.activity_login).setVisibility(View.VISIBLE);
            }
        }
    }

    public void loadLoginInfo(){
        //myInstance.loadHistoryList();
        myInstance.setUsername(username);
        myInstance.setPassword(password);

        myInstance.setRememberClick(true);
        //myInstance.setRememberClick(checkBoxField.isChecked());
        myInstance.setEmail(response.split("\\|")[1]);
        myInstance.setGuest(false);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (MainActivity.intent != null) {
            MainActivity.intent.putExtra("goTo", 0);
        }
//        MeTab.loginSwitch();
        startActivity(intent);
        finish();
        myInstance.loadFavouriteList();
        myInstance.loadCouponList();
        myInstance.loadCouponHistoryList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        MyApp.setCurActivity(activity);

    }
}
