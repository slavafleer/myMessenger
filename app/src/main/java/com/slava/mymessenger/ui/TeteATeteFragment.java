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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.slava.mymessenger.ParseConstants;
import com.slava.mymessenger.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Slava on 26/04/2015.
 */
public class TeteATeteFragment extends ListFragment {
    @InjectView(R.id.teteATeteProgressBar) ProgressBar mProgressBar;
    List<ParseObject> mMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tete_a_tete, container, false);
        ButterKnife.inject(this, rootView);

        mProgressBar.setVisibility(View.INVISIBLE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_TEXT_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENTS_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        mProgressBar.setVisibility(View.VISIBLE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mProgressBar.setVisibility(View.INVISIBLE);
                if(e == null) {
                    // Success
                    mMessages = list;
                    ArrayList<String> userNames = new ArrayList<String>();

                    for(ParseObject message : mMessages) {
                        // For tete-a-tete only
                        if(message.getList(ParseConstants.KEY_RECIPIENTS_IDS).size() == 1) {
                            userNames.add(message.getString(ParseConstants.KEY_SENDER_NAME));
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1, userNames);
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
                            .setTitle(R.string.tete_a_tete_error_title)
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
