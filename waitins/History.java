package logicreat.waitins;

import android.util.Log;

import com.android.volley.toolbox.RequestFuture;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Aaron on 2016-05-17.
 */
public class History {
    private MyApp myInstance = MyApp.getOurInstance();

    private final String LOAD_RES_NAME_OPERATION = "6";

    private String comId;
    private String startTime;
    private String numPeople;
    private String resName;
    private String totalWaitingTime;
    private static String lastUpdatedTime;

    public History(String newComId, String startTime,
                   String duration, String numPeople) {
        this.comId = newComId;
        this.startTime = startTime;
        this.totalWaitingTime = duration;
        this.numPeople = numPeople;
        if (myInstance.getAllResManager().getRestaurant(comId) != null) {
            resName = myInstance.getAllResManager().getRestaurant(comId).getName();
        } else {

            Map<String, String> params = new HashMap<>();
            params.put("operation", LOAD_RES_NAME_OPERATION);
            params.put("comId", comId);

            resName = myInstance.sendSynchronousStringRequest(MyApp.LOADRES_URL, params);
        }
    }

    public String getNumPeople() {
        return numPeople;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public String getResName() {
        return resName;
    }

    public String getComId(){
        return comId;
    }

    public static String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public static void setLastUpdatedTime(String newLastUpdatedTime) {
        lastUpdatedTime = newLastUpdatedTime;
    }


}
