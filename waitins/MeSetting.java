package logicreat.waitins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aaron on 2016-07-13.
 */
public class MeSetting extends Fragment {
    Activity activity = MyApp.getCurActivity();

    private ExpandableListView expListView;
    private Button historyButton;

    private String[] settingList = {activity.getString(R.string.me_settings_clean_storage),
                                            activity.getString(R.string.me_settings_language),
                                            activity.getString(R.string.me_settings_feedback),
                                            activity.getString(R.string.me_settings_notification),
                                            activity.getString(R.string.me_settings_change_password),
                                            activity.getString(R.string.me_settings_check_history)};
//    HashMap<String, String> listDataChild = {"Change Password", "Clean Storage", "Language", "Feedback" , "Notification"};


    public static MeSetting newInstance(int page){
        MeSetting meSetting = new MeSetting();
        return meSetting;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//      listDataChild = new HashMap<String, String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.me_setting, container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        expListView = (ExpandableListView) view.findViewById(R.id.setting_exp_list_view);

        /*if (MyApp.getOurInstance().isGuest()){
            settingList = new String[]{activity.getString(R.string.me_settings_clean_storage),
                    activity.getString(R.string.me_settings_language),
                    activity.getString(R.string.me_settings_feedback),
                    activity.getString(R.string.me_settings_notification),
                    activity.getString(R.string.me_settings_change_password)};
        }*/
        final MeSettingListAdapter adapter = new MeSettingListAdapter(MyApp.getCurActivity(), new ArrayList<>(Arrays.asList(settingList)));

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
                //Log.d(TAG, "View name:" + view.getTransitionName());

                if (groupPosition == 0) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyApp.getCurActivity());
                    mBuilder.setNegativeButton("No", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MyApp.getOurInstance().deleteDatabase();
                                }
                            })
                            .setMessage(getString(R.string.delete_database))
                            .create()
                            .show();
                    return true;
                }

                if (groupPosition == 2) {
                    // return false to tell the system the click is not been handled so that
                    // the group will expand
                    return false;
                }

                if (groupPosition == 1) {
                    Intent intent = new Intent(activity, LanguageSelection.class);
                    activity.startActivity(intent);
                    return true;
                }

                if (groupPosition == 4) {
                    if (MyApp.getOurInstance().isGuest()) {
                        MyApp.getOurInstance().returnToLogin();
                        return true;
                    } else {
                        return false;
                    }
                }

                if (groupPosition == 3) {
                    return false;
                }

                if (groupPosition == 5){
                    if (MyApp.getOurInstance().isGuest()){
                        MyApp.getOurInstance().returnToLogin();
                        return true;
                    }else{
                        Intent intent = new Intent(MyApp.getCurActivity(), MyHistoryActivity.class);
                        MyApp.getCurActivity().startActivity(intent);
                        return true;
                    }
                }
                return true;
            }
        });


        expListView.setAdapter(adapter);
        return view;
    }

}
