package logicreat.waitins;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aaron on 2016-05-17.
 */
public class HistoryListAdapter extends ArrayAdapter<History> {
    private final Activity context;
    private final ArrayList<History> historyList;

    public HistoryListAdapter(Activity context, ArrayList<History> historyList){

        super(context, R.layout.historylist, historyList);

        this.context = context;
        this.historyList = historyList;
    }

    public View getView(int position, View rowView, ViewGroup parent){

        if (rowView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.historylist, null, true);
        }

        History temp = historyList.get(position);

        TextView tvResName = (TextView) rowView.findViewById(R.id.history_res_name);
        TextView tvPeople = (TextView) rowView.findViewById(R.id.history_people);
        TextView tvStartTime = (TextView) rowView.findViewById(R.id.history_start_time);
        TextView tvDuration = (TextView) rowView.findViewById(R.id.history_duration);
        ImageView ivImage = (ImageView) rowView.findViewById(R.id.history_image);

        tvResName.setText(temp.getResName());
        tvPeople.setText(String.format("%s:%s",MyApp.getCurActivity().getString(R.string.history_cell_number_people), temp.getNumPeople()));
        tvStartTime.setText(String.format("%s:%s",MyApp.getCurActivity().getString(R.string.history_cell_start_time), temp.getStartTime()));
        tvDuration.setText(String.format("%s:%s",MyApp.getCurActivity().getString(R.string.history_cell_wait_time), temp.getTotalWaitingTime()));

        Restaurant res = MyApp.getOurInstance().getAllResManager().getRestaurant(temp.getComId());
        if (res.getCircleImage() == null){
            res.setCircleImage(MyApp.getOurInstance().cropCircle(res.getImage()));
        }
        ivImage.setImageBitmap(res.getCircleImage());
        //if ()

        return rowView;
    }
}
