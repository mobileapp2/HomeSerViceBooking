package com.designtech9studio.puntersapp.Fragment;

import android.content.Context;
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

public class VendorBankDetailFragment extends Fragment implements View.OnClickListener {
    CardView cardView,cardView1,cardView2;

    String bankName, accountNum, ifsc, vendorId;
    Context context;
    EditText bank_NameTxt, bank_accountNumTxt, bank_IfscTxt;
    Button bank_Submit;
    public VendorBankDetailFragment(String bankName, String accountNum, String ifsc, String vendorId, Context context) {
        // Required empty public constructor
        this.bankName =bankName;
        this.accountNum =accountNum;
        this.ifsc =ifsc;
        this.vendorId =vendorId;
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
        View RootView = inflater.inflate(R.layout.fragment_vendor_bank_detail, container, false);

        bank_NameTxt = RootView.findViewById(R.id.bank_NameTxt);
        bank_accountNumTxt = RootView.findViewById(R.id.bank_accountNumTxt);
        bank_IfscTxt = RootView.findViewById(R.id.bank_IfscTxt);
        bank_Submit = RootView.findViewById(R.id.bank_Submit);

        bank_NameTxt.setText(bankName);
        bank_accountNumTxt.setText(accountNum);
        bank_IfscTxt.setText(ifsc);

        bank_Submit.setOnClickListener(this);

        return RootView;
    }

    @Override
    public void onClick(View view) {

        bankName = bank_NameTxt.getText().toString().trim();
        accountNum = bank_accountNumTxt.getText().toString();
        ifsc = bank_IfscTxt.getText().toString();

        if (bankName.equals("") || accountNum.equals("") || ifsc.equals("")){
            Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show();
            return;
        }

        VendorDatabaseHelper vendorDatabaseHelper = new VendorDatabaseHelper();
        vendorDatabaseHelper.updateVendorBankDetails(bankName, accountNum, ifsc, vendorId);
        Toast.makeText(getContext(), "Record Updated!", Toast.LENGTH_SHORT).show();

    }
}

