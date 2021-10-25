package logicreat.waitins;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchingActivity extends AppCompatActivity {

    private ListView searchLv;
    private ListView suggestionLv;

    private SuggestionDisplayAdapter suggestionDisplayAdapter;

    private ArrayList<String> displayList;

    private final int MAX_DISPLAY = 10;

    private SearchView search;

    Thread suggestionThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

        MyApp.setCurActivity(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(MyApp.width/2,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar, null), params);
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchLv = (ListView) findViewById(R.id.search_list);
        suggestionLv = (ListView) findViewById(R.id.suggestion_list);

        displayList = new ArrayList<>();

        ArrayList<String> temp = MyApp.getOurInstance().getDbHandler().getSuggestion();
        for (int i = 0 ; i < temp.size(); i ++){
            displayList.add(temp.get(i));
        }

        suggestionDisplayAdapter = new SuggestionDisplayAdapter(this, displayList);

        suggestionLv.setAdapter(suggestionDisplayAdapter);

        suggestionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String query = displayList.get(position);
                MyApp.getOurInstance().getDbHandler().addSuggestion(query);
                Intent intent = new Intent(MyApp.getCurActivity(), SearchableActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.qr_map_hide_action_bar, menu);

        //SearchManager searchManager =
        //        (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MenuItem searchItem = menu.findItem(R.id.search);
        search = (SearchView) MenuItemCompat.getActionView(searchItem);

        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText editText = (EditText) search.findViewById(id);
        editText.setTextColor(Color.BLACK);
        editText.setTypeface(Typeface.SERIF);
        editText.setTextSize(15);
        editText.setPadding(0, 0, 0, 0);

        // modifying search view icon
        int iconId = search.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView icon = (ImageView) search.findViewById(iconId);
        icon.setScaleX((float)0.8);
        icon.setScaleY((float)0.8);

        // expanding the search view
        search.setIconifiedByDefault(false);
        searchItem.expandActionView();
        search.setQueryHint(getString(R.string.search_Bar_Hint));

        // modifying search view layout
        int searchPlateId = search.getContext().getResources().getIdentifier("android:id/search_edit_frame", null, null);
        View searchPlate = search.findViewById(searchPlateId);
        searchPlate.setBackgroundResource(R.drawable.search_rounded);

        searchPlateId = search.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        searchPlate = findViewById(searchPlateId);
        searchPlate.setBackgroundResource(R.drawable.search_rounded);

        searchPlateId = search.getContext().getResources().getIdentifier("android:id/search_bar", null, null);
        searchPlate = findViewById(searchPlateId);
        searchPlate.setBackgroundResource(R.drawable.search_rounded);
        //float temp = (float)0.7;
        //searchPlate.setScaleY(temp);

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    displayList.clear();
                    ArrayList<String> temp = MyApp.getOurInstance().getDbHandler().getSuggestion();
                    for (int i = 0 ; i < temp.size(); i ++){
                        displayList.add(temp.get(i));
                    }
                    suggestionDisplayAdapter.notifyDataSetChanged();
                }else{
                    search.setQuery("", false);
                    search.clearFocus();
                }
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MyApp.getOurInstance().getDbHandler().addSuggestion(query);
                Intent intent = new Intent(MyApp.getCurActivity(), SearchableActivity.class);
                intent.putExtra("query", query);
                MyApp.getCurActivity().startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                if (newText.equals("")){
                    displayList.clear();
                    ArrayList<String> temp = MyApp.getOurInstance().getDbHandler().getSuggestion();
                    for (int i = 0 ; i < temp.size(); i ++){
                        displayList.add(temp.get(i));
                    }
                    suggestionDisplayAdapter.notifyDataSetChanged();
                }else {
                    if (suggestionThread != null && !suggestionThread.isInterrupted()) {
                        suggestionThread.interrupt();
                    }
                    suggestionThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> params = new HashMap<>();
                            params.put("key", newText);

                            String response = MyApp.getOurInstance().sendSynchronousStringRequest(MyApp.SUGGESTION_URL, params);

                            displayList.clear();

                            if (!response.equals(MyApp.NETWORK_ERROR) && !response.equals("$")) {
                                String[] info = response.split("\\|");
                                for (int i = 0; i < info.length; i++) {
                                    displayList.add(info[i]);
                                }
                            }

                            MyApp.getCurActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    suggestionDisplayAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                    suggestionThread.start();
                }
                return false;
            }
        });
        //search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.go_to_map:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                return true;
            case R.id.go_to_qr:
                intent = new Intent(this, QRcodeActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void onResume(){
        super.onResume();
        MyApp.setCurActivity(this);
    }

}
