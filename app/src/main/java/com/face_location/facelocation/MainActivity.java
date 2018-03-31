package com.face_location.facelocation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.face_location.facelocation.model.DataBase.DataBaseHelper;
import com.face_location.facelocation.model.FacelocationAPI;
import com.face_location.facelocation.model.GetEvent.Address;
import com.face_location.facelocation.model.GetNearestEvents.NearestEventResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    public static final String TAG = "MainActivity";
    private GoogleMap mMap;
    static final LatLng mDefaultLocation = new LatLng(49.84, 24.03);
    static final int DEFAULT_ZOOM = 15;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    View mapView; //Padding the My location button
    LatLng latLng;
    TextView placeName;
    TextView placeAddress;
    TextView editMyProfileTextView, userNameTextView;
    String placeNameString;
    String placeAddressString, userAvatar, userName;
    Intent myProfileActivity;
    ImageView myProfileImageView;

    DataBaseHelper applicationDB;
    String[] userArrayData;

    String url, token, eventID;
    double latitude, longitude;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO delete this if don't get paid for progressbar
//        LogInActivity.pDialog.hide();

        applicationDB = DataBaseHelper.getInstance(this);

        url = getString(R.string.base_url);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // mapView = mapFragment.getView();  //Padding the My location button
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.cross);
        float val = 1.1f;
        fab.setScaleY(val);
        fab.setScaleX(val);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newLocation = new Intent(getApplicationContext(), AddLocationFirstActivity.class);
                startActivity(newLocation);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //getting header with all elements in it. Then finding element by ID and setListener
        View header = navigationView.getHeaderView(0);

        myProfileImageView = (ImageView) header.findViewById(R.id.myProfileImageView);
        myProfileImageView.setOnClickListener(this);

        editMyProfileTextView = (TextView) header.findViewById(R.id.editMyProfileTextView);
        editMyProfileTextView.setOnClickListener(this);

        userNameTextView = (TextView) header.findViewById(R.id.userNameTextView);
        userNameTextView.setOnClickListener(this);

        placeName = (TextView) findViewById(R.id.placeName);
        placeAddress = (TextView) findViewById(R.id.placeAddress);

        userArrayData = applicationDB.retrieveFirstLoginValues();

        if (userArrayData != null) {
            userAvatar = userArrayData[10];
            if (userAvatar != null) {
                if (userAvatar.equals(getString(R.string.def_avatar)) || userAvatar.equals(getString(R.string.def_avatar_second))) {
                    //go further
                } else {
                    myProfileImageView.setBackground(null);
                    Glide
                            .with(MainActivity.this)
                            .load(userAvatar)
                            .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                            .apply(RequestOptions
                                    .circleCropTransform())
//                            .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                            .into(myProfileImageView);
                }
            }

            userName = userArrayData[6];
            if (userName != null) {
                userNameTextView.setText(userName);
            } else {
                userNameTextView.setText(getString(R.string.your_name_menu));
            }
        }
    }

    public void showNearestEvents(final double latitude, double longitude) {
        boolean published = true;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String[] userInfo = applicationDB.retrieveFirstLoginValues();
        token = userInfo[5];
        Log.i(TAG, "ТОКЕН ЮЗЕРА ПРИ ВХОДЕ: " + token);

        FacelocationAPI api = retrofit.create(FacelocationAPI.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Auth", token);

        Log.i(TAG, "ОБЪЕКТ: " + latitude + " " + longitude + " " + published);

        Call<List<NearestEventResponse>> call = api.getNearestEvents(headers, latitude, longitude, published);
        call.enqueue(new Callback<List<NearestEventResponse>>() {
            @Override
            public void onResponse(Call<List<NearestEventResponse>> call, Response<List<NearestEventResponse>> response) {
                Log.i(TAG, "ОТВЕТ СЕРВЕРА: " + response.toString());

                List<NearestEventResponse> nearestEvents = response.body();
                if (nearestEvents.isEmpty()){
                    Log.i(TAG, "ПОБЛИЗОСТИ НЕТ ИНВЕНТОВ");
                }else {
                    for (int i = 0; i < nearestEvents.size(); i++) {
                        NearestEventResponse event = nearestEvents.get(i);
                        eventID = event.getId();
                        Log.i(TAG, "ID ИВЕНТА ПОБЛИЗОСТИ: " + eventID);
                        List<com.face_location.facelocation.model.GetEvent.Location> locations = event.getLocations();
                        if (locations.isEmpty()){
                            Log.i(TAG, "НЕТ!!! ЛОКАЦИЙ В ИВЕНТЕ");
                        }else {
                            for (int j = 0; j < locations.size(); j++) {
                                com.face_location.facelocation.model.GetEvent.Location location = locations.get(j);
                                Address address = location.getAddress();
                                List<Double> latlongList = address.getPosition();
                                if (latlongList.isEmpty()){
                                    Log.i(TAG, "В ЛОКАЦИИ НЕТ КООРДИНАТ");
                                } else {
                                    for (int k = 0; k < latlongList.size(); k++) {
                                        double lat = latlongList.get(0);
                                        double lon = latlongList.get(1);

                                        LatLng latlng = new LatLng(lat, lon);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(latlng);
                                        markerOptions.snippet(eventID);
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.event_marker));
                                        mMap.addMarker(markerOptions);
                                    }
                                }
                            }
                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<List<NearestEventResponse>> call, Throwable t) {
                Log.i(TAG, "ОШИБКА ПОЛУЧАНИЯ БЛИЖАЙШИХ ЛОКАЦИЙ НА КАРТЕ" + t.toString());
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

        if (userArrayData != null){
            userAvatar = userArrayData[10];
            if (userAvatar != null){
                if (userAvatar.equals(getString(R.string.def_avatar)) || userAvatar.equals(getString(R.string.def_avatar_second))){
                    //go further
                } else {
                    myProfileImageView.setBackground(null);
                    Glide
                            .with(MainActivity.this)
                            .load(userAvatar)
                            .thumbnail(0.1f)
                            .apply(RequestOptions.circleCropTransform())
                            .into(myProfileImageView);
                }
            }

            userName = userArrayData[6];
            if (userName != null){
                userNameTextView.setText(userName);
            } else {
                userNameTextView.setText(getString(R.string.your_name_menu));
            }
        }

        //stop location updates when Activity is no longer active
        //if (mGoogleApiClient != null) {
        //    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        //}
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userArrayData != null){
            userAvatar = userArrayData[10];
            if (userAvatar != null){
                if (userAvatar.equals(getString(R.string.def_avatar)) || userAvatar.equals(getString(R.string.def_avatar_second))){
                    //go further
                } else {
                    myProfileImageView.setBackground(null);
                    Glide
                            .with(MainActivity.this)
                            .load(userAvatar)
                            .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                            .apply(RequestOptions
                                    .circleCropTransform())
//                            .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                            .into(myProfileImageView);
                }
            }

            userName = userArrayData[6];
            if (userName != null){
                userNameTextView.setText(userName);
            } else {
                userNameTextView.setText(getString(R.string.your_name_menu));
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (userArrayData != null){
            userAvatar = userArrayData[10];
            if (userAvatar != null){
                if (userAvatar.equals(getString(R.string.def_avatar)) || userAvatar.equals(getString(R.string.def_avatar_second))){
                    //go further
                } else {
                    myProfileImageView.setBackground(null);
                    Glide
                            .with(MainActivity.this)
                            .load(userAvatar)
                            .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                            .apply(RequestOptions
                                    .circleCropTransform())
//                            .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                            .into(myProfileImageView);
                }
            }

            userName = userArrayData[6];
            if (userName != null){
                userNameTextView.setText(userName);
            } else {
                userNameTextView.setText(getString(R.string.your_name_menu));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (userArrayData != null){
            userAvatar = userArrayData[10];
            if (userAvatar != null){
                if (userAvatar.equals(getString(R.string.def_avatar)) || userAvatar.equals(getString(R.string.def_avatar_second))){
                    //go further
                } else {
                    myProfileImageView.setBackground(null);
                    Glide
                            .with(MainActivity.this)
                            .load(userAvatar)
                            .thumbnail(0.1f) //shows mini image which weight 0.1 from real image while real image is downloading
                            .apply(RequestOptions
                                    .circleCropTransform())
//                            .placeholder(R.drawable.oval)) //shows drawable while real/mini image is downloading
                            .into(myProfileImageView);
                }
            }

            userName = userArrayData[6];
            if (userName != null){
                userNameTextView.setText(userName);
            } else {
                userNameTextView.setText(getString(R.string.your_name_menu));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //LocationForAdapter Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

            } else {
                //Request LocationForAdapter Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    String eventID = marker.getSnippet();
                    Log.i(TAG, "СНИПЕТ МАРКЕРА НА КОТОРЫЙ НАЖАЛИ: " + eventID);

                    if (eventID != null){
                        Intent eventActivity = new Intent(MainActivity.this, EventActivity.class);
                        Log.i(TAG, "ID ИВЕНТА НА КОТОРЫЙ НАЖАЛИ: " + eventID);
                        eventActivity.putExtra("id", eventID);
                        eventActivity.putExtra("latitude", latitude);
                        eventActivity.putExtra("longitude", longitude);
                        startActivity(eventActivity);
                    }
                    return true;
                } else {
                    checkLocationPermission();
                }
                return true;
            }
        });

        //turn off google maps toolbar for native google maps app launch
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //turn off google maps My location button
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        //Get the last known coordinates. Lust know coordinates updates every 1 sec
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng lastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, DEFAULT_ZOOM));
        }else {
            latitude = 49.84;
            longitude = 24.03;
        }

            showNearestEvents(latitude, longitude);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {

        counter++;
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(getString(R.string.my_location));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        if (counter == 1){
            showNearestEvents(location.getLatitude(), location.getLongitude());
        }

        //move map camera
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.permission_needed)
                        .setMessage(R.string.location_permission_text)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
                    mMap.addMarker(new MarkerOptions()
                            .position(mDefaultLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));

                    showNearestEvents(49.84, 24.03);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    Creates 3 right dots menu with options from file menu.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


//    Handle clicks on options in 3 right dots menu
//    The action bar will automatically handle clicks on the Home/Up button, so long
//    as you specify a parent activity in AndroidManifest.xml.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.geo_search) {
            //Getting autocomplete overlay for geo search
            mMap.clear();
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_events) {
            Intent myAppActionsActivity = new Intent(this, MyAppActionsActivity.class);
            startActivity(myAppActionsActivity);

        } else if (id == R.id.visited_events) {
            Intent myAppActionsActivity = new Intent(this, MyAppActionsActivity.class);
            myAppActionsActivity.putExtra("tabNumber", 1);
            startActivity(myAppActionsActivity);

        } else if (id == R.id.my_locations) {
            Intent myAppActionsActivity = new Intent(this, MyAppActionsActivity.class);
            myAppActionsActivity.putExtra("tabNumber", 2);
            startActivity(myAppActionsActivity);

        } else if (id == R.id.create_event) {
            Intent searchLocationActivity = new Intent(this, SearchLocationActivity.class);
            startActivity(searchLocationActivity);

        } else if (id == R.id.add_location) {
            Intent addLocationFirstActivity = new Intent(this, AddLocationFirstActivity.class);
            startActivity(addLocationFirstActivity);

        } else if (id == R.id.support) {
            Intent supportActivity = new Intent(this, SupportActivity.class);
            startActivity(supportActivity);

        } else if (id == R.id.exit) {

            boolean result = applicationDB.updateFirstLoginValue(userArrayData[0]);
            if (result == true){
                Log.i(TAG, "Обновление в БД: УСПЕШНО");

            } else {
                Log.i(TAG, "Обновление в БД: НЕ ОБНОВЛЕНО :(");
            }

            Intent startActivity = new Intent(this, StartActivity.class);
            startActivity(startActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myProfileImageView:
                myProfileActivity = new Intent(this, MyProfileActivity.class);
                startActivity(myProfileActivity);
                break;
            case R.id.editMyProfileTextView:
                myProfileActivity = new Intent(this, MyProfileActivity.class);
                startActivity(myProfileActivity);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng toLatLng = place.getLatLng();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toLatLng, DEFAULT_ZOOM));
                showNearestEvents(toLatLng.latitude, toLatLng.longitude);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Toast.makeText(this, "An error!!!", Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    //Set fonts for activity
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
