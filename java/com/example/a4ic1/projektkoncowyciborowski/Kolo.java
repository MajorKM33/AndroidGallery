package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by 4ic1 on 2016-10-07.
 */
public class Kolo extends View {

    int w;
    int h;

    public Kolo(Context context, int width, int height) {
        super(context);
        w = width;
        h = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(w/2, h/2, w/4, paint);
    }

}
