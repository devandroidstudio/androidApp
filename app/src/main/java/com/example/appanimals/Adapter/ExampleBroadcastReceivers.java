package com.example.appanimals.Adapter;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.example.appanimals.Fragment.SingInFragment;

public class ExampleBroadcastReceivers extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            if (isNetworkAvilable(context)){
//                Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(context, "Internet Disconnected", Toast.LENGTH_SHORT).show();
                ShowToast_Short(context);
            }
        }
    }
    public void ShowToast_Short(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Internet Disconnected");
        builder.setMessage("Please connect to the internet to proceed further").setCancelable(false).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(intent);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                context.startActivity(new Intent(context, SingInFragment.class));
                if (isNetworkAvilable(context)){
                    dialog.dismiss();
                }


            }
        });
        AlertDialog alertDialog = builder.create();
        if (isNetworkAvilable(context)) {
            alertDialog.dismiss();
        }
        else {
            alertDialog.show();
        }

    }

    private boolean isNetworkAvilable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null){
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Network network = connectivityManager.getActiveNetwork();
            if (network == null){
                return false;
            }
            else {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                    return true;
                }
            }

            //return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        }
        else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                return true;
            }

            //return networkInfo != null && networkInfo.isConnected();
        }
        return true;

//        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//            //we are connected to a network
//            return true;
//        }
//        else
//           return false;
    }
}
