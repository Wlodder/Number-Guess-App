package com.test.itstesttime;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MenuActivity extends Activity {

    private boolean cleared = false, screenSaved = false;
    private int paintColor = Color.BLACK;
    public AmbilWarnaDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        cleared = false;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ambilwarna_dialog);


        dialog = new AmbilWarnaDialog(this, Color.BLACK, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                finish();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                paintColor = color;
                Intent intent = new Intent();
                intent.putExtra("PAINT_COLOR" , paintColor);

                intent.putExtra("CLEARED", cleared);

                setResult(RESULT_OK ,intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }


            @Override
            public void onClear()
            {
                cleared = true;
                Intent intent  = new Intent();
                intent.putExtra("CLEARED", cleared);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onSave()
            {
                screenSaved = true;
                Intent intent  = new Intent();
                intent.putExtra("SAVED", screenSaved);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        dialog.show();

    }


}
