package logicreat.waitins;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aaron on 2016-07-05.
 */
public class InLineTab extends Fragment {
    private static TextView tvWaitingNumber;
    private static TextView tvNumberPeople;
    private static TextView tvTicketTime;
    private static TextView tvEstimateTime;
    private static TextView tvWaitingText;
    private static TextView tvAccpetCallText;
    private static TextView tvPeopleWaiting;
    private static Button bGps;
    private static ImageView ivImage;
    private static long startTime = 0;

    private static View view;

    private static Handler timerHandler = new Handler();
    private static Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int)(millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int hour = minutes / 60;
            minutes = minutes % 60;

            tvTicketTime.setText(String.format("%d:%d:%02d",hour, minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    public static InLineTab newInstance(){
        InLineTab inLineTab = new InLineTab();
        return inLineTab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_in_line, container, false);

        this.view = view;

        setupInLine();

        return view;
    }

    public void setupInLine(){
        ivImage = (ImageView) view.findViewById(R.id.inline_imageview);
        tvWaitingNumber = (TextView) view.findViewById(R.id.inline_your_waiting_num_value);
        tvNumberPeople = (TextView) view.findViewById(R.id.inline_before_you_value);
        tvEstimateTime = (TextView) view.findViewById(R.id.inline_estimated_wait_time_value);
        tvWaitingText = (TextView) view.findViewById(R.id.inline_your_waiting_num);
        tvTicketTime = (TextView) view.findViewById(R.id.inline_how_long_waited_value);
        tvAccpetCallText = (TextView) view.findViewById(R.id.inline_accept_call_message);
        tvPeopleWaiting = (TextView) view.findViewById(R.id.inline_people_in_line_value);
        bGps = (Button) view.findViewById(R.id.inline_map_button);


        setupInLineInfo();
    }

    public static void setupInLineInfo(){
        if (MyApp.getTicketNum().equals("0")){
            String notInLineMessage = MyApp.getCurActivity().getString(R.string.inline_not_in_line);
            if (MyApp.getOurInstance().isGuest()){
                notInLineMessage = MyApp.getCurActivity().getString(R.string.inline_requires_login_ticket);
            }
            ((TextView)view.findViewById(R.id.inline_not_text)).setText(notInLineMessage);
            tvWaitingNumber.setText(notInLineMessage);
            tvNumberPeople.setText("");
            tvEstimateTime.setText("");
            tvTicketTime.setText("");
            tvPeopleWaiting.setText("");
            view.findViewById(R.id.inline_layout).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.inline_not_text).setVisibility(View.VISIBLE);
        }else{
            setTvEstimateTime();
            tvWaitingText.setText(MyApp.getCurActivity().getString(R.string.inline_your_waiting_num) + MyApp.getOurInstance().getAllResManager().getRestaurant(MyApp.getLineUpComId()).getName());
            tvWaitingNumber.setText(MyApp.getTicketNum());
            tvNumberPeople.setText(MyApp.getNumPeopleBefore());
            tvPeopleWaiting.setText(MyApp.getNumPeople());
            startTime = MyApp.getStartTime();
            bGps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(MyApp.getCurActivity(), MapsActivity.class);
                    intent.putExtra("Lat", MyApp.getLat());
                    intent.putExtra("Lon", MyApp.getLon());
                    intent.putExtra("comId", MyApp.getLineUpComId());
                    MyApp.getCurActivity().startActivity(intent);
                }
            });
            timerHandler.postDelayed(timerRunnable, 0);

            if (MyApp.isDidAccept()){
                tvAccpetCallText.setVisibility(View.VISIBLE);
            }else{
                tvAccpetCallText.setVisibility(View.GONE);
            }
            view.findViewById(R.id.inline_not_text).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.inline_layout).setVisibility(View.VISIBLE);
        }
    }

    public static void setTvEstimateTime(){
        Thread loadEstimatedThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("numberPeople", MyApp.getNumPeople());
                params.put("comId", MyApp.getLineUpComId());

                final String temp = MyApp.getOurInstance().sendSynchronousStringRequest(MyApp.LOAD_ESTIMATED_URL, params);

                if (temp.equals("$")){
                    tvEstimateTime.post(new Runnable() {
                        @Override
                        public void run() {
                            tvEstimateTime.setText("N/A");
                        }
                    });
                }else if (temp.equals("@")){
                }else if (temp.equals(MyApp.NETWORK_ERROR)){
                } else {
                    tvEstimateTime.post(new Runnable() {
                        @Override
                        public void run() {
                            tvEstimateTime.setText("" + (Integer.parseInt(temp) * Integer.parseInt(MyApp.getNumPeople())));
                        }
                    });
                }
                MyApp.getCurActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //myInstance.hideLoading();
                        view.findViewById(R.id.activity_in_line).setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        loadEstimatedThread.start();
    }

}
