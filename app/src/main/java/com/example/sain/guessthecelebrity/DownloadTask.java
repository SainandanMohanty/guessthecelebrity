package com.example.sain.guessthecelebrity;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        try {
            String result = "";
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            int data = reader.read();

            while (data != -1) {
                char ch = (char) data;
                result = result.concat(String.valueOf(ch));
                data = reader.read();
            }

            return result;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
