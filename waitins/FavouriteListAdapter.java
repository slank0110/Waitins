package logicreat.waitins;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aaron on 2016-04-30.
 */
public class FavouriteListAdapter extends ArrayAdapter<Restaurant> {
    private Activity context;
    private ArrayList<Restaurant> favouriteList;

    private MyApp myInstance = MyApp.getOurInstance();

    public FavouriteListAdapter(Activity context, ArrayList<Restaurant> favouriteList) {
        super(context, R.layout.me_favouritecell, favouriteList);

        this.context = context;
        this.favouriteList = favouriteList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null){
            convertView = inflater.inflate(R.layout.me_favouritecell, null, true);
        }

        final View view = convertView;

        TextView txtTitle = (TextView) view.findViewById(R.id.me_favourite_name);
        TextView txtAddress = (TextView) view.findViewById(R.id.me_favourite_address);
        TextView txtSmallStatus = (TextView) view.findViewById(R.id.me_favourite_inline_value_small);
        TextView txtMediumStatus = (TextView) view.findViewById(R.id.me_favourite_inline_value_medium);
        TextView txtLargeStatus = (TextView) view.findViewById(R.id.me_favourite_inline_value_large);
        Button bInLine = (Button) view.findViewById(R.id.me_favourite_lineup_button);
        ImageView imageView = (ImageView) view.findViewById(R.id.me_favourite_item_image_inside);

        final Restaurant temp = favouriteList.get(position);
        final String[] status = temp.getStatus();

        bInLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comId = temp.getComId();

                Intent intent = new Intent(MyApp.getCurActivity(), RestaurantActivity.class);
                intent.putExtra("comId", comId);
                MyApp.getCurActivity().startActivity(intent);
            }
        });

        txtTitle.setText(temp.getName());
        txtAddress.setText(temp.getAddress());
        if (status != null) {
            txtSmallStatus.setText(status[0]);
            txtMediumStatus.setText(status[1]);
            txtLargeStatus.setText(status[2]);
        }else{
            txtSmallStatus.setText("-");
            txtMediumStatus.setText("-");
            txtLargeStatus.setText("-");
        }

        //temp = resManager.getRestaurant(comIdList.get(position));
        try {
            if (temp.getCircleImage() == null) {
                temp.setCircleImage(myInstance.cropCircle(temp.getImage()));
            }
            imageView.setImageBitmap(temp.getCircleImage());
        }catch(OutOfMemoryError e){
            e.printStackTrace();
        }
        return view;
    }

    /*public void refreshCell(View view, final int position){
        TextView txtTitle = (TextView) view.findViewById(R.id.me_favourite_name);
        TextView txtAddress = (TextView) view.findViewById(R.id.me_favourite_address);
        TextView txtPhone = (TextView) view.findViewById(R.id.me_favourite_phone_num);
        TextView txtStatus = (TextView) view.findViewById(R.id.me_favourite_inline_value);
        Button bInLine = (Button) view.findViewById(R.id.me_favourite_lineup_button);

        final Restaurant temp = favouriteList.get(position);


    }*/
}