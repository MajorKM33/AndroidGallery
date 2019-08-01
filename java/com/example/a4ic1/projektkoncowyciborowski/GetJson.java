package com.example.a4ic1.projektkoncowyciborowski;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by MajorKM33 on 14.12.2016.
 */
public class GetJson extends AsyncTask<String, Void, String> {

    private JSONArray allImagesJson = null; //obiekt JSONArray
    private ArrayList<JsonData> jsonList = new ArrayList<>();
    private ProgressDialog pDialog;
    ArrayList<ImageData> lista  = new ArrayList<>();
    ArrayList<String> listaString;
    private Drawable loadedImage;
    Context context;
    String urlImg;
    ImageView iv;
    ImageView imageView;
    LinearLayout imageViewer;
    int location;
    int width;

    public GetJson(Context _context, LinearLayout _imageViewer, int _location, int _width) {
        this.context = _context;
        this.location = _location;
        this.width = _width;
        this.imageViewer = _imageViewer;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Pobieranie");
        pDialog.setCancelable(false);
    }

    @Override
    protected String doInBackground(String... params) {

        HttpPost httpPost = new HttpPost(Prefs.getServerUrlDown());
        Log.d("TARGET",Prefs.getServerUrlDown());
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;

        try {
            httpResponse = httpClient.execute(httpPost);
            String jsonString = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
            Log.d("TARGET",jsonString);
            //jesli jsonString nie jest pusty wtedy parsuje go na obiekt JSON
            JSONObject jsonObj = new JSONObject(jsonString);
            //a potem rozbijam na tablicę obiektów
            allImagesJson = jsonObj.getJSONArray("resources");
            //teraz mogę pobierać dane for-em z elementów tej tablicy
            for (int i = 0; i < allImagesJson.length(); i++) {

                    JSONObject object = allImagesJson.getJSONObject(i);
                    // poszczególne pola
                    String imageId = object.getString("public_id");
                    String imageVer = object.getString("version");
                    String imageSize = object.getString("bytes");
                    String imageSaveTime = object.getString("created_at");
                    //tutaj dodaj do ArrayList-y obiekt klasy ImageData
                    //Log.d("TARGET", imageUrl + " " + imageSize + " " + imageSaveTime);
                    jsonList.add(new JsonData(imageSaveTime,imageSize,imageId,imageVer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonList.size(); i++) {
            new LoadImageTask(context, imageViewer, jsonList.get(i).getPId(), jsonList.get(i).getVer(), jsonList.get(i).getBytes(), jsonList.get(i).getCreated_at(),location,width).execute();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("TARGET","sending req...");
        pDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.d("TARGET","Downloaded.");
        pDialog.dismiss();
     }

    public Drawable getPhoto( Drawable ph ){
        return ph;
    }

}
