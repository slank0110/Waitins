package logicreat.waitins;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Aaron on 2016-04-29.
 */
public class MyApp implements Serializable {
    // constants
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "PHPSESSID";
    private final String SOCKET_ADDR = "159.203.30.14";
    private final int SOCKET_PORT = 8080;
    private final String CHECK_UPDATE_OPERATION = "2";
    private final String CHECK_HISTORY_UPDATE_OPERATION = "1";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String REMEMBER = "remember";
    private final String LANGUAGE = "language";
    private final String PASSCODE = "HELLOISENDTOAPPLE";

    static final double RESTAURANTPICTURERATIO = 482.0/1072.0;
    static final double PREPAYMENTPICTURERATIO = 300.0/750.0;
    static final String NETWORK_ERROR = "";
    static final String SOCKET_NETWORK_ERROR = "Network";

    // post URL
    static final String LOADRES_URL = "http://159.203.30.14/waitinsApp/loadRes.php";
    static final String LOGIN_URL = "http://159.203.30.14/waitinsApp/userLogin.php";
    static final String LOAD_FAVOURITE_URL = "http://159.203.30.14/waitinsApp/favourite.php";
    static final String PROMOTION_IMAGE_URL = "http://159.203.30.14/waitinsApp/promPic.php";
    static final String LOAD_PROMOTION_URL = "http://159.203.30.14/waitinsApp/allPromotion.php";
    static final String LOAD_SINGLE_PROMOTION_URL = "http://159.203.30.14/waitinsApp/loadPromotion.php";
    static final String LOAD_ESTIMATED_URL = "http://159.203.30.14/waitinsApp/expectedWaitingTime.php";
    static final String LOAD_PROM_PLATE_URL = "http://159.203.30.14/waitinsApp/promDetail.php";
    static final String LOAD_PLATE_PIC_URL = "http://159.203.30.14/waitinsApp/promAllPlatePic.php";
    static final String LOAD_PROM_STATUS_URL = "http://159.203.30.14/waitinsApp/promStatus.php";
    static final String LOAD_DISCOVERY_URL = "http://159.203.30.14/waitinsApp/discovery.php";
    static final String LOAD_HISTORY_URL = "http://159.203.30.14/waitinsApp/userLoadHistory.php";
    static final String LOAD_ME_URL = "http://159.203.30.14/waitinsApp/userLoadVisit.php";
    static final String SEARCH_URL = "http://159.203.30.14/waitinsApp/search.php";
    static final String REGISTER_URL = "http://159.203.30.14/waitinsApp/userRegister.php";
    static final String SUGGESTION_URL = "http://159.203.30.14/waitinsApp/searchSuggest.php";
    static final String CHANGE_PASSWORD_URL = "http://159.203.30.14/waitinsApp/userChangePassword.php";
    static final String FEEDBACK_URL = "http://159.203.30.14/waitinsApp/suggestion.php";
    static final String LOAD_COUPON_URL = "http://159.203.30.14/waitinsApp/userLoadCurCoupon.php";
    static final String LOAD_COUPON_HISTORY_URL = "http://159.203.30.14/waitinsApp/userLoadUsedCoupon.php";
    static final String LOAD_ME_VISIT_URL = "http://159.203.30.14/waitinsApp/userLoadVisit.php";
    static final String TEST_CROP_URL = "http://159.203.30.14/waitinsApp/testDecode.php";
    static final String CALL_APPLE_URL = "http://159.203.30.14/waitinsApp/userCallApple.php";
    static final String FORGOT_URL = "http://159.203.30.14/waitinsApp/userForgotPassword.php";
    static final String MAP_URL = "http://159.203.30.14/waitinsApp/mapLoadRes.php";


    // bottom bar
    static final String TAB1 = "Home";
    static final String TAB2 = "Promotion";
    static final String TAB3 = "In Line";
    static final String TAB4 = "Me";

    // variables
    private static MyApp ourInstance;
    private static Context context;
    private static Activity curActivity;
    private RequestQueue queue;
    private DBHandler dbHandler;
    private SharedPreferences preferences;
    private SharedPreferences.Editor prefEditor;
    private ResManager allResManger;
    private NotificationManager notificationManager;
    private ActivityManager activityManager;
    private LocationManager locationManager;
    private PromotionManager promotionManager;
    private FragmentManager fragmentManager;
    private Vibrator v;
    private Restaurant curRes;
    private ArrayList<History> historyList;

    // asset manager
    private static AssetManager assetManager;
    private static Typeface promotion_title_font;
    private static Typeface promotion_price_font;

    // ticket info
    private static final int CALL_DIALOG_DURATION = 30;
    private static boolean didResponse = true;
    private static String lineUpComId;
    private static String curResName;
    private static String ticketNum = "0";
    private static String numPeopleBefore;
    private static long startTime;
    private static String numPeople;
    private static double lat;
    private static double lon;
    private static boolean didAccept = false;

    // socket
    private Socket mySocket = null;
    private Scanner in;
    private String curMessage = "";
    private String curType = "";
    private OutputStream o;
    private boolean socketReconnecting = false;
    private boolean connectingThreadRunning = false;
    //private boolean readingThreadRunning = false;

    // user info
    private String username;
    private String password;
    private String email;
    private String prefLanguage;
    private boolean rememberClick;
    private boolean guest = true;

    // new data manage
    private ArrayList<String> discoveryList;
    private ArrayList<Restaurant> discoveryResList;
    private ArrayList<String> favouriteList;
    private ArrayList<Restaurant> favouriteResList;
    private ArrayList<Coupon> couponList;
    private ArrayList<Coupon> couponHistoryList;
    private ArrayList<Promotion> promotionTabPromotionList;
    private String preDiscoveryPost = "";
    private String preFavouritePost = "";
    private String preCouponPost = "";
    private String preCouponHistoryPost = "";
    private String preAllPromotionPost = "";

    // Testing BMP
    private Bitmap upToDateBitmap;
    private Bitmap defaultPicture;
    private Bitmap defaultPromotionPicture;

    // loading dialog
    private ProgressDialog progressDialog;

    // permission
    final static int CAMERA_PERMISSIONS_CODE = 1;
    final static int GPS_PERMISSIONS_CODE = 2;
    final static int VIBRATE_PERMISSIONS_CODE = 3;

    // network
    final static int TIME_OUT = 30;
    final static int SOCKET_TIME_OUT = 30;
    static boolean finishedDiscoveryLoading;
    static boolean finishedFavouriteLoading;
    static boolean finishedTicketLoading;
    static boolean finishedCouponLoading;
    static boolean readingThreadRunning;

    // resolution
    static int width;
    static int height;
    static int gridHeight = 0;
    static int actionBarHeight;

    // map marker
    private BitmapDescriptor leisure;
    private BitmapDescriptor busy;
    private BitmapDescriptor full;

    // require Application Context
    private MyApp(Context context){

        MyApp.context = context;

        queue = Volley.newRequestQueue(context);

        ourInstance = this;
        dbHandler = new DBHandler(context);

        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);

        if (isMyServiceRunning(SocketService.class)){
        }else{
        }

        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // create asset manager
        assetManager = context.getAssets();

        // get the fonts from the asset
        promotion_price_font = Typeface.createFromAsset(assetManager,
                String.format(Locale.US, "fonts/%s", "myriad_pro_bold_condensed_italic.ttf"));
        promotion_title_font = Typeface.createFromAsset(assetManager,
                String.format(Locale.US, "fonts/%s", "mvboli.ttf"));

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        prefEditor = preferences.edit();

        username = preferences.getString(USERNAME, "");
        password = preferences.getString(PASSWORD, "");
        prefLanguage = preferences.getString(LANGUAGE, "");
        rememberClick = preferences.getBoolean(REMEMBER, false);

        allResManger = new ResManager();

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        promotionManager = new PromotionManager();
        //adMap = new HashMap<>();

        discoveryList = new ArrayList<>();
        discoveryResList = new ArrayList<>();
        favouriteList = new ArrayList<>();
        favouriteResList = new ArrayList<>();
        couponList = new ArrayList<>();
        couponHistoryList = new ArrayList<>();
        promotionTabPromotionList = new ArrayList<>();

        if (dbHandler.getAllHistory() == null){
            historyList = new ArrayList<>();
        }else{
            historyList = dbHandler.getAllHistory();
        }
        finishedDiscoveryLoading = false;
        finishedFavouriteLoading = true;
        finishedTicketLoading = false;
        finishedCouponLoading = true;

        readingThreadRunning = false;

        //loadAdPage();
        upToDateBitmap = resourceImageToBitmap(R.drawable.left_arrow_24);
        defaultPicture = resourceImageToBitmap(R.drawable.default_res);
        defaultPromotionPicture = resourceImageToBitmap(R.drawable.waintins_logo);

        MapsInitializer.initialize(context);
        try {
            leisure = BitmapDescriptorFactory.fromResource(R.drawable.map_green);
            busy = BitmapDescriptorFactory.fromResource(R.drawable.map_yellow);
            full = BitmapDescriptorFactory.fromResource(R.drawable.map_red);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    public static boolean isDidResponse() {
        return didResponse;
    }

    public static void setDidResponse(boolean didResponse) {
        MyApp.didResponse = didResponse;
    }

    public static void setCurActivity(Activity activity){
        curActivity = activity;
    }

    public static Activity getCurActivity(){
        return curActivity;
    }

    public static void setTicketNum(String newTicketNum){
        ticketNum = newTicketNum;
    }

    public static boolean isDidAccept() {
        return didAccept;
    }

    public static void setDidAccept(boolean didAccept) {
        MyApp.didAccept = didAccept;
    }

    // singleton implementation
    public static MyApp getOurInstance(Context context){
        if (ourInstance == null){
            ourInstance = new MyApp(context);
        }
        return ourInstance;
    }

    // another get for Request
    public static MyApp getOurInstance(){
        return ourInstance;
    }

    public ResManager getAllResManager(){
        return allResManger;
    }

    public PromotionManager getPromotionManager(){
        return promotionManager;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public ArrayList<String> getFavouriteList() {
        return favouriteList;
    }

    public ArrayList<Restaurant> getDiscoveryResList() {
        return discoveryResList;
    }

    public void loadDatabaseCouponInfo(){
        ArrayList<Coupon> result = dbHandler.getCouponList();
        for (int i = 0; i < result.size(); i++){
            Promotion temp = dbHandler.getPromotion(result.get(i).getPromotionId());
            if (temp != null) {
                promotionManager.addPromotion(temp);
                Restaurant temp1 = dbHandler.getRestaurant(temp.getComId());
                if (temp1 != null) {
                    allResManger.addToMap(temp1, true);
                    temp.setResName(temp1.getName());
                }
            }
        }
    }

    public void loadDatabaseCouponHistoryInfo(){
        ArrayList<Coupon> result = dbHandler.getCouponHistoryList();
        for (int i = 0; i < result.size(); i++){
            Promotion temp = dbHandler.getPromotion(result.get(i).getPromotionId());
            if (temp != null) {
                promotionManager.addPromotion(temp);
                Restaurant temp1 = dbHandler.getRestaurant(temp.getComId());
                if (temp1 != null) {
                    allResManger.addToMap(temp1, true);
                    temp.setResName(temp1.getName());
                }
            }
        }
    }

    public ArrayList<Coupon> getCouponList(){
        if (checkNetworkConnection()) {
            return couponList;
        }else{
            ArrayList<Coupon> result = dbHandler.getCouponList();
            return result;
        }
    }

    public ArrayList<Coupon> getCouponHistoryList(){
        if (checkNetworkConnection()) {
            return couponHistoryList;
        }else{
            ArrayList<Coupon> result = dbHandler.getCouponHistoryList();
            return result;
        }
    }

    public ArrayList<Restaurant> getFavouriteResList(){
        return favouriteResList;
    }

    public ArrayList<Promotion> getPromotionTabPromotionList() {
        return promotionTabPromotionList;
    }

    public ArrayList<History> getHistoryList(){
        return historyList;
    }

    public void addHistory(History history){
        historyList.add(history);
    }

    public Bitmap getDefaultPromotionPicture() {
        return defaultPromotionPicture;
    }

    public void loadHistoryList(){
        historyList = new ArrayList<>();
        String response;

        Map<String, String> params = new HashMap<>();
        params.put("operation", "0");

        response = sendSynchronousStringRequest(LOAD_HISTORY_URL, params);


        if (response.equals(NETWORK_ERROR)){
            return;
        }
        if (response.equals("$")){
        }else if (response.equals("@")){
        }else if (response.equals("%&!#")){
            if (reLogin()){
                loadHistoryList();
            }else{
                disconnect();
                curActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(curActivity, LoginActivity.class);
                        intent.putExtra("New", false);
                        curActivity.startActivity(intent);
                        Toast.makeText(curActivity, "Your session has expire, please re-login", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else{
            String[] historyList = response.split("\\|");

            for (int i = 0; i < historyList.length; i++){

                // converting end time and start time into duration
                String[] info = historyList[i].split("&");
                Timestamp ts1 = Timestamp.valueOf(info[1]);
                Timestamp ts2 = Timestamp.valueOf(info[2]);
                Long millis = ts2.getTime() - ts1.getTime();
                History history = new History(info[0], info[1], longToString(millis), info[3]);
                addHistory(history);

                params = new HashMap<>();
                params.put("operation", "6");
                params.put("comId", info[0]);

                String name = sendSynchronousStringRequest(LOADRES_URL, params);
                if (!name.equals(NETWORK_ERROR)) {
                    getRestaurant(info[0], name, true);
                }
            }
        }
    }

    public void loadDiscoveryList(){
        finishedDiscoveryLoading = false;

        Map<String, String> params = new HashMap<>();
        String response = sendSynchronousStringRequest(LOAD_DISCOVERY_URL, params);

        if (response.equals(NETWORK_ERROR)){
            finishedDiscoveryLoading = true;
            return;
        }

        if (!preDiscoveryPost.equals("") || !response.equals(preDiscoveryPost)) {
            if (!response.equals("")) {
                String[] restaurants = response.split("\\|");

                ArrayList<Restaurant> discoveryResList = new ArrayList<>();
                ArrayList<String> discoveryList = new ArrayList<>();

                if (response.equals("$")) {
                    this.discoveryResList.clear();
                    return;
                }

                // inner loop variable
                preDiscoveryPost = response;
                String[] info;
                String resName, comId;

                for (int i = 0; i < restaurants.length; i++) {
                    // separating the information of the restaurant
                    info = restaurants[i].split("&");
                    comId = info[0];
                    resName = info[1];
                    discoveryList.add(comId);
                    discoveryResList.add(getRestaurant(comId, resName, true));
                }
                this.discoveryList = discoveryList;

                updateAllDiscoveryStatus();

                this.discoveryResList.clear();
                for (int i = 0; i < discoveryResList.size(); i++){
                    this.discoveryResList.add(discoveryResList.get(i));
                }
            }
        }
        finishedDiscoveryLoading = true;
    }

    public void loadFavouriteList(){
        finishedFavouriteLoading = false;
        Map<String, String> params = new HashMap<>();
        params.put("operation", "3");

        String response = sendSynchronousStringRequest(LOAD_FAVOURITE_URL, params);

        String[] restaurants = response.split("\\|");

        if (response.equals("%&!#")){
            if (reLogin()){
                loadFavouriteList();
            }else{
                disconnect();
                curActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.logOut();
                        Toast.makeText(curActivity, "Your session has expire, please re-login", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else if (!response.equals(NETWORK_ERROR) && !response.equals("$")) {
            if (preFavouritePost.equals("") || !preFavouritePost.equals(response)) {
                ArrayList<String> favouriteList = new ArrayList<>();
                ArrayList<Restaurant> favouriteResList = new ArrayList<>();
                preFavouritePost = response;
                // inner loop variable
                String[] info;
                String resName, comId;
                // RequestFuture<Bitmap> loadImageFuture;

                for (int i = 0; i < restaurants.length; i++) {
                    // separating the information of the restaurant
                    info = restaurants[i].split("&");
                    comId = info[0];
                    resName = info[1];
                    favouriteList.add(comId);
                    //logger.log(Level.INFO, String.format("Added %s to favourite list", resName));

                    getRestaurant(comId, resName, true);
                    loadFullResInfo(comId);
                    favouriteResList.add(allResManger.getRestaurant(comId));
                }
                updateAllFavouriteStatus();
                this.favouriteList = favouriteList;
                this.favouriteResList = favouriteResList;
            }else{
            }
        }
        finishedFavouriteLoading = true;
    }

    public void loadCouponHistoryList(){
        String response = sendSynchronousStringRequest(LOAD_COUPON_HISTORY_URL, new HashMap<String, String>());

        if (response.equals("%&!#")){
            if (reLogin()) {
                loadCouponHistoryList();
                return;
            } else {
                disconnect();
                curActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.logOut();
                        Toast.makeText(curActivity, "Your session has expire, please re-login", Toast.LENGTH_LONG).show();
                    }
                });
                return;
            }
        }

        if (!response.equals("$") && !response.equals(NETWORK_ERROR)){

            if (preCouponHistoryPost.equals("") || !preCouponHistoryPost.equals(response)){
                preCouponHistoryPost = response;

                ArrayList<Coupon> tempList = new ArrayList<>();

                String[] info = response.split("\\|");
                for (int i = 0; i < info.length; i++){
                    final String[] details = info[i].split("&");
                    Coupon newCoupon = new Coupon(details[0], details[1], details[2]);
                    newCoupon.setStatus(details[3]);

                    if (loadSinglePromotion(details[0])) {

                        dbHandler.addPromotion(promotionManager.getPromotion(details[0]));

                        final String comId = promotionManager.getPromotion(details[0]).getComId();

                        Thread loadImageThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String resName = getResName(comId);
                                getRestaurant(comId, resName, true);
                                Bitmap promotionImage = loadPromotionImage(details[0]);
                                if (promotionImage != null) {
                                    promotionManager.getPromotion(details[0]).setPromotionImage(promotionImage);
                                    dbHandler.updatePromotionImage(details[0], promotionImage);
                                }
                            }
                        });
                        loadImageThread.start();
                    }

                    tempList.add(newCoupon);
                    dbHandler.addCoupon(newCoupon);

                }

                couponHistoryList.clear();
                for (int i = 0; i < tempList.size(); i++){
                    couponHistoryList.add(tempList.get(i));
                }
            }
        }
    }

    public void loadCouponList(){
        finishedCouponLoading = false;
        String response = sendSynchronousStringRequest(LOAD_COUPON_URL, new HashMap<String, String>());

        if (response.equals("%&!#")) {
            if (reLogin()) {
                loadCouponList();
                return;
            } else {
                disconnect();
                curActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.logOut();
                        Toast.makeText(curActivity, "Your session has expire, please re-login", Toast.LENGTH_LONG).show();
                    }
                });
                return;
            }
        }

        if (!response.equals("$") && !response.equals(NETWORK_ERROR)){
            if (preCouponPost.equals("") || !preCouponPost.equals(response)) {
                preCouponPost = response;

                ArrayList<Coupon> tempList = new ArrayList<>();

                String[] info = response.split("\\|");
                for (int i = 0; i < info.length; i++) {
                    final String[] details = info[i].split("&");
                    Coupon newCoupon = new Coupon(details[0], details[1], details[2]);

                    if (loadSinglePromotion(details[0])) {

                        dbHandler.addPromotion(promotionManager.getPromotion(details[0]));

                        final String comId = promotionManager.getPromotion(details[0]).getComId();

                        Thread loadImageThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String resName = getResName(comId);
                                getRestaurant(comId, resName, true);
                                Bitmap promotionImage = loadPromotionImage(details[0]);
                                if (promotionImage != null) {
                                    promotionManager.getPromotion(details[0]).setPromotionImage(promotionImage);
                                    dbHandler.updatePromotionImage(details[0], promotionImage);
                                }
                            }
                        });
                        loadImageThread.start();
                    }

                    tempList.add(newCoupon);
                    //couponList.add(newCoupon);
                    dbHandler.addCoupon(newCoupon);
                }

                couponList.clear();
                for (int i = 0; i < tempList.size(); i++){
                    couponList.add(tempList.get(i));
                }
            }
        }
        finishedCouponLoading = true;
    }

    public Restaurant getRestaurant(String comId, String resName, boolean priority){
        Restaurant result;

        if (!allResManger.ifExist(comId)){

            result = dbHandler.getRestaurant(comId);
            if (result == null){
                result = new Restaurant(comId, resName);

                Map<String, String> params = new HashMap<>();
                params.put("operation", "1");
                params.put("comId", comId);

                Bitmap image = sendSynchronousImageRequest(LOADRES_URL, params);

                if (image != null){
                    result.setImage(image);
                    allResManger.addToMap(result, priority);
                    dbHandler.addRestaurant(result);
                }else{
                }
            }else{
                Map<String, String> params = new HashMap<>();
                params.put("operation", "2");
                params.put("comId", comId);
                params.put("lastUpdate", result.getLastUpdated());

                Bitmap image = sendSynchronousImageRequest(LOADRES_URL, params);
                if (image == null){
                }else if (image.equals(upToDateBitmap)){
                }else{
                    result.setImage(image);
                    dbHandler.updateResImage(comId, image);
                }

                allResManger.addToMap(result, priority);
            }
        }else{
            result = allResManger.getRestaurant(comId);
        }

        return result;
    }

    public byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // compress(CompressFormat, quality, outputStream)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    public Bitmap byteArrayToBitmap(byte[] bArray){
        // decode the byte array into a bitmap
        if (bArray == null){
            return null;
        }
        return BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
    }

    public Bitmap resourceImageToBitmap(int id){
        return BitmapFactory.decodeResource(context.getResources(), id);
    }

    public BitmapDescriptor getFull() {
        return full;
    }

    public BitmapDescriptor getLeisure() {
        return leisure;
    }

    public BitmapDescriptor getBusy() {
        return busy;
    }

    public String longToString(long millis){

        int seconds = (int)(millis / 1000);
        int minutes = (seconds / 60);
        seconds = seconds % 60;
        int hour = minutes /60;
        minutes = minutes % 60;

        return String.format("%d hr %d min %d sec", hour, minutes, seconds);
    }

    public void setCurRes(String comId){
        if (allResManger.ifExist(comId)){
            curRes = allResManger.getRestaurant(comId);
        }else{
        }
    }

    public Restaurant getCurRes(){
        return this.curRes;
    }

    public void addRequest(Request request){
        queue.add(request);
    }

    public void disconnect(){
        if (this.mySocket != null){
            try{
                this.mySocket.close();
                resetInfoOnLogout();

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void resetInfoOnLogout(){
        historyList = new ArrayList<>();
        discoveryList = new ArrayList<>();
        discoveryResList = new ArrayList<>();
        favouriteList = new ArrayList<>();
        favouriteResList = new ArrayList<>();
        couponList = new ArrayList<>();
        couponHistoryList = new ArrayList<>();
        promotionTabPromotionList = new ArrayList<>();

        dbHandler.cleanCoupon();

        ticketNum = "0";
        curMessage = "";
        curType = "";
        preFavouritePost = "";
        preDiscoveryPost = "";
        preAllPromotionPost = "";
        preCouponHistoryPost = "";
        preCouponPost = "";
        username = "";
        password = "";
        finishedFavouriteLoading = true;
        finishedTicketLoading = false;
        prefEditor.clear();
        prefEditor.apply();
    }

    public void deleteDatabase(){
        context.deleteDatabase(DBHandler.DATABASE_NAME);
        dbHandler = new DBHandler(context);
    }

    public void setSocket(){
        try{
            if (mySocket == null || mySocket.isClosed()) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent serviceIntent = new Intent(curActivity, SocketService.class);
                        curActivity.startService(serviceIntent);
                    }
                });
                t.start();
                mySocket = new Socket(SOCKET_ADDR, SOCKET_PORT);
                in = new Scanner(mySocket.getInputStream());
                startReading();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void writeSocket(String message){
        if (!socketReconnecting) {
            if (mySocket != null && !this.mySocket.isClosed()) {
                try {
                    o = mySocket.getOutputStream();
                    o.write(message.getBytes());
                } catch (IOException e) {
                    curType = SOCKET_NETWORK_ERROR;
                    e.printStackTrace();
                    curActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(curActivity, "Network error, trying to reconnect", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                curType = SOCKET_NETWORK_ERROR;
            }
        }else{
            curType = SOCKET_NETWORK_ERROR;
        }
    }

    public void socketReconnect(){
        try {
            if (guest){
                socketReconnecting = false;
                return;
            }
            socketReconnecting = true;
            Thread.sleep(5000);
            //this.mySocket.close();
            mySocket = new Socket(SOCKET_ADDR, SOCKET_PORT);
            in = new Scanner(mySocket.getInputStream());
            o = mySocket.getOutputStream();
            Thread.sleep(2000);
            socketReconnecting = false;
            if (!username.equals("") && !password.equals("")) {
                writeSocket(String.format("User Login %s %s", username, password));
                loadPromotion();
            }
        }catch (IOException e){
            e.printStackTrace();
            socketReconnect();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void startReading(){
        Thread waitingForResponse = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(mySocket != null && !mySocket.isClosed()){
                        try {
                            in = new Scanner(mySocket.getInputStream());
                            String message = in.nextLine();
                            curMessage = message.split("&")[1];
                            curType = message.split("&")[0];
                            handleResponse(message);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    readingThreadRunning = false;
                }catch(NoSuchElementException e){
                    readingThreadRunning = false;
                    if (!guest) {
                        if (!connectingThreadRunning) {
                            connectingThreadRunning = true;
                            Thread reconnectThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    socketReconnect();
                                    connectingThreadRunning = false;
                                }
                            });
                            reconnectThread.start();
                        }
                        try {
                            Thread.sleep(5000);
                            startReading();
                        } catch (InterruptedException temp) {
                            temp.printStackTrace();
                        }
                    }
                    //logger.log(Level.INFO, ""+(mySocket == null));
                    //writeSocket(String.format("User Login %s %s", username, password));
                }catch(IndexOutOfBoundsException e){
                    readingThreadRunning = false;
                    e.printStackTrace();
                    startReading();
                }
            }
        });
        if (!readingThreadRunning) {
            readingThreadRunning = true;
            waitingForResponse.start();
        }else{
        }
    }

    public void handleResponse(String response){
        String[] info = response.split("&");

        if (info[0].equals("UserLogin")){
            if (info[1].equals("$")) {
                ticketNum = "0";
            } else if (info[1].equals("@") || info[1].equals("!") || info[1].equals("#")){
            } else{
                String[] temp = info[1].split("\\|");
                Timestamp ts = Timestamp.valueOf(temp[3]);
                temp[3] = "" + ts.getTime();
                setTicketInfo(temp);
                finishedTicketLoading = true;
            }
        }else if (info[0].equals("UserSubmit")){
            if (info[1].equals("@")){
            }else{

                final Map<String, String> params = new HashMap<>();
                params.put("comId", getLineUpComId());
                params.put("passcode", PASSCODE);

                Thread callAppleThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendSynchronousStringRequest(CALL_APPLE_URL, params);
                    }
                });
                callAppleThread.start();
            }
        }else if (info[0].equals("ResCall")){
            if (info[1].equals("$")) {
                didResponse = false;
                // the restaurant is calling this user
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.waitins)
                                .setContentTitle("Ticket Info")
                                .setContentText("Your are being called")
                                .setAutoCancel(true);

                // message disappear
                Intent notificationIntent = new Intent(curActivity, MainActivity.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent intent = PendingIntent.getActivity(curActivity, 0, notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
                mBuilder.setContentIntent(intent);
                //mBuilder.setContentIntent(resultPendingIntent);

                notificationManager.notify(1, mBuilder.build());

                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(context, notification);
                r.play();
                v.vibrate(500);

                while (curActivity == null){

                }
                curActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCallDialog(CALL_DIALOG_DURATION);
                    }
                });
            }
        }else if (info[0].equals("UserDelete")) {
            if (info[1].equals("$")) {
                ticketNum = "0";
            }
        }else if (info[0].equals("UserReact")){
        }else if (info[0].equals("ResSeat")){
            if (info[1].equals("$")) {

                didAccept = false;
                ticketNum = "0";
                curActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(curActivity,
                                "Your have been seated", Toast.LENGTH_LONG).show();
                        InLineTab.setupInLineInfo();
                    }
                });
            }else{
                numPeopleBefore = info[1];
                curActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(curActivity, )
                        InLineTab.setupInLineInfo();
                    }
                });
            }
        }else if (info[0].equals("ResDelete")){
            ticketNum = "0";
            curActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(curActivity,
                            "Your ticket have been deleted", Toast.LENGTH_LONG).show();
                    InLineTab.setupInLineInfo();
                }
            });
        }else if (info[0].equals("UserForceLogin")){
            curActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(curActivity, "You have be logged in from another place", Toast.LENGTH_LONG).show();
                    MainActivity.logOut();
                }
            });
        }else if (info[0].equals("ResLogout")){
            if (info[1].equals("$")){
                curActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ticketNum = "0";

                        Toast.makeText(curActivity, "The restaurant you are in line have closed", Toast.LENGTH_LONG).show();
                        InLineTab.setupInLineInfo();
                    }
                });
            }
        }else if(info[0].equals("CheckCall")){
            final String display;
            int time = 30;
            if (info[1].equals("$")) {
                display = "0";
            }else if (info[1].equals("$$")){
                display = "Your ticket have been deleted by the restaurant";
            }else if (info[1].equals("$$$")){
                display = "The restaurant you are in line with have closed their line up";
            }else{
                time = Integer.parseInt(info[1]);
                if (time < 0){
                    display = "Your ticket have been automatically delayed";
                }else{
                    display = "1";
                }
            }

            if (display.equals("0")){
            }else if (display.equals("1")){
                final int timeLeft = time;
                curActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCallDialog(timeLeft);
                    }
                });
            }else{
                final String text = display;
                curActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(curActivity);
                        mBuilder.setNegativeButton("Ok", null)
                                .setMessage(text)
                                .create()
                                .show();
                        Toast.makeText(curActivity, text, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else if (info[0].equals("General")){
            if (info[1].equals("Login Require")){
                curActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(curActivity, "Login Required, please try again after login", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void setTicketInfo(String[] info){
        lineUpComId = info[0];
        ticketNum = info[1];
        numPeopleBefore = info[2];
        startTime = Long.parseLong(info[3]);
        numPeople = info[4];
        Map<String, String> params = new HashMap<>();
        params.put("operation", "7");
        params.put("comId", lineUpComId);

        String response = sendSynchronousStringRequest(LOADRES_URL, params);


        if (response.equals(NETWORK_ERROR)){
            return;
        }

        if (response.equals("@")){
        }else if (response.equals("$")){
        }else {
            info = response.split("\\|");
            lat = Double.parseDouble(info[1]);
            lon = Double.parseDouble(info[0]);
        }

        response = getResName(lineUpComId);

        if (response.equals("@")){
        }else{
            curResName = response;
        }

        getRestaurant(lineUpComId, response, true);
        loadFullResInfo(lineUpComId);

    }

    public void showCallDialog(int timeLeft){
        String temp = "";
        temp += allResManger.getRestaurant(lineUpComId).toString();

        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int)(millis / 1000);
        int minutes = (seconds/60);
        int hour = minutes /60;
        minutes = minutes % 60;

        //temp += String.format("\nTotal Time Waited: %d hour %d minutes", hour, minutes);
        temp += "\n" + curActivity.getString(R.string.total_time_waited) + hour + " hour " + minutes + " min";

        CustomTicketDialog test = new CustomTicketDialog(curActivity, temp, timeLeft);
        test.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });

        try {
            while (curActivity == null){

            }
            test.show();
        }catch(WindowManager.BadTokenException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void checkTicketCall(){
        if (!didResponse){
            showCallDialog(CALL_DIALOG_DURATION);
        }
    }

    public String getPrefLanguage() {
        return preferences.getString(LANGUAGE, "");
    }

    /*
    Refresh the app and change language locale
     */
    public void refreshLocale(String locale) {
        prefEditor.putString(LANGUAGE, locale);
        prefEditor.apply();
        Locale myLocale = new Locale(locale);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        curActivity.getApplicationContext().getResources().updateConfiguration(config, curActivity.getApplicationContext().getResources().getDisplayMetrics());
    }

    public boolean reLogin(){

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        String response = sendSynchronousStringRequest(LOGIN_URL, params);

        if (response.equals("@") || response.equals(NETWORK_ERROR)){
            return false;
        }else{
            return true;
        }
    }

    public void checkVibratePermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(curActivity, Manifest.permission.VIBRATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(curActivity, Manifest.permission.VIBRATE)){
            }else{
                ActivityCompat.requestPermissions(curActivity, new String[]{Manifest.permission.VIBRATE}, VIBRATE_PERMISSIONS_CODE);
            }
        }else{
        }
    }

    public void checkCameraPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(curActivity, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(curActivity, Manifest.permission.CAMERA)){
            }else{
                ActivityCompat.requestPermissions(curActivity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSIONS_CODE);
            }
        }else{
        }
    }

    public void checkGpsPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(curActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(curActivity, Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(curActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERMISSIONS_CODE);
            }
        }else{
        }
    }


    public boolean checkNetworkConnection(){
        boolean networkState;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        networkState = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!networkState){
        }
        return networkState;
    }

    public void checkWifiConnection(){
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        if (!wifi.isWifiEnabled()){

        }
    }

    public static String getLineUpComId() {
        return lineUpComId;
    }

    public static String getTicketNum() {
        return ticketNum;
    }

    public static String getNumPeopleBefore() {
        return numPeopleBefore;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static double getLon() {
        return lon;
    }

    public static double getLat() {
        return lat;
    }

    public static String getCurResName() {
        return curResName;
    }

    public static Context getContext() {
        return context;
    }

    public String getCurMessage(){
        return this.curMessage;
    }

    public String getCurType(){
        return this.curType;
    }

    public void resetCurType(){
        this.curType = "";
    }


    public String getUsername() {
        return username;
    }

    public boolean isRememberClick() {
        return rememberClick;
    }

    public void setRememberClick(boolean rememberClick) {
        this.rememberClick = rememberClick;
        prefEditor.putBoolean(REMEMBER, rememberClick);
        prefEditor.apply();
    }

    public void setUsername(String username) {
        this.username = username;
        prefEditor.putString(USERNAME, username);
        prefEditor.apply();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        prefEditor.putString(PASSWORD, password);
        prefEditor.apply();
    }

    public Bitmap getDefaultPicture() {
        return defaultPicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGuest() {
        return guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public Bitmap getUpToDateBitmap(){
        return upToDateBitmap;
    }

    public static String getNumPeople() {
        return numPeople;
    }

    public static Typeface getPromotion_price_font() {
        return promotion_price_font;
    }

    public static Typeface getPromotion_title_font() {
        return promotion_title_font;
    }

    public void returnToLogin(){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(curActivity);
            builder.setMessage(curActivity.getString(R.string.return_to_login_message))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(curActivity, LoginActivity.class);
                            intent.putExtra("New", false);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            curActivity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .setCancelable(false);

            AlertDialog dialog = builder.create();
            if (!curActivity.isFinishing()){
                dialog.show();
            }
        }finally{
        }
    }

    public void updateAllFavouriteStatus(){
        for (int i = 0; i < favouriteList.size(); i++){
            String comId = favouriteList.get(i);
            String[] status = getResStatus(comId);

            Restaurant res = allResManger.getRestaurant(comId);
            if (res != null){
                res.setStatus(status);
            }
        }
    }

    public void updateAllDiscoveryStatus(){
        for (int i = 0; i < discoveryList.size(); i++){
            String comId = discoveryList.get(i);
            String[] status = getResStatus(comId);

            Restaurant res = allResManger.getRestaurant(comId);
            if (res != null){

                res.setStatus(status);
            }
        }
    }

    public String[] getResStatus(String comId){
        String[] numBefore = {"-", "-", "-"};
        Map<String, String> params = new HashMap<>();
        params.put("operation", "5");
        params.put("comId", comId);

        String status = sendSynchronousStringRequest(LOADRES_URL, params);

        if (status.equals("@") || status.equals(NETWORK_ERROR)){
        }else{
            numBefore = status.split("\\|");
        }
        return numBefore;
    }

    public String getResDistance(String comId){
        Location location = null;
        String result = "-";
        //checkGpsPermission();
        try {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }catch (SecurityException e){
            e.printStackTrace();
        }
        if (location == null){
            return result;
        }else{

            Map<String, String> params = new HashMap<>();
            params.put("operation", "8");
            params.put("comId", comId);
            params.put("longitude", "" + location.getLongitude());
            params.put("latitude", "" + location.getLatitude());

            String message = sendSynchronousStringRequest(LOADRES_URL, params);

            if (message.equals("@") || message.equals(NETWORK_ERROR)){
                return result;
            }else if (message.equals("$")){
                return result;
            }else {
                return message;
            }
        }
    }

    public double[] getResLatLng(String comId){
        double[] result = {43.8073300, -79.4231150};

        Map<String, String> params = new HashMap<>();
        params.put("operation", "7");
        params.put("comId", comId);

        String response = sendSynchronousStringRequest(LOADRES_URL, params);

        if (response.equals("@") || response.equals(NETWORK_ERROR)){
            return result;
        }else if (response.equals("$")) {
            return result;
        }else{
            String[] info = response.split("\\|");
            result[0] = Double.parseDouble(info[1]);
            result[1] = Double.parseDouble(info[0]);
            return result;
        }
    }

    public String getResName(String comId){
        String result;

        Map<String, String> params = new HashMap<>();
        params.put("operation", "6");
        params.put("comId", comId);

        result = sendSynchronousStringRequest(LOADRES_URL, params);

        if (result.equals(NETWORK_ERROR)){
            return "N/A";
        }
        return result;
    }

    public DBHandler getDbHandler() {
        return dbHandler;
    }

    public boolean loadPromotion(){
        Map<String, String> params = new HashMap<>();
        String response = sendSynchronousStringRequest(LOAD_PROMOTION_URL, params);

        if (response.equals(NETWORK_ERROR)){
            return false;
        }

        if (response.equals(preAllPromotionPost)){
            return false;
        }
        else if (response.equals("$")){
            preAllPromotionPost = response;
            return true;
        }else {
            preAllPromotionPost = response;
            String[] promotions = response.split("\\|");
            ArrayList<Promotion> temp = new ArrayList<>();
            for (int i = 0; i < promotions.length; i++){

                String[] info = promotions[i].split("&");

                Promotion newPromotion ;
                newPromotion = promotionManager.getPromotion(info[0]);

                if (newPromotion == null){
                    newPromotion = dbHandler.getPromotion(info[0]);

                    if (newPromotion != null){
                        promotionManager.addPromotion(newPromotion);
                        temp.add(newPromotion);
                    }else {
                        newPromotion =
                                new Promotion(info[0], info[1], info[2],
                                        Integer.parseInt(info[3]), Integer.parseInt(info[4]),
                                        info[6], info[7], info[8]);

                        newPromotion.setNumberSold(Integer.parseInt(info[5]));
                        promotionManager.addPromotion(newPromotion);
                        dbHandler.addPromotion(newPromotion);
                        temp.add(newPromotion);
                    }

                }else{
                    temp.add(newPromotion);
                }

            }

            promotionTabPromotionList.clear();
            for (int i = 0; i < temp.size(); i++){
                promotionTabPromotionList.add(temp.get(i));
            }
            return true;
        }
    }

    public boolean loadSinglePromotion(String promotionId){

        if (promotionManager.ifExist(promotionId)){
            return true;
        }

        Promotion newPromotion = dbHandler.getPromotion(promotionId);
        if (newPromotion != null){
            promotionManager.addPromotion(newPromotion);
            return true;
        }

        Map<String, String> params = new HashMap<>();
        params.put("promotionId", promotionId);

        String response = sendSynchronousStringRequest(LOAD_SINGLE_PROMOTION_URL, params);

        if (!response.equals("@") && !response.equals(NETWORK_ERROR)){
            String[] info = response.split("\\|");

            newPromotion =
                    new Promotion(promotionId, info[0], info[1],
                            Integer.parseInt(info[2]), Integer.parseInt(info[3]),
                            info[5], info[6], info[7]);

            newPromotion.setNumberSold(Integer.parseInt(info[4]));
            promotionManager.addPromotion(newPromotion);
            return true;
        }
        return false;
    }

    public Bitmap loadPromotionImage(String promotionId) {
        Bitmap result = null;
        if (promotionManager.ifExist(promotionId) && !promotionManager.getPromotion(promotionId).imageExist()){

            Map<String, String> params = new HashMap<>();
            params.put("promId", promotionId);

            result = sendSynchronousImageRequest(PROMOTION_IMAGE_URL, params);

        }else if (promotionManager.ifExist(promotionId)){
            result = promotionManager.getPromotion(promotionId).getPromotionImage();
        }
        return result;
    }

    public String loadPromotionInfo(String promotionId){
        String result;

        Map<String, String> params = new HashMap<>();
        params.put("promotionId", promotionId);

        result = sendSynchronousStringRequest(LOAD_PROM_PLATE_URL, params);

        return result;
    }

    public String loadPromotionStatus(String promotionId){
        String result;
        Map<String, String> params = new HashMap<>();
        params.put("promId", promotionId);

        // not checked
        result = sendSynchronousStringRequest(LOAD_PROM_STATUS_URL, params);

        return result;
    }

    public Bitmap loadPlateImage(String plateId, String promotionId){
        Bitmap result;

        Map<String, String> params = new HashMap<>();
        params.put("platePic", plateId);
        params.put("promotionId", promotionId);

        result = sendSynchronousImageRequest(LOAD_PLATE_PIC_URL, params);

        return result;
    }

    public void loadFullResInfo(String comId){
        String response;
        boolean needUpdate;
        String fullLastUpdate;
        String backLastUpdate;

        String[] info = new String[0];
        Bitmap backImage;

        Restaurant res = allResManger.getRestaurant(comId);

        // if current res not inside database, insert it in
        if (dbHandler.getRestaurant(comId) == null){
            dbHandler.addRestaurant(res);
        }


        // if res info is not already set
        if (!res.isFullInfoSet()){

            // check if info exist in database
            ArrayList<Object> fullInfo = dbHandler.loadFullInfo(comId);
            fullLastUpdate = (String) fullInfo.get(20);
            backLastUpdate = (String) fullInfo.get(22);


            // if full info not inside database, get new one
            // no update require in this case
            if (fullLastUpdate == null){
                Map<String, String> params = new HashMap<>();
                params.put("operation", "4");
                params.put("comId", comId);
                response = sendSynchronousStringRequest(LOADRES_URL, params);
                if (response.equals(NETWORK_ERROR)){
                    return;
                }
                info = response.split("\\|");
                res.setFullResInfo(info);

                params = new HashMap<>();
                params.put("operation", "3");
                params.put("comId", comId);
                backImage = sendSynchronousImageRequest(LOADRES_URL, params);
                res.setBackGroundImage(backImage);

                needUpdate = false;
            }else{
                // if info exist, update is required
                res.setResFullLastUpdate(fullLastUpdate);
                res.setResBackLastUpdate(backLastUpdate);
                // take out info from database
                info = fullInfo.subList(0, 20).toArray(info);
                backImage = byteArrayToBitmap((byte[])fullInfo.get(21));
                needUpdate = true;
            }
        }else{
            needUpdate = true;
            fullLastUpdate = res.getResFullLastUpdate();
            backLastUpdate = res.getResBackLastUpdate();
            info = res.getFullResInfo();
            backImage = res.getBackGroundImage();
        }

        // if update is needed
        if (needUpdate){
            // check for full info update
            Map<String, String> params = new HashMap<>();
            params.put("operation", "10");
            params.put("comId", comId);
            params.put("lastUpdate", fullLastUpdate);

            response = sendSynchronousStringRequest(LOADRES_URL, params);

            if (!response.equals("&") && !response.equals(NETWORK_ERROR)) {
                info = response.split("\\|");
            }

            res.setFullResInfo(info);

            // check for back image update
            params = new HashMap<>();
            params.put("operation", "9");
            params.put("comId", comId);
            params.put("lastUpdate", backLastUpdate);

            Bitmap imageResponse = sendSynchronousImageRequest(LOADRES_URL, params);
            if (imageResponse == null){
            }else if (imageResponse.equals(upToDateBitmap)){
            }else{
                backImage = imageResponse;
            }
            res.setBackGroundImage(backImage);
        }

        dbHandler.updateFullInfo(comId, info);
        dbHandler.updateBackground(comId, backImage);

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth){
            final int halfHeight = height/2;
            final int halfWidth = width/2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public boolean isMyServiceRunning(Class<?> serviceClass){
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceClass.getName().equals(service.service.getClassName())){
                context.stopService(new Intent(context, SocketService.class));
                return true;
            }
        }
        return false;
    }

    /*
    Crops given image with round corners
     */
    public Bitmap cropCircle(Bitmap bm){

        int width = bm.getWidth();
        int height = bm.getHeight();

        Bitmap cropped_bitmap;

    /* Crop the bitmap so it'll display well as a circle. */
        // crop the image so that it only displays the center square of the image
        if (width > height) {
            cropped_bitmap = Bitmap.createBitmap(bm,
                    (width / 2) - (height / 2), 0, height, height);
        } else {
            cropped_bitmap = Bitmap.createBitmap(bm, 0, (height / 2)
                    - (width / 2), width, width);
        }

        BitmapShader shader = new BitmapShader(cropped_bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        height = cropped_bitmap.getHeight();
        width = cropped_bitmap.getWidth();

        Bitmap mCanvasBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        // create a frame to draw the bitmap using the style declare in paint
        Canvas canvas = new Canvas(mCanvasBitmap);
        canvas.drawCircle(width/2, height/2, width/2, paint);

        return mCanvasBitmap;
    }

    // check the response headers for session cookie and save it
    public final void checkSessionCookie(Map<String, String> headers){
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)){
            // get the cookie id from the response
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0){
                // store the id into share preferences (a type of storage)
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                prefEditor.putString(SESSION_COOKIE, cookie);
                prefEditor.apply();
            }
        }
    }

    // Adds session cookie to headers if exists.
    public final void addSessionCookie(Map<String, String> headers) {
        // get the session id from previous request
        String sessionId = preferences.getString(SESSION_COOKIE, "");
        if (sessionId.length() > 0) {
            // format the string and add it into the new request header
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }

    public String getSessionCookie(){
        String sessionId = preferences.getString(SESSION_COOKIE, "");
        if (sessionId.length() > 0) {
            // format the string and add it into the new request header
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            return builder.toString();
        }else{
            return "";
        }
    }

    public String sendSynchronousStringRequest(String URL, Map<String, String> params){

        final RequestFuture<String> stringFuture = RequestFuture.newFuture();

        String result = NETWORK_ERROR;

        if (!checkNetworkConnection()){
            return result;
        }

        try {
            GeneralStringRequest request = new GeneralStringRequest(URL, params, stringFuture);
            request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            addRequest(request);

            result = stringFuture.get(TIME_OUT, TimeUnit.SECONDS);

        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (TimeoutException e){
            curActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(curActivity, "Network Time Out, Please try again", Toast.LENGTH_LONG).show();
                }
            });
        }
        return result;
    }

    public Bitmap sendSynchronousImageRequest(String URL, Map<String, String> params){
        Bitmap result = defaultPicture;

        if (!checkNetworkConnection()){
            return null;
        }

        final RequestFuture<Bitmap> bitmapFuture = RequestFuture.newFuture();


        try {
            GeneralImageRequest request = new GeneralImageRequest(URL, params, bitmapFuture, bitmapFuture);
            //request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            addRequest(request);
            result = bitmapFuture.get(TIME_OUT, TimeUnit.SECONDS);
            request.cancel();


        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (TimeoutException e){
            curActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(curActivity, "Network Time Out, Please try again", Toast.LENGTH_LONG).show();
                }
            });
        }
        return result;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.density * 160f / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

}
