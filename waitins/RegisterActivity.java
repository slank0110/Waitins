package logicreat.waitins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aaron on 2016-04-24.
 */
public class RegisterActivity extends AppCompatActivity {

    private MyApp myInstance = MyApp.getOurInstance();

    Activity activity = this;

    Button registerButton;

    EditText usernameField;
    EditText passwordField;
    EditText passwordRetypeField;
    EditText emailField;
    EditText emailRetypeField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usernameField = (EditText) findViewById(R.id.etRegisterUsername);
        passwordField = (EditText) findViewById(R.id.etRegisterPassword);
        passwordRetypeField = (EditText) findViewById(R.id.etRegisterPasswordRetype);
        emailField = (EditText) findViewById(R.id.etRegisterEmail);
        emailRetypeField = (EditText) findViewById(R.id.etRegisterEmailRetype);

        registerButton = (Button) findViewById(R.id.bConfirmRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setClickable(false);
                register();
            }
        });
    }

    public void register(){
        Thread registerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                String password2 = passwordRetypeField.getText().toString();
                String email = emailField.getText().toString();
                String email2 = emailRetypeField.getText().toString();

                if (email.equals(email2) && password.equals(password2)) {


                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);
                    params.put("email", email);
                    String passcode = username + username;
                    passcode = new StringBuilder(passcode).reverse().toString();
                    passcode = passcode + "HeLlo";
                    params.put("passcode", passcode);

                    String response = myInstance.sendSynchronousStringRequest(MyApp.REGISTER_URL, params);
                    String message;

                    if (response.equals(MyApp.NETWORK_ERROR)){
                        message = "Network error";
                    }else if (response.equals("@1")){
                        message = "Invalid Email";
                    }else if (response.equals("@2")){
                        message = "Password Too Long";
                    }else if (response.equals("@3")){
                        message = "Invalid Username";
                    }else if (response.equals("@4")) {
                        message = "Invalid Username";
                    }else if (response.equals("@5")) {
                        message = "Character in username";
                    }else if (response.equals("@6")) {
                        message = "Empty password or username";
                    }else if (response.equals("@U")) {
                        message = "Username used";
                    }else if (response.equals("@E")) {
                        message = "Email used";
                    }else if (response.equals("@M")) {
                        message = "Mysql Error";
                    }else{
                        message = "Register Success";
                    }

                    final String toastMessage = message;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, toastMessage, Toast.LENGTH_LONG).show();
                            if (toastMessage.equals("Register Success")){
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                registerButton.setClickable(true);
                            }
                        }
                    });
                }else{
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Invalid retype, please try again", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                registerButton.setClickable(true);
            }
        });
        registerThread.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
