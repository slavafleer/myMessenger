package com.slava.mymessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.slava.mymessenger.alerts.CustomErrorDialogFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SignupActivity extends ActionBarActivity {
    private static final String DIALOG_ERROR_TAG = "error_dialog";

    @InjectView(R.id.signupUsernameField) EditText mUsername;
    @InjectView(R.id.signupPasswordField) EditText mPassword;
    @InjectView(R.id.signupConfirmedPassword) EditText mConfirmedPassword;
    @InjectView(R.id.signupEmailField)    EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.signupButton)
    protected void onClickSignupButton() {
        // trim() removes spaces before and after the string,
        // spaces between words in the string remain.
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmedPassword = mConfirmedPassword.getText().toString().trim();
        String email = mEmail.getText().toString().trim();

        final CustomErrorDialogFragment dialog = new CustomErrorDialogFragment();

        if(username.isEmpty() || password.isEmpty() ||
                email.isEmpty() || confirmedPassword.isEmpty() ) {
            // Show error dialog alert
            dialog.setTitle(getString(R.string.error_message_title));
            dialog.setMessage(getString(R.string.fill_in_all_fields_error_text));
            dialog.show(getFragmentManager(), DIALOG_ERROR_TAG);

        } else if(! password.equals(confirmedPassword)) {
            // Show error for confirmed password
            dialog.setTitle(getString(R.string.password_error_title_text));
            dialog.setMessage(getString(R.string.confirmed_password_not_matched_error_text));
            dialog.show(getFragmentManager(), DIALOG_ERROR_TAG);
        } else {
            // Create new user and save it on Parse.com
            ParseUser newUser = new ParseUser();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        //Success - Start Main Activity
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        // Clearing activities history.
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        // Show the error to an user
                        dialog.setTitle(getString(R.string.signup_error_title));
                        // e returned with lowercase first char.
                        // Changes it to uppercase.
                        String message = e.getMessage();
                        dialog.setMessage(message.substring(0,1).toUpperCase()
                                + message.substring(1) + ".");
                        dialog.show(getFragmentManager(), DIALOG_ERROR_TAG);
                    }
                }
            });
        }
    }
}
