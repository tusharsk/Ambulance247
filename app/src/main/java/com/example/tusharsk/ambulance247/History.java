package com.example.tusharsk.ambulance247;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class History extends AppCompatActivity {
    String cab_no[];

    String userid=SaveSettings.UserID;
    String destination[];
    RecyclerView recyclerView;
    history_adapter history_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_6);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        history_adapter=new history_adapter(this);
        recyclerView.setAdapter(history_adapter);


        Background_history background_history=new Background_history();
        background_history.execute();
    }

    class Background_history extends AsyncTask<Void,Void,String>
    {   String json_url="https://anubhavaron000001.000webhostapp.com/previous_history.php";

        @Override
        protected void onPreExecute() {
            //   Toast.makeText(login_signup.this,"Hey",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String JSON_STRING) {

            JSONObject jsonObject;
            JSONArray jsonArray;
            try {
                jsonObject=new JSONObject(JSON_STRING);
                int count=0;
                jsonArray=jsonObject.getJSONArray("server response");

                String userid_a;
                String cab_number_a;
                String destination_a;
                int c=0;

                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);

                    userid_a=JO.getString("userid");

                    if(userid_a.equals(userid))
                    {
                        c++;
                    }


                    count++;
                }

                count=0;
                cab_no=new String[c];
                destination=new String[c];
                c=0;
                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);

                    userid_a=JO.getString("userid");
                    cab_number_a=JO.getString("cab_number");
                    destination_a=JO.getString("destination");

                    if(userid_a.equals(userid))
                    {   cab_no[c]=cab_number_a;
                        destination[c]=destination_a;


                        c++;
                    }


                    count++;
                }
                history_adapter.swapCursor(cab_no,destination);







            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(JSON_STRING);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String json_string;
            try {
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((json_string=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(json_string+"\n");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
