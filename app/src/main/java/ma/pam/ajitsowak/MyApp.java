package ma.pam.ajitsowak;

import android.content.Context;
import android.util.Base64;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import ma.pam.ajitsowak.api.WooApi;
import ma.pam.ajitsowak.room.RoomDB;
import ma.pam.ajitsowak.utils.SharedPrefUtils;

public class MyApp extends MultiDexApplication {

    private static MyApp appInstance;
    private static SharedPrefUtils sharedPrefUtils;
    public static String language;
    private static int appTheme;
    private static boolean mModeFlag;
    private static WooApi wooApi;
    private static RoomDB room;

    public void onCreate() {
        super.onCreate();
        appInstance = this;
        wooApi = WooApi.getInstance();
        room =  RoomDB.getInstance(this);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static RoomDB getRoom() {
        return room;
    }
    public static WooApi getWooApi() {
        return wooApi;
    }
    public static SharedPrefUtils getSharedPrefUtils() {
        return sharedPrefUtils;
    }

    public static void setSharedPrefUtils(SharedPrefUtils var1) {
        sharedPrefUtils = var1;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String var1) {
        language = var1;
    }
    public int getAppTheme() {
        return appTheme;
    }
    public void setAppTheme(int var1) {
        appTheme = var1;
    }

    public static MyApp getAppInstance() {
        return appInstance;
    }

    public void changeLanguage(String aLanguage) {
        language = aLanguage;
    }
}









