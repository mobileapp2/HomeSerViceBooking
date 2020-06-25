package com.designtech9studio.puntersapp.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.designtech9studio.puntersapp.Model.CartDataModel;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LocalDatabaseHelper {


    Context context;
    SQLiteDatabase database;
    String tableName = "cart6";

    public LocalDatabaseHelper(Context c) {
        context = c;
        database = context.openOrCreateDatabase("Billing.db", Context.MODE_PRIVATE, null);
        Log.i("Database","Created");


        /*childId SubId CatId childName description imageLink qty amount */
        database.execSQL("Create table if NOT EXISTS "+tableName+" (childId int, subCatId int, catId int , childName varchar, description varchar, image varchar, qty int, amount int)");
        Log.i("Databse", "Table created");
    }

    public void insertData(CatSubChildModel data, boolean decrementOperation) {
        try{
            int defaultQty = 1;
            String insertQuery="";
            String description = "";

            Cursor c = database.rawQuery("Select * from "+tableName+" where childId = "+data.getSubChildId(), null);
            //c.moveToFirst();
            if (!c.moveToFirst()) {
                insertQuery = "Insert into " + tableName + " values ("+data.getSubChildId() + ", "+data.getSubCatId() + ", "+ data.getCatId() +", '"+ data.getSubChildName() + "' ,'"+
                        description + "', '"+data.getChildImage()+"', "+defaultQty + ", " + (int)data.getAmount() + ")";
            }else{
                /*entry exist update qty*/
                int qty = c.getInt(c.getColumnIndex("qty"));
                if (!decrementOperation) qty+=1;
                else qty-=1;
                Log.i("InsertQuery_QTY", ""+qty);
                if (qty!=0){
                    insertQuery = "update " + tableName + " set qty="+qty +" where childId="+data.getSubChildId();

                }

            }
            Log.i("InsertQuery", insertQuery);
            database.execSQL(insertQuery);
            Log.i("Database", "Data inserted");

        }catch (Exception e) {
            System.out.println("SqlException: "+e.getMessage());
        }
    }
    public void flushCart() {
        String insertQuery = "delete from " +tableName;
        database.execSQL(insertQuery);
        Log.i("DeleteQuery", insertQuery);
    }

    public void deleteItem(CatSubChildModel data) {
        String insertQuery = "delete from " +tableName + " where childId = " + data.getSubChildId();;
        database.execSQL(insertQuery);
        Log.i("InsertQuery", insertQuery);
    }
    public void deleteItem(int id) {
        String insertQuery = "delete from " +tableName + " where childId = " + id;;
        database.execSQL(insertQuery);
        Log.i("InsertQuery", insertQuery);
    }

    public List<CatSubChildModel> fetchCartData() {

        List<CatSubChildModel> result = new ArrayList<>();
        try{
            String query = "Select * from " + tableName + " order by childId";
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    /*childId SubId CatId childName description imageLink qty amount */
                    CatSubChildModel model = new CatSubChildModel();
                    model.setSubChildId(cursor.getInt(0));
                    model.setSubCatId(cursor.getInt(1));
                    model.setCatId(cursor.getInt(2));
                    model.setSubChildName(cursor.getString(3));
                    model.setChildDescription(cursor.getString(4));
                    model.setChildImage(cursor.getString(5));
                    int qty = cursor.getInt(6);
                    int amount = cursor.getInt(7);
                    model.setAmount((double)amount);
                    model.setQty(qty);
                    model.setTotal(amount);
                    System.out.println("CART " +model.toString());
                    result.add(model);

                } while (cursor.moveToNext());
            }


        }catch (SQLException e) {
            Log.i("Exception", e.getMessage());

        }
        return result;

    }
    public void deleteCartTable() {
        try{
            String query = "Delete from " + tableName;
            database.execSQL(query);
            Log.i("Database", "TableDeleted");
        }catch (SQLException e) {
            System.out.println("SqlException: " +e.getMessage());
        }
    }

    public int getTotalItemsInCart() {
        List<CatSubChildModel> result = fetchCartData();
        int qty = 0;
        for (CatSubChildModel i: result) {
            qty+= i.getQty();
        }
        return qty;
    }

    public int getGrandTotal() {
        List<CatSubChildModel> result = fetchCartData();
        int grandTotal = 0;
        for (CatSubChildModel i: result) {
            grandTotal+= i.getAmount()* i.getQty();
        }
        return grandTotal;
    }






}
