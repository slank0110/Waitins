package logicreat.waitins;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.renderscript.RenderScript;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.google.android.gms.maps.MapsInitializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MainViewPagerAdapter adapter;
    private Fragment curFragment;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();

    private static AHBottomNavigationViewPager viewPager;
    private static AHBottomNavigation bottomNavigation;

    private static MyApp myInstance;

    private Activity activity = this;

    private String password;
    private String username;

    private Menu actionMenu;
    private MenuInflater inflater;
    private SearchView search;

    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SocketService.mySocket != null){
            try {
                SocketService.mySocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        myInstance = MyApp.getOurInstance(getApplicationContext());
        MyApp.setCurActivity(this);

        // setting up the screen resolution
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        MyApp.width = size.x;
        MyApp.height = size.y;

        intent = getIntent();
        if (!MyApp.getOurInstance().getPrefLanguage().equals("")){
            if (!(getResources().getConfiguration().locale.toString().equals(MyApp.getOurInstance().getPrefLanguage()))){
                MyApp.getOurInstance().refreshLocale(MyApp.getOurInstance().getPrefLanguage());
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return;
            }
        }

        // setting up the title
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(MyApp.width/2,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar, null), params);

        actionBar.hide();

        MyApp.actionBarHeight = (int) getApplicationContext().getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize }).getDimension(0, 0);
        MyApp.gridHeight = (int) (MyApp.height - (MyApp.height * 0.09f + MyApp.actionBarHeight));

        /*if (!myInstance.getPrefLanguage().equals("")) {
            Locale current = getResources().getConfiguration().locale;
            Log.d(TAG, "Got pref " + myInstance.getPrefLanguage() + " got current " + current.toString());
            if (!current.toString().equals(myInstance.getPrefLanguage())){
                myInstance.refreshLocale(myInstance.getPrefLanguage());
                finish();
                startActivity(getIntent());
            }
        }*/

        // setting up the tab navigation
        Thread loadInfoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (myInstance.isGuest()) {
                    logIn();
                }
                if (!myInstance.checkNetworkConnection()) {
                    myInstance.loadDatabaseCouponHistoryInfo();
                    myInstance.loadDatabaseCouponInfo();
                }
                myInstance.loadDiscoveryList();
                myInstance.loadPromotion();
                myInstance.checkGpsPermission();
                MyApp.getCurActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setup();
                        actionBar.show();
                    }
                });
            }
        });
        loadInfoThread.start();
        loadInfoThread.setPriority(Thread.MAX_PRIORITY);
    }

    private void setup(){
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.main_bottom_navigation);
        viewPager = (AHBottomNavigationViewPager) findViewById(R.id.main_view_pager);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.discovery_tab_bar,
                R.drawable.toolbar_icons_home, R.color.colorPrimaryYellow);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.promotion_tab_bar,
                R.drawable.toolbar_icons_promotion, R.color.colorPrimaryYellow);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.inLine_tab_bar,
                R.drawable.toolbar_icons_inline, R.color.colorPrimaryYellow);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.myprofile_tab_bar,
                R.drawable.toolbar_icons_me, R.color.colorPrimaryYellow);

        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);
        bottomNavigationItems.add(item4);

        bottomNavigation.setForceTitlesDisplay(true);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#3D3D3D"));
        //bottomNavigation.setBackgroundColor(Color.parseColor("#1D1C19"));
        bottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.colorLightGrey));
        bottomNavigation.setInactiveColor(Color.parseColor("#FDD007"));

        bottomNavigation.addItems(bottomNavigationItems);
        bottomNavigation.setTitleTextSize(25, 25);

        viewPager.setOffscreenPageLimit(4);
        myInstance.setFragmentManager(getSupportFragmentManager());
        adapter = new MainViewPagerAdapter(myInstance.getFragmentManager());
        viewPager.setAdapter(adapter);

        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
            }
        });

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {


                curFragment =  adapter.getCurFragment();

                if (wasSelected) {
                    return true;
                }

                if (curFragment != null){
                }

                viewPager.setCurrentItem(position, false);

                if (position == 0){
                    actionMenu.clear();
                    inflater.inflate(R.menu.qr_map_hide_action_bar, actionMenu);
                    setupSearch();
                    HomeTab.updateHome();
                }else if (position == 1){
                    actionMenu.clear();
                    inflater.inflate(R.menu.qr_map_hide_action_bar, actionMenu);
                    setupSearch();
                    PromotionTab.updatePromotion();
                } else if (position == 2){
                    actionMenu.clear();
                    inflater.inflate(R.menu.inline_action_bar, actionMenu);
                    InLineTab.setupInLineInfo();
                }else if (position == 3){
                    actionMenu.clear();
                    inflater.inflate(R.menu.my_history_action_bar, actionMenu);
                    if (myInstance.isGuest()) {
                        actionMenu.findItem(R.id.my_history_log_out).setTitle(getString(R.string.login_text));
                    }else{
                        actionMenu.findItem(R.id.my_history_log_out).setTitle(getString(R.string.logout_text));
                    }
                    MeTab.updateMe();
                }

                //bottomNavigation.setNotification("", position);

                return true;
            }
        });
        bottomNavigation.setVisibility(View.VISIBLE);
    }

    public void logIn(){
        password = myInstance.getPassword();
        username = myInstance.getUsername();


        if (!password.equals("") && !username.equals("")){
            myInstance.setSocket();
            myInstance.writeSocket(String.format("User Login %s %s", username, password));

            String curType = myInstance.getCurType();

            while (!curType.equals("UserLogin") && !curType.equals(MyApp.SOCKET_NETWORK_ERROR)){
                curType = myInstance.getCurType();
            }

            if (curType.equals(MyApp.SOCKET_NETWORK_ERROR)) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, getString(R.string.toast_network_error_login), Toast.LENGTH_LONG).show();
                    }
                });
                myInstance.resetCurType();
                return;
            }
            myInstance.resetCurType();

            String socketResponse = myInstance.getCurMessage();

            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);

            final String response = myInstance.sendSynchronousStringRequest(MyApp.LOGIN_URL, params);

            if (response.equals(MyApp.NETWORK_ERROR)){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, getString(R.string.toast_network_error_login), Toast.LENGTH_SHORT).show();
                    }
                });
                myInstance.resetCurType();
                return;
            }

            if (response.equals("@") || socketResponse.equals("@")){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, getString(R.string.toast_login_failed_wrong_pass), Toast.LENGTH_LONG).show();
                    }
                });
            }else if(socketResponse.equals("!")){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, getString(R.string.toast_logged_in_another_place), Toast.LENGTH_LONG).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
                                                    myInstance.setEmail(response.split("\\|")[1]);
                                                    myInstance.setGuest(false);
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
            }else if(socketResponse.equals("#")){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, getString(R.string.toast_multiple_login_same_device), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, getString(R.string.login_success), Toast.LENGTH_LONG).show();
                    }
                });

                myInstance.setEmail(response.split("\\|")[1]);
                loadLoginInfo();
            }
        }
    }

    public void loadLoginInfo(){
        myInstance.loadFavouriteList();
        myInstance.loadCouponList();
        myInstance.loadCouponHistoryList();
        myInstance.setUsername(username);
        myInstance.setPassword(password);
        myInstance.setGuest(false);
        myInstance.writeSocket("User CheckCall");
    }

    public void delete(){

        myInstance.writeSocket("User Delete");
        //String message = myInstance.readSocket()[1];
        String curType = myInstance.getCurType();

        while(!curType.equals("UserDelete") && !curType.equals("Network")){
            curType = myInstance.getCurType();
        }

        if (curType.equals(MyApp.SOCKET_NETWORK_ERROR)){
            Toast.makeText(activity, getString(R.string.toast_network_error_try_again), Toast.LENGTH_LONG).show();
            myInstance.resetCurType();
            return;
        }

        myInstance.resetCurType();

        String message = myInstance.getCurMessage();

        if (message.equals("@")){
            Toast.makeText(activity, getString(R.string.toast_delete_failed), Toast.LENGTH_LONG).show();
        }else if (message.equals("$")){
            Toast.makeText(activity, getString(R.string.toast_delete_success), Toast.LENGTH_LONG).show();
            InLineTab.setupInLineInfo();
            findViewById(R.id.activity_in_line).setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(activity, getString(R.string.toast_timed_out_try_again), Toast.LENGTH_LONG).show();
        }
    }

    public static void logOut(){
        myInstance.setGuest(true);
        myInstance.disconnect();
        viewPager.setCurrentItem(0);
        bottomNavigation.setCurrentItem(0);

        InLineTab.setupInLineInfo();
        MeTab.updateMe();
    }

    public void setupSearch(){
        MenuItem searchItem = actionMenu.findItem(R.id.search);
        search = (SearchView) MenuItemCompat.getActionView(searchItem);

        // modifying edit text in search view
        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText editText = (EditText) search.findViewById(id);
        editText.setTextColor(Color.BLACK);
        editText.setTextSize(12);
        editText.setTypeface(Typeface.SERIF);
        editText.setPadding(0, 0, 0, 0);

        // modifying search view icon
        int iconId = search.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView icon = (ImageView) search.findViewById(iconId);
        icon.setScaleX((float)0.8);
        icon.setScaleY((float)0.8);

        // expanding the search view in actionbar
        search.setIconifiedByDefault(false);
        searchItem.expandActionView();
        search.setQueryHint(getString(R.string.main_act_search));

        // modifying search view layout
        int searchPlateId = search.getContext().getResources().getIdentifier("android:id/search_edit_frame", null, null);
        View searchPlate = search.findViewById(searchPlateId);
        searchPlate.setBackgroundResource(R.drawable.search_rounded);

        searchPlateId = search.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        searchPlate = findViewById(searchPlateId);
        searchPlate.setBackgroundResource(R.drawable.search_rounded);

        searchPlateId = search.getContext().getResources().getIdentifier("android:id/search_bar", null, null);
        searchPlate = findViewById(searchPlateId);
        searchPlate.setBackgroundResource(R.drawable.search_rounded);

        //float temp = (float)0.7;

        //searchPlate.setScaleY(temp);

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Intent intent = new Intent(activity, SearchingActivity.class);
                    activity.startActivity(intent);
                }else{
                    search.clearFocus();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //super.onCreateOptionsMenu(menu);
        inflater = getMenuInflater();
        actionMenu = menu;
        inflater.inflate(R.menu.qr_map_hide_action_bar, menu);

        // setting up the search view in action bar
        setupSearch();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        AlertDialog.Builder builder;
        switch (item.getItemId()){
            case R.id.go_to_map:
                intent = new Intent(activity, MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            case R.id.go_to_qr:
                intent = new Intent(activity, QRcodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            case R.id.inline_delete:
                if (!myInstance.isGuest()) {
                    builder = new AlertDialog.Builder(this);
                    builder.setMessage(getString(R.string.main_delete))
                            .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    delete();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), null)
                            .setCancelable(false)
                            .create()
                            .show();
                }else{
                    myInstance.returnToLogin();
                }
                return true;
            case R.id.my_history_log_out:
                if (!myInstance.isGuest()) {
                    builder = new AlertDialog.Builder(this);
                    builder.setMessage(getString(R.string.log_out_message))
                            .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    logOut();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), null)
                            .setCancelable(false)
                            .create()
                            .show();
                    return true;
                }else{
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    //finish();
                    return true;
                }
            case R.id.my_history_history:
                intent = new Intent(activity, MyHistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // used to display overflow menu icons
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume(){
        super.onResume();

        MyApp.setCurActivity(this);
        myInstance.checkTicketCall();

        int tabNum = intent.getIntExtra("goTo", -1);
        boolean checkForTicket = intent.getBooleanExtra("checkForTicket", false);

        if (checkForTicket){
            myInstance.writeSocket("User CheckCall");
            intent.putExtra("checkForTicket", false);
            myInstance.getNotificationManager().cancelAll();
        }

        if (viewPager != null && tabNum != -1) {
            viewPager.setCurrentItem(tabNum);
            bottomNavigation.setCurrentItem(tabNum);
            intent.putExtra("goTo", -1);
        }

        PromotionTab.updatePromotion();
        MeTab.updateMe();

        if (search != null){
            search.clearFocus();
        }

        /*if (bottomNavigation == null || viewPager == null){
            finish();
            startActivity(intent);
        }*/
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }
}
