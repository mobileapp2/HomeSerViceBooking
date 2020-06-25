package com.designtech9studio.puntersapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.ConnectionClass;
import com.designtech9studio.puntersapp.R;
import com.squareup.picasso.Picasso;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ListActivity extends AppCompatActivity {

    private ArrayList<ClassListItems> itemsArrayList;
    private MyAppAdapter myAppAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false;
    private ConnectionClass connectionClass;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_data);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        connectionClass = new ConnectionClass();
        itemsArrayList = new ArrayList<ClassListItems>();

        SyncData orderData = new SyncData();
        orderData.execute("");
    }

    private class SyncData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = ProgressDialog.show(ListActivity.this, "Synchronising",
                    "RecyclerView Loading! Please Wait...", true);
        }

        protected String doInBackground(String... strings){
            try{
                Connection conn = connectionClass.CONN();
                if (conn == null){
                    success = false;
                }
                else{
                    String query = "Select CatName,ImageUpload From CategoryMaster Where Status=1";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null){
                        while (rs.next()){
                            try {
                                String strImage = rs.getString("ImageUpload").replace("~","");
                                String strImg = "https://perfectpunters.com" + strImage;
                              //  itemsArrayList.add(new ClassListItems(rs.getString("CatName"),strImg,"1"));
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
            Toast.makeText(ListActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false)
            {

            }
            else {
                try {
                    myAppAdapter = new MyAppAdapter(itemsArrayList, ListActivity.this);
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

            public TextView textName;
            public ImageView imageView;
            public View layout;

            public ViewHolder(View v)
            {
                super(v);
                layout = v;
                textName = (TextView) v.findViewById(R.id.textName);
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
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        public void onBindViewHolder(ViewHolder holder, final int position){

            try
                {
                    final ClassListItems classListItems = values.get(position);
                    holder.textName.setText(classListItems.getName());
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

            //Picasso.get().load(classListItems.getImg()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return values.size();
        }
    }

}
