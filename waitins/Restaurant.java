package logicreat.waitins;

import android.app.Activity;
import android.graphics.Bitmap;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Aaron on 2016-04-30.
 */
public class Restaurant {

    // initial information
    private String comId;
    private String name;
    private Bitmap image;
    private Bitmap circleImage;

    // Restaurant Card information
    private String address;
    private String email;
    private String phoneNumber;
    private String postalCode;
    private String tableSize1;
    private String tableSize2;
    private Bitmap backGroundImage;
    private boolean isFullInfoSet;

    // estimated time
    private int small;
    private int medium;
    private int large;
    private long lastEstimatedUpdate;

    // in line status
    private String[] status;

    // Hours of operation
    private String monOpen;
    private String monClose;
    private String tueOpen;
    private String tueClose;
    private String wedOpen;
    private String wedClose;
    private String thurOpen;
    private String thurClose;
    private String friOpen;
    private String friClose;
    private String satOpen;
    private String satClose;
    private String sunOpen;
    private String sunClose;

    // last update time
    private String lastUpdated;
    private String resFullLastUpdate;
    private String resBackLastUpdate;

    // distance
    private String distance;

    public Restaurant(String comId, String name){
        this.comId = comId;
        this.name = name;
        this.isFullInfoSet = false;
        this.lastEstimatedUpdate = -1;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean equals(Restaurant other){
        return this.comId.equals(other.getComId());
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdated(){
        return this.lastUpdated;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public Bitmap getImage(){
        return this.image;
    }

    public String getName(){
        return this.name;
    }

    public String getAddress(){
        return this.address;
    }

    public String getComId(){
        return this.comId;
    }

    public Bitmap getBackGroundImage() {
        return backGroundImage;
    }

    public void setBackGroundImage(Bitmap backGroundImage) {
        this.backGroundImage = backGroundImage;
        this.resBackLastUpdate = new Timestamp(System.currentTimeMillis()).toString();
    }

    public boolean isFullInfoSet() {
        return isFullInfoSet;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTableSize1() {
        return tableSize1;
    }

    public String getTableSize2() {
        return tableSize2;
    }

    public int getLarge() {
        return large;
    }

    public void setLarge(int large) {
        this.large = large;
    }

    public int getSmall() {
        return small;
    }

    public void setSmall(int small) {
        this.small = small;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public long getLastEstimatedUpdate() {
        return lastEstimatedUpdate;
    }

    public String[] getStatus(){
        return status;
    }

    public void setStatus(String[] status){
        this.status = status;
    }

    public void setLastEstimatedUpdate(long lastEstimatedUpdate) {
        this.lastEstimatedUpdate = lastEstimatedUpdate;
    }

    public boolean isEstimatedTimeUpdated(){
        return this.lastEstimatedUpdate == -1 ||
                (System.currentTimeMillis() - this.lastEstimatedUpdate > 3600000);
    }

    public String getResFullLastUpdate() {
        return resFullLastUpdate;
    }

    public void setResFullLastUpdate(String resFullLastUpdate) {
        this.resFullLastUpdate = resFullLastUpdate;
    }

    public String getResBackLastUpdate() {
        return resBackLastUpdate;
    }

    public void setResBackLastUpdate(String resBackLastUpdate) {
        this.resBackLastUpdate = resBackLastUpdate;
    }

    public void setFullResInfo(/*String address, String email, String phoneNumber,
                               String postalCode, String tableSize1, String tableSize2,
                               String monOpen, String monClose, String tueOpen, String tueClose,
                               String wedOpen, String wedClose, String thurOpen, String thurClose,
                               String friOpen, String friClose, String satOpen, String satClose,
                               String sunOpen, String sunClose,*/
                               String[] info){
        this.address = info[0]; this.email = info[1]; this.phoneNumber = info[2];
        this.postalCode = info[3]; this.tableSize1 = info[4]; this.tableSize2 = info[5];
        this.monOpen = info[6]; this.monClose = info[7];
        this.tueOpen = info[8]; this.tueClose = info[9];
        this.wedOpen = info[10]; this.wedClose = info[11];
        this.thurOpen = info[12]; this.thurClose = info[13];
        this.friOpen = info[14]; this.friClose = info[15];
        this.satOpen = info[16]; this.satClose = info[17];
        this.sunOpen = info[18]; this.sunClose = info[19];
        this.isFullInfoSet = true;
        this.resFullLastUpdate = new Timestamp(System.currentTimeMillis()).toString();
    }

    public String[] getFullResInfo(){
        String[] result = {this.address, this.email, this.phoneNumber,
                this.postalCode, this.tableSize1, this.tableSize2,
                this.monOpen, this.monClose, this.tueOpen, this.tueClose,
                this.wedOpen, this.wedClose, this.thurOpen, this.thurClose,
                this.friOpen, this.friClose, this.satOpen, this.satClose,
                this.sunOpen, this.sunClose};
        return result;
    }

    public String[] getTodayHour(){

        String[] result = new String[2];
        Long tsLong = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(tsLong);

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
            result[0] = this.monOpen;
            result[1] = this.monClose;
            return result;
        }else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
            result[0] = this.tueOpen;
            result[1] = this.tueClose;
            return result;
        }else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
            result[0] = this.wedOpen;
            result[1] = this.wedClose;
            return result;
        }else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
            result[0] = this.thurOpen;
            result[1] = this.thurClose;
            return result;
        }else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
            result[0] = this.friOpen;
            result[1] = this.friClose;
            return result;
        }else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            result[0] = this.satOpen;
            result[1] = this.satClose;
            return result;
        }else{
            result[0] = this.sunOpen;
            result[1] = this.sunClose;
            return result;
        }
    }

    public String getHourOperation(){

        // get current activity
        Activity cur = MyApp.getCurActivity();

        String result = "";
        result += cur.getString(R.string.restaurant_card_ho_monday) + checkClose(this.monOpen, this.monClose) + "\n";
        result += cur.getString(R.string.restaurant_card_ho_tuesday) + checkClose(this.tueOpen, this.tueClose) + "\n";
        result += cur.getString(R.string.restaurant_card_ho_wednesday) + checkClose(this.wedOpen, this.wedClose) + "\n";
        result += cur.getString(R.string.restaurant_card_ho_thursday) + checkClose(this.thurOpen, this.thurClose) + "\n";
        result += cur.getString(R.string.restaurant_card_ho_friday) + checkClose(this.friOpen, this.friClose) + "\n";
        result += cur.getString(R.string.restaurant_card_ho_saturday) + checkClose(this.satOpen, this.satClose) + "\n";
        result += cur.getString(R.string.restaurant_card_ho_sunday) + checkClose(this.sunOpen, this.sunClose);
        return result;
    }

    public String checkClose(String open, String close){
        if (open.equals("$")){
            return "Close";
        }else{
            return open + " - " + close;
        }
    }

    public Bitmap getCircleImage() {
        return circleImage;
    }

    public void setCircleImage(Bitmap circleImage) {
        this.circleImage = circleImage;
    }

    public String toString(){
        return String.format("%s\n%s", getName(), getAddress());
    }
}
