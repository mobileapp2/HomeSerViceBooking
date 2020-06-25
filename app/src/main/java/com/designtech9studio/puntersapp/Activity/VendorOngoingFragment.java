package com.designtech9studio.puntersapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.designtech9studio.puntersapp.Adapter.BookingAdapter;
import com.designtech9studio.puntersapp.Adapter.VendorBookingAdapter;
import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.R;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VendorOngoingFragment extends Fragment {

    LinearLayout msg_Layout;
    List<BookingModel> data;
    RecyclerView recyclerView;
    Context context;
    TextView msgTv;
    String msg;
    boolean hidefeatures;
    public VendorOngoingFragment(List<BookingModel> data, Context context, String msg, boolean hidefeatures) {
        // Required empty public constructor
        this.data = data;
        this.context = context;
        this.msg = msg;
        this.hidefeatures = hidefeatures;

        Log.i("OngoingInside", data.size()+"");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView =  inflater.inflate(R.layout.vendor_ongoing_fragment, container, false);
        msg_Layout = RootView.findViewById(R.id.vendorBookingNoBookingLinerLayout);
        msgTv = RootView.findViewById(R.id.homeservice_msg);

        msgTv.setText(msg);
        recyclerView = RootView.findViewById(R.id.vendorBookingRecycler);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        if (data.size() > 0) {
            msg_Layout.setVisibility(View.GONE);
        }

        VendorBookingAdapter bookingAdapter = new VendorBookingAdapter(context, data, "", hidefeatures);
        recyclerView.setAdapter(bookingAdapter);


        return RootView;
    }

}


