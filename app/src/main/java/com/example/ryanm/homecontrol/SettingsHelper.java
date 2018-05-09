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
                settings = new SettingFile();
                saveSettings();
            }
        }
        else{
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
        int count = settings.serverIP.split(Pattern.quote(".")).length;
        if(count < 4) {
            return false;
        }
        try {
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
