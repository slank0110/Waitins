package logicreat.waitins;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Aaron on 2016-04-30.
 */
public class DiscoveryListAdapter extends ArrayAdapter<Restaurant> {
    private Activity context;

    private ArrayList<Restaurant> resList;

    private MyApp myInstance = MyApp.getOurInstance();

    TextView txtTitle;
    TextView txtDistance;
    ImageView imageView;
    ImageView gpsButton;

    public DiscoveryListAdapter(Activity context, ArrayList<Restaurant> resList) {
        super(context, R.layout.restaurantcell, resList);

        this.context = context;
        this.resList = resList;
    }

    public View getView(final int position, View rowView, ViewGroup parent) {

        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.restaurantcell, null, true);
        }

        final View view = rowView;

        final Restaurant res = resList.get(position);

        final String comId = res.getComId();
        final String resName = res.getName();
        final Bitmap image = res.getImage();
        final String[] status = res.getStatus();
        final String distance = res.getDistance();

        txtTitle = (TextView) view.findViewById(R.id.disc_card_view_res_name);
        txtDistance = (TextView) view.findViewById(R.id.disc_card_view_distance);

        imageView = (ImageView) view.findViewById(R.id.disc_card_image);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MyApp.width, (int)(MyApp.RESTAURANTPICTURERATIO * MyApp.width));
        imageView.setLayoutParams(params);


        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(MyApp.width, (int)(MyApp.RESTAURANTPICTURERATIO * MyApp.width) + MyApp.getOurInstance().dpToPx(45));
        view.setLayoutParams(params1);

        gpsButton = (ImageView) view.findViewById(R.id.disc_card_direction);

        TextView txtSmall = (TextView) rowView.findViewById(R.id.disc_card_view_inline_small_value);
        TextView txtMedium = (TextView) rowView.findViewById(R.id.disc_card_view_inline_medium_value);
        TextView txtBig = (TextView) rowView.findViewById(R.id.disc_card_view_inline_large_value);

        txtTitle.setText(resName);
        txtDistance.setText(distance);

        if (status == null){
            txtSmall.setText("-");
            txtMedium.setText("-");
            txtBig.setText("-");
        }else {
            txtSmall.setText(status[0]);
            txtMedium.setText(status[1]);
            txtBig.setText(status[2]);
        }

        if (image != null){
            imageView.setImageBitmap(image);
        }else{
            Thread loadImageThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> params = new HashMap<>();
                    params.put("operation", "1");
                    params.put("comId", comId);
                    resList.get(position).setImage(myInstance.sendSynchronousImageRequest(MyApp.LOADRES_URL, params));
                }
            });
            loadImageThread.start();
            //Log.d(TAG, "Server Error, no image for discovery");
        }

        if (distance == null){
            txtDistance.setText("Loading...");
            Thread distanceThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    res.setDistance(myInstance.getResDistance(comId));
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
            Thread distanceThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    res.setDistance(myInstance.getResDistance(comId));
                }
            });
            distanceThread.start();
        }

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread loadInfoThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        double[] temp = myInstance.getResLatLng(res.getComId());
                        final Intent intent = new Intent(MyApp.getCurActivity(), MapsActivity.class);
                        intent.putExtra("Lat", temp[0]);
                        intent.putExtra("Lon", temp[1]);
                        intent.putExtra("comId", comId);
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
        return view;
    }

    public void refreshCell(View rowView, final int position){

        final Restaurant res = resList.get(position);

        String resName = res.getName();
        Bitmap image = res.getImage();
        String[] status = res.getStatus();
        String distance = res.getDistance();

        TextView txtTitle = (TextView) rowView.findViewById(R.id.disc_card_view_res_name);
        TextView txtDistance = (TextView) rowView.findViewById(R.id.disc_card_view_distance);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.disc_card_image);
        ImageView gpsButton = (ImageView) rowView.findViewById(R.id.disc_card_direction);

        TextView txtSmall = (TextView) rowView.findViewById(R.id.disc_card_view_inline_small_value);
        TextView txtMedium = (TextView) rowView.findViewById(R.id.disc_card_view_inline_medium_value);
        TextView txtBig = (TextView) rowView.findViewById(R.id.disc_card_view_inline_large_value);

        txtTitle.setText(resName);
        txtDistance.setText(distance);

        if (status == null){
            txtSmall.setText("-");
            txtMedium.setText("-");
            txtBig.setText("-");
        }else {
            txtSmall.setText(status[0]);
            txtMedium.setText(status[1]);
            txtBig.setText(status[2]);
        }

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
                        double[] temp = myInstance.getResLatLng(res.getComId());
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
