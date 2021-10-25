package logicreat.waitins;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.RequestFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SearchableActivity extends AppCompatActivity {

    private MyApp myInstance;

    final String RES_SEPARATOR = "\\|";

    private ArrayList<String> itemName;
    private ArrayList<String> comIdList;
    private ArrayList<String[]> statusList;
    private ArrayList<String> distanceList;
    private ArrayList<Bitmap> imgBitmap;

    private ArrayList<String> searchList;

    private ListView list;
    private String response;
    private ProgressBar footer;

    // used for refresh search
    private static final int LOAD_ONCE = 5;
    private SearchListAdapter adapter;
    private String[] restaurants;
    private int curPosition;
    private boolean finish = true;
    private int preLast;

    private ResManager allResManager;

    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        myInstance = MyApp.getOurInstance(this.getApplicationContext());
        allResManager = myInstance.getAllResManager();

        MyApp.setCurActivity(activity);

        // title bar in the center
        // setting up the title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(MyApp.width/2,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar, null), params);

        actionBar.setDisplayHomeAsUpEnabled(true);

        footer = new ProgressBar(this);

        list = (ListView) findViewById(R.id.search_list);
        list.addFooterView(footer);
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE){
                }
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL){
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, final int totalItemCount) {
                if (view.getId() == R.id.search_list){
                    final int lastItem = firstVisibleItem + visibleItemCount;
                    if (lastItem == totalItemCount){
                        if (preLast != lastItem && finish) {
                            preLast = lastItem;
                            Thread loadSearch = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    finish = false;
                                    loadSearch(curPosition);
                                    finish = true;

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (restaurants != null && totalItemCount >= restaurants.length) {
                                                //footer.setVisibility(View.INVISIBLE);
                                                list.removeFooterView(footer);
                                            }
                                        }
                                    });
                                }
                            });
                            loadSearch.start();
                        }
                    }
                }
            }
        });

        Intent intent = getIntent();

        String query = intent.getStringExtra("query");
        if (query != null){
            search(query);
        }
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

    public void search(final String query){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // reseting data for a new search
                searchList = new ArrayList<>();

                itemName = new ArrayList<>();
                comIdList = new ArrayList<>();
                imgBitmap = new ArrayList<>();
                statusList = new ArrayList<>();
                distanceList = new ArrayList<>();

                curPosition = 0;
                restaurants = null;
                adapter = null;

                Map<String, String> params = new HashMap<>();
                params.put("key", query);

                response = myInstance.sendSynchronousStringRequest(MyApp.SEARCH_URL, params);

                if (!response.equals("$") && !response.equals(MyApp.NETWORK_ERROR)) {
                    restaurants = response.split(RES_SEPARATOR);
                    loadSearch(curPosition);
                }

                // setting up the list adapter in the UI Thread
                list.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new SearchListAdapter(activity, comIdList, itemName, imgBitmap, statusList, distanceList);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                                String comId = searchList.get(position);
                                Intent intent = new Intent(SearchableActivity.this, RestaurantActivity.class);
                                intent.putExtra("comId", comId);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
        t.start();
    }

    public void loadSearch(int start){

        if (!(restaurants == null)) {
            int end;
            if ((LOAD_ONCE + start) > restaurants.length) {
                end = restaurants.length;
            } else {
                end = LOAD_ONCE + start;
            }

            // inner loop variable
            String[] info;
            String resName;
            String comId;
            String status[];
            String distance;
            Bitmap image;

            for (int i = start; i < end; i++) {
                info = restaurants[i].split("&");
                comId = info[0];
                resName = info[1];
                searchList.add(comId);
                curPosition++;

                status = myInstance.getResStatus(comId);
                //distance = myInstance.getResDistance(comId);
                distance = null;

                //int indexOfRes = allResManager.ifExist(comId);
                boolean exist = allResManager.ifExist(comId);

                // -1 means does not exist
                if (!exist) {
                    image = myInstance.getRestaurant(comId, resName, false).getImage();
                } else {
                    Restaurant temp = allResManager.getRestaurant(comId);
                    image = temp.getImage();
                }

                comIdList.add(comId);
                itemName.add(resName);
                statusList.add(status);
                distanceList.add(distance);
                imgBitmap.add(image);

                /*if (adapter != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "notifying change");
                            adapter.notifyDataSetChanged();
                        }
                    });
                }*/
            }
            if (adapter != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        preLast = -1;

    }
}
