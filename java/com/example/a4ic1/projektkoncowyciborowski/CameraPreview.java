package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.DOMImplementationSource;

import java.io.IOException;

/**
 * Created by 4ic1 on 2016-09-23.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    private Camera _camera;
    private SurfaceHolder _surfaceHolder;

    public CameraPreview(Context context, Camera _camera) {
        super(context);
        this._camera = _camera;
        this._surfaceHolder = this.getHolder();
        this._surfaceHolder.addCallback(this);
        this._camera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            _camera.setPreviewDisplay(_surfaceHolder);
            _camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            _camera.setPreviewDisplay(_surfaceHolder);
            _camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

}
