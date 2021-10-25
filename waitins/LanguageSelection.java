package logicreat.waitins;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class LanguageSelection extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_setting_language);
        //MyApp.setCurActivity(this);
        final Activity curActivity = this;
        final Activity activity = MyApp.getCurActivity();

        TextView english = (TextView) findViewById(R.id.me_setting_language_english);
        TextView chinese_simp = (TextView) findViewById(R.id.me_setting_language_cn_simplified);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(MyApp.width/2,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar, null), params);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // get the current language locale
        Locale current = getResources().getConfiguration().locale;

        // add OnClickListener to the languages
        if (current.toString().equals("zh")) {
            // remove the onClick listener on Simplified Chinese
            chinese_simp.setOnClickListener(null);
            english.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApp.getOurInstance().refreshLocale("en");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.finish();
                            activity.startActivity(activity.getIntent());
                            curActivity.finish();
                        }
                    });
                }
            });
        }
        else {
            english.setOnClickListener(null);
            chinese_simp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApp.getOurInstance().refreshLocale("zh");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.finish();
                            activity.startActivity(activity.getIntent());
                            curActivity.finish();
                        }
                    });
                }
            });
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
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
