package logicreat.waitins;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aaron on 2016-08-02.
*/
public class CouponListAdapter extends ArrayAdapter<Coupon> {
    private Activity context;
    private ArrayList<Coupon> couponList;

    public CouponListAdapter(Activity context, ArrayList<Coupon> couponList){
        super(context, R.layout.me_couponcell, couponList);
        this.context = context;
        this.couponList = couponList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        final Coupon coupon = couponList.get(position);
        Promotion curCouponPromotion = null;

        if (MyApp.getOurInstance().getPromotionManager().ifExist(coupon.getPromotionId())){
            curCouponPromotion = MyApp.getOurInstance().getPromotionManager().getPromotion(coupon.getPromotionId());
        }

        if (convertView == null){
            convertView = inflater.inflate(R.layout.me_couponcell, null, true);
        }

        final View view = convertView;

        TextView promoResName = (TextView) view.findViewById(R.id.me_coupon_res_name);
        TextView promoTitle = (TextView) view.findViewById(R.id.me_coupon_coupon_name);
        TextView promoBuyDate = (TextView) view.findViewById(R.id.me_coupon_buy_date);
        TextView promoExpireDate = (TextView) view.findViewById(R.id.me_coupon_expiry_date);
        TextView promoCode = (TextView) view.findViewById(R.id.me_coupon_view_code);

        if (curCouponPromotion != null){
            promoResName.setText(curCouponPromotion.getResName());
            promoTitle.setText(curCouponPromotion.getPromotionName());
            promoExpireDate.setText(curCouponPromotion.getExpireDate());
        }

        promoBuyDate.setText(coupon.getBoughtTime());

        promoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyApp.getCurActivity());
                mBuilder.setMessage(coupon.getExchangeCode())
                        .setNegativeButton("Ok", null)
                        .create()
                        .show();
            }
        });

        return view;
    }
}
