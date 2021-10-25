package logicreat.waitins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Aaron on 2016-07-04.
 */
public class HomeTab extends Fragment {

    private View view;

    private static ListView list;

    private static boolean finishedUpdate = true;

    private static DiscoveryListAdapter discoveryListAdapter;

    public static HomeTab newInstance(){
        HomeTab homeTab = new HomeTab();
        return homeTab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_discovery, container, false);
        this.view = view;

        setupHome();

        return view;
    }

    public void setupHome(){
        list = (ListView) view.findViewById(R.id.discovery_list);

        finishedUpdate = false;

        discoveryListAdapter =
                new DiscoveryListAdapter(MyApp.getCurActivity(), MyApp.getOurInstance().getDiscoveryResList());

        list.setAdapter(discoveryListAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant temp = MyApp.getOurInstance().getDiscoveryResList().get(position);
                String comId = temp.getComId();

                Intent intent = new Intent(MyApp.getCurActivity(), RestaurantActivity.class);
                intent.putExtra("comId", comId);
                MyApp.getCurActivity().startActivity(intent);
            }
        });
        finishedUpdate = true;
    }

    public static void updateHome() {
        Thread loadDiscoveryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                finishedUpdate = false;
                if (MyApp.finishedDiscoveryLoading) {
                    MyApp.getOurInstance().loadDiscoveryList();
                    MyApp.getCurActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            discoveryListAdapter.notifyDataSetChanged();
                        }
                    });
                }
                finishedUpdate = true;
            }
        });
        loadDiscoveryThread.start();

    }
}
