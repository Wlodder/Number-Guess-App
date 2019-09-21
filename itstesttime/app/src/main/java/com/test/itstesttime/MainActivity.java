package com.test.itstesttime;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import org.tensorflow.lite.Interpreter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends Activity implements SensorEventListener {

    public MyView paintView;
    private SensorManager sensorManager;
    private Sensor sensor;
    private int paintColor = Color.BLACK;
    private final static int defaultColor = Color.BLACK;
    private final static int REQUEST_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private Interpreter tflite;
    private Bitmap currentBMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        paintView = findViewById(R.id.paintView);
        paintView.setColor(defaultColor);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);
        try{
            tflite = new Interpreter(loadModelFile());
        }catch(IOException e)
        {
            System.out.println(e);
        }
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        verifyStoragePermissions(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        if(sensorEvent.values[1] > 15)
            startMenu();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        // do not uncomment hardware picks up multiple sensor changes before disabling
        // sensorManager.unregisterListener(this,sensor);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        sensorManager.unregisterListener(this, sensor);
    }

    public void startMenu()
    {
        Intent intent = new Intent(this, MenuActivity.class);
        sensorManager.unregisterListener(this,sensor);
        startActivityForResult(intent, REQUEST_CODE, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent)
    {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        switch (requestCode)
        {

            // This request code is set by startActivityForResult(intent, REQUEST_CODE) method.
            case REQUEST_CODE:
                if(resultCode == RESULT_OK)
                {
                    Log.i("changing Color", "" + resultCode);
                    paintColor = dataIntent.getIntExtra("PAINT_COLOR", Color.BLACK);
                    paintView.setColor(paintColor);
                    if(dataIntent.getBooleanExtra("CLEARED",false))
                        paintView.clear();
                    if(dataIntent.getBooleanExtra("SAVED", false))
                    {
                       currentBMP =  Screenshot.takescreenshotOfRootView(paintView);
                       currentBMP = storeScreenshot(currentBMP, "woo2.jpg");
                       float[][] c = readBMP(currentBMP);
                       String statement = predict(c);

                       Toast popup = Toast.makeText(getApplicationContext(), "We predict " + statement, Toast.LENGTH_SHORT);
                       popup.show();
                    }
                }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("PAINT_COLOR",paintColor);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        paintColor = savedInstanceState.getInt("PAINT_COLOR");
        paintView.setColor(paintColor);

    }


    public static void verifyStoragePermissions(Activity activity)
    {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE, 1);
        }
    }

    public Bitmap storeScreenshot(Bitmap bitmap, String filename)
    {
        // original works
        //String path = getFilesDir() + "/" + filename;
        // other
        String path = "/storage/self/primary/Pictures/" + filename;
        String path2woo = "/storage/self/primary/Pictures/woo3.jpg";

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 28, 28, false);
        OutputStream out = null;
        File imageFile = new File(path);
        File compressedFile = new File(path2woo);
        try
        {
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90,out);
            out.flush();
            out.close();
            out = new FileOutputStream(compressedFile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
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
            return scaledBitmap;
        }
    }

    private MappedByteBuffer loadModelFile() throws IOException
    {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("linear.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }

    public String predict(float[][] a)
    {
        float[][] inputVals = a;
        float[][] outputVals = new float[1][10];
        // System.out.println(tflite);
        tflite.run(inputVals, outputVals);

        float currentMax = 0;
        int currentGuess = -1;
        for(int i = 0; i < outputVals.length; i++)
            for(int j = 0; j < outputVals[0].length; j++)
            {
                if(currentMax < outputVals[i][j])
                {
                    currentMax = outputVals[i][j];
                    currentGuess = j;
                }
                System.out.print(outputVals[i][j] + " ");
            }
        System.out.println();

        return "" + currentGuess + " with " + currentMax + " probability ";
    }

    private float[][] readBMP(Bitmap b)
    {
        float[][] r = new float[28][28];
        for(int i = 0; i < 28; i++)
            for(int j = 0; j < 28; j++)
                r[i][j] = b.getPixel(i,j) / 255.0f;


        return r;
    }
}
