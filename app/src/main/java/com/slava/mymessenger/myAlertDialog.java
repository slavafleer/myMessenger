package com.slava.mymessenger;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Slava on 20/04/2015.
 */
public class myAlertDialog extends AlertDialog {
    private Context mContext;
    private String mTitle;
    private String mMessage;

    protected myAlertDialog(Context context, String title, String message) {
        super(context);

        mContext = context;
        mTitle = title;
        mMessage = message;
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
            .setTitle(mTitle)
            .setMessage(mMessage)
            .setPositiveButton(android.R.string.ok, null);
    AlertDialog dialog = builder.create();

}
