package com.slava.mymessenger.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Slava on 22/04/2015.
 */
public class CustomErrorDialogFragment extends DialogFragment {
    private String mTitle;
    private String mMessage;

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(mTitle)
                .setMessage(mMessage)
                .setPositiveButton(android.R.string.ok, null);

        return builder.create();
    }
}
