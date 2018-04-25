package com.example.ryanm.homecontrol;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewDevice extends AppCompatActivity {
    String IP = "";
    int id = 0;
    boolean status1 = false;
    boolean status2 = false;
    Button one;
    Button two;
    Button all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_device);
        IP = getIntent().getStringExtra("IP");
        id = getIntent().getIntExtra("id",0);
        System.out.println("THE ID IS: " + id);
        ((TextView)findViewById(R.id.text)).setText(getIntent().getStringExtra("name"));
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        all = findViewById(R.id.all);
        System.out.println("IP: " + IP);
        String[] args = {IP, "/status", "GET"};
        new NetworkInteraction().execute(args);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            System.out.println("Finishing...");
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void toggleOne(View v) {
        if(!status1) {
            String[] args = {IP, "/1/on", "POST"};
            new NetworkInteraction().execute(args);
        }
        else {
            String[] args = {IP, "/1/off", "POST"};
            new NetworkInteraction().execute(args);
        }
    }

    public void toggleTwo(View v) {
        if(!status2) {
            String[] args = {IP, "/2/on", "POST"};
            new NetworkInteraction().execute(args);
        }
        else {
            String[] args = {IP, "/2/off", "POST"};
            new NetworkInteraction().execute(args);
        }
    }

    public void toggle(View v) {
        if(!status1 || !status2) {
            String[] args = {IP, "/on", "POST"};
            new NetworkInteraction().execute(args);
        }
        else {
            String[] args = {IP, "/off", "POST"};
            new NetworkInteraction().execute(args);
        }
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
            try {
                JSONTokener token = new JSONTokener(response);
                JSONObject object = (JSONObject) token.nextValue();
                int one = object.getInt("status1");
                int two = object.getInt("status2");
                status1 = one != 0;
                status2 = two != 0;
                Button button1 = findViewById(R.id.one);
                Button button2 = findViewById(R.id.two);
                Button doall = findViewById(R.id.all);
                button1.setEnabled(true);
                button2.setEnabled(true);
                doall.setEnabled(true);
                if(status1) {
                    button1.setBackgroundColor(Color.parseColor("green"));
                }
                else {
                    button1.setBackgroundColor(Color.parseColor("grey"));
                }
                if(status2) {

                    button2.setBackgroundColor(Color.parseColor("green"));
                }
                else {
                    button2.setBackgroundColor(Color.parseColor("grey"));
                }
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
