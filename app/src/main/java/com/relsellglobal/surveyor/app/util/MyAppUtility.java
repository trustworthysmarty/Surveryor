package com.relsellglobal.surveyor.app.util;

/**
 * Created by anil on 8/7/15.
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.LruCache;
import android.view.Display;
import android.widget.Toast;

import com.relsellglobal.surveyor.app.contentproviders.AppContentProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class MyAppUtility extends Application {

    //sensible place to declare a log tag for the application
    public static final String LOG_TAG = "myapp";
    private static Display sDisplay = null;
    private boolean showDBToastMsg = true;
    private boolean settingsEnabled = true;
    private boolean mDebugModeForBeta = false;
    private boolean mUserLoggedIn = false;
    private String mUserName;
    private String base64ConfigKey;


    private ActionBar mActionBar;

    ActionBarDrawerToggle mDrawerToggle;


    private String mUserPhoneNo;
    private boolean isLocal = false;








    public ActionBarDrawerToggle getmDrawerToggle() {
        return mDrawerToggle;
    }

    public void setmDrawerToggle(ActionBarDrawerToggle mDrawerToggle) {
        this.mDrawerToggle = mDrawerToggle;
    }

    public ActionBar getmActionBar() {
        return mActionBar;
    }

    public void setmActionBar(ActionBar mActionBar) {
        this.mActionBar = mActionBar;
    }

    public boolean ismUserLoggedIn() {
        return mUserLoggedIn;
    }

    public void setmUserLoggedIn(boolean mUserLoggedIn) {
        this.mUserLoggedIn = mUserLoggedIn;
    }


    public boolean ismDebugModeForBeta() {
        return mDebugModeForBeta;
    }

    public void setmDebugModeForBeta(boolean mDebugModeForBeta) {
        this.mDebugModeForBeta = mDebugModeForBeta;
    }


    public String getmUserPhoneNo() {
        return mUserPhoneNo;
    }

    public void setmUserPhoneNo(String mUserPhoneNo) {
        this.mUserPhoneNo = mUserPhoneNo;
    }


    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }


    //instance
    private static MyAppUtility instance = null;

    //keep references to our global resources
    private static LruCache<String, Bitmap> mMemoryCache;


    /**
     * Convenient accessor, saves having to call and cast getApplicationContext()
     */
    public static MyAppUtility getInstance() {
        checkInstance();
        return instance;
    }

    /**
     * Accessor for some resource that depends on a context
     */
    public LruCache<String, Bitmap> getMemoryCache() {
        if (mMemoryCache == null) {
            checkInstance();
            // check here for cache if available
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/2th of the available memory for this memory cache.
            final int cacheSize = maxMemory;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
        return mMemoryCache;
    }



    private static void checkInstance() {
        if (instance == null)
            throw new IllegalStateException("Application not created yet!");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //provide an instance for our static accessors
        instance = this;
    }

    public void showToastMsgForDB(Context context, String msg, boolean major) {
        if (showDBToastMsg || major) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void writeToPrefs(String fileName, String key, String value) {

        SharedPreferences sharedpreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void writeToLongPrefs(String fileName, String key, long value) {

        SharedPreferences sharedpreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public String getDataFromPefs(String fileName, String key) {
        String value = null;
        SharedPreferences prefs = getSharedPreferences(fileName, MODE_PRIVATE);
        value = prefs.getString(key, null);
        return value;
    }

    public long getLongDataFromPerfs(String fileName, String key) {
        long value = -1;
        SharedPreferences prefs = getSharedPreferences(fileName, MODE_PRIVATE);
        value = prefs.getLong(key, -1);
        return value;
    }

    public String getServer() {

        return "api.buzz.ninja";
    }

    public String getPort() {

        return "80";
    }

    public void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ getApplicationContext().getPackageName() +"/databases/"+ AppContentProvider.DATABASE_NAME;
        String backupDBPath = AppContentProvider.DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
