package com.slava.mymessenger.alerts;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Slava on 22/04/2015.
 */
public class ShowToast {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
