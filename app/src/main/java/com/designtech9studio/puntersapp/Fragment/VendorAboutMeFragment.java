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

public class VendorAboutMeFragment extends Fragment implements View.OnClickListener {
    CardView cardView,cardView1,cardView2;
    String businessName, mobileNumber, website, introduction, vendorId;
    EditText business_txt, mobile_txt, website_txt, into_txt;
    Button submitBtn;
    public VendorAboutMeFragment(String businessName, String mobileNumber, String website, String introduction, String vendorId) {
        // Required empty public constructor
        this.businessName = businessName;
        this.mobileNumber = mobileNumber;
        this.website = website;
        this.introduction = introduction;
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
        View RootView = inflater.inflate(R.layout.fragment_vendor_aboutme, container, false);

        business_txt = RootView.findViewById(R.id.aboutMe_BusinessTxt);
        mobile_txt = RootView.findViewById(R.id.aboutMe_MobileTxt);
        website_txt = RootView.findViewById(R.id.aboutMe_websiteTxt);
        into_txt = RootView.findViewById(R.id.aboutMe_IntroTxt);

        business_txt.setText(businessName);
        mobile_txt.setText(mobileNumber);
        website_txt.setText(website);
        into_txt.setText(introduction);

        submitBtn = RootView.findViewById(R.id.aboutMe_SubmitBtn);
        submitBtn.setOnClickListener(this);

        return RootView;
    }

    @Override
    public void onClick(View view) {

        businessName = business_txt.getText().toString().trim();
        website = website_txt.getText().toString().trim();
        mobileNumber = mobile_txt.getText().toString().trim();
        introduction = into_txt.getText().toString().trim();

        VendorDatabaseHelper vendorDatabaseHelper = new VendorDatabaseHelper();
        vendorDatabaseHelper.updateVendorAboutMeRecord(businessName, mobileNumber, website, introduction, vendorId);
        Toast.makeText(getContext(), "Record Updated!", Toast.LENGTH_SHORT).show();

        System.out.println("RecordUpdate");


    }
}

