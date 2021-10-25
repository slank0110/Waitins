package logicreat.waitins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

// FragmentActivity
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MyApp myInstance;

    private GoogleMap mMap;
    final private Activity activity = this;
    LatLng curCamera;
    LatLng newLatLng;

    String comId;

    String numBefore = "-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setTitle("Map");
        myInstance = MyApp.getOurInstance(this.getApplicationContext());
        MyApp.setCurActivity(activity);

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("Lat", 43.8073300);
        double lon = intent.getDoubleExtra("Lon", -79.4231150);
        comId = intent.getStringExtra("comId");

        newLatLng = new LatLng(lat, lon);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // problem with gps location

        mMap = googleMap;

        //LatLng curLocation;
        Location location = null;

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try{
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }catch(SecurityException e){
            e.printStackTrace();
        }

        if (location != null && newLatLng.longitude == -79.4231150 && newLatLng.latitude == 43.8073300) {
            newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }else{
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 15));
        try {
            mMap.setMyLocationEnabled(true);
        }catch(SecurityException e){
            e.printStackTrace();
        }

        mMap.setBuildingsEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);


        curCamera = mMap.getCameraPosition().target;

        final Response.Listener<String> mapResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(!response.equals("&")) {
                    String[] restaurants = response.split("\\|");

                    for (int i = 0; i < restaurants.length; i++) {
                        String[] info = restaurants[i].split("&");
                        newLatLng = new LatLng(Double.parseDouble(info[3]), Double.parseDouble(info[2]));
                        MarkerOptions marker = new MarkerOptions();
                        marker.position(newLatLng);
                        marker.title(info[1] + "|" + info[0]);

                        if (info[4].equals("1")){
                            marker.icon(MyApp.getOurInstance().getLeisure());
                        }else if (info[4].equals("2")){
                            marker.icon(MyApp.getOurInstance().getBusy());
                        }else if (info[4].equals("3")){
                            marker.icon(MyApp.getOurInstance().getFull());
                        }

                        Marker temp = mMap.addMarker(marker);
                        if (info[0].equals(comId)){
                            temp.showInfoWindow();
                        }
                    }
                }
            }
        };

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mMap.clear();
                curCamera = cameraPosition.target;
                Map<String, String> params = new HashMap<>();
                params.put("longitude", ""+curCamera.longitude);
                params.put("latitude", ""+curCamera.latitude);
                params.put("distance", "3");
                GeneralStringRequest mapRequest = new GeneralStringRequest(MyApp.MAP_URL, params, mapResponseListener);
                myInstance.addRequest(mapRequest);
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.infowindow, null);

                TextView tvResName = (TextView) v.findViewById(R.id.info_res_name);

                tvResName.setText(marker.getTitle().split("\\|")[0]);

                return v;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker){
                final Marker tempMarker = marker;
                String[] info = marker.getTitle().split("\\|");
                final String comId = info[1];
                final String resName = info[0];
                Thread requestThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myInstance.getRestaurant(comId, resName, false);
                        String[] status = myInstance.getResStatus(comId);

                        numBefore = "" + (Integer.parseInt(status[0]) + Integer.parseInt(status[1]) + Integer.parseInt(status[2]));
                        //myInstance.checkDatabase(comId, resName, false, false);
                        //numBefore = myInstance.getResStatus(comId)[0];
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tempMarker.setSnippet(numBefore);
                                tempMarker.showInfoWindow();
                            }
                        });
                    }
                });
                requestThread.start();
                //marker.showInfoWindow();
                return true;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Log.d(TAG, marker.getSnippet());
                Intent intent = new Intent(activity, RestaurantActivity.class);
                intent.putExtra("comId", marker.getTitle().split("\\|")[1]);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        MyApp.setCurActivity(activity);
        myInstance.checkTicketCall();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //MyApp.setCurActivity(null);
    }
}