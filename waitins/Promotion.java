package logicreat.waitins;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Aaron on 2016-06-11.
 */

public class Promotion {
    private String promotionId;
    private String comId;
    private String promotionName;
    private String totalCost;
    private String originalCost;
    private String resName;
    private String expireDate;

    private int numPlate;
    private int totalNumber;
    private int numberSold;

    private Bitmap promotionImage;

    private ArrayList<Plate> plateList;
    private String plateListString;
    private String description;

    private boolean plateLoaded;

    public Promotion(String promotionId, String comId, String promotionName,
                     int numPlate, int totalNumber, String totalCost, String originalCost,
                     String expireDate){
        this.promotionId = promotionId;
        this.comId = comId;
        this.promotionName = promotionName;
        this.numPlate = numPlate;
        this.totalNumber = totalNumber;
        //this.numberSold = numSold;
        this.totalCost = totalCost;
        this.originalCost = originalCost;
        this.expireDate = expireDate;
        this.plateLoaded = false;

        this.plateList = new ArrayList<>();
    }

    public String getExpireDate() {
        return expireDate;
    }

    public boolean isPlateLoaded() {
        return plateLoaded;
    }

    public void setPlateLoaded(boolean plateLoaded) {
        this.plateLoaded = plateLoaded;
    }

    public boolean imageExist(){
        return promotionImage != null;
    }

    public String getComId() {
        return comId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public String getOriginalCost() {
        return originalCost;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;

    }

    public int getNumPlate() {
        return numPlate;
    }

    public int getNumberSold() {
        return numberSold;
    }

    public void setNumberSold(int numberSold) {
        this.numberSold = numberSold;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public Bitmap getPromotionImage() {
        return promotionImage;
    }

    public void setPromotionImage(Bitmap promotionImage) {
        this.promotionImage = promotionImage;
    }

    public ArrayList<Plate> getPlateList() {
        return plateList;
    }

    public void setPlateList(ArrayList<Plate> plateList) {
        this.plateList = plateList;
    }

    public int getNumberLeft(){
        return totalNumber - numberSold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlateListString() {
        return plateListString;
    }

    public void setPlateListString(String plateListString) {
        this.plateListString = plateListString;
    }
}
