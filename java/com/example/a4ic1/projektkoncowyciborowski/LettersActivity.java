package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;

public class LettersActivity extends AppCompatActivity {

    String[] lista;
    LinearLayout listView;
    RelativeLayout textPreview;
    RelativeLayout colorPicker;
    ImageView colorWheel;
    ImageButton confirmColor;
    ImageButton cancelColor;
    ImageButton bBorderColor;
    ImageButton bTextColor;
    ImageView colorPreview;
    int borderColor = Color.BLACK;
    int textColor = Color.RED;
    int pickerMode = 0;
    Bitmap bmp;
    int kolor;
    EditText editText;
    TextView tv;
    int width;
    int height;
    Typeface selectedFont;
    String selFont;
    String textEntered;
    ImageButton confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letters);
        getSupportActionBar().hide();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        textPreview = (RelativeLayout) findViewById(R.id.textPreview);
        listView = (LinearLayout) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.editText);
        confirm = (ImageButton) findViewById(R.id.confirm);

        colorPicker = (RelativeLayout) findViewById(R.id.relative1);
        colorWheel = (ImageView) findViewById(R.id.colorWheel);
        confirmColor = (ImageButton) findViewById(R.id.confirmColor);
        cancelColor = (ImageButton) findViewById(R.id.cancelColor);
        bTextColor = (ImageButton) findViewById(R.id.bTextColor);
        bBorderColor = (ImageButton) findViewById(R.id.bBorderColor);
        colorPreview = (ImageView) findViewById(R.id.colorPreview);

        colorWheel.setMinimumHeight(colorWheel.getWidth());

        colorPicker.setVisibility(View.INVISIBLE);

        AssetManager assetManager = getAssets();
        try {
            lista = assetManager.list("fonts"); // fonts to nazwa podfolderu w assets
        } catch (IOException e) {
            e.printStackTrace();
        }

        for( int i = 0; i < lista.length; i++ ){
            tv = new TextView(LettersActivity.this);

            final Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/" + lista[i]);
            tv.setTypeface(tf);
            tv.setText(lista[i]);
            //tv.setWidth(width);
            tv.setTextSize(40);
            final int finalI = i;
            final int finalI1 = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedFont = tf;
                    selFont = lista[finalI1];
                    //Log.d("TARGET",selectedFont+"");
                    textEntered = editText.getText().toString();
                    textPreview.removeAllViews();
                    textPreview.addView(new PreviewText(LettersActivity.this,textEntered,selectedFont,borderColor,textColor));
                }
            });
            listView.addView(tv);
        }

        TextWatcher textWatcher = new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textEntered = editText.getText().toString();
                textPreview.removeAllViews();
                textPreview.addView(new PreviewText(LettersActivity.this,textEntered,selectedFont,borderColor,textColor));
            }
        };

        editText.addTextChangedListener(textWatcher);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextConstruct constr = new TextConstruct(textEntered,selFont,textColor,borderColor);
                //Log.d("TARGET", "" + constr.getText() + " " + constr.getFont() + " " + constr.getTcolor() + " " + constr.getBcolor());
                Intent intent = new Intent();
                intent.putExtra("tc1", constr.getText());
                intent.putExtra("tc2", constr.getFont());
                intent.putExtra("tc3", constr.getTcolor()+"");
                intent.putExtra("tc4", constr.getBcolor()+"");
                setResult(100, intent);
                finish();
            }
        });

        bTextColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerMode = 1;
                colorPicker.setVisibility(View.VISIBLE);
            }
        });

        bBorderColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerMode = 2;
                colorPicker.setVisibility(View.VISIBLE);
            }
        });

        confirmColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( pickerMode == 1 ) {
                    textColor = kolor;
                }
                else if( pickerMode == 2 ){
                    borderColor = kolor;
                }
                textEntered = editText.getText().toString();
                textPreview.removeAllViews();
                textPreview.addView(new PreviewText(LettersActivity.this,textEntered,selectedFont,borderColor,textColor));
                colorPicker.setVisibility(View.INVISIBLE);
            }
        });

        cancelColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorPicker.setVisibility(View.INVISIBLE);
            }
        });

        colorWheel.setDrawingCacheEnabled(true);

        colorWheel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //pobranie Bitmapy z obrazka:
                        bmp = view.getDrawingCache();
                        //pobranie koloru piksela z odpowiedniego miejsca bitmapy:
                        kolor = bmp.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());
                        //tu sprawdzaj i ustawiaj na bieżąco podgląd koloru:
                        colorPreview.setBackgroundColor(kolor);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //pobranie Bitmapy z obrazka:
                        bmp = view.getDrawingCache();
                        //pobranie koloru piksela z odpowiedniego miejsca bitmapy:
                        kolor = bmp.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());
                        //tu sprawdzaj i ustawiaj na bieżąco podgląd koloru:
                        colorPreview.setBackgroundColor(kolor);
                        break;
                }
                return true;
            }
        });


    }
}
