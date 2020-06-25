package com.designtech9studio.puntersapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Adapter.ProductAdapter;
import com.designtech9studio.puntersapp.Converter;
import com.designtech9studio.puntersapp.Model.ProductModel;
import com.designtech9studio.puntersapp.R;
import com.designtech9studio.puntersapp.View.CartActivity;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderActivityWomen extends AppCompatActivity implements ProductAdapter.CallBackUs, ProductAdapter.HomeCallBack {

    public static ArrayList<ProductModel> arrayList = new ArrayList<>();
    public static int cart_count = 0;
    ProductAdapter productAdapter;
    RecyclerView productRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_women);

        addProduct();
        productAdapter = new ProductAdapter(arrayList, this, this);
        productRecyclerView = findViewById(R.id.product_recycler_view);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setAdapter(productAdapter);

    }


    private void addProduct() {
        ProductModel productModel = new ProductModel("Sara Fruit Cleanup", "1049", "20", R.drawable.bag);
        arrayList.add(productModel);
        ProductModel productModel1 = new ProductModel("Tan Clearing Facial", "1499", "10", R.drawable.shoe);
        arrayList.add(productModel1);
        ProductModel productModel2 = new ProductModel("Oil Control Facial", "1299", "10", R.drawable.springrolls);
        arrayList.add(productModel2);

        ProductModel productModel3 = new ProductModel("Brightening Booster Facial", "2499", "20", R.drawable.burger);
        arrayList.add(productModel3);
        ProductModel productModel12 = new ProductModel("Golden Glow Facial", "1999", "10", R.drawable.chicken);
        arrayList.add(productModel12);

    }

    @Override
    public void addCartItemView() {
        //addItemToCartMethod();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(OrderActivityWomen.this, cart_count, R.drawable.ic_shopping_cart_white_24dp));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.cart_action:
                if (cart_count < 1) {
                    Toast.makeText(this, "there is no item in cart", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, CartActivity.class));
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void updateCartCount(Context context) {
        invalidateOptionsMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();
    }
}


