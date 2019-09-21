package com.test.itstesttime;

import android.graphics.Bitmap;
import android.view.View;

public class Screenshot {

    public static Bitmap takescreenshot(View v)
    {
        View screenView = v.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap b = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return b;
    }

    public static Bitmap takescreenshotOfRootView(View v)
    {
        return takescreenshot(v.getRootView());
    }



}
