package com.designtech9studio.puntersapp.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Adapter.ImageAdapter;
import com.designtech9studio.puntersapp.Adapter.MainPageAdapter;
import com.designtech9studio.puntersapp.Adapter.MainPageAdapterLower;
import com.designtech9studio.puntersapp.Adapter.RecomendedSericeAdapter;
import com.designtech9studio.puntersapp.BuildConfig;
import com.designtech9studio.puntersapp.ConnectionClass;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Model.AddressModel;
import com.designtech9studio.puntersapp.Model.BannerModel;
import com.designtech9studio.puntersapp.Model.CategoryMasterModel;
import com.designtech9studio.puntersapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class
ClientHomepageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ClientHomepageActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    private static final String LOCATION_ADDRESS_KEY = "location-address";
    public static final String GOOGLE_ACCOUNT = "google_account";
    private TextView profileName, profileEmail;
    private ImageView profileImage;
    private Button signOut,allcategories;

    private FusedLocationProviderClient mFusedLocationClient;

    AlertDialog alert;

    boolean newAddress = false;

    /**
     * Represents a geographical location.
     */
    private Location mLastLocation;

    /**
     * Tracks whether the user has requested an address. Becomes true when the user requests an
     * address and false when the address (or an error message) is delivered.
     */
    private boolean mAddressRequested;

    /**
     * The formatted location address.
     */
    private String mAddressOutput;

    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;

    /**
     * Displays the location address.
     */
    private TextView mLocationAddressTextView;

    /**
     * Visible while the address is being fetched.
     */
    private ProgressBar mProgressBar;

    /**
     * Kicks off the request to fetch an address when pressed.
     */
    private Toolbar toolbar;
    private ArrayList<ClassListItems> itemsArrayList;
    //private MyAppAdapter myAppAdapter;

    private MainPageAdapter mainPageAdapter;
    private MainPageAdapterLower mainPageAdapterBelower;

    private RecyclerView recyclerView; //serviceRecyclerView_2;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean success = false;
    private ConnectionClass connectionClass;

    List<CategoryMasterModel> serviceList_Main;

    Menu searchMenu;

    List<BannerModel> bannerModelList;
    RecyclerView bannerReCyler, recommendedServicesRecycler;

    ImageView Salon_At_Home, Spa_At_Home, Appliance_Repair, House_Cleaning, Home_Design, Shifting_Home, Tution_Classes, Other_Services;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_homepage);
        try{
            recyclerView = findViewById(R.id.serviceRecyclerView);
            //serviceRecyclerView_2 = findViewById(R.id.serviceRecyclerView_2);
            bannerReCyler = findViewById(R.id.clientHomePageBanner);
            recommendedServicesRecycler = findViewById(R.id.recommendedServicesRecycler);

            Salon_At_Home = findViewById(R.id.Salon_At_Home);
            Spa_At_Home = findViewById(R.id.Spa_At_Home);
            Appliance_Repair = findViewById(R.id.Appliance_Repair);
            House_Cleaning = findViewById(R.id.House_Cleaning);
            Home_Design = findViewById(R.id.Home_Design);
            Shifting_Home = findViewById(R.id.Shifting_Home);
            Tution_Classes = findViewById(R.id.Tution_Classes);
            Other_Services = findViewById(R.id.Other_Services);

            Salon_At_Home.setOnClickListener(this);
            Spa_At_Home.setOnClickListener(this);
            Appliance_Repair.setOnClickListener(this);
            House_Cleaning.setOnClickListener(this);
            Home_Design.setOnClickListener(this);
            Shifting_Home.setOnClickListener(this);
            Tution_Classes.setOnClickListener(this);
            Other_Services.setOnClickListener(this);




            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            mResultReceiver = new AddressResultReceiver(new Handler());

            mLocationAddressTextView = (TextView) findViewById(R.id.location);



            mLocationAddressTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* startActivity(new Intent(ClientHomepageActivity.this, LocationActivity.class));
                    finish();*/
                }
            });
            mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
            mAddressRequested = false;
            mAddressOutput = "";
            updateValuesFromBundle(savedInstanceState);

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
       /* recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);*/

            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);

            connectionClass = new ConnectionClass();
            itemsArrayList = new ArrayList<ClassListItems>();

            SyncData orderData = new SyncData();
            orderData.execute("");
            updateUIWidgets();

            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
            Menu menu = bottomNavigationView.getMenu();
            MenuItem menuItem = menu.getItem(0);
            menuItem.setChecked(true);

            if (!checkPermissions()) {
                requestPermissions();
            } else {
                getAddress();
            }
            if (mLastLocation != null) {
                startIntentService();
                return;
            }

            // If we have not yet retrieved the user location, we process the user's request by setting
            // mAddressRequested to true. As far as the user is concerned, pressing the Fetch Address button
            // immediately kicks off the process of getting the address.
            AlertDialog.Builder builder = new AlertDialog.Builder(this,  R.style.AlertDialogTheme);
            builder.setMessage("Please Log In First To proceed")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            alert = builder.create();
            //Setting the title manually
            alert.setTitle("Log In");

            mAddressRequested = true;
            updateUIWidgets();
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    SharedPreferenceHelper helper = new SharedPreferenceHelper(getApplicationContext());
                    if (item.getItemId()!=R.id.ic_android && !helper.isUserLogedIn()) {
                        alert.show();
                        //Toast.makeText(ClientHomepageActivity.this, "Please! Log in first", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    switch (item.getItemId()){

                        case R.id.ic_android:

                            break;

                        case R.id.ic_books:
                            Intent intent2 = new Intent(ClientHomepageActivity.this, MyAppointments.class);
                            startActivity(intent2);
                            break;

                        case R.id.ic_center_focus:
                            Intent intent3 = new Intent(ClientHomepageActivity.this, ClientProfileActivity .class);
                            startActivity(intent3);
                            break;

                        case R.id.ic_backup:
                            Intent intent4 = new Intent(ClientHomepageActivity.this, CheckOutActivity.class);
                            startActivity(intent4);
                            break;
                    }


                    return false;
                }
            });
            Button services2 = findViewById(R.id.services2);
            Button services1 = findViewById(R.id.services1);
            Button allcategories = findViewById(R.id.allcategories);



            allcategories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AllServices.class);
                    startActivity(intent);
                }
            });
            services1.setOnClickListener(this);
            services2.setOnClickListener(this);

            mLocationAddressTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SetAddress.class);
                    startActivity(intent);
                }
            });

            if (getIntent().hasExtra(IntentsTags.NEW_ADDRESS)) {
                Log.i("NewAddSet", "True");
                newAddress = true;

            }


       /* salon_card.setOnClickListener(this);
        service_card.setOnClickListener(this);
        clean_card.setOnClickListener(this);*/
        /*men_card.setOnClickListener(this);
        paint_card.setOnClickListener(this);
        makeup_card.setOnClickListener(this);
        teach_card.setOnClickListener(this);
        pest_card.setOnClickListener(this);*/

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }/*DataBaseHelper dataBaseHelper = new DataBaseHelper();
        dataBaseHelper.getBookingDetails("36");*/
    @SuppressWarnings("MissingPermission")

    private class SyncData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){

            progress = new ProgressDialog(ClientHomepageActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();

        }

        protected String doInBackground(String... strings){
            try{
                DataBaseHelper dataBaseHelper = new DataBaseHelper();


                //dataBaseHelper.alterBooking2();
                serviceList_Main = dataBaseHelper.getMainPageServicesData();
                bannerModelList = dataBaseHelper.getBannerData();

                //System.out.println("Get wedding sub cat");
                //dataBaseHelper.getSubCategoryData("8");

                /*Devide the service list into two*/
                /*int n = serviceList_Main.size() / 2;
                for (int i=0;i<serviceList_Main.size();i++) {
                    if (i<n) {
                        serviceList_upper.add(serviceList_Main.get(i));
                    }else {
                        serviceList_lower.add(serviceList_Main.get(i));
                    }
                }*/
                Log.i("Date", "Distributed");

            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return "";
        }
        protected void onPostExecute(String msg){

            progress.dismiss();
            try {

                int d = serviceList_Main.size() * 250;
                mainPageAdapter = new MainPageAdapter(serviceList_Main, ClientHomepageActivity.this);
                Log.i("Adapter", "Setting");
                Log.i("AdapterList", String.valueOf(serviceList_Main.size()));
                LinearLayout.LayoutParams lp =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, d);
                recyclerView.setLayoutParams(lp);

                /*LinearLayoutManager l4 = new LinearLayoutManager(ClientHomepageActivity.this);
                l4.setOrientation(LinearLayoutManager.HORIZONTAL);
                l4.setMeasuredDimension();
                recyclerView.setLayoutManager(l4);*/

                recyclerView.setAdapter(mainPageAdapter);



                //recyclerView.setLayoutManager(l4);


                //mainPageAdapterBelower = new MainPageAdapterLower(serviceList_Main, ClientHomepageActivity.this);

                List<String> bannerImages = BannerModel.getBannerImageList(bannerModelList);

                ImageAdapter imageAdapter = new ImageAdapter(bannerImages, ClientHomepageActivity.this);
                LinearLayoutManager l2 = new LinearLayoutManager(ClientHomepageActivity.this);
                l2.setOrientation(LinearLayoutManager.HORIZONTAL);
                bannerReCyler.setLayoutManager(l2);
                bannerReCyler.setAdapter(imageAdapter);

                List<CategoryMasterModel> recommendedService = new ArrayList<>();
                for (CategoryMasterModel i: serviceList_Main){
                    if (i.getMainPage() == 1) {
                        recommendedService.add(i);
                    }
                }

                RecomendedSericeAdapter recomendedSericeAdapter = new RecomendedSericeAdapter(recommendedService, ClientHomepageActivity.this);
                LinearLayoutManager l3 = new LinearLayoutManager(ClientHomepageActivity.this);
                l3.setOrientation(LinearLayoutManager.HORIZONTAL);
                recommendedServicesRecycler.setLayoutManager(l3);
                recommendedServicesRecycler.setAdapter(recomendedSericeAdapter);

                    /*myAppAdapter = new MyAppAdapter(itemsArrayList, ClientHomepageActivity.this);
                    recyclerView.setAdapter(myAppAdapter);*/
            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }


    void setAddressAndCity(Location location) {

        if (location!= null){
            mLastLocation = location;
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(sydney.latitude, sydney.longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    System.out.println(addresses.toString());
                    Address address = addresses.get(0);
                    String streatFeature1 = address.getFeatureName();
                    String state = address.getAdminArea();
                    String stratefeature2 = address.getSubAdminArea();
                    String city = address.getLocality();
                    String country = address.getCountryName();
                    String lat = String.valueOf(address.getLatitude());
                    String longitude = String.valueOf(address.getLongitude());
                    String completeAddress = address.getAddressLine(0);

                    if (completeAddress!=null) {
                        stratefeature2 = "";
                        if (city!=null)
                        completeAddress =completeAddress.replace(city, "");
                        //.replace(country, "").replace(state, "").split(",")
                        if (country!=null)
                        completeAddress =completeAddress.replace(country, "");
                        if (state!=null)
                            completeAddress = completeAddress.replace(state, "");

                        String add[] = completeAddress.split(",");
                        //System.out.println("Complete Adress Seperate " + stratefeature2 + " " + temp2 + "   " +temp3);

                        for (int i=0;i<add.length;i++) {
                            System.out.println("Complete Address " + add[i]);
                            if (i==0) {
                                streatFeature1 = add[i].trim();
                            }else{
                                stratefeature2+= add[i].trim() + " ";
                            }
                        }


                        AddressModel addressModel = new AddressModel();

                        addressModel.setStreetAddress1(streatFeature1);
                        addressModel.setStreetAddress2(stratefeature2);
                        addressModel.setStateName(state);
                        addressModel.setCountryName(country);
                        addressModel.setLat(lat);
                        addressModel.setLot(longitude);
                        addressModel.setCityName(city);
                        System.out.println("Complete Address: " + completeAddress);
                        System.out.println("Complete Address: " + addressModel.toString());
                        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
                        if (!getIntent().hasExtra(IntentsTags.NEW_ADDRESS))
                        sharedPreferenceHelper.setAddress(addressModel);
                    }





                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void getAddress() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.w(TAG, "onSuccess:null");
                            return;
                        }

                        mLastLocation = location;
                        setAddressAndCity(mLastLocation);


                        // Determine whether a Geocoder is available.
                        if (!Geocoder.isPresent()) {
                            showSnackbar(getString(R.string.no_geocoder_available));
                            return;
                        }

                        // If the user pressed the fetch address button before we had the location,
                        // this will be set to true indicating that we should kick off the intent
                        // service after fetching the location.
                        if (mAddressRequested) {
                            startIntentService();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getLastLocation:onFailure", e);
                    }
                });
    }

    private void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(ClientHomepageActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(ClientHomepageActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client_homepage_bar, menu);
        searchMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent search = new Intent(this,SubCategorySearch.class);
        startActivity(search);
        return true;
       /* switch (item.getItemId()){
            case R.id.action_search:
                Intent search = new Intent(this,LocationActivity.class);
                startActivity(search);
                return true;*/
        //}

        //noinspection SimplifiableIfStatement


        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent i;

        String tag= view.getTag().toString();

        int id = getId(tag);

        if (id !=0) {
            Intent intent = new Intent(getApplicationContext(), AllServices.class);

            intent.putExtra(IntentsTags.CAT_ID, String.valueOf(id));
            startActivity(intent);
        }

       /* switch (view.getId()){


            case R.id.services1 : i = new Intent(this, AllServices.class); startActivity(i);break;
            case R.id.services2 : i = new Intent(this, AllServices.class); startActivity(i);break;
            case R.id.allcategories : i = new Intent(this, AllServices.class); startActivity(i);break;
            case R.id.card1 : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.salon_card : i = new Intent(this, Salon_Women.class); startActivity(i); break;
            case R.id.service_card : i = new Intent(this, Service_List_Activity.class); startActivity(i); break;
            case R.id.men_card : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.makeup_card : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.clean_card : i = new Intent(this, Cleaning_List_Activity.class); startActivity(i); break;
            case R.id.paint_card : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.teach_card : i = new Intent(this, AllServices.class); startActivity(i); break;
            case R.id.pest_card : i = new Intent(this, AllServices.class); startActivity(i); break;

            default:break;
        }*/
    }

    private int getId(String tag) {

        for (CategoryMasterModel categoryMasterModel: serviceList_Main) {
            if (tag.equalsIgnoreCase(categoryMasterModel.getCatName())) {
                return categoryMasterModel.getCatId();
            }
        }
        return 0;
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                //showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
            updateUIWidgets();
        }
    }
    private void displayAddressOutput() {
        if (getIntent().hasExtra(IntentsTags.NEW_ADDRESS)) {

            Log.i("Seting", "NewAddress");
            SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
            AddressModel addressModel = sharedPreferenceHelper.getAddress();
            String address = addressModel.toString();
            mLocationAddressTextView.setText(address);
        }else {
            Log.i("Seting", "OldAddress");
            mLocationAddressTextView.setText(mAddressOutput);
        }

    }
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    private void updateUIWidgets() {
       // if (mAddressRequested) {
         //   mProgressBar.setVisibility(ProgressBar.VISIBLE);
         //   mFetchAddressButton.setEnabled(false);
       // } else {
         //   mProgressBar.setVisibility(ProgressBar.GONE);
          //  mFetchAddressButton.setEnabled(true);
       // }
    }
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                displayAddressOutput();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getAddress();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });

            }

        }

    }
}

