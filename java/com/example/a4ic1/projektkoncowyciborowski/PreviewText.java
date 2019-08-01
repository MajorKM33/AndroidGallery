package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

/**
 * Created by MajorKM33 on 24.11.2016.
 */
public class PreviewText extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // obiekt ustawiający kolor, czcionkę, tło i inne parametry View
    String txt;
    int c1;
    int c2;
    public boolean selected = false;

    public PreviewText(Context context, String textEntered, Typeface font, int c01, int c02) {
        super(context);
        txt = textEntered;
        c1 = c01;
        c2 = c02;
        paint.reset(); // czyszczenie
        paint.setAntiAlias(true);// wygładzanie
        paint.setTextSize(75);// wielkość fonta

        //AssetManager assetManager = getAssets();
        //String[] lista = assetManager.list("fonts");
        //Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/"+font);
        paint.setTypeface(font);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(c2);
        canvas.drawText(txt, 25, 75, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(c1);
        canvas.drawText(txt, 25, 75, paint);
    }

    public Rect getRect(){
        Rect rect = new Rect();
        paint.getTextBounds(txt, 0, txt.length(), rect);
        return rect;
    }
}
