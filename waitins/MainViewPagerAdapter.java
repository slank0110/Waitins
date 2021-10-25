package logicreat.waitins;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Aaron on 2016-07-04.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment curFragment;

    public MainViewPagerAdapter(FragmentManager fm){
        super(fm);

        fragments.clear();
        fragments.add(HomeTab.newInstance());
        fragments.add(PromotionTab.newInstance());
        fragments.add(InLineTab.newInstance());
        fragments.add(MeTab.newInstance());
    }

    public Fragment getCurFragment(){
        return curFragment;
    }

    @Override
    public Fragment getItem(int position){
        return fragments.get(position);
    }

    @Override
    public int getCount(){
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object){
        if (getCurFragment() != object){
            curFragment = ((Fragment) object);
        }

        super.setPrimaryItem(container, position, object);
    }

}
