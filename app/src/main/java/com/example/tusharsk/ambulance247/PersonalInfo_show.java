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
        Toast.makeText(this," id "+SaveSettings.UserID,Toast.LENGTH_SHORT).show();
        String url="https://anubhavaron000001.000webhostapp.com/show.php?name="+SaveSettings.UserID;
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


                if (json.getString("msg").equalsIgnoreCase("Yes")) {


                    JSONArray jsonArray=new JSONArray(json.getString("info"));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);

                    //Toast.makeText(getApplicationContext(),"t"+jsonObject.getString("name"),Toast.LENGTH_SHORT).show();
                    tvname.setText("Name:   "+jsonObject.getString("email"));
                    tvdob.setText( "DOB:    "+jsonObject.getString("dob"));
                    tvbg.setText(  "Blood Group: "+ jsonObject.getString("bloodgroup"));
                    tvmobile.setText("Mobile:    "+jsonObject.getString("mobile"));
                    tvage.setText( "Age:     "+jsonObject.getString("age"));
                    tvemergency.setText("Emergency:   "+jsonObject.getString("emergency"));
                    tvgender.setText("Gender:     "+jsonObject.getString("gender"));
                    Toast.makeText(getApplicationContext(),"Details!!",Toast.LENGTH_SHORT).show();
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
