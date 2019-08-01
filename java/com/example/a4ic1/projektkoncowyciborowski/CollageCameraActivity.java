package com.example.a4ic1.projektkoncowyciborowski;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollageCameraActivity extends AppCompatActivity {

    private Camera camera;
    private int cameraId = -1;
    private CameraPreview _cameraPreview;
    private FrameLayout _frameLayout;
    LinearLayout bottomBar;
    boolean barsHidden = true;
    Button cameraShot;
    Button saveButton;
    byte[] fdata;
    boolean fdataReady = false;
    int width;
    int height;
    RelativeLayout wait;
    private OrientationEventListener orientationEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar().hide();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        wait = (RelativeLayout) findViewById(R.id.wait);
        bottomBar = (LinearLayout) findViewById(R.id.bottomBar);
        cameraShot = (Button) findViewById(R.id.cameraShot);
        saveButton = (Button) findViewById(R.id.saveButton);

        orientationEventListener = new OrientationEventListener(CollageCameraActivity.this) {
            @Override
            public void onOrientationChanged(int ang) {}
        };

        /*
        cameraShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, camPictureCallback);
                saveButton.setVisibility(View.VISIBLE);
                Log.d("TARGET","shot taken");
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("fotodata", fdata);
                setResult(300, intent);   // 300 - jw
                finish();
            }
        });
*/
        initCamera();
        initPreview();

        _frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, camPictureCallback);
                saveButton.setVisibility(View.VISIBLE);
                Log.d("TARGET","shot taken");
                /*
                if( barsHidden == true ){
                    ObjectAnimator anim2 = ObjectAnimator.ofFloat(bottomBar, View.TRANSLATION_Y, -(bottomBar.getMeasuredHeight()*(2/3)));
                    anim2.setDuration(300); //ms
                    anim2.start();
                    barsHidden = false;
                }
                else if( barsHidden == false ){
                    ObjectAnimator anim2 = ObjectAnimator.ofFloat(bottomBar, View.TRANSLATION_Y, bottomBar.getMeasuredHeight());
                    anim2.setDuration(300); //ms
                    anim2.start();
                    barsHidden = true;
                }*/
            }
        });
    }

    private void initCamera(){
        boolean cam = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!cam) {
            // uwaga - brak kamery
        } else {
            // wykorzystanie danych zwróconych przez kolejną funkcję getCameraId
            cameraId = getCameraId();
            // jest jakaś kamera!
            if (cameraId < 0) {
                // brak kamery z przodu!
            } else {
                if (cameraId >= 0) {
                    camera = Camera.open(cameraId);
                }
            }
        }
    }

    private int getCameraId() {
        int cid = 0;
        int camerasCount = Camera.getNumberOfCameras(); // gdy więcej niż jedna kamera
        for (int i = 0; i < camerasCount; i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cid = i;
            }
        }
        return cid;
    }

    private void initPreview(){
        _cameraPreview = new CameraPreview(CollageCameraActivity.this, camera);
        _frameLayout = (FrameLayout) findViewById(R.id.frameCamera);
        _frameLayout.addView(_cameraPreview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // jeśli nie zwolnimy (release) kamery
        //inna aplikacje nie może jej uzywać
        orientationEventListener.disable();
        if (camera != null) {
            camera.stopPreview();
            //linijka nieudokumentowana w API, bez niej jest crash przy wznawiamiu kamery
            _cameraPreview.getHolder().removeCallback(_cameraPreview);
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            initCamera();
            initPreview();
        }

        if (orientationEventListener.canDetectOrientation()) {
            // Log - listener działa
            orientationEventListener.enable();
        } else {
            // Log - listener nie działa
        }
    }

    private Camera.PictureCallback camPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // zapisz dane zdjęcia w tablicy typu byte[]
            // do poźniejszego wykorzystania
            // poniewaz zapis zdjęcia w galerii powinien być dopiero po akceptacji butonem
            fdata = data;
            Toast.makeText(CollageCameraActivity.this, "Shot taken", Toast.LENGTH_SHORT).show();
            // odswiez kamerę (zapobiega przycięciu się kamery po zrobieniu zdjęcia)
            camera.startPreview();
            fdataReady = true;

            Bitmap b = Imaging.convertB2Bm(256,256,fdata);

            Intent intent = new Intent();
            intent.putExtra("foto", b);
            setResult(300, intent);   // 300 - jw
            finish();
        }
    };

}
