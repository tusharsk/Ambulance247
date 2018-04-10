package com.example.tusharsk.ambulance247;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by pc on 4/11/2018.
 */

public class background_adding_history  extends AsyncTask<String,Void,String> {
    Context context;
    String url_insert="https://anubhavaron000001.000webhostapp.com/adding_history.php";

    public background_adding_history(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String userid=strings[0];
        String cab_no=strings[1];
        String destination=strings[2];

        try {
            URL url=new URL(url_insert);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data= URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8")+"&"+
                    URLEncoder.encode("cab_number","UTF-8")+"="+URLEncoder.encode(cab_no,"UTF-8")+"&"+
                    URLEncoder.encode("destination","UTF-8")+"="+URLEncoder.encode(destination,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream IS=httpURLConnection.getInputStream();
            IS.close();
            return "Added ";



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}