package com.kuc_arc_f.app.picasa;

import com.kuc_arc_f.fw.AppConst;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {
	private static final com.kuc_arc_f.fw.AppConst m_Const = new  AppConst();
	
    public static SharedPreferences get(Context context) {
        return context.getSharedPreferences(m_Const.KEY_PREF_USR, 0);
    }
}
