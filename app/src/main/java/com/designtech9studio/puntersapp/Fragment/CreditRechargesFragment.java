package com.designtech9studio.puntersapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.designtech9studio.puntersapp.Adapter.RechargeAdapter;
import com.designtech9studio.puntersapp.Helpers.Constant;
import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.Model.RechargeModel;
import com.designtech9studio.puntersapp.R;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CreditRechargesFragment extends Fragment implements View.OnClickListener {
    CardView cardView,cardView1,cardView2;
    LinearLayout msg_Layout;

    List<RechargeModel> data;
    RecyclerView recyclerView;
    TextView textView;
    Context context;
    public CreditRechargesFragment(ArrayList<RechargeModel> rechargeModels, Context context) {
        // Required empty public constructor
        data = rechargeModels;
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
        View RootView = inflater.inflate(R.layout.credit_recharges_fragment, container, false);
        recyclerView = RootView.findViewById(R.id.transactionRecyler);

        textView = RootView.findViewById(R.id.noTransactionTxt);
        if (data.size() > 0) {
            textView.setVisibility(View.GONE);
        }
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        RechargeAdapter rechargeAdapter = new RechargeAdapter(context, data, "msg");
        recyclerView.setAdapter(rechargeAdapter);


        return RootView;
    }

    @Override
    public void onClick(View view) {

    }
}



