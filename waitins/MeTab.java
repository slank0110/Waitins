package logicreat.waitins;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Aaron on 2016-07-05.
 */
public class MeTab extends Fragment {

    static ViewPager mPager;
    private static MeFragmentPagerAdapter mPagerAdapter;

    private View view;

    private static boolean finishedUpdate = true;

    private static TextView savedHour;
    private static TextView savedMin;
    private static TextView savedMoney;

    private static boolean cur;

    public static MeTab newInstance(){
        MeTab meTab = new MeTab();
        return meTab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_me, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        this.view = view;
        savedHour = (TextView) view.findViewById(R.id.me_time_saved_hr_value);
        savedMin = (TextView) view.findViewById(R.id.me_timed_saved_min_value);
        savedMoney = (TextView) view.findViewById(R.id.me_money_saved_value);
        cur = MyApp.getOurInstance().isGuest();

        setupMe();

        return view;
    }

    public void setupMe(){
        mPager = (ViewPager) view.findViewById(R.id.me_view_pager);

        mPagerAdapter = new MeFragmentPagerAdapter(MyApp.getOurInstance().getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.me_pager_indicator);
        tabStrip.setViewPager(mPager);
        tabStrip.setIndicatorHeight(5);
        tabStrip.setIndicatorColorResource(R.color.colorPrimaryYellow);
    }

    public static void updateMe() {
        if (mPager != null) {
            Thread updateThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    finishedUpdate = false;

                    if (!MyApp.getOurInstance().isGuest()) {

                        Map<String, String> params = new HashMap<>();
                        String response = MyApp.getOurInstance().sendSynchronousStringRequest(MyApp.LOAD_ME_VISIT_URL, params);

                        if (!response.equals("%&!#") && !response.equals("@") && !response.equals(MyApp.NETWORK_ERROR)){
                            String[] info = response.split("\\|");
                            int min = Integer.parseInt(info[0]);
                            final String money = info[1];

                            final int hour = min/60;
                            final int minute = min%60;

                            MyApp.getCurActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    savedHour.setText("" + hour);
                                    savedMin.setText("" + minute);
                                    savedMoney.setText(money);
                                }
                            });
                        }

                        MyApp.getOurInstance().loadCouponList();
                        MyApp.getCurActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (MeCoupon.adapter != null) {
                                    MeCoupon.adapter.notifyDataSetChanged();
                                }
                            }
                        });
                        MyApp.getOurInstance().loadCouponHistoryList();
                        MyApp.getCurActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (MeCouponHistory.adapter != null) {
                                    MeCouponHistory.adapter.notifyDataSetChanged();
                                }
                            }
                        });
                        //MyApp.getOurInstance().loadFavouriteList();
                        MyApp.getOurInstance().updateAllFavouriteStatus();
                    }else{
                        MyApp.getCurActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                savedMoney.setText("-");
                                savedHour.setText("-");
                                savedMin.setText("-");
                            }
                        });
                    }

                    //final MeFragmentPagerAdapter mPagerAdapter = new MeFragmentPagerAdapter(MyApp.getOurInstance().getFragmentManager());
                    MyApp.getCurActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (MeCoupon.adapter != null) {
                                    MeCoupon.adapter.notifyDataSetChanged();
                                }
                                if (MeFavourite.favouriteListAdapter != null) {
                                    MeFavourite.favouriteListAdapter.notifyDataSetChanged();
                                }

                                //mPager.setAdapter(null);
                                //mPager.setAdapter(mPagerAdapter);
                                mPagerAdapter.notifyDataSetChanged();
                                // bug alert
                                //mPagerAdapter.changeFragment(0);
                                if (cur != MyApp.getOurInstance().isGuest()) {
                                    cur = MyApp.getOurInstance().isGuest();
                                    mPagerAdapter.changeFragment(1);
                                    mPagerAdapter.notifyDataSetChanged();
                                }
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                            //mPager.setCurrentItem(0);
                        }
                    });
                    finishedUpdate = true;
                }
            });
            if (finishedUpdate) {
                updateThread.start();
            } else {
            }
        }
    }

    public static void loginSwitch(){
        if (mPagerAdapter != null) {
            mPagerAdapter.changeFragment(0);
            mPagerAdapter.changeFragment(1);
            mPagerAdapter.changeFragment(2);
            mPagerAdapter.notifyDataSetChanged();
        }
    }
}
