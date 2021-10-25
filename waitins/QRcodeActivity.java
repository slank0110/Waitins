package logicreat.waitins;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.toolbox.RequestFuture;
import com.google.zxing.Result;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRcodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    //BottomBar bottomBar;
    String comId;

    Activity activity = this;
    RequestFuture<String> loadInfo;

    private MyApp myInstance = MyApp.getOurInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_qrcode);
        setTitle("QRcode");
        MyApp.setCurActivity(activity);

        myInstance.checkCameraPermission();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(MyApp.width/2,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar, null), params);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        // back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        //mScannerView.setResultHandler(this);
        //mScannerView.startCamera(); //start Camera
    }


    @Override
    protected void onResume(){
        super.onResume();
        MyApp.setCurActivity(this);
        myInstance.checkTicketCall();
        try {
            Thread.sleep(1);
            MyApp.setCurActivity(activity);
            mScannerView.setResultHandler(this);
            mScannerView.startCamera(); //start Camera
            //bottomBar.selectTabAtPosition(3, true);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void handleResult(Result rawResult){
        comId = rawResult.getText();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                String response = myInstance.getResName(comId);
                if (response.equals("@")){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Restaurant not found");
                            builder.setMessage("Please Scan another QRCode");
                            AlertDialog alert1 = builder.create();
                            alert1.show();
                            mScannerView.resumeCameraPreview((ZXingScannerView.ResultHandler) activity);
                        }
                    });
                }else {
                    myInstance.getRestaurant(comId, response, false);
                    Intent intent = new Intent(QRcodeActivity.this, RestaurantActivity.class);
                    intent.putExtra("comId", comId);
                    mScannerView.resumeCameraPreview((ZXingScannerView.ResultHandler) activity);
                    startActivity(intent);
                }
            }
        });
        t.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        //MyApp.setCurActivity(null);
        mScannerView.stopCamera();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
       // bottomBar.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
