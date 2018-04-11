package com.example.tusharsk.ambulance247;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by tusharsk on 9/4/18.
 */


public class SaveSettings {
    public  static String UserID="";
    public  static String user_name="";
    public  static String flag="";
    Context context;
    SharedPreferences ShredRef;
    public  SaveSettings(Context context){
        this.context=context;
        ShredRef=context.getSharedPreferences("myRef",Context.MODE_PRIVATE);
    }

    void SaveData(String UserID,String user_name,String flag){

        SharedPreferences.Editor editor=ShredRef.edit();
        editor.putString("UserID",UserID);
        editor.putString("user_name",user_name);
        editor.putString("flag",flag);
        editor.commit();
        LoadData();
    }

    void LoadData(){
        UserID= ShredRef.getString("UserID","0");
        user_name=ShredRef.getString("user_name",null);
        flag=ShredRef.getString("flag",null);
        if (UserID.equals("0")){

            Intent intent=new Intent(context, Login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }
    String UserPresent()
    {
        UserID= ShredRef.getString("UserID","0");
        if(UserID.equals("0"))
            return "0";
        else
            return UserID;
    }

    String Username()
    {
        return  user_name;
    }
    String Userflag()
    {
        return flag;
    }
    void DeleteData()
    {
        SharedPreferences.Editor editor=ShredRef.edit();
        editor.clear();
        editor.commit();
    }
}