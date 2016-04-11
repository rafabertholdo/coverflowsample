package com.mogujie.coverflowsample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rafaelgb on 07/04/2016.
 */
class RequestTask extends AsyncTask<String, String, Bitmap> {

    public IApiAccessResponse delegate=null;
    @Override
    protected Bitmap doInBackground(String... uri) {

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(uri[0]);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if(delegate!=null)
        {
            delegate.postResult(result);
        }
        else
        {
            Log.e("ApiAccess", "You have not assigned IApiAccessResponse delegate");
        }
    }
}