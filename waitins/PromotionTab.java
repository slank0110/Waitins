package logicreat.waitins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by Aaron on 2016-07-04.
 */
public class PromotionTab extends Fragment {
    private static final int LOAD_ONCE = 8;

    private static PromotionAdapter adapter;
    private static ArrayList<Promotion> promotionList;
    private static ArrayList<Promotion> adapterPromotionList;
    private static int curPosition;

    private static boolean updatingPromotion = false;

    private static GridView promotionGrid;

    private int preLast = -1;
    private boolean finish = true;

    public static PromotionTab newInstance(){
        PromotionTab promotionTab = new PromotionTab();
        return promotionTab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_promotion, container, false);

        setupPromotion(view);
        return view;
    }

    public void setupPromotion(View view){
        promotionGrid = (GridView) view.findViewById(R.id.promotion_gridview);

        adapterPromotionList = new ArrayList<>();

        promotionList = MyApp.getOurInstance().getPromotionTabPromotionList();

        curPosition = 0;

        promotionGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE){
                }
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL){
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getId() == R.id.promotion_gridview){
                    final int lastItem = firstVisibleItem + visibleItemCount;

                    if (totalItemCount != 0 && lastItem == totalItemCount){
                        if (adapter != null && preLast != lastItem && finish){
                            preLast = lastItem;
                            Thread loadPromotion = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    finish = false;
                                    loadMore(curPosition);
                                    finish = true;
                                }
                            });
                            loadPromotion.start();
                        }
                    }
                }
            }
        });

        promotionGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String promotionId = promotionList.get(position).getPromotionId();
                Intent intent = new Intent(MyApp.getCurActivity(), NewPlateActivity.class);
                intent.putExtra("promotionId", promotionId);

                MyApp.getCurActivity().startActivity(intent);
            }
        });

        Thread loadPromotionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadMore(curPosition);

                adapter = new PromotionAdapter(MyApp.getCurActivity(), adapterPromotionList);
                promotionGrid.post(new Runnable() {
                    @Override
                    public void run() {
                        promotionGrid.setAdapter(adapter);
                    }
                });
            }
        });
        loadPromotionThread.start();
    }

    public static void loadMore(int start){
        int end;
        if ((LOAD_ONCE + start) > promotionList.size()){
            end = promotionList.size();
        }else{
            end = LOAD_ONCE + start;
        }


        for (int i = start; i < end; i++){
            //String promotionId = promotionList.get(i);
            Promotion promotion = promotionList.get(i);
            promotion.setResName(MyApp.getOurInstance().getResName(promotion.getComId()));
            adapterPromotionList.add(promotion);
            curPosition ++;
        }


        if (adapter != null){
            MyApp.getCurActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public static void updatePromotion(){
        if (promotionList != null) {
            if (!updatingPromotion) {
                Thread updatingPromotionThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updatingPromotion = true;

                        if (MyApp.getOurInstance().loadPromotion()){
                            promotionList = MyApp.getOurInstance().getPromotionTabPromotionList();
                            adapterPromotionList.clear();
                            curPosition = 0;
                            loadMore(curPosition);
                            adapter = new PromotionAdapter(MyApp.getCurActivity(), adapterPromotionList);
                            promotionGrid.post(new Runnable() {
                                @Override
                                public void run() {
                                    promotionGrid.setAdapter(adapter);
                                }
                            });
                        }

                        for (int i = 0; i < promotionList.size(); i++) {
                            //String promotionId = promotionList.get(i);
                            Promotion promotion = promotionList.get(i);
                            if (!MyApp.getOurInstance().loadPromotionStatus(promotion.getPromotionId()).equals("")){
                                promotion.setNumberSold(Integer.parseInt(MyApp.getOurInstance().loadPromotionStatus(promotion.getPromotionId())));
                            }
                        }

                        if (adapter != null) {
                            MyApp.getCurActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        updatingPromotion = false;
                    }
                });
                updatingPromotionThread.start();
            }
        }
    }
}
