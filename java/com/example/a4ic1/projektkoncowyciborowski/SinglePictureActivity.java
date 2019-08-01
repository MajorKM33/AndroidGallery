package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SinglePictureActivity extends AppCompatActivity {

    String fileName;
    String folderName;
    String imagePath;
    String[] items = new String[3];
    ArrayAdapter<String> adapter;
    ImageView singleImage;
    ListView drawerList;
    DrawerLayout drawerLayout;
    PreviewText pt;
    PreviewText selectedT;
    TextBorder tb;
    TextBorder selectedB;
    RelativeLayout rLay;
    Networking net;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    float rrx;
    float rry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_picture);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        folderName = bundle.getString("folderName").toString();
        fileName = bundle.getString("fileName").toString();

        //Log.d("TARGET",folderName);
        Log.d("TARGET",fileName);

        net = new Networking();
        singleImage = (ImageView) findViewById(R.id.singleImage);
        rLay = (RelativeLayout) findViewById(R.id.rLay);

        File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File location = new File(pic, "Ciborowski");
        File location2 = new File("Ciborowski", folderName);
        File target = new File(location2, fileName);

        //String imagePath = target.getPath();

        //Log.d("TARGET",imagePath);

        Bitmap bmp = betterImageDecode(fileName);//imagePath);// funkcja decodeImage opisana jest ponizej
        singleImage.setImageBitmap(bmp); // wstawienie bitmapy do ImageView  

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.drawerList);

        items[0] = "fonts";
        items[1] = "upload";
        items[2] = "share";

        DrawerArrayAdapter<String> adapter = new DrawerArrayAdapter<String>(
                SinglePictureActivity.this,     // Context
                R.layout.my_cell,     // nazwa pliku xml naszej komórki
                items );         // tablica przechowująca dane

        drawerList.setAdapter(adapter);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if( position == 0 )     //texts
                {
                    Log.d("TARGET","button " + position);
                    Intent intent = new Intent(SinglePictureActivity.this,LettersActivity.class);
                    startActivityForResult(intent, 100);
                }
                if( position == 1 ){    //upload
                    boolean connection = net.checkConn(SinglePictureActivity.this);
                    if( !connection ){
                        AlertDialog.Builder alert = new AlertDialog.Builder(SinglePictureActivity.this);
                        alert.setTitle("Uwaga!");
                        alert.setCancelable(false);
                        alert.setMessage("Brak połączenia z internetem.");
                        alert.setNeutralButton("OK", null).show();
                    }
                    if( connection ){
                        rLay.setDrawingCacheEnabled(true);
                        Bitmap b = rLay.getDrawingCache(true);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        b.compress(Bitmap.CompressFormat.PNG, 80, stream);
                        byte[] bArray = stream.toByteArray();
                        Prefs.setToSend(bArray);

                        new UploadFoto(SinglePictureActivity.this, bArray).execute();
                    }
                }
                if( position == 2 ){    //sharing

                    SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String d = dFormat.format(new Date());

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg"); //typ danych który chcemy współdzielić
                    String tempFileName = "temp" + d + ".jpg"; // dodaj bieżąca datę do nazwy pliku
                    //teraz utwórz tymczasowy plik (obiekt File), który potem będzie współdzielony
                    //wpisz do niego przekonwertowaną na byte[] bitmapę pobraną ze zdjęcia (patrz poprzednie lekcje)
                    //zapisz tymczasowy plik na dysku na karcie SD w znanej sobie lokalizacji
                    String picDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File dir = new File(picDirectory);
                    File file = new File(dir, tempFileName);
                    rLay.setDrawingCacheEnabled(true);
                    Bitmap b = rLay.getDrawingCache(true);
                    //Bitmap b = ((BitmapDrawable) singleImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.PNG, 80, stream);
                    byte[] bArray = stream.toByteArray();

                    FileOutputStream fs;
                    try {
                        fs = new FileOutputStream(file);
                        fs.write(bArray);
                        fs.close();

                        Log.d("TARGET","saved temp");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/"+tempFileName));
                    startActivity(Intent.createChooser(share, "Podziel się plikiem!")); //pokazanie okna share
                }
            }
        });
    }

    private Bitmap betterImageDecode(String imagePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options(); //opcje przekształcania bitmapy
        options.inSampleSize = 1; // zmniejszenie jakości bitmapy 4x
        //
        myBitmap = BitmapFactory.decodeFile(imagePath, options);
        return myBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == 100 ){
            Bundle datab = data.getExtras();
            final String tc1 = datab.getString("tc1").toString();
            String tc2 = datab.getString("tc2").toString();
            int tc3 = Integer.parseInt(datab.getString("tc3"));
            int tc4 = Integer.parseInt(datab.getString("tc4"));
            //Log.d("TARGET", "" + constr.getText() + constr.getFont() + constr.getTcolor() + constr.getBcolor());
            Log.d("TARGET", tc1 + " " + tc2 + " " + tc3 + " " + tc4);
            Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/" + tc2);
            pt = new PreviewText(SinglePictureActivity.this,tc1,tf,tc4,tc3);
            rLay.addView(pt);
            //Rect rect = selectedT.getRect();
            //Log.d("TARGET",rect.width() + " " + rect.height());
            pt.setX(10);
            pt.setY(10);

            pt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {

                    float rawx = motionEvent.getRawX();
                    float rawy = motionEvent.getRawY();
                    //Log.d("TARGET", "pos x: " + motionEvent.getRawX());
                    //Log.d("TARGET", "pos y: " + motionEvent.getRawY());

                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                                selectedT = (PreviewText) v;
                                Rect rect = selectedT.getRect();
                                Log.d("TARGET",rect.width() + " " + rect.height());
                                tb = new TextBorder(SinglePictureActivity.this, rect.width(), rect.height());
                                rLay.addView(tb);
                                tb.setX(selectedT.getX()+20);
                                tb.setY(selectedT.getY()+20);
                                RelativeLayout.LayoutParams lparams;
                                lparams = new RelativeLayout.LayoutParams(rect.width()+40, rect.height()+40);
                                pt.setLayoutParams(lparams);
                                rrx = rawx - selectedT.getX();
                                rry = rawy - selectedT.getY();
                            //posx = motionEvent.getRawX();
                            break;
                        case MotionEvent.ACTION_MOVE:
                                float rx = rawx - rrx;
                                float ry = rawy - rry;
                            //Log.d("TARGET", rawx + " " + selectedT.getX() + " " + (rawx - selectedT.getX()) + " " + rx);
                                selectedT.setX(rx);
                                selectedT.setY(ry);

                                tb.setX(rx+20);
                                tb.setY(ry+20);
                            break;
                        case MotionEvent.ACTION_UP:
                            rLay.removeView(tb);
                            break;
                    }

                    return true;
                }
            });

        }
    }

}
