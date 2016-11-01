package com.wssholmes.stark.photos;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();



    private Client mKinveyClient;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;


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
    }


}
