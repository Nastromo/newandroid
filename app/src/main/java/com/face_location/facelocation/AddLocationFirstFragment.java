package com.face_location.facelocation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddLocationFirstFragment extends AppCompatActivity implements View.OnClickListener{


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    final int QUANTITY_VIEWPAGERS = 3;
    private static int currentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        TextView backButton = (TextView) findViewById(R.id.backButtonText);
        backButton.setText(getString(R.string.back).toUpperCase());
        backButton.setOnClickListener(this);


        TextView forwardButton = (TextView) findViewById(R.id.forwardButtonText);
        forwardButton.setOnClickListener(this);

        PageListener pageListener = new PageListener();
        mViewPager.addOnPageChangeListener(pageListener);

    }

    //Gets the index of the currently displayed fragment
    private static class PageListener extends ViewPager.SimpleOnPageChangeListener {
        public void onPageSelected(int position) {
            currentPage = position;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forwardButtonText:
                if(currentPage != QUANTITY_VIEWPAGERS - 1){
                    mViewPager.setCurrentItem(currentPage + 1, true);
                    break;
                } else {
                    //TODO logic for final create location button
                    onBackPressed();
                    break;
                }
            case R.id.backButtonText:
                if (currentPage == 0){
                    onBackPressed();
                    break;
                } else {
                    mViewPager.setCurrentItem(currentPage - 1, true);
                    break;
                }
        }
    }

    //Set fonts for activity
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_new_location, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        //First ViewPager fragment
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            currentPage = 0; //That's for every time when zero index fragment starts current page takes 0

            View rootView = inflater.inflate(R.layout.add_location_first_fragment, container, false);
//            backButtonText.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            final EditText newLocationTitle = (EditText) rootView.findViewById(R.id.newLocationTitle);

            newLocationTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        newLocationTitle.setHint("");
                    } else {
                        newLocationTitle.setHint(getString(R.string.enter_location_name));
                    }
                }
            });
            return rootView;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);

            switch (position){
                case 0:
                    PlaceholderFragment stepOne = new PlaceholderFragment();
                    return stepOne;
                case 1:
                    AddLocationSecondFragment stepTwo = new AddLocationSecondFragment();
                    return  stepTwo;
                case 2:
                    AddLocationThirdFragment stepThree = new AddLocationThirdFragment();
                    return  stepThree;

            }
            return null;
        }

        @Override
        public int getCount() {
            // Quantity of pages.
            return QUANTITY_VIEWPAGERS;
        }
    }

//    @Override
//    public void onBackPressed() {
//        LinearLayout editTextInput = (LinearLayout) findViewById(R.id.editTextInput);
//        editTextInput.requestFocus();
//    }
}
