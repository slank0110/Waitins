package logicreat.waitins;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.lucasr.twowayview.TwoWayView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aaron on 2016-08-03.
 */
public class NewPlateActivity extends AppCompatActivity {

    private Activity activity = this;
    private String promotionId;
    private Promotion promotion;

    private ArrayList<Promotion> otherPromotion;
    private ArrayList<Plate> plateList;
    private TwoWayView list;

    private TextView promotionTitle;
    private TextView promotionSalePrice;
    private TextView promotionOriginalPrice;
    private TextView promotionExpireDate;
    private TextView promotionNumSold;
    private TextView promotionPlateDetail;
    private TextView promotionRestaurantNotice;
    private TextView promotionPromotionTitle;
    private ListView promotionOtherPromotion;
    private Button promotionGetItNow;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plate);

        MyApp.setCurActivity(activity);

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(MyApp.width/2,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar, null), params);

        actionBar.setDisplayHomeAsUpEnabled(true);

        list = (TwoWayView) findViewById(R.id.promotion_item_detail_listview);

        promotionPromotionTitle = (TextView) findViewById(R.id.promotion_item_detail_promotion_title);
        promotionTitle = (TextView) findViewById(R.id.promotion_item_detail_title);
        promotionSalePrice = (TextView) findViewById(R.id.promotion_item_detail_sale_price_value);
        promotionOriginalPrice = (TextView) findViewById(R.id.promotion_item_detail_original_price_value);
        promotionExpireDate = (TextView) findViewById(R.id.promotion_item_detail_valid_till_date);
        promotionNumSold = (TextView) findViewById(R.id.promotion_item_detail_sold_value);
        promotionPlateDetail = (TextView) findViewById(R.id.promotion_item_detail_plate_detail_text);
        promotionRestaurantNotice = (TextView) findViewById(R.id.promotion_item_detail_restaurant_notice_text);
        promotionOtherPromotion = (ListView) findViewById(R.id.promotion_item_detail_other_promotion);
        promotionGetItNow = (Button) findViewById(R.id.promotion_item_detail_button);

        promotionGetItNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to confirm payment screen
                Intent intent = new Intent(activity, NewPlateConfirmActivity.class);
                intent.putExtra("promotionId", promotionId);
                activity.startActivity(intent);
            }
        });

        Intent intent = getIntent();
        promotionId = intent.getStringExtra("promotionId");
        promotion = MyApp.getOurInstance().getPromotionManager().getPromotion(promotionId);


        plateList = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#.00");

        promotionPromotionTitle.setText(promotion.getPromotionName());
        promotionTitle.setText(promotion.getResName());
        promotionSalePrice.setText("$" + df.format(Double.parseDouble(promotion.getTotalCost())));
        promotionOriginalPrice.setText("$" + df.format(Double.parseDouble(promotion.getOriginalCost())));
        promotionExpireDate.setText(promotion.getExpireDate());
        //promotionNumSold.setText(promotion.getNumberSold());
        promotionPlateDetail.setText("A Big Fat fish\n" +
        "A Bit Fat Fries\n" +
        "A very delicious shit.");
        promotionRestaurantNotice.setText("Smith had omitted the paragraph in question (an omission which had escaped notice for twenty years) on the ground that it was unnecessary and misplaced; but Magee suspected him of having been influenced by deeper reasons.\n" +
                "Read more at http://sentence.yourdictionary.com/paragraph#6XhbMKvc8dIELs2B.99ignorethislinenothingtoseeordohere");

        setupListView();
        setPromotionList();
    }

    public void setupListView(){
        Thread loadPlateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = MyApp.getOurInstance().loadPromotionInfo(promotionId);

                if (response.equals(MyApp.NETWORK_ERROR)){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Network error: please try again later", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                final String[] info = response.split("\\(\\$\\&\\!\\%\\^\\*\\)");


                String[] plateLists = info[1].split("&");
                String plateListText = "";
                for (int i = 0; i < plateLists.length; i++){
                    plateListText += plateLists[i] + "\n";
                }

                final String text = plateListText;

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        promotionPlateDetail.setText(text);
                        promotionRestaurantNotice.setText(info[0]);
                        promotion.setPlateListString(text);
                        promotion.setDescription(info[0]);
                    }
                });

                String plateId;

                for (int i = 2; i < info.length; i++){
                    plateId = info[i];

                    Plate plate = new Plate(plateId, promotion.getPromotionId());
                    plate.setImage(null);
                    plateList.add(plate);
                }

                promotion.setPlateList(plateList);
                promotion.setPlateLoaded(true);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewPlateListAdapter plateListAdapter = new NewPlateListAdapter(activity, plateList);
                        list.setAdapter(plateListAdapter);
                    }
                });
            }
        });
        if (promotion.isPlateLoaded()){
            plateList = promotion.getPlateList();
            promotionPlateDetail.setText(promotion.getPlateListString());
            promotionRestaurantNotice.setText(promotion.getDescription());
            NewPlateListAdapter plateListAdapter = new NewPlateListAdapter(activity, plateList);
            list.setAdapter(plateListAdapter);
        }else {
            loadPlateThread.start();
        }
    }

    public void setPromotionList(){
        Thread setPromotionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Promotion> tempList = new ArrayList<>();

                Map<String, String> params = new HashMap<>();
                params.put("operation", "11");
                params.put("comId", promotion.getComId());

                String response = MyApp.getOurInstance().sendSynchronousStringRequest(MyApp.LOADRES_URL, params);

                if (!response.equals("$") && !response.equals(MyApp.NETWORK_ERROR)){
                    String[] promotions = response.split("\\|");
                    for (int i = 0; i < promotions.length; i++){
                        String[] info = promotions[i].split("&");
                        if (!promotion.getPromotionId().equals(info[0])){
                            if (MyApp.getOurInstance().getPromotionManager().ifExist(info[0])) {
                                tempList.add(MyApp.getOurInstance().getPromotionManager().getPromotion(info[0]));
                            } else {
                                Promotion newPromotion = new Promotion(info[0], info[1], info[2],
                                        Integer.parseInt(info[3]), Integer.parseInt(info[4]),
                                         info[6], info[7], info[8]);
                                newPromotion.setNumberSold(Integer.parseInt(info[5]));
                                MyApp.getOurInstance().getPromotionManager().addPromotion(newPromotion);
                                tempList.add(newPromotion);
                            }
                        }
                    }
                }

                final ResPromotionAdapter resPromotionAdapter = new ResPromotionAdapter(activity, tempList);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        promotionOtherPromotion.setAdapter(resPromotionAdapter);
                        setListViewHeightBasedOnChildren(promotionOtherPromotion);
                        promotionOtherPromotion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String promotionId = tempList.get(i).getPromotionId();
                                Intent intent = new Intent(MyApp.getCurActivity(), NewPlateActivity.class);
                                intent.putExtra("promotionId", promotionId);
                                MyApp.getCurActivity().finish();
                                MyApp.getCurActivity().startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
        setPromotionThread.start();
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        // calculating the total height by adding the height of every view inside listview
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                // setting up for measure
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        // change the height of list view
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        MyApp.setCurActivity(activity);
        MyApp.getOurInstance().checkTicketCall();
    }
}
