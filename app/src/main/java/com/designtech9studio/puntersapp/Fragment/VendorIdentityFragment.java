package com.designtech9studio.puntersapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.R;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class VendorIdentityFragment extends Fragment implements View.OnClickListener {

    String gstNumber, panCardNumber, aadharNumber, vendorId;
    EditText gst_txt, pan_txt, aadhar_txt;
    Button identity_submitBtn;
    public VendorIdentityFragment(String gstNumber, String panCardNumber, String aadharNumber, String vendorId) {
        // Required empty public constructor
        this.gstNumber = gstNumber;
        this.panCardNumber = panCardNumber;
        this.aadharNumber = aadharNumber;
        this.vendorId = vendorId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vendor_identity, container, false);
        gst_txt = root.findViewById(R.id.identity_GSTtxt);
        pan_txt = root.findViewById(R.id.identity_PanTxt);
        aadhar_txt = root.findViewById(R.id.identity_AadharTxt);
        identity_submitBtn = root.findViewById(R.id.identity_submitBtn);

        gst_txt.setText(gstNumber);
        pan_txt.setText(panCardNumber);
        aadhar_txt.setText(aadharNumber);
        identity_submitBtn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {

        gstNumber = gst_txt.getText().toString().trim();
        panCardNumber = pan_txt.getText().toString().trim();
        aadharNumber = aadhar_txt.getText().toString().trim();

        VendorDatabaseHelper vendorDatabaseHelper = new VendorDatabaseHelper();
        vendorDatabaseHelper.updateVendorIdentityRecord(gstNumber, panCardNumber, aadharNumber, vendorId);
        Toast.makeText(getContext(), "Record Updated!", Toast.LENGTH_SHORT).show();
        System.out.println("Record Updated");


    }
}


