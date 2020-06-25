package com.designtech9studio.puntersapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.designtech9studio.puntersapp.Activity.ClientHomepageActivity;
import com.designtech9studio.puntersapp.Adapter.BookingAdapter;
import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.R;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryFragment extends Fragment implements View.OnClickListener {
    Button book_service2;
    LinearLayout msg_Layout2, button_layout2;
    List<BookingModel> data2;
    RecyclerView recyclerView2;
    Context context;
    public HistoryFragment(List<BookingModel> data, Context context) {
        // Required empty public constructor
        this.data2 = data;
        this.context = context;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.history_fragment, container, false);
        book_service2 = (Button) RootView.findViewById(R.id.book_service2);

        msg_Layout2 = RootView.findViewById(R.id.linaerLaout_Message);
        button_layout2 = RootView.findViewById(R.id.input_group);

        recyclerView2 = RootView.findViewById(R.id.bookingRecyler);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(llm);

        if (data2.size() > 0) {
            msg_Layout2.setVisibility(View.GONE);
            button_layout2.setVisibility(View.GONE);
        }

        BookingAdapter bookingAdapter = new BookingAdapter(context, data2, "");
        recyclerView2.setAdapter(bookingAdapter);
        book_service2.setOnClickListener(this);

        return RootView;
    }

    @Override
    public void onClick(View view) {
        Intent i ;
        switch (view.getId())
        {
            case R.id.book_service2 :
                i=new Intent(view.getContext(), ClientHomepageActivity.class);
                startActivity(i);
                break;
            default: break;
        }
    }
}

