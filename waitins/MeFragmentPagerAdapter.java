package logicreat.waitins;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by Administrator on 2016/6/18 0018.
 */
public class MeFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    final int PAGE_COUNT = 3;
    private int tabIcons[] = {R.drawable.ic_action_inline, R.drawable.ic_action_action_favorite_outline, R.drawable.me_3rd_tab};

    Fragment[] fragments = new Fragment[3];

    FragmentManager fm;

    public MeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment temp;
        if (position == 0){
            temp = MeSetting.newInstance(position + 1);
            //fragments[0] = temp;
            //return temp;
        }else if (position == 1){
            if (MyApp.getOurInstance().isGuest()){
                temp = MeNotLogin.newInstance();
                //fragments[1] = temp;
                //return temp;
            }else {
                temp = MeFavourite.newInstance(position + 1);
                //return MeFavourite.newInstance(position + 1);
            }
        }else{
            temp = MeCouponTab.newInstance();
        }
        fragments[position] = temp;
        return temp;
    }

    public void changeFragment(int page){
        boolean guest = MyApp.getOurInstance().isGuest();
        if (page == 0){
            fm.beginTransaction().remove(fragments[0]).commit();
            fragments[0] = MeSetting.newInstance(page + 1);
            notifyDataSetChanged();
        } else if (page == 1){
            if (fragments[1] instanceof MeNotLogin && !guest){
                fm.beginTransaction().remove(fragments[1]).commit();
                fragments[1] = MeFavourite.newInstance(page + 1);
            }else if (fragments[1] instanceof MeFavourite && guest){
                fm.beginTransaction().remove(fragments[1]).commit();
                fragments[1] = MeNotLogin.newInstance();
            }
            notifyDataSetChanged();
        }else if (page == 2){
            if (fragments[2] instanceof MeNotLogin && !guest){
                fm.beginTransaction().remove(fragments[2]).commit();
                fragments[2] = MeCouponTab.newInstance();
            }else if (fragments[2] instanceof MeCouponTab && guest){
                fm.beginTransaction().remove(fragments[2]).commit();
                fragments[2] = MeNotLogin.newInstance();
            }
            notifyDataSetChanged();
        }
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        // Generate title based on item position
//        return tabTitles[position];
//    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
}