package logicreat.waitins;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(MyApp.width/2,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar, null), params);
        actionBar.setDisplayHomeAsUpEnabled(true);

        MyApp.setCurActivity(this);

        email = (EditText) findViewById(R.id.forgot_email);
        submitButton = (Button) findViewById(R.id.forgot_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread sendRequestThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String emailString = email.getText().toString();
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", emailString);

                        String passcode = new StringBuilder(emailString).reverse().toString();
                        passcode = passcode + passcode;
                        passcode = passcode + "BYe";

                        params.put("passcode", passcode);

                        String response = MyApp.getOurInstance().sendSynchronousStringRequest(MyApp.FORGOT_URL, params);
                        final String message;

                        if (response.equals(MyApp.NETWORK_ERROR)){
                            message = "Network error, please check your network";
                        }
                        else if (response.equals("&")){
                            message = "Email not found";
                        }
                        else if (response.equals("@")){
                            message = "Server error try again";
                        }else{
                            message = "Success, please check your email";
                        }

                        MyApp.getCurActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyApp.getCurActivity(), message, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                sendRequestThread.start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
