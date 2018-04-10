package com.example.tusharsk.ambulance247;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PersonalInfo_show extends AppCompatActivity {

    TextView tvname,tvdob,tvbg,tvmobile,tvage,tvemergency,tvgender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_show);
        tvname=(TextView) findViewById(R.id.tvname);
        tvdob=(TextView) findViewById(R.id.tvdob);
        tvbg=(TextView) findViewById(R.id.tvbloodgroup);
        tvmobile=(TextView) findViewById(R.id.tvmobile);
        tvage=(TextView) findViewById(R.id.tvage);
        tvemergency=(TextView) findViewById(R.id.tvemergency);
        tvgender=(TextView) findViewById(R.id.tvgender);

        String url="";
        new MyAsyncTaskgetNews().execute(url);
    }


    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    Operations operations=new Operations(getApplicationContext());
                    NewsData = operations.ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            }catch (Exception ex){}
            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try {
                JSONObject json= new JSONObject(progress[0]);
                //display response data
                if (json.getString("msg")==null)
                    return;
                if (json.getString("msg").equalsIgnoreCase("Yes")) {
                    JSONArray jsonArray=json.getJSONArray("info");
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    tvname.setText(jsonObject.getString("name"));
                    tvdob.setText(jsonObject.getString("dob"));
                    tvbg.setText(jsonObject.getString("bloodgroup"));
                    tvmobile.setText(jsonObject.getString("mobile"));
                    tvage.setText(jsonObject.getString("age"));
                    tvemergency.setText(jsonObject.getString("emergency"));
                    tvgender.setText(jsonObject.getString("gender"));
                    //login
                }

                if (json.getString("msg").equalsIgnoreCase("No")) {
                    Toast.makeText(getApplicationContext(),"Email Already Registered!",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                //Log.d("er",  ex.getMessage());
            }


        }

        protected void onPostExecute(String  result2){


        }

    }

}
