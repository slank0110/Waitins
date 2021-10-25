package logicreat.waitins;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Aaron on 2016-06-16.
 */
public class PromotionAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Promotion> promotionList;

    private MyApp myInstance = MyApp.getOurInstance();

    public PromotionAdapter(Activity activity, ArrayList<Promotion> promotionList){
        this.activity = activity;
        this.promotionList = promotionList;
    }

    @Override
    public int getCount(){
        return promotionList.size();
    }

    @Override
    public Object getItem(int position){
        return promotionList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final Promotion promotion = promotionList.get(position);


        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.promotioncell, null, true);
        }
        if (MyApp.gridHeight != 0) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MyApp.width / 2, MyApp.gridHeight / 2);
            convertView.findViewById(R.id.promotion_cell_relative).setLayoutParams(params);
            convertView.setLayoutParams(params);
        }

        final View view = convertView;

        TextView txtTitle = (TextView) view.findViewById(R.id.promotion_cell_title);
        TextView txtPrice = (TextView) view.findViewById(R.id.promotion_cell_price);
        TextView txtRestaurant = (TextView) view.findViewById(R.id.promotion_cell_restaurant_name);
        TextView txtSold = (TextView) view.findViewById(R.id.promotion_cell_sold);
        TextView txtLeft = (TextView) view.findViewById(R.id.promotion_cell_left);

        // changing text fonts
        txtTitle.setTypeface(MyApp.getPromotion_title_font());
        txtPrice.setTypeface(MyApp.getPromotion_price_font());
        txtSold.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
        txtLeft.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));

        final ImageView imgBack = (ImageView) view.findViewById(R.id.promotion_cell_background);

        //Log.d(TAG, "position " + position + " is " + promotion.getPromotionName());
        txtTitle.setText(promotion.getPromotionName());
        DecimalFormat df = new DecimalFormat("#.00");
        txtPrice.setText("$" + df.format(Double.parseDouble(promotion.getTotalCost())));
        txtRestaurant.setText(promotion.getResName());
        txtSold.setText(""+promotion.getNumberSold());
        txtLeft.setText(""+promotion.getNumberLeft());

        if (promotion.imageExist()) {
            //Log.d(TAG, "position:" + position + " image exist");
            imgBack.setImageBitmap(promotion.getPromotionImage());
        }else {
            Thread loadImageThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap promotionImage = myInstance.loadPromotionImage(promotion.getPromotionId());
                    promotion.setPromotionImage(promotionImage);
                    MyApp.getOurInstance().getDbHandler().updatePromotionImage(promotion.getPromotionId(), promotionImage);

                    MyApp.getCurActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshCell(view, position);
                        }
                    });
                }
            });
            loadImageThread.start();
        }
        return view;
    }

    public void refreshCell(View convertView, int position){
        Promotion promotion = promotionList.get(position);

        TextView txtTitle = (TextView) convertView.findViewById(R.id.promotion_cell_title);
        TextView txtPrice = (TextView) convertView.findViewById(R.id.promotion_cell_price);
        TextView txtRestaurant = (TextView) convertView.findViewById(R.id.promotion_cell_restaurant_name);
        TextView txtSold = (TextView) convertView.findViewById(R.id.promotion_cell_sold);
        TextView txtLeft = (TextView) convertView.findViewById(R.id.promotion_cell_left);
        ImageView imgBack = (ImageView) convertView.findViewById(R.id.promotion_cell_background);

        txtTitle.setText(promotion.getPromotionName());
        txtPrice.setText(promotion.getTotalCost());
        txtRestaurant.setText(promotion.getResName());
        txtSold.setText(""+promotion.getNumberSold());
        txtLeft.setText(""+promotion.getNumberLeft());
        if (promotion.imageExist()) {
            imgBack.setImageBitmap(promotion.getPromotionImage());
        }else{
            imgBack.setImageBitmap(myInstance.getDefaultPromotionPicture());
        }
        notifyDataSetChanged();
    }
}
