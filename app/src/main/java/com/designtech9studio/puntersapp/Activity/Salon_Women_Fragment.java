package com.designtech9studio.puntersapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.designtech9studio.puntersapp.R;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class Salon_Women_Fragment extends Fragment implements View.OnClickListener {
    CardView cardView,cardView1,cardView2;
    public Salon_Women_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.salon_women_fragment, container, false);
        cardView = (CardView) RootView.findViewById(R.id.facial);
        cardView.setOnClickListener(this);

        return RootView;
    }

    @Override
    public void onClick(View view) {

    }
}



