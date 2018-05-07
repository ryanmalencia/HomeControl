package com.example.ryanm.homecontrol;

import android.content.Context;

import com.google.gson.JsonElement;

import java.util.Scanner;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

public class SignalRHubConnection {
    public static HubConnection mHubConnection;
    public static HubProxy mHubProxy;
    private static Context context;
    public static String mConnectionID;
    private static Logger logger;

    public SignalRHubConnection(Context context) {
        this.context = context;
        HubConnection conn = new HubConnection("http://10.0.0.136/","",true,logger);
        HubProxy proxy = conn.createHubProxy("PlugHub");

        proxy.subscribe(new Object() {
            @SuppressWarnings("unused")
            public void messageReceived(String name, String message) {

            }
        });

        conn.error(new ErrorCallback() {
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        conn.connected(new Runnable() {
            @Override
            public void run() {
                System.out.println("Connected to signalr hub");
            }
        });

        conn.closed(new Runnable() {
            @Override
            public void run() {
                System.out.println("Disconnected from signalr hub");
            }
        });

        conn.start().done(new Action<Void>() {
            @Override
            public void run(Void obj) throws Exception {
                System.out.println("Done connecting to signalr hub");
            }
        });

        conn.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(JsonElement jsonElement) {
                System.out.println("Received message: " + jsonElement.toString());
            }
        });

        Scanner inputReader = new Scanner(System.in);

        String line = inputReader.nextLine();

        while(!"exit".equals(line)) {
            proxy.invoke("send", "Console", line).done(new Action<Void>() {
                @Override
                public void run(Void obj) throws Exception {
                    System.out.println("SENT!");
                }
            });
            line = inputReader.next();
        }

        inputReader.close();
        conn.stop();
    }

    public static void startSignalR() {}
}
