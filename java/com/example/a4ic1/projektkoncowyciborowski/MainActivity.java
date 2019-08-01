package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Intent;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout camera;
    LinearLayout albumy;
    LinearLayout kolaz;
    LinearLayout siec;
    LinearLayout imageViewer;
    int width;
    int height;

    Networking net = new Networking();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //file.listFiles();
        File[] files = pic.listFiles();
        //Log.i("f",files[0].getName());
        File location = new File(pic, "Ciborowski");
        if( location.exists()){
            Log.i("i","ISTNIEJE");
        }
        else{
            Log.i("i","UTWORZONO");
            location.mkdir();
            for( int i = 0; i < 4; i++ ) {
                File folder = new File(location, "folder" + i);
                folder.mkdir();
            }
        }

        camera = (LinearLayout) findViewById(R.id.camera);
        albumy = (LinearLayout) findViewById(R.id.albumy);
        kolaz = (LinearLayout) findViewById(R.id.kolaz);
        siec = (LinearLayout) findViewById(R.id.siec);
        imageViewer = (LinearLayout) findViewById(R.id.imageViewer);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( Prefs.getMyPref() != "" ) {
                    Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
                if( Prefs.getMyPref() == "" ) {
                    Intent intent = new Intent(MainActivity.this, KolazActivity.class);
                    startActivity(intent);
                }
            }
        });
        albumy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AlbumActivity.class);
                startActivity(intent);
            }
        });
        kolaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ChooseCollageActivity.class);
                startActivity(intent);
            }
        });
        siec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SiecActivity.class);
                startActivity(intent);
            }
        });

        boolean connection = net.checkConn(MainActivity.this);
        if( connection )
        {
            new GetJson(MainActivity.this, imageViewer,1,width).execute();
        }
    }

    /*
        onPause();
        Log.d("XXX", "pause camera");
        onResume();
        onRestart();
        onStop();
        onStart();
        */

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("CYKL", "pause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("CYKL", "restart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CYKL", "resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("CYKL", "stop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CYKL", "start");
    }
}
