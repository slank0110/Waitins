package logicreat.waitins;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.*;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class MeSettingListAdapter extends BaseExpandableListAdapter {

    Activity context;

    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title

    public MeSettingListAdapter(Activity context, List<String> listDataHeader){
        this.context = context;
        this.listDataHeader = listDataHeader;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // if childview is Password
        if (groupPosition == 4) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.me_setting_password, null);
            final EditText oldPass = (EditText) convertView.findViewById(R.id.me_setting_old_password);
            final EditText newPass = (EditText) convertView.findViewById(R.id.me_setting_new_password);
            final EditText newPassRetype = (EditText) convertView.findViewById(R.id.me_setting_new_password_again);
            final Button saveButton = (Button) convertView.findViewById(R.id.me_setting_confirm_button);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread changePasswordThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveButton.setClickable(false);

                            String message = "new Password does not match";

                            String oldPassword = oldPass.getText().toString();
                            String newPassword = newPass.getText().toString();
                            String newPasswordRetype = newPassRetype.getText().toString();

                            if (newPassword.equals(newPasswordRetype)) {

                                Map<String, String> params = new HashMap<>();
                                params.put("password", oldPassword);
                                params.put("newPassword", newPassword);
                                String response = MyApp.getOurInstance().sendSynchronousStringRequest(MyApp.CHANGE_PASSWORD_URL, params);

                                if (response.equals("$")){
                                    message = "success, please re-login";
                                }else if (response.equals("@")){
                                    message = "server error, please again later";
                                }else if (response.equals("*")){
                                    message = "fail, password length excess 32 or less then 8";
                                }else if (response.equals(MyApp.NETWORK_ERROR)){
                                    message = "network error";
                                }else if (response.equals("%&!#")){
                                    message = "session timeout, relogging in, please try again later";
                                    MyApp.getOurInstance().reLogin();
                                }
                            }

                            final String text = message;
                            MyApp.getCurActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MyApp.getCurActivity(), text, Toast.LENGTH_LONG).show();
                                    if (text.equals("success, please re-login")){
                                        MainActivity.logOut();
                                        Intent intent = new Intent(MyApp.getCurActivity(), LoginActivity.class);
                                        MyApp.getCurActivity().startActivity(intent);
                                    }
                                }
                            });

                            saveButton.setClickable(true);
                        }
                    });
                    changePasswordThread.start();
                }
            });
        }

        // if childview clicked is feedback
        else if (groupPosition == 2) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.me_feedback, null);
            final EditText feedbackText = (EditText) convertView.findViewById(R.id.feedback_text);
            final Button button = (Button) convertView.findViewById(R.id.feedback_button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread sendFeedbackThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            button.setClickable(false);
                            String text = feedbackText.getText().toString();
                            Map<String, String> params = new HashMap<>();
                            params.put("suggestion", text);

                            String message = "";
                            String response = MyApp.getOurInstance().sendSynchronousStringRequest(MyApp.FEEDBACK_URL, params);

                            if (response.equals("$")){
                                message = "Success";
                            }else if (response.equals("@")){
                                message = "Fail";
                            }else if (response.equals(MyApp.NETWORK_ERROR)){
                                message = "Network error";
                            }

                            final String feedback = message;
                            MyApp.getCurActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MyApp.getCurActivity(), feedback, Toast.LENGTH_LONG).show();
                                }
                            });

                            button.setClickable(true);
                        }
                    });
                    sendFeedbackThread.start();
                }
            });
        }else if (groupPosition == 3){
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.me_notification, null);
            final TextView notificationText = (TextView) convertView.findViewById(R.id.me_notification_text);

            int permissionCheck = ContextCompat.checkSelfPermission(MyApp.getCurActivity(), Manifest.permission.VIBRATE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED){
                notificationText.setText(MyApp.getCurActivity().getString(R.string.me_setting_notification_granted_text));
            }else{
                notificationText.setText(MyApp.getCurActivity().getString(R.string.me_setting_notification_denied_text));
            }
        }
        else {

        }

        return convertView;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.settingcell, null);
        }

        TextView list_header = (TextView) convertView
                .findViewById(R.id.setting_option);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        list_header.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 4 || groupPosition == 2 || groupPosition == 3) {
            return 1;
        }
        else {
            return 0;
        }
    }

}