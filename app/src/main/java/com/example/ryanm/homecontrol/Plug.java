package com.example.ryanm.homecontrol;

public class Plug {
    private int PlugID;
    private String IP;
    private String Name;

    public Plug() {

    }

    public Plug(int pid, String ip, String name) {
        PlugID = pid;
        IP = ip;
        Name = name;
    }

    public int getID(){
        return PlugID;
    }

    public String getIP() {
        return IP;
    }

    public String getName() {
        return Name;
    }
}
