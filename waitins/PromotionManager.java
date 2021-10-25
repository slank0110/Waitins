package logicreat.waitins;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aaron on 2016-06-14.
 */
public class PromotionManager {


    private Map<String, Promotion> promotionMap;
    private ArrayList<String> promotionList;

    public PromotionManager(){
        promotionMap = new HashMap<>();
        promotionList = new ArrayList<>();
    }

    public void addPromotion(Promotion promotion){
        if (!ifExist(promotion.getPromotionId())) {
            promotionMap.put(promotion.getPromotionId(), promotion);
            promotionList.add(promotion.getPromotionId());
        }else{
        }
    }

    public Promotion getPromotion(String promotionId){
        return promotionMap.get(promotionId);
    }

    public boolean ifExist(String promotionId){
        return promotionMap.containsKey(promotionId);
    }

    public Map<String, Promotion> getPromotionMap() {
        return promotionMap;
    }

    public ArrayList<String> getPromotionList(){
        return promotionList;
    }

}
