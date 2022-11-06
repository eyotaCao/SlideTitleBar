package com.example.basepop.basepop.base.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class ViewUtils {
    public static View getViewFromLayout(int layout, Context context){
        return LayoutInflater.from(context).inflate(layout, null);
    }

    public static int [] getLocation(View view){
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }



    public static int getNavigationHeight(Activity activity){
        int resourceId=activity.getResources().getIdentifier("navigation_bar_height","dimen","android");
        return activity.getResources().getDimensionPixelSize(resourceId);
    }
}
