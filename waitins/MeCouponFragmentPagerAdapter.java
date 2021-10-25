package logicreat.waitins;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by Aaron on 2016-08-10.
 */
public class MeCouponFragmentPagerAdapter extends FragmentStatePagerAdapter{

    private final String[] TAB_NAME = { MyApp.getCurActivity().getString(R.string.me_coupon_available), MyApp.getCurActivity().getString(R.string.me_coupon_history)};

    private final int PAGE_COUNT = 2;

    Fragment[] fragments = new Fragment[PAGE_COUNT];

    //private

    FragmentManager fm;

    public MeCouponFragmentPagerAdapter(FragmentManager fm){
        super(fm);
        //Log.d(TAG, "new instance");
        this.fm = fm;
    }

    @Override
    public int getCount(){
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position){
        Fragment temp;
        if (position == 0){
            temp = MeCoupon.newInstance();
        }else {
            temp = MeCouponHistory.newInstance();
        }

        fragments[position] = temp;

        return temp;
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return TAB_NAME[position];
    }

    public void changeFragment(int page){
        try {
            if (page == 0) {
                fm.beginTransaction().remove(fragments[0]).commit();
                fragments[0] = MeCoupon.newInstance();
                notifyDataSetChanged();
            } else if (page == 1) {
                fm.beginTransaction().remove(fragments[1]).commit();
                fragments[1] = MeCouponHistory.newInstance();
                notifyDataSetChanged();
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
            /*try {
                Thread.sleep(500);
                changeFragment(page);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }*/
        }
    }
}
