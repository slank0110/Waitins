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

/**
 * Created by Aaron on 2016-08-10.
 */
public class MeCouponHistory extends Fragment {
    static CouponHistoryListAdapter adapter;
    static ListView couponHistoryList;
    static View view;

    public static MeCouponHistory newInstance(){
        return new MeCouponHistory();
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.me_coupon_history, container, false);
        couponHistoryList = (ListView) view.findViewById(R.id.coupon_history_list);

        setupCoupon();

        return view;
    }

    public void setupCoupon(){
        adapter = new CouponHistoryListAdapter(MyApp.getCurActivity(), MyApp.getOurInstance().getCouponHistoryList());
        couponHistoryList.setAdapter(adapter);

        couponHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyApp.getCurActivity(), NewPlateActivity.class);
                intent.putExtra("promotionId", MyApp.getOurInstance().getCouponList().get(position).getPromotionId());
                MyApp.getCurActivity().startActivity(intent);
            }
        });
    }
}
