package logicreat.waitins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyHistoryActivity extends AppCompatActivity {

    private MyApp myInstance = MyApp.getOurInstance();

    private Activity activity = this;

    private ListView list;

    private ArrayList<History> historyList;

    //BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
        setTitle("History");

        // title bar in the center
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(MyApp.width/2,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar, null), params);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //myInstance.testSocket();
        MyApp.setCurActivity(activity);
        //myInstance.showLoading();

        list = (ListView) findViewById(R.id.my_history_history);

        // creates a bottom bar for navigation

        Thread historyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                myInstance.loadHistoryList();

                historyList = myInstance.getHistoryList();

                list.post(new Runnable() {
                    @Override
                    public void run() {
                        HistoryListAdapter adapter = new HistoryListAdapter(activity, historyList);
                        list.setAdapter(adapter);
                        //myInstance.hideLoading();
                        //findViewById(R.id.my_history_progress_bar).setVisibility(View.GONE);
                        setVisible();
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                                String comId = historyList.get(position).getComId();

                                Intent intent = new Intent(MyHistoryActivity.this, RestaurantActivity.class);
                                intent.putExtra("comId", comId);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });

        historyThread.start();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        //bottomBar.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume(){
        super.onResume();
        MyApp.setCurActivity(activity);
        //bottomBar.selectTabAtPosition(3, true);
    }

    @Override
    protected void onPause(){
        super.onPause();
        MyApp.setCurActivity(null);
    }

    public void setVisible(){
        findViewById(R.id.my_history_progress_bar).setVisibility(View.GONE);
    }

}
