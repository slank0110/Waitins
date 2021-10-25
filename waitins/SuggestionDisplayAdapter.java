package logicreat.waitins;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aaron on 2016-07-11.
 */
public class SuggestionDisplayAdapter extends ArrayAdapter<String>{

    private ArrayList<String> displayList;
    private Activity context;

    public SuggestionDisplayAdapter(Activity context, ArrayList<String> displayList){
        super(context, R.layout.suggestioncell, displayList);

        this.context = context;
        this.displayList = displayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = context.getLayoutInflater().inflate(R.layout.suggestioncell, null, true);
        }

        TextView tvSuggestionCell = (TextView) convertView.findViewById(R.id.suggestion_cell_text);

        tvSuggestionCell.setText(displayList.get(position));

        return convertView;
    }

}
