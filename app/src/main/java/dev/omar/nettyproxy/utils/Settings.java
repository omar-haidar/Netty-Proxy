package dev.omar.nettyproxy.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private Context context;
    private SharedPreferences sp;
    
    public Settings(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("settings",0);
    }

    public boolean isStartupWithBoot() {
    	return sp.getBoolean("boot_startup",false);
    }
    
    public void setStartWithBoot(boolean flag) {
    	sp.edit().putBoolean("boot_startup",flag).apply();
    }


}
