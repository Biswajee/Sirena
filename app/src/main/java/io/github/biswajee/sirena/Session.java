package io.github.biswajee.sirena;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Biswajit Roy on 29-07-2018.
 */

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename) {
        prefs.edit().putString("usename", usename).apply();
    }

    public String getusename() {
        String usename = prefs.getString("usename","");
        return usename;
    }
}