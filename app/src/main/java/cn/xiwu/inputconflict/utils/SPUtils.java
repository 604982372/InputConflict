package cn.xiwu.inputconflict.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xiwu on 2017/12/23.
 */

public class SPUtils {
    static  String fileName = "cacheFile";

    //
    public static String getString(Context context,String title){
        SharedPreferences sp = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sp.getString(title,"");
    }
    public static void saveString(Context context,String title,String content){
        SharedPreferences sp = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(title,content);
        //edit.commit();
        edit.apply();
    }
    public static long getLong(Context context,String title){
        SharedPreferences sp = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sp.getLong(title,0);
    }
    public static void saveLong(Context context,String title,long content){
        SharedPreferences sp = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(title,content);
        //edit.commit();
        edit.apply();
    }
    public static int getInt(Context context,String title){
        SharedPreferences sp = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sp.getInt(title,0);
    }
    public static void saveInt(Context context,String title,int content){
        SharedPreferences sp = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(title,content);
        //edit.commit();
        edit.apply();
    }
}
