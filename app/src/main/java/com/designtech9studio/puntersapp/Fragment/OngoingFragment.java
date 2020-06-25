package com.designtech9studio.puntersapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.designtech9studio.puntersapp.Activity.ClientHomepageActivity;
import com.designtech9studio.puntersapp.Adapter.BookingAdapter;
import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.R;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OngoingFragment extends Fragment implements View.OnClickListener {
Button book_service;
LinearLayout msg_Layout, button_layout;
List<BookingModel> data;
RecyclerView recyclerView;
Context context;

public OngoingFragment(List<BookingModel> data, Context context) {
        // Required empty public constructor
        this.data = data;
        this.context = context;
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
        View RootView = inflater.inflate(R.layout.ongoing_fragment, container, false);
        book_service = (Button) RootView.findViewById(R.id.book_service);

        msg_Layout = RootView.findViewById(R.id.linaerLaout_Message);
        button_layout = RootView.findViewById(R.id.input_group);

        recyclerView = RootView.findViewById(R.id.bookingRecyler);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        if (data.size() > 0) {
            msg_Layout.setVisibility(View.GONE);
            //button_layout.setVisibility(View.GONE);
        }

        BookingAdapter bookingAdapter = new BookingAdapter(context, data, "");
        recyclerView.setAdapter(bookingAdapter);
        book_service.setOnClickListener(this);

        return RootView;
    }

    @Override
    public void onClick(View view) {
        Intent i ;
        switch (view.getId())
        {
            case R.id.book_service :
                i=new Intent(view.getContext(), ClientHomepageActivity.class);
                startActivity(i);
                break;
            default: break;
        }
    }
}

