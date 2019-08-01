package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by MajorKM33 on 01.12.2016.
 */
public class TextBorder extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int width;
    int height;

    public TextBorder(Context context, int w, int h) {
        super(context);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        //canvas.drawText(txt, 25, 75, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        //paint.setColor(c1);
        //canvas.drawText(txt, 25, 75, paint);
        canvas.drawRect(0, 0, width, height, paint);
        //canvas.drawCircle(0, height/2, 5, paint);
        //canvas.drawCircle(width, height/2, 5, paint);
        //canvas.drawCircle(width/2, 0, 5, paint);
        //canvas.drawCircle(width/2, height, 5, paint);
    }
}
