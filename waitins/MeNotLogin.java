package logicreat.waitins;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Aaron on 2016-07-13.
 */
public class MeNotLogin extends Fragment{

    public static MeNotLogin newInstance(){
        MeNotLogin meNotLogin = new MeNotLogin();
        return meNotLogin;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.me_not_login, container, false);

        TextView tvRequireLogin = (TextView) view.findViewById(R.id.require_login_text);

        tvRequireLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.getOurInstance().returnToLogin();
            }
        });

        return view;
    }
}
