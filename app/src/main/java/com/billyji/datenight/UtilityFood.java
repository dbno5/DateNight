package com.billyji.datenight;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

public class UtilityFood
{
    public UtilityFood()
    {

    }

    public boolean checkConnection(Context context)
    {
        ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting();

        if (!isConnected)
        {
            Toast toast = Toast.makeText(context, "There is no network connection.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return isConnected;

    }

    public static void AlertMessageSimple(Context context, String title, String message)
    {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, final int which)
            {

            }
        });
        // Set the Icon for the Dialog
        alertDialog.setIcon(R.mipmap.ic_alert_2);
        alertDialog.show();
    }
}
