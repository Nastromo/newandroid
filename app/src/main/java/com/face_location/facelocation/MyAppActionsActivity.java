package com.face_location.facelocation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MyAppActionsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    String TAG = "MyAppActionsActivity";
    public String LOCATION_MARK = "isEdit";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_app_actions);

        TextView backTextView = (TextView) findViewById(R.id.backTextView);
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.left_arrow_dark);
//        toolbar.setTitle(null);

        //Providing UPbutton back
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavUtils.navigateUpFromSameTask(MyAppActionsActivity.this);
//            }
//        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        //Set the right tab to start
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            int value = extras.getInt("tabNumber");
            Log.i(TAG, "tabNumber: " + value);

            if(value == 1) {
                Log.i(TAG, "value: " + value);
                mViewPager.setCurrentItem(1);
            }

            if(value == 2) {
                Log.i(TAG, "value: " + value);
                mViewPager.setCurrentItem(2);
            }
        }

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    //Makes tabs spin
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    MyEventsFragment myEventsTab = new MyEventsFragment();
                    return myEventsTab;

                case 1:
                    VisitedEventsFragment visitedEventsTab = new VisitedEventsFragment();
                    return visitedEventsTab;

                case 2:
                    MyLocationsFragment myLocationsTab = new MyLocationsFragment();
                    return myLocationsTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages
            return 3;
        }
    }
}
