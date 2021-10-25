package logicreat.waitins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Aaron on 2016-04-30.
 */
public class DBHandler extends SQLiteOpenHelper {
    private MyApp myInstance = MyApp.getOurInstance();
    // Database version
    private static final int DATABASE_VERSION = 5;
    // Database Name
    public static final String DATABASE_NAME = "MyApp";
    // Database table name
    private static final String TABLE_RES = "Restaurant";
    private static final String TABLE_HIS = "History";
    private static final String TABLE_FAV = "Favourite";
    private static final String TABLE_USER = "User";
    private static final String TABLE_PROMOTION = "Promotion";
    private static final String TABLE_SUGGESTION = "Suggestion";
    private static final String TABLE_COUPON = "Coupon";

    // Restaurant columns
    // 1) first post info
    private static final String RES_ID = "resId";
    private static final String RES_COM_ID = "comId";
    private static final String NAME = "name";
    // 2) picture
    private static final String IMAGE = "profileImage";
    private static final String RES_LAST_UPDATE = "lastUpdate";
    // 3) full info
    private static final String RES_ADDRESS = "address";
    private static final String RES_EMAIL = "email";
    private static final String RES_PHONE = "phoneNumber";
    private static final String RES_POSTAL_CODE = "postalCode";
    private static final String RES_SIZE1 = "tableSize1";
    private static final String RES_SIZE2 = "tableSize2";
    private static final String RES_MON_OPEN = "monO";
    private static final String RES_MON_CLOSE = "monC";
    private static final String RES_TUE_OPEN = "tueO";
    private static final String RES_TUE_CLOSE = "tueC";
    private static final String RES_WED_OPEN = "wedO";
    private static final String RES_WED_CLOSE = "wedC";
    private static final String RES_THUR_OPEN = "thurO";
    private static final String RES_THUR_CLOSE = "thurC";
    private static final String RES_FRI_OPEN = "friO";
    private static final String RES_FRI_CLOSE = "friC";
    private static final String RES_SAT_OPEN = "satO";
    private static final String RES_SAT_CLOSE = "satC";
    private static final String RES_SUN_OPEN = "sunO";
    private static final String RES_SUN_CLOSE = "sunC";
    private static final String RES_FULL_LAST_UPDATE = "fullLastUpdate";
    // 4)
    private static final String RES_BACK_IMAGE = "backImage";
    private static final String RES_BACK_LAST_UPDATE = "backLastUpdate";
    // History columns
    private static final String HIS_ID = "hisId";
    private static final String HIS_COM_ID = "comId";
    private static final String START_TIME = "startTime";
    private static final String DURATION = "duration";
    private static final String NUM_PEOPLE = "numPeople";
    private static final String HIS_LAST_UPDATE = "lastUpdate";
    // User columns
    private static final String USER_ID = "userId";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    // Promotion columns
    private static final String PROMOTION_ID = "promotionId";
    private static final String PROMOTION_PROM_ID = "promotionPromId";
    private static final String PROMOTION_COM_ID = "promotionComId";
    private static final String PROMOTION_NAME = "promotionName";
    private static final String PROMOTION_TOTAL_COST = "promotionTotalCost";
    private static final String PROMOTION_ORIGINAL_COST = "promotionOriginalCost";
    private static final String PROMOTION_TOTAL_NUM = "promotionTotalNum";
    private static final String PROMOTION_EXPIRY_DATE = "promotionExpiryDate";
    private static final String PROMOTION_IMAGE = "promotionImage";
    // Suggestion columns
    private static final String SUGGESTION_ID = "suggestionId";
    private static final String QUERY = "query";
    private static final int LIMIT = 10;
    // Coupon columns
    private static final String COUPON_ID = "couponId";
    private static final String COUPON_CODE = "couponCode";
    private static final String COUPON_PROMOTION_ID = "couponPromotionId";
    private static final String COUPON_BOUGHT_DATE = "couponBoughtDay";
    private static final String COUPON_STATUS = "couponStatus";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRestaurantTableQuery = "CREATE TABLE " + TABLE_RES + "(" +
                RES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RES_COM_ID + " TEXT NOT NULL UNIQUE," +
                NAME + " TEXT NOT NULL," +
                IMAGE + " BLOB," +
                RES_LAST_UPDATE + " TEXT," +
                RES_ADDRESS + " TEXT," +
                RES_EMAIL + " TEXT," +
                RES_PHONE + " TEXT," +
                RES_POSTAL_CODE + " TEXT," +
                RES_SIZE1 + " TEXT," + RES_SIZE2 + " TEXT," +
                RES_MON_OPEN + " TEXT," + RES_MON_CLOSE + " TEXT," +
                RES_TUE_OPEN + " TEXT," + RES_TUE_CLOSE + " TEXT," +
                RES_WED_OPEN + " TEXT," + RES_WED_CLOSE + " TEXT," +
                RES_THUR_OPEN + " TEXT," + RES_THUR_CLOSE + " TEXT," +
                RES_FRI_OPEN + " TEXT," + RES_FRI_CLOSE + " TEXT," +
                RES_SAT_OPEN + " TEXT," + RES_SAT_CLOSE + " TEXT," +
                RES_SUN_OPEN + " TEXT," + RES_SUN_CLOSE + " TEXT," +
                RES_FULL_LAST_UPDATE + " TEXT," +
                RES_BACK_IMAGE + " BLOB," +
                RES_BACK_LAST_UPDATE + " TEXT" + ")";
        db.execSQL(createRestaurantTableQuery);

        String createHistoryTableQuery = "CREATE TABLE " + TABLE_HIS + "(" +
                HIS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                HIS_COM_ID + " TEXT NOT NULL UNIQUE," +
                START_TIME + " TEXT NOT NULL," +
                DURATION + " TEXT NOT NULL," +
                NUM_PEOPLE + " TEXT NOT NULL," +
                HIS_LAST_UPDATE +
                " TEXT" + ")";
        db.execSQL(createHistoryTableQuery);

        String createUserTableQuery = "CREATE TABLE " + TABLE_USER + "(" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USERNAME + " TEXT NOT NULL," +
                PASSWORD + " TEXT NOT NULL" + ")";
        db.execSQL(createUserTableQuery);

        String createSuggestionTableQuery = "CREATE TABLE " + TABLE_SUGGESTION + "(" +
                SUGGESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                QUERY + " TEXT NOT NULL" + ")";
        db.execSQL(createSuggestionTableQuery);

        String createPromotionTableQuery = "CREATE TABLE " + TABLE_PROMOTION + "(" +
                PROMOTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PROMOTION_PROM_ID + " TEXT NOT NULL UNIQUE," +
                PROMOTION_COM_ID + " TEXT NOT NULL," +
                PROMOTION_NAME + " TEXT NOT NULL," +
                PROMOTION_TOTAL_COST + " TEXT NOT NULL," +
                PROMOTION_ORIGINAL_COST + " TEXT NOT NULL," +
                PROMOTION_TOTAL_NUM + " TEXT NOT NULL," +
                PROMOTION_EXPIRY_DATE + " TEXT NOT NULL," +
                PROMOTION_IMAGE + " BLOB" + ")";
        db.execSQL(createPromotionTableQuery);

        String createCouponTableQuery = "CREATE TABLE " + TABLE_COUPON + "(" +
                COUPON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COUPON_CODE + " TEXT NOT NULL UNIQUE," +
                COUPON_PROMOTION_ID + " TEXT NOT NULL," +
                COUPON_BOUGHT_DATE + " TEXT NOT NULL," +
                COUPON_STATUS + " TEXT" + ")";
        db.execSQL(createCouponTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUGGESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUPON);
        onCreate(db);
    }

    public boolean addRestaurant(Restaurant restaurant) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            // get the current time
            String curTime = getCurrentDate().toString();
            // get the profile picture of this restaurant
            byte[] bArray = myInstance.bitmapToByteArray(restaurant.getImage());
            values.put(RES_COM_ID, restaurant.getComId());
            values.put(NAME, restaurant.getName());
            values.put(IMAGE, bArray);
            values.put(RES_LAST_UPDATE, curTime);
            try {
                if (db.insert(TABLE_RES, null, values) == -1) {
                    db.close();
                    return false;
                }
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                return false;
            }
            db.close();
            return true;
        }catch(IllegalStateException e){
            return addRestaurant(restaurant);
        }
    }

    public void updateResImage(String comId, Bitmap image){
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            String curTime = getCurrentDate().toString();

            ContentValues values = new ContentValues();
            values.put(IMAGE, myInstance.bitmapToByteArray(image));
            values.put(RES_LAST_UPDATE, curTime);

            String[] whereArgs = {comId};
            int numRow = db.update(TABLE_RES, values, RES_COM_ID + " = ?", whereArgs);

            db.close();
        }catch (IllegalStateException e){
            updateResImage(comId, image);
        }
    }

    public void updateFullInfo(String comId, String[] info){
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            String curTime = getCurrentDate().toString();

            ContentValues values = new ContentValues();
            values.put(RES_ADDRESS, info[0]);
            values.put(RES_EMAIL, info[1]);
            values.put(RES_PHONE, info[2]);
            values.put(RES_POSTAL_CODE, info[3]);
            values.put(RES_SIZE1, info[4]);
            values.put(RES_SIZE2, info[5]);
            values.put(RES_MON_OPEN, info[6]);
            values.put(RES_MON_CLOSE, info[7]);
            values.put(RES_TUE_OPEN, info[8]);
            values.put(RES_TUE_CLOSE, info[9]);
            values.put(RES_WED_OPEN, info[10]);
            values.put(RES_WED_CLOSE, info[11]);
            values.put(RES_THUR_OPEN, info[12]);
            values.put(RES_THUR_CLOSE, info[13]);
            values.put(RES_FRI_OPEN, info[14]);
            values.put(RES_FRI_CLOSE, info[15]);
            values.put(RES_SAT_OPEN, info[16]);
            values.put(RES_SAT_CLOSE, info[17]);
            values.put(RES_SUN_OPEN, info[18]);
            values.put(RES_SUN_CLOSE, info[19]);
            values.put(RES_FULL_LAST_UPDATE, curTime);

            String[] whereArgs = {comId};
            int numRow = db.update(TABLE_RES, values, RES_COM_ID + " = ?", whereArgs);

            db.close();
        }catch (IllegalStateException e){
            e.printStackTrace();
            updateFullInfo(comId, info);
        }
    }

    public void updateBackground(String comId, Bitmap backImage){
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase();

            String curTime = getCurrentDate().toString();

            ContentValues values = new ContentValues();
            values.put(RES_BACK_IMAGE, myInstance.bitmapToByteArray(backImage));
            values.put(RES_BACK_LAST_UPDATE, curTime);

            String[] whereArgs = {comId};
            int numRow = db.update(TABLE_RES, values, RES_COM_ID + " = ?", whereArgs);

            db.close();
        }catch (IllegalStateException e){
            e.printStackTrace();
            updateBackground(comId, backImage);
        }
    }

    public ArrayList<Object> loadFullInfo(String comId){
        try {
            ArrayList<Object> result = new ArrayList<>();

            SQLiteDatabase db;
            db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("Select " + RES_ADDRESS + "," + RES_EMAIL +
                            "," + RES_PHONE + "," + RES_POSTAL_CODE + "," + RES_SIZE1 +
                            "," + RES_SIZE2 + "," + RES_MON_OPEN + "," + RES_MON_CLOSE +
                            "," + RES_TUE_OPEN + "," + RES_TUE_CLOSE + "," + RES_WED_OPEN +
                            "," + RES_WED_CLOSE + "," + RES_THUR_OPEN + "," + RES_THUR_CLOSE +
                            "," + RES_FRI_OPEN + "," + RES_FRI_CLOSE + "," + RES_SAT_OPEN +
                            "," + RES_SAT_CLOSE + "," + RES_SUN_OPEN + "," + RES_SUN_CLOSE +
                            "," + RES_FULL_LAST_UPDATE + "," + RES_BACK_IMAGE + "," + RES_BACK_LAST_UPDATE +
                            " From " + TABLE_RES +
                            " Where " + RES_COM_ID + "=?",
                    new String[]{comId});

            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
            } else {
                return null;
            }

            for (int i = 0; i < cursor.getColumnCount(); i++) {
                if (i == 21) {
                    result.add(cursor.getBlob(i));
                } else {
                    result.add(cursor.getString(i));
                }
            }

            cursor.close();
            db.close();

            return result;
        }catch (IllegalStateException e){
            e.printStackTrace();
            return loadFullInfo(comId);
        }
    }

    public Restaurant getRestaurant(String comId) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_RES,
                    new String[]{RES_COM_ID, NAME, IMAGE, RES_LAST_UPDATE},
                    RES_COM_ID + "=?",
                    new String[]{String.valueOf(comId)},
                    null, null, null, null);

            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
            } else {
                return null;
            }

            Restaurant restaurant = new Restaurant(cursor.getString(0),
                    cursor.getString(1));
            Bitmap bmp = myInstance.byteArrayToBitmap(cursor.getBlob(2));
            restaurant.setImage(bmp);
            restaurant.setLastUpdated(cursor.getString(3));
            cursor.close();
            db.close();

            return restaurant;
        }catch (IllegalStateException e){
            e.printStackTrace();
            return getRestaurant(comId);
        }
    }

    public ArrayList<History> getAllHistory(){
        ArrayList<History> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * " +
                "FROM " + TABLE_HIS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                //starting with 1 because we used select *
                String comId = cursor.getString(1);
                String startTime = cursor.getString(2);
                String duration = cursor.getString(3);
                String numPeople = cursor.getString(4);
                String lastUpdate = cursor.getString(5);

                History history = new History(comId, startTime, duration, numPeople);
                History.setLastUpdatedTime(lastUpdate);
                result.add(history);
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
            return result;
        }else{
            cursor.close();
            db.close();
            return null;
        }
    }

    public void addSuggestion(String query){
        try{
            SQLiteDatabase db;
            db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(QUERY, query);


            db.delete(TABLE_SUGGESTION, QUERY + "=?", new String[]{query});
            db.insert(TABLE_SUGGESTION, null, values);
            db.close();

        }catch(IllegalStateException e){
            addSuggestion(query);
        }
    }

    public ArrayList<String> getSuggestion(){
        try{
            ArrayList<String> result = new ArrayList<>();
            SQLiteDatabase db = getReadableDatabase();

            String selectQuery = "SELECT *" +
                    " FROM " + TABLE_SUGGESTION +
                    " ORDER BY " + SUGGESTION_ID + " DESC" +
                    " LIMIT " + LIMIT;

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()){
                do{
                    result.add(cursor.getString(1));
                }while(cursor.moveToNext());
                cursor.close();
                db.close();
                return result;
            }else{
                cursor.close();
                db.close();
                return result;
            }

        }catch(IllegalStateException e){
            return getSuggestion();
        }
    }

    public void addCoupon(Coupon coupon){
        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COUPON_CODE, coupon.getExchangeCode());
            values.put(COUPON_PROMOTION_ID, coupon.getPromotionId());
            values.put(COUPON_BOUGHT_DATE, coupon.getBoughtTime());
            if (coupon.getStatus() == null){
            }else{
                values.put(COUPON_STATUS, coupon.getStatus());
            }

            db.insert(TABLE_COUPON, null, values);

        }catch (IllegalStateException e){
            e.printStackTrace();
            addCoupon(coupon);
        }
    }

    public ArrayList<Coupon> getCouponList(){
        try{
            ArrayList<Coupon> result = new ArrayList<>();

            SQLiteDatabase db = getReadableDatabase();

            String selectQuery = "Select *" +
                    " From " + TABLE_COUPON +
                    " Where " + COUPON_STATUS + " is NULL";

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()){
                do{
                    result.add(new Coupon(cursor.getString(2), cursor.getString(1), cursor.getString(3)));
                }while(cursor.moveToNext());
                cursor.close();
                db.close();
                return result;
            }else{
                cursor.close();
                db.close();
                return result;
            }

        }catch (IllegalStateException e){
            e.printStackTrace();
            return getCouponList();
        }
    }

    public ArrayList<Coupon> getCouponHistoryList(){
        try{
            ArrayList<Coupon> result = new ArrayList<>();

            SQLiteDatabase db = getReadableDatabase();

            String selectQuery = "Select *" +
                    " From " + TABLE_COUPON +
                    " Where " + COUPON_STATUS + " is not NULL";

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()){
                do{
                    Coupon temp = new Coupon(cursor.getString(2), cursor.getString(1), cursor.getString(3));
                    temp.setStatus(cursor.getString(4));
                    result.add(temp);
                }while(cursor.moveToNext());
                cursor.close();
                db.close();
                return result;
            }else{
                cursor.close();
                db.close();
                return result;
            }

        }catch (IllegalStateException e){
            e.printStackTrace();
            return getCouponHistoryList();
        }
    }

    public void addPromotion(Promotion promotion){
        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(PROMOTION_PROM_ID, promotion.getPromotionId());
            values.put(PROMOTION_COM_ID, promotion.getComId());
            values.put(PROMOTION_NAME, promotion.getPromotionName());
            values.put(PROMOTION_TOTAL_COST, promotion.getTotalCost());
            values.put(PROMOTION_ORIGINAL_COST, promotion.getOriginalCost());
            values.put(PROMOTION_TOTAL_NUM, promotion.getTotalNumber());
            values.put(PROMOTION_EXPIRY_DATE, promotion.getExpireDate());

            db.insert(TABLE_PROMOTION, null, values);
            db.close();

        }catch(IllegalStateException e){
            e.printStackTrace();
            addPromotion(promotion);
        }
    }

    public Promotion getPromotion(String promotionId){
        try{
            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.rawQuery("Select *" +
                        " From " + TABLE_PROMOTION +
                        " Where " + PROMOTION_PROM_ID + "=?",
                    new String[]{promotionId});

            if (cursor.getCount() != 0){
                cursor.moveToFirst();

                String promId = cursor.getString(1);
                String promComId = cursor.getString(2);
                String promName = cursor.getString(3);
                String promTotalCost = cursor.getString(4);
                String promOriginalCost = cursor.getString(5);
                int promTotalNum = Integer.parseInt(cursor.getString(6));
                String promExpiryDate = cursor.getString(7);

                Promotion promotion = new Promotion(promId, promComId, promName, 0, promTotalNum, promTotalCost, promOriginalCost, promExpiryDate);
                promotion.setPromotionImage(myInstance.byteArrayToBitmap(cursor.getBlob(8)));

                return promotion;

            }else{
                cursor.close();
                db.close();
                return null;
            }

        }catch (IllegalStateException e){
            e.printStackTrace();
            return getPromotion(promotionId);
        }
    }

    public void updatePromotionImage(String promotionId, Bitmap promotionImage){
        try{
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("Select " + PROMOTION_IMAGE +
                            " From " + TABLE_PROMOTION +
                            " Where " + PROMOTION_PROM_ID + "=?",
                    new String[]{promotionId});

            if (cursor.getCount() != 0){
                cursor.moveToFirst();
                if (cursor.getBlob(0) == null){
                    SQLiteDatabase wdb = getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(PROMOTION_IMAGE, myInstance.bitmapToByteArray(promotionImage));
                    wdb.update(TABLE_PROMOTION, values, PROMOTION_PROM_ID + "= ?", new String[]{promotionId});
                    wdb.close();
                    cursor.close();
                    db.close();
                }else{
                    cursor.close();
                    db.close();
                }
            }else{
                cursor.close();
                db.close();
            }

        }catch (IllegalStateException e){
            e.printStackTrace();
            updatePromotionImage(promotionId, promotionImage);
        }
    }

    public Timestamp getCurrentDate() {
        return new Timestamp(System.currentTimeMillis());
    }

    public void cleanCoupon(){
        try{
            SQLiteDatabase db = getWritableDatabase();
            int numEffect = db.delete(TABLE_COUPON, "1", null);
            db.close();
        }catch (IllegalStateException e){
            cleanCoupon();
        }
    }

}
