package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PicturesActivity extends AppCompatActivity {

    String[] fileNames;
    String folderName;
    String imagePath;
    ArrayList<String> paths = new ArrayList<>();
    LinearLayout imageList;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);
        getSupportActionBar().hide();
        Bundle bundle = getIntent().getExtras();
        folderName = bundle.getString("folder").toString();

        imageList = (LinearLayout) findViewById(R.id.imageList);

        File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File location = new File(pic, "Ciborowski");

        File targetFolder = new File(location, folderName);

        File[] files = targetFolder.listFiles();
        fileNames = new String[files.length];

        //Display display = getWindowManager().getDefaultDisplay();
        //Log.d("DS",display.getWidth());
        //Log.d("DS",display.getHeight());

        //private ActionBar.LayoutParams lparams;
        //lparams = new LinearLayout.LayoutParams(300, 300);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int j = 1;
        int smallWidth = (width/100)*35;
        int largeWidth = width - smallWidth;
        int tmpSize = largeWidth;

        LinearLayout.LayoutParams lparams;
        //lparams = new LinearLayout.LayoutParams(300, 300);
        //zmiana wielkości ImageView wg tych parametrów
        //imageview.setLayoutParams(lparams);
        LinearLayout tmpLayout = new LinearLayout(PicturesActivity.this);

        for( int i = 0; i < files.length; i++ )
        {
            fileNames[i] = files[i].getName();

            //Log.d("TARGET",fileNames[i]);

            imageview = new ImageView(PicturesActivity.this);
            //imageList.addView(imageview);

            if( i%2 == 0 )
            {
                tmpLayout = new LinearLayout(PicturesActivity.this);
                imageList.addView(tmpLayout);
            }

            tmpLayout.addView(imageview);

            lparams = new LinearLayout.LayoutParams(tmpSize, 300);
            j++;
            if( j == 2 ) {
                if( tmpSize == largeWidth )
                    tmpSize = smallWidth;
                else if( tmpSize == smallWidth )
                    tmpSize = largeWidth;
                j = 0;
            }

            imageview.setLayoutParams(lparams);
            //imageview.setMaxWidth(); //display.getWidth()

            //jesli File jest plikiem
            imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imagePath = files[i].getPath();// pobierz sciezke z obiektu File    
            paths.add(files[i].getPath());
            Bitmap bmp = betterImageDecode(imagePath);// funkcja decodeImage opisana jest ponizej
            imageview.setImageBitmap(bmp); // wstawienie bitmapy do ImageView  

            final int finalI = i;
            final int finalI1 = i;
            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("TARGET", folderName);
                    //Log.d("TARGET", fileNames[finalI]);
                    Intent intent = new Intent(PicturesActivity.this,SinglePictureActivity.class);
                    intent.putExtra("folderName", folderName);
                    intent.putExtra("fileName", paths.get(finalI1));//fileNames[finalI]);
                    startActivity(intent);
                }
            });

        }


    }

    private Bitmap betterImageDecode(String imagePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options(); //opcje przekształcania bitmapy
        options.inSampleSize = 4; // zmniejszenie jakości bitmapy 4x
        //
        myBitmap = BitmapFactory.decodeFile(imagePath, options);
        return myBitmap;
    }
}
