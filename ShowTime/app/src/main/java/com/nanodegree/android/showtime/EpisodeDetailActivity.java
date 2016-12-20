package com.nanodegree.android.showtime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nanodegree.android.showtime.util.Utility;

public class EpisodeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_episode_detail);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(Utility.DETAIL_URI_EXTRA_KEY, getIntent().getData());

            EpisodeDetailFragment fragment = new EpisodeDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
                    .commit();
        }
    }
}
