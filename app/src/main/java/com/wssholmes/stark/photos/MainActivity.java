package com.wssholmes.stark.photos;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();



    private Client mKinveyClient;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabLayout.OnTabSelectedListener mTabListener;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.home_screen_view_pager);
        mViewPager.setAdapter(new MediaPagerAdapter(getSupportFragmentManager()));

        mTabLayout = (TabLayout) findViewById(R.id.home_screen_tab);

        mTabLayout.setupWithViewPager(mViewPager);




        //Progress bar to be used when using kinvey asynchronous services.
//        bar = (ProgressBar) findViewById(R.id.refresh_progress);
//        bar.setIndeterminate(true);


        //Kinvey client object to do all sorts of kinvey stuff.
        mKinveyClient = new Client.Builder(this.getApplicationContext()).build();

        //TODO: MOVE THE LOGIN TO ITS OWN ACTIVITY.
        if (!mKinveyClient.user().isUserLoggedIn()) {
//            bar.setVisibility(View.VISIBLE);
            mKinveyClient.user().login(new KinveyUserCallback() {
                @Override
                public void onSuccess(User user) {
//                    bar.setVisibility(View.GONE);
                    Log.v(LOG_TAG, "Logged in successfully as " + user.getId());
                    Toast.makeText(MainActivity.this, "New implicit user logged in successfully as " + user.getId(),
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Throwable throwable) {
//                    bar.setVisibility(View.GONE);
                    Log.e(LOG_TAG, "Login Failure", throwable);
                    Toast.makeText(MainActivity.this, "Login error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }




    @Override
    protected void onDestroy() {
        super.onPause();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
