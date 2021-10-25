package logicreat.waitins;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/18 0018.
 */
public class MeFavourite extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private MyApp myInstance = MyApp.getOurInstance();

    private int mPage;

    private static boolean loadingFavourite = false;

    static FavouriteListAdapter favouriteListAdapter;

    //RelativeLayout favouriteProgress;

    public static MeFavourite newInstance(int page) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MeFavourite meFavourite = new MeFavourite();
        meFavourite.setArguments(args);
        return meFavourite;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Thread loadFavouriteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadingFavourite = true;
                myInstance.loadFavouriteList();
                loadingFavourite = false;
            }
        });

        if (!loadingFavourite) {
            loadFavouriteThread.start();
        }

        mPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_favourite, container, false);
        ListView favouriteList = (ListView) view.findViewById(R.id.favourite_list);

        setupFavourite(favouriteList);
        return view;
    }

    public void setupFavourite(final ListView lv){

        Thread loadFavouriteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                favouriteListAdapter = new FavouriteListAdapter(MyApp.getCurActivity(), myInstance.getFavouriteResList());
                MyApp.getCurActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv.setAdapter(favouriteListAdapter);
                    }
                });
            }
        });
        loadFavouriteThread.start();
    }
}
