package com.designtech9studio.puntersapp.Activity;

import android.app.Activity;
import android.app.AuthenticationRequiredException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Helpers.Constant;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Model.UserModel;
import com.designtech9studio.puntersapp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.PlatformServiceClient;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Object Button;
    android.widget.Button signIn;
    TextView signUp;
    private SignInButton googleSignInButton;
    private GoogleSignInClient googleSignInClient;
    //private LoginButton facebookLogIn;
    CallbackManager facebookCallbackManager;

   /// private FirebaseAuth mAuth;
    EditText mobileNumberEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mobileNumberEditText = findViewById(R.id.mobileNumberEditText);
        signIn = findViewById(R.id.sign_in);
       // FirebaseUser currentUser = mAuth.getCurrentUser();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.designtech9studio.puntersapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
        }
        catch (NoSuchAlgorithmException e) {
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = mobileNumberEditText.getText().toString();
                if (mobileNumber.matches("\\d{10}")) {
                    /*Generate Otp*/
                    String otp = "";
                    Random random = new Random();
                    for (int i=0; i<4;i++) {
                        otp+=String.valueOf(random.nextInt(9));
                    }
                    /*SMS OTP*/

                    Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                    intent.putExtra(IntentsTags.OTP, otp);
                    intent.putExtra(IntentsTags.MOBILE, mobileNumber);

                    startActivity(intent);

                }else {
                    Toast.makeText(LoginActivity.this, "Mobile Number should contain 10 digits", Toast.LENGTH_LONG).show();
                }
            }
        });
        googleSignInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

        android.widget.Button Skip_now = findViewById(R.id.push_button);
        Skip_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ClientHomepageActivity.class));
                finish();
            }
        });
        TextView signUp_text = findViewById(R.id.signin_vendor);
        signUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Vendor_SignIn.class));
                finish();
            }
        });

      //  FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
    //    facebookCallbackManager = CallbackManager.Factory.create();
///0        facebookLogIn.setReadPermissions(Arrays.asList("email"));
        /*facebookLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickFBLogin(v);
            }
        });*/

       /* if (mAuth!=null) {
            if (mAuth.getCurrentUser()!=null)
            registerUser(mAuth.getCurrentUser());
        }*/

        /*facebookLogIn.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken.getCurrentAccessToken().getPermissions();
                Log.i("AccessToken", loginResult.getAccessToken().toString());
                handelAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });*/



        //logger.logPurchase(BigDecimal.valueOf(4.32), Currency.getInstance("USD"));


        /*DataBaseHelper dataBaseHelper= new DataBaseHelper();
        dataBaseHelper.updateTransactionAppTable();*/
        //check login
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());

        if (sharedPreferenceHelper.isUserLogedIn()) {
            startActivity(new Intent(getApplicationContext(), ClientHomepageActivity.class));
            finish();
        }else if (sharedPreferenceHelper.isVendorLogedIn()) {
            startActivity(new Intent(getApplicationContext(), VendorNewLeadActivity.class));
            finish();
        }


    }
    public void buttonClickFBLogin(View view) {
        Log.i("facebook", "clicked");

        LoginManager.getInstance().registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken.getCurrentAccessToken().getPermissions();
                handelAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "User Canceled ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void handelAccessToken(AccessToken accessToken) {

        //AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
       /* mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.i("facebook", "register");
                    registerUser(user);
                    Log.i("facebook", "success");

                }else {
                    Toast.makeText(LoginActivity.this, "Could Not register", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
    public void registerUser(FirebaseUser user) {
        Log.i("facebook", user.getEmail());
        Log.i("facebook", user.getDisplayName());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }
    }
    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        String email = googleSignInAccount.getEmail();
        String userName = googleSignInAccount.getDisplayName();
        Log.i("Gmail_Email", email);
        Log.i("Gamil_Username", userName);

        String temp[] = userName.split(" ");
        String fn = "", ln ="";

        if (temp != null) {
            if (temp.length>=1)fn = temp[0];
            if (temp.length>=2)ln = temp[1];
        }
        DataBaseHelper dataBaseHelper = new DataBaseHelper();

        UserModel userModel = new UserModel();
        userModel.setEmailId(email);
        userModel.setFirstName(fn);
        userModel.setLastName(ln);
        userModel.setRoleId(Constant.CUSTOMER_ROLE);

        int user_id = dataBaseHelper.getIdViaMail(email);
        if (user_id == 0) {
            /*Insert into table*/
            user_id = dataBaseHelper.registerUserViaMail(userModel);
        }

        SharedPreferenceHelper helper = new SharedPreferenceHelper(getApplicationContext());
        helper.getUserLogIn(String.valueOf(user_id), String.valueOf(Constant.CUSTOMER_ROLE));
        Log.i("SharedPref", "Gamil Login " +user_id);
        /*Intent intent = new Intent(getApplicationContext(), ClientHomepageActivity.class);
        startActivity(intent);
        finish();*/

        Intent intent = new Intent(this, ClientHomepageActivity.class);

        intent.putExtra(ClientProfileActivity.GOOGLE_ACCOUNT, googleSignInAccount);

        startActivity(intent);
        finish();
    }
    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d(TAG, "Not logged in");
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

