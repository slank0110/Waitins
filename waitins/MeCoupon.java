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
 * Created by Aaron on 2016-08-02.
 */
public class MeCoupon extends Fragment {

    static CouponListAdapter adapter;
    static ListView couponList;
    static View view;

    public static MeCoupon newInstance(){
        return new MeCoupon();
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.me_coupon, container, false);
        couponList = (ListView) view.findViewById(R.id.coupon_list);

        setupCoupon();

        return view;
    }

    public void setupCoupon(){
        adapter = new CouponListAdapter(MyApp.getCurActivity(), MyApp.getOurInstance().getCouponList());
        couponList.setAdapter(adapter);

        couponList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyApp.getCurActivity(), NewPlateActivity.class);
                intent.putExtra("promotionId", MyApp.getOurInstance().getCouponList().get(position).getPromotionId());
                MyApp.getCurActivity().startActivity(intent);
            }
        });
    }
}
