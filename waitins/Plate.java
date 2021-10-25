package logicreat.waitins;

import android.graphics.Bitmap;

/**
 * Created by Aaron on 2016-06-11.
 */
public class Plate {
    private String plateId;
    private String plateName;
    private String plateCost;
    private Bitmap plateImage;
    private String promotionId;

    public Plate(String plateId, String promotionId){
        this.plateId = plateId;
        this.promotionId = promotionId;
    }

    public String getPlateName() {
        return plateName;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public String getPlateCost() {
        return plateCost;
    }

    public void setPlateCost(String plateCost) {
        this.plateCost = plateCost;
    }

    public Bitmap getImage() {
        return plateImage;
    }

    public void setImage(Bitmap image) {
        this.plateImage = image;
    }

    public String getPlateId() {
        return plateId;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }

    public boolean imageExist(){
        return plateImage != null;
    }

}
