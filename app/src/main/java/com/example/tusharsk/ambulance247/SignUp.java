package com.example.tusharsk.ambulance247;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUp extends AppCompatActivity {

    EditText etemail,etname,etpassword;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etemail=(EditText)findViewById(R.id.etemail);
        etname=(EditText)findViewById(R.id.etname);
        etpassword=(EditText)findViewById(R.id.etpassword);
        bt=(Button) findViewById(R.id.btsignup);

    }

    public void SignUp(View view) {
        String e,p,n;
        e=etemail.getText().toString();
        p=etpassword.getText().toString();
        n=etname.getText().toString();

        if(!e.matches("")&&(!p.matches(""))&&(!n.matches(""))){
            String url="https://anubhavaron000001.000webhostapp.com/signup.php?name="+e+"&email="+n+"&password="+p;
            bt.setEnabled(false);
            new MyAsyncTaskgetNews().execute(url);
        }
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
                    Toast.makeText(getApplicationContext(),"Account Created!", Toast.LENGTH_LONG).show();
                    //login
                    Intent i= new Intent(SignUp.this,Login_Activity.class);
                    startActivity(i);
                    finish();

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

