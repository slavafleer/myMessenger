package com.slava.mymessenger;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.slava.mymessenger.alerts.CustomErrorDialogFragment;
import com.slava.mymessenger.alerts.ShowToast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ResetPasswordActivity extends ActionBarActivity {
    private static final String DIALOG_ERROR_TAG = "error_dialog";
    @InjectView(R.id.resetEmailField) EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.resetButton)
    public void onClickResetButton() {
        HelpfullMethods.hideKeyboard(this);
        final CustomErrorDialogFragment dialog = new CustomErrorDialogFragment();
        if(mEmail.getText().toString().isEmpty()) {
            // Show error dialog
            dialog.setTitle(getString(R.string.reset_error_title));
            dialog.setMessage(getString(R.string.reset_no_email_error_message));
            dialog.show(getFragmentManager(),DIALOG_ERROR_TAG);
        } else {
            // Send Reset Request to Parse.com
            ParseUser.requestPasswordResetInBackground(mEmail.getText().toString(),
                    new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Success
                                ShowToast.showToast(ResetPasswordActivity.this,
                                        getString(R.string.reset_success_sent_toast));
                            } else {
                                // Show the error to an user
                                dialog.setTitle(getString(R.string.reset_error_title));
                                // e returned with lowercase first char.
                                // Changes it to uppercase.
                                String message = e.getMessage();
                                dialog.setMessage(message.substring(0, 1).toUpperCase()
                                        + message.substring(1) + ".");
                                dialog.show(getFragmentManager(), DIALOG_ERROR_TAG);
                            }
                        }
                    });
        }
    }
}
