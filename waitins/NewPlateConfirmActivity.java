package logicreat.waitins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class NewPlateConfirmActivity extends AppCompatActivity {

    private String promotionId;

    TextView totalCost;
    TextView promotionTitle;
    TextView firstValue;
    TextView secondValue;
    TextView hstValue;
    TextView totalValue;

    RelativeLayout relativeLayout;

    ListView confirmListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotion_confirm);

        totalCost = (TextView) findViewById(R.id.promotion_confirm_total_cost_value);
        promotionTitle = (TextView) findViewById(R.id.recepit_first);
        firstValue = (TextView) findViewById(R.id.value_first);
        secondValue = (TextView) findViewById(R.id.value_second);
        hstValue = (TextView) findViewById(R.id.plate_confirm_hst_value);
        totalValue = (TextView) findViewById(R.id.plate_confirm_total_cost);
        confirmListView = (ListView) findViewById(R.id.promotion_confirm_list_view);

        relativeLayout = (RelativeLayout) findViewById(R.id.promotion_confirm_relativeLayout);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(MyApp.width, (int)(MyApp.PREPAYMENTPICTURERATIO * MyApp.width));
        relativeLayout.setLayoutParams(params1);

        Intent intent = getIntent();
        promotionId = intent.getStringExtra("promotionId");

        Promotion curPromotion = MyApp.getOurInstance().getPromotionManager().getPromotion(promotionId);

        ArrayList<String> dishName = new ArrayList<>();
        String[] plateList = curPromotion.getPlateListString().split("&");

        for (int i = 0 ; i < plateList.length; i ++){
            dishName.add(plateList[i]);
        }

        PlateConfirmListAdapter plateConfirmListAdapter = new PlateConfirmListAdapter(this, dishName);

        confirmListView.setAdapter(plateConfirmListAdapter);

        DecimalFormat df = new DecimalFormat("#.00");

        totalCost.setText("$" + df.format(Double.parseDouble(curPromotion.getTotalCost())));
        promotionTitle.setText(curPromotion.getPromotionName());
        firstValue.setText("$" + df.format(Double.parseDouble(curPromotion.getTotalCost())));
        secondValue.setText("$" + df.format(Double.parseDouble(curPromotion.getTotalCost())));
        hstValue.setText("$" + df.format(Double.parseDouble(curPromotion.getTotalCost()) * 0.13));
        totalValue.setText("$" + df.format((Double.parseDouble(curPromotion.getTotalCost()) * 1.13)));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(MyApp.width/2,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar, null), params);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Button paymentButton = (Button) findViewById(R.id.promotion_confirm_payment_button);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to payment web page
                Intent paymentWeb = new Intent(NewPlateConfirmActivity.this, PaymentActivity.class);
                paymentWeb.putExtra("promotionId", promotionId);
                startActivity(paymentWeb);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        MyApp.setCurActivity(this);
        MyApp.getOurInstance().checkTicketCall();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
