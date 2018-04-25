package com.example.ryanm.homecontrol;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    static final String serverUrl = "192.168.1.62:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] args = {serverUrl,"/devices", "GET"};
        new NetworkInteraction().execute(args);
    }

    public void viewDevice(String IP, String name) {
        Intent intent = new Intent(this, ViewDevice.class);
        intent.putExtra("name", name);
        intent.putExtra("IP", IP);
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
            LinearLayout layout = findViewById(R.id.devices);
            try {
                int i = 0;
                layout.removeAllViews();
                JSONTokener token = new JSONTokener(response);
                JSONArray array = (JSONArray) token.nextValue();
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
                    temp.setText(object.getString("name"));
                    temp.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                viewDevice(object.getString("IP"),object.getString("name"));
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
