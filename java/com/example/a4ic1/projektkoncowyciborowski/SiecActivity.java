package com.example.a4ic1.projektkoncowyciborowski;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SiecActivity extends AppCompatActivity {

    ImageView iv;
    LinearLayout imageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siec);
        getSupportActionBar().hide();

        imageViewer = (LinearLayout)findViewById(R.id.imageViewer);
        Display display = getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        boolean connection = new Networking().checkConn(SiecActivity.this);
        if( connection ){
            new GetJson(SiecActivity.this,imageViewer,2,displayWidth).execute();
        }
        else{
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(SiecActivity.this);
            alert.setTitle("Uwaga!");
            alert.setCancelable(false);
            alert.setMessage("Brak połączenia z siecią!");
            alert.setNeutralButton("OK", null).show();
        }
    }
}
