package com.encom.puls;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.games.multiplayer.realtime.RealTimeSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by shaggi on 23/08/14.
 */
public class MainActivity extends Activity {
    String readTwitterFeed;
    ArrayList<String> values = new ArrayList<String>();
    ListView listView;
    StableArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        this.listView = (ListView) findViewById(R.id.listView);
        readTwitterFeed = readTwitterFeed();
        initTimeLine();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent = new Intent();
        switch (item.getItemId()) {

            case R.id.refresh:
                updateTimeLine();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initTimeLine(){


        try {
            JSONArray jsonArray = new JSONArray(readTwitterFeed);
            Log.i(MainActivity.class.getName(),
                    "Number of entries " + jsonArray.length());
            //values = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                values.add(jsonArray.getJSONObject(i).toString());
            }

                this.adapter = new StableArrayAdapter(this ,values);
                listView.setAdapter(this.adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                        Toast.makeText(getApplicationContext(),
                                "Click ListItem Number " + position, Toast.LENGTH_SHORT)
                                .show();
                    }

                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateTimeLine(){
        readTwitterFeed = readTwitterFeed();
        try {
            JSONArray jsonArray = new JSONArray(readTwitterFeed);
            for (int i = 0; i < jsonArray.length(); i++) {
                values.add(jsonArray.getJSONObject(i).toString());
            }
            this.adapter.notifyDataSetChanged();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String readTwitterFeed() {
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://192.168.1.102/index.json");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(MainActivity.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}

