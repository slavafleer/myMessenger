package com.slava.mymessenger.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.slava.mymessenger.ParseConstants;
import com.slava.mymessenger.R;
import com.slava.mymessenger.alerts.CustomErrorDialogFragment;
import com.slava.mymessenger.alerts.ShowToast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TextMessageActivity extends ActionBarActivity {
    private static final String DIALOG_ERROR_TAG = "error_dialog";
    private MenuItem sendMenuItem;
    @InjectView(R.id.textMessageField) EditText mTextMessage;
    final CustomErrorDialogFragment dialog = new CustomErrorDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_message);
        ButterKnife.inject(this);

        // Appearing Send icon in Action Bar while texting.
        mTextMessage.addTextChangedListener(watcher);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.toString().equals("")) {
                sendMenuItem.setVisible(false);
            } else {
                sendMenuItem.setVisible(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_message, menu);
        sendMenuItem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_text_message) {
            onClickTextMessageButton();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.textMessageButton)
    public void onClickTextMessageButton() {
        if(! mTextMessage.getText().toString().equals("")) {
            // Create TextMessage ParseObject
            //creatTextMessage();
            Intent intent = new Intent(this, RecipientActivity.class);
            startActivity(intent);
        } else {
            // Show dialog for no text in field.
            dialog.setTitle(getString(R.string.text_messenger_error_title));
            dialog.setMessage(getString(R.string.write_message_before_sending));
            dialog.show(getFragmentManager(), DIALOG_ERROR_TAG);
        }
    }

    public void creatTextMessage() {
        ParseObject textMessage = new ParseObject(ParseConstants.CLASS_TEXT_MESSAGES);
        textMessage.put(ParseConstants.KEY_TEXT, mTextMessage.getText().toString());
        textMessage.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ShowToast.showToast(TextMessageActivity.this, "The message was sent.");
                } else {
                    // Show the error to an user
                    dialog.setTitle(getString(R.string.text_messenger_error_title));
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
