package logicreat.waitins;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.RequestFuture;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Aaron on 2016-04-30.
 */
public class SearchListAdapter extends ArrayAdapter<String> {
    private Activity context;
    private ArrayList<String> itemName;
    private ArrayList<String> comIdList;
    private ArrayList<String[]> statusList;
    private ArrayList<String> distanceList;
    private ArrayList<Bitmap> imgBitmap;

    private MyApp myInstance = MyApp.getOurInstance();

    TextView txtTitle;
    TextView txtDistance;
    ImageView imageView;
    ImageView gpsButton;

    String resName;
    String comId;
    String[] status;
    String distance;
    Bitmap image;

    public SearchListAdapter(Activity context, ArrayList<String> comIdList,
                                ArrayList<String> itemName, ArrayList<Bitmap> imgBitmap,
                                ArrayList<String[]> statusList, ArrayList<String> distanceList) {
        super(context, R.layout.restaurantcell, itemName);

        this.context = context;
        this.comIdList = comIdList;
        this.itemName = itemName;
        this.imgBitmap = imgBitmap;
        this.statusList = statusList;
        this.distanceList = distanceList;
    }

    public View getView(final int position, View rowView, ViewGroup parent) {

        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.restaurantcell, null, true);
        }

        final View view = rowView;
        comId = comIdList.get(position);
        resName = itemName.get(position);
        image = imgBitmap.get(position);
        status = statusList.get(position);
        distance = distanceList.get(position);

        txtTitle = (TextView) rowView.findViewById(R.id.disc_card_view_res_name);
        txtDistance = (TextView) rowView.findViewById(R.id.disc_card_view_distance);

        imageView = (ImageView) rowView.findViewById(R.id.disc_card_image);
        gpsButton = (ImageView) rowView.findViewById(R.id.disc_card_direction);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MyApp.width, (int)(MyApp.RESTAURANTPICTURERATIO * MyApp.width));
        imageView.setLayoutParams(params);


        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(MyApp.width, (int)(MyApp.RESTAURANTPICTURERATIO * MyApp.width) + MyApp.getOurInstance().dpToPx(45));
        view.setLayoutParams(params1);

        txtTitle.setText(resName);
        txtDistance.setText(distance);
        imageView.setImageBitmap(image);

        TextView txtSmall = (TextView) rowView.findViewById(R.id.disc_card_view_inline_small_value);
        TextView txtMedium = (TextView) rowView.findViewById(R.id.disc_card_view_inline_medium_value);
        TextView txtBig = (TextView) rowView.findViewById(R.id.disc_card_view_inline_large_value);

        txtSmall.setText(status[0]);
        txtMedium.setText(status[1]);
        txtBig.setText(status[2]);

        if (distance == null){
            txtDistance.setText("Loading...");
            Thread distanceThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    distanceList.set(position, myInstance.getResDistance(comId));
                    MyApp.getCurActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshCell(view, position);
                        }
                    });
                }
            });
            distanceThread.start();
        }else{
            txtDistance.setText(distance);
        }

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread loadInfoThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        double[] temp = myInstance.getResLatLng(comIdList.get(position));
                        final Intent intent = new Intent(MyApp.getCurActivity(), MapsActivity.class);
                        intent.putExtra("Lat", temp[0]);
                        intent.putExtra("Lon", temp[1]);
                        MyApp.getCurActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyApp.getCurActivity().startActivity(intent);
                            }
                        });
                    }
                });
                loadInfoThread.start();
            }
        });

        // finish a view
        return rowView;
    }

    public void refreshCell(View rowView, final int position){

        String resName = itemName.get(position);
        Bitmap image = imgBitmap.get(position);
        String status[] = statusList.get(position);
        String distance = distanceList.get(position);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.disc_card_view_res_name);
        TextView txtDistance = (TextView) rowView.findViewById(R.id.disc_card_view_distance);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.disc_card_image);
        ImageView gpsButton = (ImageView) rowView.findViewById(R.id.disc_card_direction);

        TextView txtSmall = (TextView) rowView.findViewById(R.id.disc_card_view_inline_small_value);
        TextView txtMedium = (TextView) rowView.findViewById(R.id.disc_card_view_inline_medium_value);
        TextView txtBig = (TextView) rowView.findViewById(R.id.disc_card_view_inline_large_value);

        txtSmall.setText(status[0]);
        txtMedium.setText(status[1]);
        txtBig.setText(status[2]);

        txtTitle.setText(resName);
        txtDistance.setText(distance);

        if (!(image == null)){
            imageView.setImageBitmap(image);
        }else{
        }

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread loadInfoThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        double[] temp = myInstance.getResLatLng(comIdList.get(position));
                        final Intent intent = new Intent(MyApp.getCurActivity(), MapsActivity.class);
                        intent.putExtra("Lat", temp[0]);
                        intent.putExtra("Lon", temp[1]);
                        MyApp.getCurActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyApp.getCurActivity().startActivity(intent);
                            }
                        });
                    }
                });
                loadInfoThread.start();
            }
        });
        notifyDataSetChanged();
    }
}
