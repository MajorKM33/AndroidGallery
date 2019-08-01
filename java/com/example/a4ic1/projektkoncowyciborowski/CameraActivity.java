package com.example.a4ic1.projektkoncowyciborowski;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private Camera camera;
    private int cameraId = -1;
    private CameraPreview _cameraPreview;
    private FrameLayout _frameLayout;
    private Kolo kolo;
    private Miniatura miniatura;
    private Miniatura tmpMiniatura;
    LinearLayout topBar;
    LinearLayout bottomBar;
    boolean barsHidden = true;
    Button cameraShot;
    Button saveButton;
    Button resButton;
    Button balButton;
    Button colButton;
    Button expButton;
    private float posx;
    byte[] fdata;
    private Camera.Parameters camParams;
    String[] resArray = new String[1];
    String[] balArray = new String[1];
    String[] colArray = new String[1];
    String[] expArray = new String[1];
    String[] optionsArray = new String[5];
    int width;
    int height;
    private ArrayList<byte[]> lista= new ArrayList<byte[]>() ;
    private ArrayList<Miniatura> miniatury = new ArrayList<Miniatura>();
    private ArrayList<Integer> orientationList = new ArrayList<Integer>();
    private OrientationEventListener orientationEventListener;
    int lastOrientation = 0;
    int ang2 = 0;
    int orient = 0;
    boolean changeOrient = false;
    RelativeLayout wait;

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

        optionsArray[0] = "Podgląd";
        optionsArray[1] = "Usuń bieżące";
        optionsArray[2] = "Usuń wszystkie";
        optionsArray[3] = "Zapisz bieżące";
        optionsArray[4] = "Zapisz wszystkie";

        wait = (RelativeLayout) findViewById(R.id.wait);

        topBar = (LinearLayout) findViewById(R.id.topBar);
        bottomBar = (LinearLayout) findViewById(R.id.bottomBar);
        cameraShot = (Button) findViewById(R.id.cameraShot);
        saveButton = (Button) findViewById(R.id.saveButton);
        resButton = (Button) findViewById(R.id.resButton);
        balButton = (Button) findViewById(R.id.balButton);
        colButton = (Button) findViewById(R.id.colButton);
        expButton = (Button) findViewById(R.id.expButton);

        orientationEventListener = new OrientationEventListener(CameraActivity.this) {
            @Override
            public void onOrientationChanged(int ang) {
                Log.d("ANGLE",ang + " orient " + orient);
                // i zwraca kąt 0 - 360 stopni podczas obracania ekranem w osi Z
                // tutaj wykonaj animacje butonów i miniatur zdjęć
                if( ang > 45 && ang < 315 && orient == 0 ){
                    lastOrientation = 0;
                    ang2 = 90;
                    orient = 1;
                    changeOrient = true;
                }
                if( ((ang >= 315 && ang <= 359) || (ang >= 0 && ang <= 45)) && orient == 1 ){
                    lastOrientation = 90;
                    ang2 = 0;
                    orient = 0;
                    changeOrient = true;
                }

                if( changeOrient ) {
                    ObjectAnimator.ofFloat(cameraShot, View.ROTATION, lastOrientation, ang2)
                            .setDuration(1000)
                            .start();
                    ObjectAnimator.ofFloat(saveButton, View.ROTATION, lastOrientation, ang2)
                            .setDuration(1000)
                            .start();
                    ObjectAnimator.ofFloat(resButton, View.ROTATION, lastOrientation, ang2)
                            .setDuration(1000)
                            .start();
                    ObjectAnimator.ofFloat(balButton, View.ROTATION, lastOrientation, ang2)
                            .setDuration(1000)
                            .start();
                    ObjectAnimator.ofFloat(expButton, View.ROTATION, lastOrientation, ang2)
                            .setDuration(1000)
                            .start();
                    ObjectAnimator.ofFloat(colButton, View.ROTATION, lastOrientation, ang2)
                            .setDuration(1000)
                            .start();

                    for (int j = 0; j < miniatury.size(); j++) {
                        ObjectAnimator.ofFloat(miniatury.get(j), View.ROTATION, lastOrientation, ang2)
                                .setDuration(1000)
                                .start();
                    }
                    changeOrient = false;
                }
            }
        };

        resButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                alert.setTitle("Rozdzielczość");
                //nie może mieć setMessage!!!
                final String[] opcje = resArray;
                alert.setItems(opcje, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("TARGET",opcje[which]);
                        camParams.setPictureSize( Integer.parseInt(opcje[which].split("x")[0]), Integer.parseInt(opcje[which].split("x")[1]) );
                        camera.setParameters(camParams);
                    }
                });
                //
                alert.show();
                // camParams.getSupportedPictureSizes();
            }
        });

        balButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                alert.setTitle("Balans bieli");
                //nie może mieć setMessage!!!
                final String[] opcje = balArray;
                alert.setItems(opcje, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("TARGET",opcje[which]);
                        camParams.setWhiteBalance(opcje[which]);
                        camera.setParameters(camParams);
                    }
                });
                //
                alert.show();
            }
        });

        colButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                alert.setTitle("Efekty kolorystyczne");
                //nie może mieć setMessage!!!
                final String[] opcje = colArray;
                alert.setItems(opcje, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("TARGET",opcje[which]);
                        camParams.setColorEffect(opcje[which]);
                        camera.setParameters(camParams);
                    }
                });
                //
                alert.show();
            }
        });

        expButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                alert.setTitle("Naświetlenie");
                //nie może mieć setMessage!!!
                final String[] opcje = expArray;
                alert.setItems(opcje, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("TARGET",opcje[which]);
                        camParams.setExposureCompensation(Integer.parseInt(opcje[which]));
                        camera.setParameters(camParams);
                    }
                });
                //
                alert.show();
            }
        });

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
                Log.d("TARGET","zapis");
                String targetFolder = Prefs.getMyPref();
                Log.d("TARGET",targetFolder);
                File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File location = new File(pic, "Ciborowski");
                File target = new File(location, targetFolder);
                SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String d = dFormat.format(new Date());
                File myFoto = new File(target,d+".jpg");
                Log.d("TARGET",myFoto.getAbsolutePath());
                // zapis danych zrobionego foto - konieczne dodac try catch
                FileOutputStream fs;
                try {
                    //fs = new FileOutputStream(myFoto);
                    //fs.write(fdata);
                    //fs.close();
                    Bitmap tmp = Imaging.convertB2Bm(1280,720,fdata);
                    Matrix matrix = new Matrix();
                    // rotate Bitmap
                    matrix.postRotate(90);
                    //zwracam nową obróconą
                    tmp = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
                    fs = new FileOutputStream(myFoto);
                    tmp.compress(Bitmap.CompressFormat.JPEG, 100, fs);
                    fs.close();
                    Log.d("TARGET","saved");
                    //Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                    //startActivity(intent);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        initCamera();
        initPreview();
        initParameters();

        _frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( barsHidden == true ){
                    ObjectAnimator anim = ObjectAnimator.ofFloat(topBar, View.TRANSLATION_Y, topBar.getMeasuredHeight()*(2/3));
                    anim.setDuration(300); //ms
                    anim.start();
                    ObjectAnimator anim2 = ObjectAnimator.ofFloat(bottomBar, View.TRANSLATION_Y, -(bottomBar.getMeasuredHeight()*(2/3)));
                    anim2.setDuration(300); //ms
                    anim2.start();
                    barsHidden = false;
                }
                else if( barsHidden == false ){
                    ObjectAnimator anim = ObjectAnimator.ofFloat(topBar, View.TRANSLATION_Y, -(topBar.getMeasuredHeight()));
                    anim.setDuration(300); //ms
                    anim.start();
                    ObjectAnimator anim2 = ObjectAnimator.ofFloat(bottomBar, View.TRANSLATION_Y, bottomBar.getMeasuredHeight());
                    anim2.setDuration(300); //ms
                    anim2.start();
                    barsHidden = true;
                }
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
	    /*
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                cid = i;
            }
	    */
        }
        return cid;
    }

    private void initPreview(){
        _cameraPreview = new CameraPreview(CameraActivity.this, camera);
        _frameLayout = (FrameLayout) findViewById(R.id.frameCamera);
        _frameLayout.addView(_cameraPreview);
        kolo = new Kolo(CameraActivity.this, width, height);
        _frameLayout.addView(kolo);
    }

    private void initParameters(){
        if( camera.getParameters() != null )
            camParams = camera.getParameters();

        resArray = new String[camParams.getSupportedPictureSizes().size()];
        for( int i = 0; i < camParams.getSupportedPictureSizes().size(); i++ )
            resArray[i] = camParams.getSupportedPictureSizes().get(i).width+"x"+camParams.getSupportedPictureSizes().get(i).height;

        balArray = new String[camParams.getSupportedWhiteBalance().size()];
        for( int i = 0; i < camParams.getSupportedWhiteBalance().size(); i++ )
            balArray[i] = camParams.getSupportedWhiteBalance().get(i);

        colArray = new String[camParams.getSupportedColorEffects().size()];
        for( int i = 0; i < camParams.getSupportedColorEffects().size(); i++ )
            colArray[i] = camParams.getSupportedColorEffects().get(i);

        ArrayList<Integer> expList = new ArrayList<Integer>(); //przykład zdefiniowania kolekcji typu String
        for( int i = camParams.getMinExposureCompensation(); i <= camParams.getMaxExposureCompensation(); i++ )
            expList.add(i); // dodanie do listy
        //expList.size() ; // rozmiar listy
        //expList.get(1); //pobranie elementu listy

        expArray = new String[expList.size()];
        for( int i = 0; i < expList.size(); i++ )
            expArray[i] = expList.get(i).toString();
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
            Toast.makeText(CameraActivity.this, "Shot taken", Toast.LENGTH_SHORT).show();
            // odswiez kamerę (zapobiega przycięciu się kamery po zrobieniu zdjęcia)
            camera.startPreview();
            lista.add(data);
            //setMiniatures(lista.get(lista.size()));
            Bitmap tmp;
            int pSize = 200;
            tmp = Imaging.convertB2Bm(pSize, pSize, data);
            miniatura = new Miniatura(CameraActivity.this, tmp, pSize, pSize, 0, 0);
            miniatury.add(miniatura);
            orientationList.add(orient);
            miniatura.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    final Miniatura m = (Miniatura) view;
                    AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                    alert.setTitle("Opcje");
                    //nie może mieć setMessage!!!
                    final String[] opcje = optionsArray;
                    alert.setItems(opcje, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("TARGET", opcje[which]);
                            wait.setVisibility(view.VISIBLE);
                            switch (which) {
                                case 0:
                                    Handler handlerp = new Handler();
                                    handlerp.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //tutaj zapis do pliku lub inne długotrwałe operacje
                                            tmpMiniatura = new Miniatura(CameraActivity.this, Imaging.convertB2Bm(width, height, lista.get(miniatury.indexOf(m))), width, height, 0, 0);
                                            _frameLayout.addView(tmpMiniatura);
                                            wait.setVisibility(view.INVISIBLE);
                                            tmpMiniatura.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    _frameLayout.removeView(tmpMiniatura);
                                                }
                                            });
                                        }
                                    }, 1);

                                    break;
                                case 1:
                                    lista.remove(miniatury.indexOf(m));
                                    _frameLayout.removeView(miniatury.get(miniatury.indexOf(m)));
                                    miniatury.remove(miniatury.indexOf(m));
                                    orientationList.remove(miniatury.indexOf(m));
                                    wait.setVisibility(view.INVISIBLE);
                                    break;
                                case 2:
                                    lista.clear();
                                    for (int n = 0; n < miniatury.size(); n++)
                                        _frameLayout.removeView(miniatury.get(n));
                                    miniatury.clear();
                                    orientationList.clear();
                                    wait.setVisibility(view.INVISIBLE);
                                    break;
                                case 3:
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //tutaj zapis do pliku lub inne długotrwałe operacje
                                            savePicture(lista.get(miniatury.indexOf(m)),orientationList.get(miniatury.indexOf(m)));
                                            wait.setVisibility(view.INVISIBLE);
                                        }
                                    }, 1);
                                    //tutaj pokazanie ekranu z informacją o zapisie lub progressa

                                    break;
                                case 4:
                                    Handler handler2 = new Handler();
                                        handler2.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                for (int n = 0; n < miniatury.size(); n++) {
                                                    //tutaj zapis do pliku lub inne długotrwałe operacje
                                                    savePicture(lista.get(n),orientationList.get(n));
                                                }
                                                wait.setVisibility(view.INVISIBLE);
                                            }
                                        }, 1);

                                        //tutaj pokazanie ekranu z informacją o zapisie lub progressa

                                    break;
                            }
                        }
                    });
                    //
                    alert.show();
                    // camParams.getSupportedPictureSizes();

                    return false;
                }
            });

            miniatura.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    final Miniatura m = (Miniatura) v;
                    Log.d("XX", "pos x: " + motionEvent.getRawX());
                    Log.d("XX", "pos y: " + motionEvent.getRawY());

                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            posx = motionEvent.getRawX();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (motionEvent.getRawX() - 100 > posx) {
                                lista.remove(miniatury.indexOf(m));
                                orientationList.remove(miniatury.indexOf(m));
                                _frameLayout.removeView(miniatury.get(miniatury.indexOf(m)));
                                miniatury.remove(miniatury.indexOf(m));
                            }
                            break;
                        case MotionEvent.ACTION_UP:

                            break;
                    }

                    return false;
                }
            });

            //int w1 = 300, w2 = 300;
            for (int j = 0; j < miniatury.size(); j++) {
                double a = Math.PI / miniatury.size() * (j - 2) * 2;
                double w1 = -1 * Math.sin(a) * (width / 4) + (height / 2) - 300;
                double w2 = -1 * Math.cos(a) * (width / 4) + (width / 2) + 100;
                miniatury.get(j).setX(Float.parseFloat("" + Math.floor(w1)));
                miniatury.get(j).setY(Float.parseFloat("" + Math.floor(w2)));
            }

            //w/2, h/2, w/4
            _frameLayout.addView(miniatura);
        }
    };

    public void savePicture(byte[] dataTab, int orientation){
        Log.d("TARGET","zapis");
        String targetFolder = Prefs.getMyPref();
        Log.d("TARGET",targetFolder);
        File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File location = new File(pic, "Ciborowski");
        File target = new File(location, targetFolder);
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String d = dFormat.format(new Date());
        File myFoto = new File(target,d+".jpg");
        Log.d("TARGET",myFoto.getAbsolutePath());
        // zapis danych zrobionego foto - konieczne dodac try catch
        FileOutputStream fs;
        try {
            //fs = new FileOutputStream(myFoto);
            //fs.write(dataTab);
            //fs.close();
            Bitmap tmp = Imaging.convertB2Bm(1280,720,dataTab);
            Matrix matrix = new Matrix();
            // rotate Bitmap
            if( orientation == 1 )
                matrix.postRotate(-90);
            //zwracam nową obróconą
            tmp = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
            fs = new FileOutputStream(myFoto);
            tmp.compress(Bitmap.CompressFormat.JPEG, 100, fs);
            fs.close();
            Log.d("TARGET","saved");
            Log.d("TARGET","saved");
            //Intent intent = new Intent(CameraActivity.this, MainActivity.class);
            //startActivity(intent);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
