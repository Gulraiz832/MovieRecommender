package com.example.trailer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Broadcast extends BroadcastReceiver {

    static String title;
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
        if (isConnected){
            Toast.makeText(context,"Connected To Internet Loading Started!!",Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(context,"Please Connect To Internet",Toast.LENGTH_LONG).show();
        }

    }
     /*if(MainActivity.active){
               Context context1=MainActivity.context;
               Intent intent1 =new Intent(context1,MainActivity.class);
               ((Activity)context1).finish();
               context1.startActivity(intent1);

           }
            if(TrailerPage.active){
                title=TrailerPage.title;
                Context context1=TrailerPage.trailercontext;
                Intent intent1 =new Intent(context1,TrailerPage.class);
                intent1.putExtra("Name",title);
                ((Activity)context1).finish();
                context1.startActivity(intent1);

            }*/
}
