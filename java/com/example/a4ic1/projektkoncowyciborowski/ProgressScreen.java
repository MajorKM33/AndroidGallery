package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by MajorKM33 on 01.11.2016.
 */
public class ProgressScreen extends ImageView {

    int w;
    int h;

    public ProgressScreen(Context context, int weight, int height) {
        super(context);
        w = weight;
        h = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("TARGET","Draw started");
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.GRAY);
        canvas.drawRect(0,0,w,h,paint);
        Log.d("TARGET","Draw completed");
    }
}
