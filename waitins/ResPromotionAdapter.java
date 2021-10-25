package logicreat.waitins;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Aaron on 2016-07-15.
 */

public class ResPromotionAdapter extends ArrayAdapter<Promotion> {
    private final Activity context;
    private final ArrayList<Promotion> promotionList;

    public ResPromotionAdapter(Activity context, ArrayList<Promotion> promotionList){
        super(context, R.layout.respromotioncell, promotionList);
        this.context = context;
        this.promotionList = promotionList;
    }

    public View getView(int position, View rowView, ViewGroup parent){
        if (rowView == null){
            rowView = context.getLayoutInflater().inflate(R.layout.respromotioncell, null, true);
        }

        TextView tvPromotionName = (TextView) rowView.findViewById(R.id.res_promotion_cell_name);
        TextView tvPromotionPrice = (TextView) rowView.findViewById(R.id.res_promotion_cell_price);
        TextView tvPromotionLeft = (TextView) rowView.findViewById(R.id.res_promotion_cell_left);

        Promotion temp = promotionList.get(position);
        DecimalFormat df = new DecimalFormat("#.00");

        tvPromotionName.setText(temp.getPromotionName());
        tvPromotionPrice.setText("" + df.format(Double.parseDouble(temp.getTotalCost())));
        tvPromotionLeft.setText("" + (temp.getTotalNumber() - temp.getNumberSold()));

        return rowView;
    }
}
