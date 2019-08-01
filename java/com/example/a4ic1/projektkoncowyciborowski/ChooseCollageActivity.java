package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;

public class ChooseCollageActivity extends AppCompatActivity {

    int width;
    int height;

    RelativeLayout lay;
    private ArrayList<ImageData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_collage);
        getSupportActionBar().hide();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x; // 800
        height = size.y; // 1216

        Log.d("SIZE",width+"");
        Log.d("SIZE",height+"");

        list = new ArrayList<>();
        lay = (RelativeLayout) findViewById(R.id.lay);

        for( int i = 0; i < lay.getChildCount(); i++ ){
            final int finalI = i;
            lay.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (finalI){
                        case 0:
                            Log.d("TARGET","1");
                            list.add(new ImageData(0,0,width/3,width/3));
                            list.add(new ImageData(width/3,0,width/3,width/3));
                            list.add(new ImageData((width/3)*2,0,width/3,width/3));
                            list.add(new ImageData(0,width/3,width,height-(width/3)));
                            break;
                        case 1:
                            Log.d("TARGET","2");
                            list.add(new ImageData(0,0,(width/3)*2,width/3));
                            list.add(new ImageData((width/3)*2,0,width/3,width/3));
                            list.add(new ImageData(0,width/3,(width/3)*2,height-(width/3)));
                            list.add(new ImageData((width/3)*2,width/3,width/3,height-(width/3)));
                            break;
                        case 2:
                            Log.d("TARGET","3");
                            list.add(new ImageData(0,0,width/3,width/3));
                            list.add(new ImageData(width/3,0,width/3,width/3));
                            list.add(new ImageData((width/3)*2,0,width/3,width/3));
                            list.add(new ImageData(0,width/3,(width/3)*2,height/3));
                            list.add(new ImageData((width/3)*2,width/3,width/3,height/3));
                            list.add(new ImageData(0,(width/3)+(height/3),width,height-((width/3)+(height/3))));
                            break;
                    }

                    startCollage();
                }
            });
        }

    }

    private void startCollage(){
        Log.d("TARGET","klik");
        Intent intent = new Intent(ChooseCollageActivity.this,CollageActivity.class);
        intent.putExtra("list",list);
        startActivity(intent);
    }
}
