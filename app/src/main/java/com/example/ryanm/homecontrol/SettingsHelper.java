package com.example.ryanm.homecontrol;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class SettingsHelper {
    private SettingFile settings;
    private Context context;

    SettingsHelper(Context context) {
        this.context = context;
        File file = new File(context.getFilesDir() + "/settings.bin");
        if(file.exists()){
            try {
                FileInputStream fis = context.openFileInput("settings.bin");
                ObjectInputStream ois = new ObjectInputStream(fis);
                settings = (SettingFile) ois.readObject();
                ois.close();
            }
            catch(Exception e)
            {
                System.out.println("Error: File not found");
                settings = new SettingFile();
                saveSettings();
            }
        }
        else{
            System.out.println("File not found");
            settings = new SettingFile();
            saveSettings();
        }
    }

    public void setIP(String IP) {
        settings.serverIP = IP;
    }

    public String getIP() {
        return settings.serverIP;
    }

    public boolean saveSettings() {
        System.out.println("saving:" + settings.serverIP);
        int count = settings.serverIP.split(Pattern.quote(".")).length;
        System.out.println("count: " + count);
        if(count < 4) {
            System.out.println("invalid entry");
            return false;
        }
        try {
            System.out.println("saving...");
            FileOutputStream fos = context.openFileOutput("settings.bin", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(settings);
            oos.flush();
            oos.close();
            return true;
        }catch(IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
