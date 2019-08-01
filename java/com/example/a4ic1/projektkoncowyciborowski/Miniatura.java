package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by 4ic1 on 2016-10-07.
 */
public class Miniatura extends ImageView {

    Bitmap bitmap;
    int preSize1;
    int preSize2;
    double w;
    double h;
    int mode;

    public Miniatura(Context context, Bitmap b, int pS1, int pS2, double width, double height) {
        super(context);
        bitmap = b;
        preSize1 = pS1;
        preSize2 = pS2;
        w = width;
        h = height;
        this.setLayoutParams(new LinearLayout.LayoutParams(preSize1,preSize2));
        this.setBackgroundColor(Color.BLACK);
        this.setImageBitmap(bitmap);
        this.setTranslationX(0);
        this.setTranslationY(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("TARGET","Draw started");
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0,0,preSize1,preSize2,paint);
        Log.d("TARGET","Draw completed");
    }
}
