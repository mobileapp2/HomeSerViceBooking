package com.designtech9studio.puntersapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.designtech9studio.puntersapp.Adapter.CheckOutAdapter;
import com.designtech9studio.puntersapp.Adapter.ChildCatAdapter;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.LocalDatabaseHelper;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.R;

import java.util.List;

public class CheckOutActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView checkOutRecyclerView;
    List<CatSubChildModel> cartDetails;

    TextView count_box_tv, totalAmount_tv, checkOutConfirm_tv;
    LocalDatabaseHelper localDatabaseHelper;

    CheckOutAdapter checkOutAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        setTitle("Check Out");

        localDatabaseHelper = new LocalDatabaseHelper(this);
        checkOutRecyclerView = findViewById(R.id.checkOutRecyclerView);
        totalAmount_tv = findViewById(R.id.checkOutTotalAmount);
        count_box_tv = findViewById(R.id.checkOutcount_box);
        toolbar = findViewById(R.id.checkOutToolbar);
        checkOutConfirm_tv = findViewById(R.id.checkOutConfirm);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        checkOutRecyclerView.setLayoutManager(llm);


        cartDetails = localDatabaseHelper.fetchCartData();
        checkOutAdapter = new CheckOutAdapter(cartDetails, this);
        checkOutRecyclerView.setAdapter(checkOutAdapter);
        setCountBoxAndTotal();

        checkOutConfirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(count_box_tv.getText().toString());
                if (qty==0) {
                    Toast.makeText(CheckOutActivity.this, "Please! Add Some Item In Cart", Toast.LENGTH_LONG).show();
                }else{
                    /*Intent to payment activity*/
                    SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
                    if (sharedPreferenceHelper.isUserLogedIn()) {
                        /*User loged in*/
                        Log.i("User", "LogedIn");
                        Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(CheckOutActivity.this, "Please! Login First", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

                Log.i("BACK", "Detected");
            }
        });

    }

    public void setCountBoxAndTotal() {
        int count = localDatabaseHelper.getTotalItemsInCart();
        int amount = localDatabaseHelper.getGrandTotal();
        count_box_tv.setText(String.valueOf(count));
        totalAmount_tv.setText(Html.fromHtml("&#8377")+" " + String.valueOf(amount));
        if (checkOutAdapter!=null)
            checkOutAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("CheckOutBack", "BackAction");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
