package com.slava.mymessenger.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.slava.mymessenger.ParseConstants;
import com.slava.mymessenger.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Slava on 26/04/2015.
 */
public class BodiesFragment extends ListFragment {
    private static final String DIALOG_ERROR_TAG = "error_dialog";
    protected List<ParseUser> mBodies;
    protected ParseRelation<ParseUser> mBuddiesRelation;
    protected ParseUser mCurrentUser;
    @InjectView(R.id.fragmentBuddiesProgressBar) ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bodies, container, false);
        // In fragments, use ButterKnife.inject(this, rootView)
        ButterKnife.inject(this, rootView);
        mProgressBar.setVisibility(View.INVISIBLE);

        return rootView;
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
                    // TODO: while quickly returning to the fragment the app crushs with java.lang.IllegalStateException: Content view not yet created
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1, usernames);
                    setListAdapter(adapter);
                } else {
                    // Didn't worked with custom Fragment - not supported import android.support.v4.app.ListFragment;

                    // Show the error to an user
                    // e returned with lowercase first char.
                    // Changes it to uppercase.
                    String message = e.getMessage();
                    message = (message.substring(0, 1).toUpperCase()
                            + message.substring(1) + ".");
                    //dialog.show(getFragmentManager(), DIALOG_ERROR_TAG);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext())
                            .setTitle(getString(R.string.edit_buddies_error_title))
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
