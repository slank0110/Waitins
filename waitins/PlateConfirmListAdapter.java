package logicreat.waitins;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class PlateConfirmListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> dishName;
    private Activity context;

    public PlateConfirmListAdapter(Activity context, ArrayList<String> dishName) {
        super(context, R.layout.plate_confirm_list_cell, dishName);

        this.context = context;
        this.dishName = dishName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = context.getLayoutInflater().inflate(R.layout.plate_confirm_list_cell, null, true);
        }

        TextView dishText = (TextView) convertView.findViewById(R.id.plate_confirm_listview_item);

        dishText.setText(dishName.get(position));

        return convertView;
    }

}
