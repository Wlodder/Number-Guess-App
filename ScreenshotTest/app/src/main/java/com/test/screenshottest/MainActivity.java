package com.test.screenshottest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    String[] appPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private Button btn;
    private View main;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        main = findViewById(R.id.activity_main);
        btn = findViewById(R.id.button);
        final Activity thisActivity = this;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //using https://medium.com/@ssaurel/taking-a-screenshot-programmatically-in-android-apps-67619cb80bf8
                Bitmap b = Screenshot.takescreenshotOfRootView(main);
                storeScreenshot(b, "woo2.jpg");
                /*
                shareScreen();
                Bitmap bitmap = takeScreenShot(thisActivity);
                Utils.savePic(bitmap, getFilesDir() + "/woo2.jpg");*/
            }
        });
    }



    private void shareScreen() {
        try {
            /*
            File cacheDir = new File(
                    android.os.Environment.getDataDirectory(),
                    "data");

            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }*/
            // saves to the internal storage unaccessible by non root

            /*
            String path = new File(
                    getFilesDir(), "screenshot.jpg").getAbsolutePath();
            */

            String path = new File("/storage/self/primary/Pictures","woo3.jpg").getAbsolutePath();
            Utils.savePic(Utils.takeScreenShot(this), path);

            Toast.makeText(getApplicationContext(), "Screenshot Saved", Toast.LENGTH_SHORT).show();


        } catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }
    }

    public static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        //Find the screen dimensions to create bitmap in the same size.
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public void storeScreenshot(Bitmap bitmap, String filename)
    {
        // original works
        //String path = getFilesDir() + "/" + filename;
        // other
        String path = "/storage/self/primary/Pictures/" + filename;
        OutputStream out = null;
        File imageFile = new File(path);
        try
        {
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90,out);
            out.flush();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(out != null)
                    out.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void checkAndRequestPermissions()
    {
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions)
        {
            if(ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED)
            {
                listPermissionsNeeded.add(perm);
            }
        }


            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);



    }

}