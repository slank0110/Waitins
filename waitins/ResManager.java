package logicreat.waitins;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aaron on 2016-05-01.
 */
public class ResManager {

    private static final int MAX = 10;

    private Map<String, Restaurant> resMap;
    private List<String> resList;

    // constructer
    public ResManager(){
        resMap = new HashMap<>();
        resList = new ArrayList<>();
    }

    public ResManager(Map<String, Restaurant> resMap){
        this.resMap = resMap;
        resList = new ArrayList<>();
    }

    public Map<String, Restaurant> getResMap(){
        return this.resMap;
    }

    @Override
    public String toString(){
        String result = "";
        for (Map.Entry<String, Restaurant> entry : resMap.entrySet()){
            result += entry.getValue().getName() + "\n";
        }
        return result;
    }

    public void addToMap(Restaurant newRes, boolean priority){
        if (resList.size() >= MAX){
            resMap.remove(resList.get(0));
            resList.remove(0);
            if (!priority){
                resList.add(newRes.getComId());
            }
            // the else case should never happen
            // the list shouldn't excess with resList empty
        }else{
            if (!priority){
                resList.add(newRes.getComId());
            }
        }
        resMap.put(newRes.getComId(), newRes);
    }

    public boolean ifExist(String comId){
        return resMap.containsKey(comId);
    }

    public void delFromMap(String comId){
        if (ifExist(comId)){
            resMap.remove(comId);
        }
    }

    public Restaurant getRestaurant(String comId){
        if (ifExist(comId)){
            return resMap.get(comId);
        }else{
            return null;
        }
    }

    public void addPriority(String comId){
        if (resList.contains(comId)){
            resList.remove(comId);
        }
    }

    public void removePriority(String comId){
        if (!resList.contains(comId)){
            resList.add(comId);
        }
    }
}
