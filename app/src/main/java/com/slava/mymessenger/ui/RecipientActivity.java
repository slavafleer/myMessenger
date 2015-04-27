package com.slava.mymessenger.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.slava.mymessenger.ParseConstants;
import com.slava.mymessenger.R;
import com.slava.mymessenger.alerts.CustomErrorDialogFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecipientActivity extends ActionBarActivity {
    private static final String DIALOG_ERROR_TAG = "error_dialog";
    protected List<ParseUser> mBodies;
    protected ParseRelation<ParseUser> mBuddiesRelation;
    protected ParseUser mCurrentUser;
    @InjectView(R.id.recipientList) ListView mRecipientList;
    @InjectView(R.id.recipientProgressBar) ProgressBar mProgressBar;
    final CustomErrorDialogFragment dialog = new CustomErrorDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);
        mRecipientList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mBuddiesRelation = mCurrentUser.getRelation(ParseConstants.KEY_BUDDIES_RELATION);

        ParseQuery<ParseUser> query = mBuddiesRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        mProgressBar.setVisibility(View.VISIBLE);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                mProgressBar.setVisibility(View.INVISIBLE);
                if (e == null) {
                    // Success
                    mBodies = parseUsers;
                    String[] usernames = new String[parseUsers.size()];
                    int i = 0;
                    for (ParseUser user : parseUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            mRecipientList.getContext(),
                            android.R.layout.simple_list_item_checked, usernames);
                    mRecipientList.setAdapter(adapter);
                } else {
                    // Show the error to an user
                    dialog.setTitle(getString(R.string.edit_buddies_error_title));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipient, menu);
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
}
