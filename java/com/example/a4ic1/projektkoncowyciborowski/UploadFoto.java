package com.example.a4ic1.projektkoncowyciborowski;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * Created by MajorKM33 on 05.12.2016.
 */
public class UploadFoto extends AsyncTask<String, Void, String> {

    Context context;
    byte [] photo;
    private ProgressDialog pDialog;
    String result = "";

    public UploadFoto(Context _context, byte[] _photo) {
        this.context = _context;
        this.photo = _photo;
        pDialog = new ProgressDialog(_context);
        pDialog.setMessage("Wysyłanie...");
        pDialog.setCancelable(false);
    }

    @Override
    protected String doInBackground(String... params) {

        //byte[] bytes = params[0];

        HttpPost httpPost = new HttpPost(Prefs.getServerUrl()); // URL_SERWERA proponuję zapisać w osobnej klasie np Settings w postaci stałej
        httpPost.setEntity(new ByteArrayEntity(photo)); // bytes - nasze zdjęcie przekonwertowane na byte[]
        DefaultHttpClient httpClient = new DefaultHttpClient(); // klient http
        HttpResponse httpResponse = null; // obiekt odpowiedzi z serwera
        try{
            httpResponse = httpClient.execute(httpPost); // wykonanie wysłania
            result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8); // odebranie odpowiedzi z serwera, którą potem wyświetlimy w onPostExecute
        }
        catch(Exception io){

        }

        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.d("TARGET",result);
        pDialog.dismiss();

        Log.e("ODP",result);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Response");
        alert.setCancelable(false);
        alert.setMessage(result);
        alert.setNeutralButton("OK", null).show();

    }
}
