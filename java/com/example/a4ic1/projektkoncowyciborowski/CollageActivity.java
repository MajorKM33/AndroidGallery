package com.example.a4ic1.projektkoncowyciborowski;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollageActivity extends AppCompatActivity {

    int width;
    int height;
    private ArrayList<ImageData> list = new ArrayList<>();
    RelativeLayout container;
    ImageView iv;
    ImageView usedIv;
    String[] optionsArray = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);
        getSupportActionBar().hide();

        optionsArray[0] = "Galeria";
        optionsArray[1] = "Aparat";
        optionsArray[2] = "Mini-Aparat";
        optionsArray[3] = "Rotacja";
        optionsArray[4] = "Zapisz";

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x; // 800
        height = size.y; // 1216

        ArrayList<ImageData> list = (ArrayList<ImageData>) getIntent().getExtras().getSerializable("list");
        container = (RelativeLayout) findViewById(R.id.container);

        //Log.d("TARGET","rozmiar listy 1 "+list.get(1).getX());

        int count = list.size();

        for( int i = 0; i < count; i++ ){
            iv = new ImageView(CollageActivity.this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageResource(R.drawable.k1);
            iv.setX(list.get(i).getX());
            iv.setY(list.get(i).getY());
            iv.setLayoutParams(new RelativeLayout.LayoutParams(list.get(i).getW(),list.get(i).getH()));
            container.addView(iv);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    usedIv = (ImageView) view;
                    AlertDialog.Builder alert = new AlertDialog.Builder(CollageActivity.this);
                    alert.setTitle("Opcje");
                    alert.setItems(optionsArray, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, 100); // 100 - stała wartośc która posłuży do identyfikacji tej akcji
                                    break;
                                case 1:
                                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    //jesli jest dostepna zewnetrzny aparat
                                    if (intent2.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(intent2, 200); // 200 - jw
                                    }
                                    break;
                                case 2:
                                    Intent intent3 = new Intent(CollageActivity.this,CollageCameraActivity.class);
                                    startActivityForResult(intent3, 300);
                                    break;
                                case 3:
                                    Matrix matrix = new Matrix();
                                    matrix.postRotate(90);
                                    Bitmap oryginal = ((BitmapDrawable) usedIv.getDrawable()).getBitmap();
                                    Bitmap rotated = Bitmap.createBitmap(oryginal, 0, 0, oryginal.getWidth(), oryginal.getHeight(), matrix, true);
                                    usedIv.setImageBitmap(rotated);
                                    break;
                                case 4:
                                    container.setDrawingCacheEnabled(true);
                                    Bitmap b = container.getDrawingCache(true);

                                    File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                    File location = new File(pic, "Ciborowski");
                                    File target = new File(location, "kolaze");
                                    if( target.exists()){
                                        Log.i("i","ISTNIEJE");
                                    }
                                    else{
                                        Log.i("i","UTWORZONO");
                                        target.mkdir();
                                    }
                                    SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                                    String d = dFormat.format(new Date());
                                    File myFoto = new File(target,d+".jpg");
                                    Log.d("TARGET",myFoto.getAbsolutePath());
                                    // zapis danych zrobionego foto - konieczne dodac try catch
                                    FileOutputStream fs;
                                    try {
                                        fs = new FileOutputStream(myFoto);
                                        b.compress(Bitmap.CompressFormat.JPEG, 100, fs);
                                        fs.close();
                                        Log.d("TARGET","saved Collage");
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }

                        }
                    });
//
                    alert.show();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == 100 ){
            Uri imgData = data.getData();
            InputStream stream = null;
            try {
                stream = getContentResolver().openInputStream(imgData);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap b = BitmapFactory.decodeStream(stream);
            usedIv.setImageBitmap(b);
            Log.d("TARGET","działa");
        }

        if( requestCode == 200 ){
            Bundle extras = data.getExtras();
            Bitmap b = (Bitmap) extras.get("data");
            usedIv.setImageBitmap(b);
            Log.d("TARGET","działa");
        }

        if( requestCode == 300 ){

            Bundle extras = data.getExtras();
            //byte[] xdata = (byte[]) extras.get("fotodata");
            //Bitmap b = Imaging.convertB2Bm(usedIv.getHeight(),usedIv.getWidth(),xdata);
            Bitmap b = (Bitmap) extras.get("foto");
            usedIv.setImageBitmap(b);
            Log.d("TARGET","działa");
        }
    }
}
