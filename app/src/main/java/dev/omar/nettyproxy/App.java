package dev.omar.nettyproxy;

import dev.omar.nettyproxy.ui.base.BaseApp;
import dev.omar.nettyproxy.utils.Settings;

public class App extends BaseApp {

    private Settings mSettings;
    private static volatile App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }

    public static App get() {
        return sApp;
    }

    public Settings getSettings() {
        if (mSettings == null) {
            mSettings = new Settings(getApplicationContext());
        }
        return mSettings;
    }
}
