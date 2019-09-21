package com.test.itstesttime;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class FillTool {
    private static Bitmap cacheBitmap;
    private static int mBaseColor;
    private static ArrayList<Pixel> pixelsForFill = new ArrayList<Pixel>();
    private static float[] pixels = new float[250];
    private static int arraySize = 250;
    private static int counter = 0;

    public static void setCacheBitmap(Bitmap b)
    {
        cacheBitmap = b;
    }

    public static void setBaseColor(int x, int  y)
    {
         mBaseColor = cacheBitmap.getPixel(x,y);
    }

    public static void bucketFill(int x, int y, int colorSet)
    {
        if(x < 0 || y < 0 || x >= cacheBitmap.getWidth() || y >= cacheBitmap.getHeight())
        {
            return;
        }
        else if(cacheBitmap.getPixel(x,y) == (colorSet))
            return;
        else if (cacheBitmap.getPixel(x,y) != mBaseColor)
            return;
        else {
            cacheBitmap.setPixel(x, y, colorSet);
            pixelsForFill.add(new Pixel(x, y));
        }
        bucketFill(x + 100,y, colorSet);
        bucketFill(x, y + 100, colorSet);
        bucketFill(x - 100, y, colorSet);
        bucketFill(x, y - 100, colorSet);
    }

    public static void moveToPixelArray()
    {
        Pixel tempPixel;
        int i = pixelsForFill.size() - 1;
        int j = 0;
        while(i >= 0 && j < pixels.length)
        {
            tempPixel = pixelsForFill.get(i--);
            pixels[j] = tempPixel.getX();
            pixels[j + 1] = tempPixel.getY();
            j += 2;
            if(j < pixels.length) {
                pixels = Arrays.copyOf(pixels, 2 * arraySize);
                arraySize = pixels.length;
            }

        }
    }

    public static float[] getPixels()
    {
        return pixels;
    }

    // resting the cacheBitmap after each fill
    public static void reset()
    {
        cacheBitmap = null;
        pixelsForFill.clear();
        arraySize = 250;
        pixels = new float[250];
    }

}
