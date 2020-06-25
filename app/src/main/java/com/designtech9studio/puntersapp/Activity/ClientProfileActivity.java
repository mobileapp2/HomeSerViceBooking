package com.designtech9studio.puntersapp.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Model.ProfileModel;
import com.designtech9studio.puntersapp.R;
import com.designtech9studio.puntersapp.View.CartActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ClientProfileActivity extends AppCompatActivity {
    public static final String GOOGLE_ACCOUNT = "google_account";
    private static final int REQUEST_PHONE_CALL = 101;
    private TextView profileName, profileEmail;
    private ImageView profileImage;
    private Button signOut;
    private GoogleSignInClient googleSignInClient;
    private Toolbar toolbar;
    private TextView about;
    TextView edit_bt, save_btn;
    DataBaseHelper dataBaseHelper;
    ProfileModel profileModel;
    EditText name_ed, email_ed, mobile_ed;
    String userId;

    TextView callTv, termsTv, privacyTv;

    TextView aboutTv, shareTv, rateTv;

    TextView name_tv, email_tv, mobile_tv, signOutButton;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name_ed = findViewById(R.id.profileName_ed);
        email_ed = findViewById(R.id.profileEmai_ed);
        mobile_ed = findViewById(R.id.profileMobile_ed);
        edit_bt = findViewById(R.id.profileEditButton);
        save_btn = findViewById(R.id.profileSaveBtn);
        signOutButton = findViewById(R.id.signOutButton);

        callTv = findViewById(R.id.call);
        termsTv = findViewById(R.id.terms);
        privacyTv = findViewById(R.id.privacy);



        name_tv = findViewById(R.id.profilename);
        email_tv = findViewById(R.id.profileEmail);
        mobile_tv = findViewById(R.id.profileMobile);

        TextView about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientProfileActivity.this, ClientAboutActivity.class));
                finish();
            }
        });
        termsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.perfectpunters.com/Terms_Condition.aspx"));
                startActivity(browserIntent);
            }
        });
        privacyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.perfectpunters.com/privacy.aspx"));
                startActivity(browserIntent);
            }
        });
        callTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + "+917203836930"));
                System.out.println("in call us");
                if (ContextCompat.checkSelfPermission(ClientProfileActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(ClientProfileActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                }
                else
                {
                    startActivity(intent);
                }

            }
        });
        /*final TextView help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientProfileActivity.this, ClientAboutActivity.class));
                finish();
            }
        });*/
        final TextView share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               shareMehtod();

            }
        });
        TextView rate = findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="+getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }

            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceHelper helper = new SharedPreferenceHelper(getApplicationContext());
                helper.getUserSignOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(getApplicationContext(),gso);
                googleSignInClient.signOut();
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_android:
                        Intent intent1 = new Intent(ClientProfileActivity.this, ClientHomepageActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_books:
                        Intent intent2 = new Intent(ClientProfileActivity.this, MyAppointments.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_center_focus:

                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(ClientProfileActivity.this, CartActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
        userId = sharedPreferenceHelper.getUserid();
        dataBaseHelper = new DataBaseHelper();

        SyncData syncData = new SyncData();
        syncData.execute();
        /*mobile_tv.setText("Mobile: " + profileModel.getPhone());
        email_tv.setText("Email: "+profileModel.getEmail());
        name_tv.setText("Name: "+ profileModel.getName());
        *//*Pr3int users*/
        mobile_ed.setEnabled(false);
        email_ed.setEnabled(false);
        name_ed.setEnabled(false);
        save_btn.setEnabled(false);

        edit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_ed.setEnabled(true);
                email_ed.setEnabled(true);
                name_ed.setEnabled(true);
                save_btn.setEnabled(true);
                name_ed.requestFocus();

            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mobile_ed.getText().toString();
                String email = email_ed.getText().toString();
                String name = name_ed.getText().toString();
                String firstName ="", lastName = "";
                String temp[] = name.split(" ");
                if (temp!=null) {
                    firstName = temp[0];
                    if (temp.length==2) lastName = temp[1];
                }
                if (!mobile.matches("\\d{10}")){
                    Toast.makeText(ClientProfileActivity.this, "Mobile Number Should Contain 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    Toast.makeText(ClientProfileActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    dataBaseHelper.setUpdatedProfile(userId, firstName, lastName, email, mobile);
                    save_btn.setEnabled(false);
                    mobile_ed.setEnabled(false);
                    email_ed.setEnabled(false);
                    name_ed.setEnabled(false);
                    Toast.makeText(ClientProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }

                Log.i("Back", "To activity");


            }
        });

        //dataBaseHelper.printAllUser();
    }

    private void shareMehtod() {
        ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle("Chooser title")

                .setText("http://play.google.com/store/apps/details?id=" +
                        this.getPackageName())
                .startChooser();

    }

    private class SyncData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){

            progress = new ProgressDialog(ClientProfileActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{

                profileModel = dataBaseHelper.getProfileData(userId);
            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();

            }
            return "";
        }
        protected void onPostExecute(String msg){

            progress.dismiss();
            try {

                mobile_ed.setText(profileModel.getPhone());
                email_ed.setText(profileModel.getEmail());
                name_ed.setText(profileModel.getName());

            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }

    private void setDataOnView() {
        GoogleSignInAccount googleSignInAccount = getIntent().getParcelableExtra(GOOGLE_ACCOUNT);
       // Picasso.with(Context).load(googleSignInAccount.getPhotoUrl()).centerInside().fit().into(profileImage);

        profileName.setText(googleSignInAccount.getDisplayName());
        profileEmail.setText(googleSignInAccount.getEmail());
    }
    public void onBackPressed() {
        super.onBackPressed();
    }
}

