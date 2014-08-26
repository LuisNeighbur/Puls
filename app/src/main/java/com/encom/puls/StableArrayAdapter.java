package com.encom.puls;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shaggi on 23/08/14.
 */
public class StableArrayAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
    private final Context context;
    private  ArrayList<String> values;
    final ArrayList<String> list = new ArrayList<String>();
    public StableArrayAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.simple_list_item, values);

        for (int i = 0; i < values.toArray().length; ++i) {
            //list.add(values[i]);
            mIdMap.put(values.get(i), i);
        }
       /* for (int i = 0; i < list.size(); ++i) {
            mIdMap.put(list.get(i), i);
        }*/
        this.context = context;
        this.values = values;
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.simple_list_item, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView profileName = (TextView) rowView.findViewById(R.id.profile_name);
        TextView state = (TextView) rowView.findViewById(R.id.state);
        try {
            JSONObject jsonData = new JSONObject(this.values.get(position));
            if(jsonData.has("img")){
                imageView.setImageURI(Uri.parse(jsonData.getString("img")));
            }else{
                imageView.setImageResource(R.drawable.ic_launcher);
            }

            profileName.setText(jsonData.getString("name"));
            state.setText(jsonData.getString("state"));
        }catch(Exception e){
            Log.d("Json", e.getMessage());
        }

        return rowView;
    }
}
