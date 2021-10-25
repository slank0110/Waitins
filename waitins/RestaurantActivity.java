package logicreat.waitins;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RestaurantActivity extends AppCompatActivity {

    private MyApp myInstance = MyApp.getOurInstance();

    private final String INFO_SEPARATOR = "\\|";
    private final String LOAD_BACKGROUND_OPERATION = "3";
    private final String LOAD_FULL_OPERATION = "4";
    private final String LOAD_STATUS_OPERATION = "5";

    private final String ADD_FAVOURITE_OPERATION = "1";
    private final String DEL_FAVOURITE_OPERATION = "2";

    // status view
    private TextView tvSmallStatus;
    private TextView tvMediumStatus;
    private TextView tvLargeStatus;

    // info view
    private TextView tvResAddress;
    private TextView tvResAddress2;
    private TextView tvResPhoneNumber;
    private TextView tvHourOperation;
    private TextView tvSmallSize;
    private TextView tvMediumSize;
    private TextView tvLargeSize;
    private TextView tvEstimatedSmall;
    private TextView tvEstimatedMedium;
    private TextView tvEstimatedLarge;
    private ImageView ivFavourite;
    private ImageView ivBackGround;
    private ImageView ivGps;
    private Button button;
    private Dialog d;

    // show view
    private TextView tvTableSize;
    private TextView tvWaitingParty;
    private TextView tvEstimatedTime;
    private TextView tvOptionList;
    private TextView tvAddress;

    // promotion
    private ListView promotionList;

    private Activity activity = this;

    private Restaurant curRes;
    private String comId;
    private String resName;
    private boolean favourite;

    private int small;
    private int medium;
    private int large;

    private boolean guest;

    private Location location;
    private String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_restaurant_card);

        // status info
        tvSmallStatus = (TextView) findViewById(R.id.restaurant_card_waiting_parties_small);
        tvMediumStatus = (TextView) findViewById(R.id.restaurant_card_waiting_parties_medium);
        tvLargeStatus = (TextView) findViewById(R.id.restaurant_card_waiting_parties_large);
        tvEstimatedSmall = (TextView) findViewById(R.id.restaurant_card_estimated_wait_time_small);
        tvEstimatedMedium = (TextView) findViewById(R.id.restaurant_card_estimated_wait_time_medium);
        tvEstimatedLarge = (TextView) findViewById(R.id.restaurant_card_estimated_wait_time_large);

        // info view
        tvResAddress = (TextView) findViewById(R.id.new_res_card_address);
        tvResAddress2 = (TextView) findViewById(R.id.new_res_card_gps_distance);
        tvHourOperation = (TextView) findViewById(R.id.new_res_card_open_hours);
        tvSmallSize = (TextView) findViewById(R.id.restaurant_card_table_size_small);
        tvMediumSize = (TextView) findViewById(R.id.restaurant_card_table_size_medium);
        tvLargeSize = (TextView) findViewById(R.id.restaurant_card_table_size_large);
        ivFavourite = (ImageView) findViewById(R.id.new_res_card_favourite);
        ivBackGround = (ImageView) findViewById(R.id.new_res_card_res_image);
        ivGps = (ImageView) findViewById(R.id.new_res_card_gps);
        promotionList = (ListView) findViewById(R.id.new_res_card_listview);

        // show view
        tvTableSize = (TextView) findViewById(R.id.restaurant_card_table_size);
        tvWaitingParty = (TextView) findViewById(R.id.restaurant_card_waiting_parties);
        tvEstimatedTime = (TextView) findViewById(R.id.restaurant_card_estimated_wait_time);
        //tvOptionList = (TextView) findViewById(R.id.restaurantCardOptionList);
        //tvAddress = (TextView) findViewById(R.id.restaurantCardAddress);

        guest = myInstance.isGuest();

        ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavouriteClick();
            }
        });

        // ticket related
        button = (Button) findViewById(R.id.new_res_card_get_in_line_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInLineOnClick();
            }
        });

        ivGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread loadLatLongThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        double[] temp = myInstance.getResLatLng(curRes.getComId());
                        final Intent intent = new Intent(MyApp.getCurActivity(), MapsActivity.class);
                        intent.putExtra("Lat", temp[0]);
                        intent.putExtra("Lon", temp[1]);
                        intent.putExtra("comId", curRes.getComId());

                        MyApp.getCurActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyApp.getCurActivity().startActivity(intent);
                            }
                        });
                    }
                });
                loadLatLongThread.start();
            }
        });

        button.setText(R.string.restaurant_card_button);
        if (!MyApp.getTicketNum().equals("0")){
            button.setClickable(false);
            button.setBackgroundResource(R.color.colorLightGrey);
        }
        d = new Dialog(RestaurantActivity.this);

        // back navigation
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // used to know which restaurant is selected
        Intent intent = getIntent();
        comId = (String) intent.getSerializableExtra("comId");
        myInstance.setCurRes(comId);
        resName = myInstance.
                getAllResManager().getRestaurant(comId).getName();
        setTitle(resName);
        MyApp.setCurActivity(activity);
        curRes = myInstance.getCurRes();
        setDistance();

        //myInstance.showLoading();

        // operation hour
        tvHourOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setNegativeButton(getString(R.string.ok), null)
                        .setMessage(curRes.getHourOperation())
                        .setTitle(R.string.restaurant_card_hour_of_operation)
                        .create()
                        .show();
            }
        });

        if (!curRes.isFullInfoSet()) {
            Thread loadFullThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final Map<String, String> params = new HashMap<>();
                    params.put("operation", LOAD_BACKGROUND_OPERATION);
                    params.put("comId", comId);

                    final Bitmap imageResponse = myInstance.sendSynchronousImageRequest(MyApp.LOADRES_URL, params);

                    params.put("operation", LOAD_FULL_OPERATION);
                    params.put("comId", comId);

                    String response = myInstance.sendSynchronousStringRequest(MyApp.LOADRES_URL, params);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivBackGround.setImageBitmap(imageResponse);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MyApp.width, (int)(MyApp.RESTAURANTPICTURERATIO * MyApp.width));
                            ivBackGround.setLayoutParams(params);
                            curRes.setBackGroundImage(imageResponse);

                        }
                    });

                    if (!response.equals("@") && !response.equals(MyApp.NETWORK_ERROR)) {
                        final String[] info = response.split(INFO_SEPARATOR);
                        curRes.setFullResInfo(info);
                        final String address = info[0] + " " + info[3];
                        phoneNumber = info[2];

                        String[] hours = curRes.getTodayHour();
                        final String hourOperation = getString(R.string.restaurant_card_open_today_from_text) + hours[0] + " - " + hours[1];

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvResAddress.setText(address);
                                tvHourOperation.setText(hourOperation);
                            }
                        });

                        final String small = getString(R.string.restaurant_card_table_size_small) + (Integer.parseInt(info[4]) - 1) + ")";
                        final String medium = getString(R.string.restaurant_card_table_size_medium) + info[4] + " - " + (Integer.parseInt(info[5]) - 1) + ")";
                        final String large = getString(R.string.restaurant_card_table_size_large) + (Integer.parseInt(info[5]) - 1) + "+)";
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvSmallSize.setText(small);
                                tvMediumSize.setText(medium);
                                tvLargeSize.setText(large);
                            }
                        });

                        setStatus();
                        if (!guest) {
                            setFavourite();
                        }
                    }else{
                        MyApp.getCurActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Failed to load restaurant info",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
            loadFullThread.setPriority(Thread.MAX_PRIORITY);
            loadFullThread.start();

        }else{
            ivBackGround.setImageBitmap(curRes.getBackGroundImage());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MyApp.width, (int)(MyApp.RESTAURANTPICTURERATIO * MyApp.width));
            ivBackGround.setLayoutParams(params);
            tvResAddress.setText(curRes.getAddress() + " " + curRes.getPostalCode());
            phoneNumber = curRes.getPhoneNumber();
            String hourOperation = getString(R.string.restaurant_card_open_today_from_text);
            String[] hours = curRes.getTodayHour();
            hourOperation += hours[0] + " - " + hours[1];
            tvHourOperation.setText(hourOperation);

            String small = getString(R.string.restaurant_card_table_size_small) + "1 - " + (Integer.parseInt(curRes.getTableSize1()) - 1) + ")";
            String medium = getString(R.string.restaurant_card_table_size_medium) + curRes.getTableSize1() + " - " + (Integer.parseInt(curRes.getTableSize2()) - 1) + ")";
            String large = getString(R.string.restaurant_card_table_size_large) + (Integer.parseInt(curRes.getTableSize2()) - 1) + "+)";
            tvSmallSize.setText(small);
            tvMediumSize.setText(medium);
            tvLargeSize.setText(large);
            Thread loadStatus = new Thread(new Runnable() {
                @Override
                public void run() {
                    setStatus();
                }
            });
            loadStatus.start();
            if (!guest) {
                setFavourite();
            }
        }
    }

    // setting the status of this restaurant
    public void setStatus(){

        final Map<String, String> params = new HashMap<>();
        params.put("operation", LOAD_STATUS_OPERATION);
        params.put("comId", comId);

        String response = myInstance.sendSynchronousStringRequest(MyApp.LOADRES_URL, params);

        if (!response.equals("@") && !response.equals(MyApp.NETWORK_ERROR)) {
            final String[] status = response.split(INFO_SEPARATOR);
            if (status[3].equals("0")){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setClickable(false);
                        button.setBackgroundResource(R.color.colorLightGrey);
                        button.setText(R.string.restaurant_card_button_unavailable);
                    }
                });
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvSmallStatus.setText(status[0]);
                    tvMediumStatus.setText(status[1]);
                    tvLargeStatus.setText(status[2]);
                    setEstimatedTime(status);
                }
            });
        }else{
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Failed to load status",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    // setting the favourite symbol
    public void setFavourite(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myInstance.getFavouriteList().contains(comId)){
                    ivFavourite.setImageResource(R.drawable.like_filled_24);
                    favourite = true;
                }else{
                    ivFavourite.setImageResource(R.drawable.like_24);
                    favourite = false;
                }
            }
        });
    }

    // this is what happens when favourite is clicked
    public void onFavouriteClick(){

        // change the restaurant manager in MyApp accordingly
        if (!guest) {
            Thread favouriteThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String response;
                    if (!favourite) {
                        Map<String, String> params = new HashMap<>();
                        params.put("operation", ADD_FAVOURITE_OPERATION);
                        params.put("comId", comId);

                        response = myInstance.sendSynchronousStringRequest(MyApp.LOAD_FAVOURITE_URL, params);

                        if (response.equals(MyApp.NETWORK_ERROR)){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Network problem, please try again later", Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }

                        if (response.equals("$")){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show();
                                    setFavourite();
                                }
                            });

                            myInstance.getFavouriteList().add(0, comId);
                            myInstance.getFavouriteResList().add(0, curRes);
                            myInstance.getAllResManager().addPriority(comId);
                        }else{
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Fail", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    } else {
                        Map<String, String> params = new HashMap<>();
                        params.put("operation", DEL_FAVOURITE_OPERATION);
                        params.put("comId", comId);

                        response = myInstance.sendSynchronousStringRequest(MyApp.LOAD_FAVOURITE_URL, params);

                        if (response.equals(MyApp.NETWORK_ERROR)){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Network problem, please try again later", Toast.LENGTH_LONG).show();;
                                }
                            });
                            return;
                        }

                        if (response.equals("$")){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show();
                                    setFavourite();
                                }
                            });

                            myInstance.getFavouriteList().remove(comId);
                            myInstance.getFavouriteResList().remove(curRes);
                            myInstance.getAllResManager().removePriority(comId);
                        }else{
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Fail", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    if (response.equals("%&!#")){
                        if (myInstance.reLogin()){
                            onFavouriteClick();
                        }else{
                            myInstance.disconnect();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(activity, LoginActivity.class);
                                    intent.putExtra("New", false);
                                    activity.startActivity(intent);
                                    Toast.makeText(activity, "Your session has expire, please reLogin", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                }
            });
            favouriteThread.start();
        }else{
            myInstance.returnToLogin();
        }
    }

    public void getInLineOnClick(){
        if (!guest) {
            d.setContentView(R.layout.rescard_dialog);
            final Button b = (Button) d.findViewById(R.id.dialog_button);
            TextView tv = (TextView) d.findViewById(R.id.dialog_title);
            final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
            np.setMaxValue(30);
            np.setMinValue(1);

            String title = "Number of people";
            tv.setText(title);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread submitThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            b.setClickable(false);
                            submit(np.getValue());
                            b.setClickable(true);
                        }
                    });
                    submitThread.start();
                }
            });
            d.show();
        }else{
            myInstance.returnToLogin();
        }
    }

    public void setEstimatedTime(String[] status){
        small = Integer.parseInt(status[0]);
        medium = Integer.parseInt(status[1]);
        large = Integer.parseInt(status[2]);
        Thread setEstimatedThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (curRes.isEstimatedTimeUpdated()) {
                    // getting estimated time for small

                    Map<String, String> params = new HashMap<>();
                    params.put("numberPeople", "" + (Integer.parseInt(curRes.getTableSize1()) - 1));
                    params.put("comId", comId);

                    String response = myInstance.sendSynchronousStringRequest(MyApp.LOAD_ESTIMATED_URL, params);

                    if (response.equals("$") && !response.equals(MyApp.NETWORK_ERROR)) {
                        small = -1;
                        curRes.setSmall(small);
                    } else if (response.equals("@")) {
                        small = -1;
                        curRes.setSmall(small);
                    } else {
                        curRes.setSmall(Integer.parseInt(response));
                        small = Integer.parseInt(response) * small;
                    }

                    params = new HashMap<>();
                    params.put("numberPeople", "" + (Integer.parseInt(curRes.getTableSize1())));
                    params.put("comId", comId);

                    response = myInstance.sendSynchronousStringRequest(MyApp.LOAD_ESTIMATED_URL, params);

                    if (response.equals("$") && !response.equals(MyApp.NETWORK_ERROR)) {
                        medium = -1;
                        curRes.setMedium(medium);
                    } else if (response.equals("@")) {
                        medium = -1;
                        curRes.setMedium(medium);
                    } else {
                        curRes.setMedium(Integer.parseInt(response));
                        medium = Integer.parseInt(response) * medium;
                    }

                    params = new HashMap<>();
                    params.put("numberPeople", "" + (Integer.parseInt(curRes.getTableSize2())));
                    params.put("comId", comId);

                    response = myInstance.sendSynchronousStringRequest(MyApp.LOAD_ESTIMATED_URL, params);

                    if (response.equals("$") && !response.equals(MyApp.NETWORK_ERROR)) {
                        large = -1;
                        curRes.setLarge(large);
                    } else if (response.equals("@")) {
                        large = -1;
                        curRes.setLarge(large);
                    } else {
                        curRes.setLarge(Integer.parseInt(response));
                        large = Integer.parseInt(response) * large;
                    }
                    curRes.setLastEstimatedUpdate(System.currentTimeMillis());
                }else{
                    if (curRes.getSmall() == -1){
                        small = -1;
                    }else {
                        small = curRes.getSmall() * small;
                    }

                    if (curRes.getMedium() == -1){
                        medium = -1;
                    }else {
                        medium = curRes.getMedium() * medium;
                    }

                    if (curRes.getLarge() == -1) {
                        large = -1;
                    }else {
                        large = curRes.getLarge() * large;
                    }

                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (small <= -1) {
                            tvEstimatedSmall.setText("N/A");
                        }else {
                            tvEstimatedSmall.setText("" + small);
                        }

                        if (medium <= -1) {
                            tvEstimatedMedium.setText("N/A");
                        }else {
                            tvEstimatedMedium.setText("" + medium);
                        }

                        if (large <= -1){
                            tvEstimatedLarge.setText("N/A");
                        }else {
                            tvEstimatedLarge.setText("" + large);
                        }

                        setPromotionList();
                    }
                });
            }
        });
        setEstimatedThread.start();
    }

    public void setPromotionList(){
        Thread setPromotionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Promotion> tempList = new ArrayList<>();
                Map<String, String> params = new HashMap<>();
                params.put("operation", "11");
                params.put("comId", comId);

                String response = myInstance.sendSynchronousStringRequest(MyApp.LOADRES_URL, params);

                if (!response.equals("$") && !response.equals(MyApp.NETWORK_ERROR)){
                    String[] promotions = response.split("\\|");
                    for (int i = 0; i < promotions.length; i++){
                        String[] info = promotions[i].split("&");
                        if (myInstance.getPromotionManager().ifExist(info[0])){
                            tempList.add(myInstance.getPromotionManager().getPromotion(info[0]));
                        }else{
                            Promotion newPromotion = new Promotion(info[0], info[1], info[2],
                                    Integer.parseInt(info[3]), Integer.parseInt(info[4]),
                                     info[6], info[7], info[8]);
                            newPromotion.setNumberSold(Integer.parseInt(info[5]));
                            myInstance.getPromotionManager().addPromotion(newPromotion);
                            tempList.add(newPromotion);
                        }
                    }
                }
                final ResPromotionAdapter resPromotionAdapter = new ResPromotionAdapter(activity, tempList);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        promotionList.setAdapter(resPromotionAdapter);
                        setListViewHeightBasedOnChildren(promotionList);
                        setVisible();
                        promotionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String promotionId = tempList.get(i).getPromotionId();
                                Intent intent = new Intent(MyApp.getCurActivity(), NewPlateActivity.class);
                                intent.putExtra("promotionId", promotionId);
                                MyApp.getCurActivity().startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
        setPromotionThread.start();
    }

    public void setDistance(){
        location = null;

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        try{
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }catch(SecurityException e){
            e.printStackTrace();
        }

        if (location == null){
            tvResAddress2.setText(phoneNumber + " N/A");
        }else {
            Thread loadDistance = new Thread(new Runnable() {
                @Override
                public void run() {

                    Map<String, String> params = new HashMap<>();
                    params.put("operation", "8");
                    params.put("comId", comId);
                    params.put("longitude", "" + location.getLongitude());
                    params.put("latitude", "" + location.getLatitude());

                    final String message = myInstance.sendSynchronousStringRequest(MyApp.LOADRES_URL, params);

                    if (message.equals(MyApp.NETWORK_ERROR) || message.equals("$") || message.equals("@")){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvResAddress2.setText(phoneNumber + " " + "-");
                            }
                        });
                    }
                    else if (message.equals("@")){
                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvResAddress2.setText(phoneNumber + " " + message);
                            }
                        });
                    }
                }
            });
            loadDistance.start();
        }
    }

    // submit on click
    public void submit(int numPeople){

        final int numPpl = numPeople;

        myInstance.writeSocket(String.
                format("User Submit %s %s",
                        curRes.getComId(), ""+numPpl));
        //String message = myInstance.readSocket()[1];
        String curType = myInstance.getCurType();

        while(!curType.equals("UserSubmit") && !curType.equals(MyApp.SOCKET_NETWORK_ERROR)){
            curType = myInstance.getCurType();
        }

        if (curType.equals(MyApp.SOCKET_NETWORK_ERROR)) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Network Error, Please try again later", Toast.LENGTH_LONG).show();
                    myInstance.resetCurType();
                }
            });
            return;
        }

        myInstance.resetCurType();

        String message = myInstance.getCurMessage();

        if (message.equals("@")){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Fail to submit Ticket", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            String[] messageInfo = message.split("\\|");
            String[] info = String.format("%s|%s|%s|%s|%s",
                    curRes.getComId(),
                    messageInfo[0], messageInfo[1],
                    System.currentTimeMillis(), numPpl).split("\\|");
            myInstance.setTicketInfo(info);
            d.dismiss();
            Intent intent = new Intent(activity, MainActivity.class);
            MainActivity.intent.putExtra("goTo", 2);
            startActivity(intent);
        }
    }

    public void setVisible(){
        findViewById(R.id.restaurant_card_progress_bar).setVisibility(View.GONE);
        findViewById(R.id.activity_temp_restaurant).setVisibility(View.VISIBLE);
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

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        // calculating the total height by adding the height of every view inside listview
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                // setting up for measure
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        // change the height of list view
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
