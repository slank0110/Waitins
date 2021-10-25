package logicreat.waitins;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Aaron on 2016-08-03.
 */
public class NewPlateListAdapter extends ArrayAdapter<Plate> {

    private Activity activity;
    private ArrayList<Plate> plateArrayList;

    public NewPlateListAdapter(Activity activity, ArrayList<Plate> plateArrayList){
        super(activity, R.layout.newplatecell, plateArrayList);
        this.activity = activity;
        this.plateArrayList = plateArrayList;
    }

    public View getView(final int position, View rowView, ViewGroup parent){
        if (rowView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.newplatecell, null, true);
        }

        final View view = rowView;
        final Plate plate = plateArrayList.get(position);

        ImageView plateImage = (ImageView) view.findViewById(R.id.plate_cell_image);

        if (plate.imageExist()){
            plateImage.setImageBitmap(plate.getImage());
        }else{
            Thread imageThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    plate.setImage(MyApp.getOurInstance().loadPlateImage(plate.getPlateId(), plate.getPromotionId()));
                    MyApp.getCurActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshView(view, position);
                        }
                    });
                }
            });
            imageThread.start();
        }

        return view;
    }

    public void refreshView(View rowView, int position){
        Plate plate = plateArrayList.get(position);

        ImageView plateImage = (ImageView) rowView.findViewById(R.id.plate_cell_image);

        plateImage.setImageBitmap(plate.getImage());
        notifyDataSetChanged();
    }

}
