package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by MajorKM33 on 14.12.2016.
 */
public class LoadImageTask extends AsyncTask<String, Void, String> {

    private Drawable loadedImage;
    private Context context;
    private ImageView iv;
    private LinearLayout imageViewer;
    private String publicId;
    String version;
    String created_at;
    String bytes;
    int location;
    int width;

    public LoadImageTask(Context _context, LinearLayout _imageViewer, String _publicId, String _version, String _bytes, String _created_at, int _location, int _width) {
        this.context = _context;
        this.imageViewer = _imageViewer;
        this.publicId = _publicId;
        this.version = _version;
        this.bytes = _bytes;
        this.created_at = _created_at;
        this.location = _location;
        this.width = _width;
    }

    @Override
    protected String doInBackground(String[] params) {

        String url = "http://res.cloudinary.com/mkm333/image/upload/v" + version + "/" + publicId + ".jpg";
        Log.d("TARGET",url);
        loadedImage = LoadImageFromWeb(url);

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if( location == 1 ){

            ImageView iv = new ImageView(context);
            iv.setImageDrawable(loadedImage);
            iv.setLayoutParams(new LinearLayout.LayoutParams(width, imageViewer.getHeight()));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViewer.addView(iv);
        }

        if( location == 2 ) {
            TextView tx = new TextView(context);
            float size = Float.parseFloat(bytes)/1024;
            String size2 = size + "";
            if( size2.contains(".") ){
                if( size2.length() - size2.indexOf(".") > 2 ){
                    size2 = size2.substring(0,size2.indexOf(".") + 3);
                }
            }
            tx.setTextSize(32);
            created_at = created_at.replace("T"," ");
            created_at = created_at.replace("Z"," ");
            tx.setText("Size:" + size2 + "kB" + " " + "Date: " + created_at);

            ImageView iv = new ImageView(context);
            iv.setImageDrawable(loadedImage);
            iv.setLayoutParams(new LinearLayout.LayoutParams(width, 480));

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://res.cloudinary.com/mkm333/image/upload/v" + version + "/" + publicId + "33" + ".png"));
                    context.startActivity(intent);
                }
            });

            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViewer.addView(iv);
            imageViewer.addView(tx);
        }
    }

    public Drawable LoadImageFromWeb(String url) {

        InputStream inputStream = null;
        try {
            inputStream = (InputStream) new URL(url).getContent();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Drawable.createFromStream(inputStream, url);
    }

}
