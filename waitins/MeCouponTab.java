package logicreat.waitins;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by Aaron on 2016-08-10.
 */
public class MeCouponTab extends Fragment {

    static ViewPager mPager;
    PagerSlidingTabStrip pagerTitleStrip;
    //private static MeCouponFragmentPagerAdapter meCouponFragmentPagerAdapter;

    //private View view;
    private static boolean loadingCoupon = false;

    public static MeCouponTab newInstance(){
        MeCouponTab meCouponTab = new MeCouponTab();
        return meCouponTab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.me_coupon_tab, container, false);

        mPager = (ViewPager) view.findViewById(R.id.me_coupon_view_pager);
        pagerTitleStrip = (PagerSlidingTabStrip) view.findViewById(R.id.me_coupon_title_strip);

        final MeCouponFragmentPagerAdapter meCouponFragmentPagerAdapter = new MeCouponFragmentPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(meCouponFragmentPagerAdapter);
        pagerTitleStrip.setViewPager(mPager);
        pagerTitleStrip.setIndicatorHeight(5);
        pagerTitleStrip.setIndicatorColor(R.color.colorPrimaryYellow);

        return view;
    }
}
