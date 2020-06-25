package com.designtech9studio.puntersapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.ConnectionClass;
import com.designtech9studio.puntersapp.R;
import com.squareup.picasso.Picasso;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Service_List_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private boolean success = false;
    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "Water Supply", "Electrician", "Plumber", "Washing Machine",
            "Carpenter", "Refrigerator", "Welding", "R.O And Water Purifier","A.C Repair","Geyser","Kitchen Appliance",
    };


    int[] listviewImage = new int[]{
            R.drawable.water_tank1, R.drawable.electrician_home1, R.drawable.plumber_home1, R.drawable.washing_machine1,
            R.drawable.carpenter_home1, R.drawable.refrigerator_home1, R.drawable.welding_home1, R.drawable.water_purifier1,
            R.drawable.ac_repair1, R.drawable.geyser_repair1,R.drawable.kitchen_appliance1,
    };

    String[] listviewShortDescription = new String[]{
            "Description", "Description", "Description", "Description",
            "Description", "Description", "Description", "Description", "Description", "Description", "Description",
    };

    String strCatId,strSUbCatId,strSubChildCat;
    String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
    private ArrayList<ClassListItems> itemsArrayList;
    private MyAppAdapter myAppAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ConnectionClass connectionClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__list_);

        Intent _intent = getIntent();
        strCatId = _intent.getStringExtra("CatID");
        strSUbCatId = _intent.getStringExtra("SubCatID");
        strSubChildCat = _intent.getStringExtra("ChildSubCatID");
        connectionClass = new ConnectionClass();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        connectionClass = new ConnectionClass();
        itemsArrayList = new ArrayList<ClassListItems>();

        SyncData orderData = new SyncData();
        orderData.execute("");
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        try{
            Connection conn = connectionClass.CONN();
            if (conn == null){
                success = false;
            }
            else{
                String Query = "EXEC BindProductListAndroid(?,?,?)";
                CallableStatement stmt = conn.prepareCall(Query);
                int catid =0;

                stmt.setString("@catId",strCatId.toString());
                stmt.setString("@SubCatID",strSUbCatId.toString());
                stmt.setString("@ChildsubCatId",strSubChildCat.toString());
                ResultSet rs = stmt.executeQuery();
                if (rs != null){
                    while (rs.next()){
                        try {
                            String strImage = rs.getString("ImageUpload").replace("~","");
                            String strImg = "https://perfectpunters.com" + strImage;

                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("listview_title", rs.getString("ChildSUbName"));
                                hm.put("listview_discription", rs.getString("Description"));
                                //hm.put("listview_image", Integer.toString(listviewImage[i]));
                                aList.add(hm);


                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    msg = "Found";
                    success = true;
                }else{
                    msg = "No Data found!";
                    success = false;
                }
            }
        }catch (Exception e) {

        }



        setSupportActionBar(toolbar);




        String[] from = {"listview_title","listview_discription" };//, "listview_discription"};
        int[] to = {R.id.listview_item_title,R.id.listview_item_short_description};//, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);
    }
    private class SyncData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = ProgressDialog.show(Service_List_Activity.this, "Synchronising",
                    "RecyclerView Loading! Please Wait...", true);
        }

        protected String doInBackground(String... strings){
            try{
                Connection conn = connectionClass.CONN();
                if (conn == null){
                    success = false;
                }
                else{
                    String Query = "EXEC BindProductListAndroid(?,?,?)";
                    CallableStatement stmt = conn.prepareCall(Query);
                    int catid =0;

                    stmt.setInt("@catId",catid);
                    stmt.setInt("@SubCatID",0);
                    stmt.setInt("@ChildsubCatId",0);
                    ResultSet rs = stmt.executeQuery();
                    if (rs != null){
                        while (rs.next()){
                            try {
                                String strImage = rs.getString("ImageUpload").replace("~","");
                                String strImg = "https://perfectpunters.com" + strImage;
                                itemsArrayList.add(new ClassListItems(rs.getString("SubCatName"),strImg,rs.getString("CatID"),rs.getString("SUbCatID"),rs.getString("ChildSUbID")));
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found";
                        success = true;
                    }else{
                        msg = "No Data found!";
                        success = false;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }
        protected void onPostExecute(String msg){

            progress.dismiss();
            Toast.makeText(Service_List_Activity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false)
            {

            }
            else {
                try {
                    myAppAdapter = new MyAppAdapter(itemsArrayList, Service_List_Activity.this);
                    recyclerView.setAdapter(myAppAdapter);
                }catch (Exception ex){

                }
            }
        }
    }

    public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.ViewHolder>{
        private List<ClassListItems> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView textName,txtCatID,txtSubCatID,txtChildSubCatId;
            public ImageView imageView;
            public View layout;

            public ViewHolder(View v)
            {
                super(v);
                layout = v;
                textName = (TextView) v.findViewById(R.id.textName);
                txtCatID = (TextView) v.findViewById(R.id.txtCatID);
                txtSubCatID = (TextView) v.findViewById(R.id.txtSubCatID);
                txtChildSubCatId = (TextView) v.findViewById(R.id.childSubCatID);

                imageView = (ImageView) v.findViewById(R.id.imageView);
            }
        }
        public MyAppAdapter(List<ClassListItems> myDataset,Context context){
            values = myDataset;
            this.context = context;
        }

        public MyAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.list_content,parent,false);
            MyAppAdapter.ViewHolder vh = new MyAppAdapter.ViewHolder(v);
            return vh;
        }



        public void onBindViewHolder(MyAppAdapter.ViewHolder holder, final int position){

            try
            {
                final ClassListItems classListItems = values.get(position);
                holder.textName.setText(classListItems.getName());

                holder.txtCatID.setText(classListItems.getCatID());
                holder.txtSubCatID.setText(classListItems.getSubCatId());
                holder.txtChildSubCatId.setText(classListItems.getChildSubCatId());

                //Picasso.with(this).load("htt://cdn.journaldev.com/wp-content/uploads/2016/11/android-image-picker-project-structure.png").into(imageView)
                String _strGetImage = classListItems.getImg().toString();
                //~/UploadImage/CategoryImage/26/Untitled_design_(25).png

                Picasso.get()
                        .load(_strGetImage)
                        .placeholder(R.mipmap.ic_launcher)
                        .resize(220, 220)

                        .into(holder.imageView);
                    /*
                    Picasso
                            .get()
                            .load(classListItems.getImg())
                            .resize(50, 50)
                            .noFade()
                            .into(holder.imageView);
                    */
            }
            catch (Exception ex)
            {

            }

            try {


                holder.textName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String _strCatID = itemsArrayList.get(position).getCatID();
                        String _strsubCatID = itemsArrayList.get(position).getSubCatId();
                        String _strChildSubCatID = itemsArrayList.get(position).getChildSubCatId();
                        Intent intent = new Intent(context, Service_List_Activity.class);
                        intent.putExtra("CatID", _strCatID);
                        intent.putExtra("SubCatID", _strsubCatID);
                        intent.putExtra("ChildSubCatID", _strChildSubCatID);
                        context.startActivity(intent);
                    }
                });
            }
            catch (Exception ex)
            {

            }
            //Picasso.get().load(classListItems.getImg()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return values.size();
        }
    }
}