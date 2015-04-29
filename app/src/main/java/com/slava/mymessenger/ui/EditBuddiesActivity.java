package com.slava.mymessenger.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.slava.mymessenger.ParseConstants;
import com.slava.mymessenger.R;
import com.slava.mymessenger.alerts.CustomErrorDialogFragment;
import com.slava.mymessenger.alerts.ShowToast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditBuddiesActivity extends ActionBarActivity {
    private static final String DIALOG_ERROR_TAG = "error_dialog";
    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mBuddiesRelation;
    protected ParseUser mCurrentUser;
    @InjectView(R.id.editBuddiesList) ListView mUsersList;
    @InjectView(R.id.editBuddiesProgressBar) ProgressBar mProgressBar;
    final CustomErrorDialogFragment dialog = new CustomErrorDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buddies);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);
        mUsersList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mUsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mUsersList.isItemChecked(position)) {
                    // Add buddy
                    mBuddiesRelation.add(mUsers.get(position));
                    mCurrentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Success
                                ShowToast.showToast(EditBuddiesActivity.this,
                                        getString(R.string.new_buddy_added));
                            } else {
                                // Show the error to an user
                                dialog.setTitle(getString(R.string.edit_buddies_error_title));
                                // e returned with lowercase first char.
                                // Changes it to uppercase.
                                String message = e.getMessage();
                                dialog.setMessage(message.substring(0, 1).toUpperCase()
                                        + message.substring(1) + ".");
                                dialog.show(getFragmentManager(), DIALOG_ERROR_TAG);
                            }
                        }
                    });
                } else {
                    // Remove Buddy
                    mBuddiesRelation.remove(mUsers.get(position));
                    mCurrentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if( e == null) {
                                // Success
                                ShowToast.showToast(EditBuddiesActivity.this,
                                        getString(R.string.remove_buddy));
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
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mBuddiesRelation = mCurrentUser.getRelation(ParseConstants.KEY_BUDDIES_RELATION);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        mProgressBar.setVisibility(View.VISIBLE);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                mProgressBar.setVisibility(View.INVISIBLE);
                if (e == null) {
                    // Success
                    mUsers = parseUsers;
                    String[] usernames = new String[parseUsers.size()];
                    int i = 0;
                    for (ParseUser user : parseUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditBuddiesActivity.this,
                            android.R.layout.simple_list_item_checked, usernames);
                    mUsersList.setAdapter(adapter);
                } else {
                    // Show the error to an user
                    dialog.setTitle(getString(R.string.edit_buddies_error_title));
                    // e returned with lowercase first char.
                    // Changes it to uppercase.
                    String message = e.getMessage();
                    dialog.setMessage(message.substring(0, 1).toUpperCase()
                            + message.substring(1) + ".");
                    dialog.show(getFragmentManager(), DIALOG_ERROR_TAG);
                }

                addBuddiesCheckMarks();
            }
        });
    }

    private void addBuddiesCheckMarks() {
        mBuddiesRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> buddies, ParseException e) {
                if(e == null) {
                    for(int i = 0; i < mUsers.size(); i++) {
                        ParseUser user = mUsers.get(i);
                        for(ParseUser buddy : buddies) {
                            if(buddy.getObjectId().equals(user.getObjectId())) {
                                mUsersList.setItemChecked(i, true);
                            }
                        }
                    }
                } else {
                    // Show the error to an user
                    dialog.setTitle(getString(R.string.edit_buddies_error_title));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_buddies, menu);
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
