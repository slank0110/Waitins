package logicreat.waitins;

/**
 * Created by Aaron on 2016-08-04.
 */
public class Coupon {

    private String promotionId;
    private String boughtTime;
    private String exchangeCode;
    private String status;

    public Coupon(String promotionId, String exchangeCode, String boughtTime){
        this.promotionId = promotionId;
        this.boughtTime = boughtTime;
        this.exchangeCode = exchangeCode;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public String getBoughtTime() {
        return boughtTime;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
