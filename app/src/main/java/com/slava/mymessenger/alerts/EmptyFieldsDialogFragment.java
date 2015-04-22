package com.slava.mymessenger.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.slava.mymessenger.R;

/**
 * Created by Slava on 22/04/2015.
 */
public class EmptyFieldsDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.error_message_title))
                .setMessage(context.getString(R.string.fill_in_all_fields_error_text))
                .setPositiveButton(android.R.string.ok, null);

        return builder.create();
    }
}
