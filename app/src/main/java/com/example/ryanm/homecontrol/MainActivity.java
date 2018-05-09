package com.example.ryanm.homecontrol;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SettingsHelper helper = new SettingsHelper(getApplicationContext());
        String[] args = {helper.getIP(),"/api/plug/get/all", "GET"};
        new NetworkInteraction().execute(args);
    }

    public void viewDevice(String IP, String name, int id) {
        Intent intent = new Intent(this, ViewDevice.class);
        intent.putExtra("name", name);
        intent.putExtra("IP", IP);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void openSettings(MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    class NetworkInteraction extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

        }

        protected String doInBackground(String... params) {
            String request = "http://" + params[0] + params[1];
            String method = params[2];
            try {
                URL url = new URL(request);
                System.out.println(url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(method);
                urlConnection.connect();
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null)
                    {
                        sb.append(line);
                    }
                    br.close();
                    return sb.toString();
                }
                finally {
                    urlConnection.disconnect();
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        protected void onPostExecute(String response) {
            System.out.println(response);
            if(response == null) {
                return;
            }
            System.out.println("Response: " + response);
            ProgressBar status = findViewById(R.id.statusSpinner);
            LinearLayout layout = findViewById(R.id.devices);
            status.setVisibility(View.GONE);
            try {
                int i = 0;
                layout.removeAllViews();
                JSONObject temp_object = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray array = temp_object.getJSONArray("Plugs");
                while(i < array.length()) {
                    final JSONObject object = array.getJSONObject(i);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(10,10,10,0);
                    Button temp = new Button(getApplicationContext());
                    temp.setLayoutParams(params);
                    temp.setGravity(Gravity.CENTER);
                    temp.setPadding(0,25,0,25);
                    temp.setBackgroundColor(Color.GRAY);
                    temp.setText(object.getString("Name"));
                    temp.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                viewDevice(object.getString("IP"),object.getString("Name"),object.getInt("PlugID"));
                            }
                            catch(Exception e) {
                                System.out.println("Error");
                            }
                        }
                    });
                    layout.addView(temp);
                    i++;
                }
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
