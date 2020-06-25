package com.designtech9studio.puntersapp;

import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BusinessImplement {
    ConnectionClass ConnectionClass = new ConnectionClass();

    public  String Save_Record_Vendor(int Mode, int vendorID, String VendorName,String FirstName,
                                      String LastName, int countryID, int stateId, int cityId,
                                      String Address, String MobileNo, String Lat, String Lot,
                                      String CompanyName, String GstNo, String AdharcardNo,
                                      int CreatedBy, int Status, String PancardNo

    )
    {
        String Result = "";
        Connection con = ConnectionClass.CONN();

        String insertRegisterPage = "{call AddVenderMaster(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            callableStatement = con.prepareCall(insertRegisterPage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            callableStatement.setInt("@Mode",Mode);
            callableStatement.setInt("@VendorId",vendorID);
            callableStatement.setString("@VendorName",VendorName);
            callableStatement.setString("@FirstName",FirstName);
            callableStatement.setString("@LastName",LastName);
            callableStatement.setInt("@CountryId",countryID);

            callableStatement.setInt("@StateId",stateId);
            callableStatement.setInt("@CityID",cityId);
            callableStatement.setString("@Address",Address);
            callableStatement.setString("@MObileNO",MobileNo);
            callableStatement.setString("@Lat",Lat);
            callableStatement.setString("@Lot",Lot);
            callableStatement.setString("@CompanyName",CompanyName);
            callableStatement.setString("@GstNo",GstNo);
            callableStatement.setString("@AdharCardNo",AdharcardNo);
            callableStatement.setInt("@CreatedBy",CreatedBy);
            callableStatement.setInt("@Status",Status);
            callableStatement.setString("@PanCardNo",PancardNo);
            callableStatement.registerOutParameter("@RESULT", Types.VARCHAR);
            callableStatement.executeUpdate();

            Result = callableStatement.getString("@RESULT");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Result.toString();

    }

    public String SaveRecordVendorDetails(int Mode, String FirstName, String LastName, String VendorName,String CompanyName,
                                          String GSTNo,String AdharCardNo, String PanCardNo, String MobileNo, String Address)
    {
        String Result = "";
        Connection con = ConnectionClass.CONN();

        String insertVendorDetail = "{call AndroidAddVendorMaster(?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            callableStatement = con.prepareCall(insertVendorDetail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            callableStatement.setInt("@Mode",Mode);
            callableStatement.setString("@FirstName",FirstName);
            callableStatement.setString("@LastName",LastName);
            callableStatement.setString("@VendorName",VendorName);
            callableStatement.setString("@CompanyName",CompanyName);
            callableStatement.setString("@GSTNo",GSTNo);

            callableStatement.setString("@AdharCardNO",AdharCardNo);
            callableStatement.setString("@PanCardNo",PanCardNo);
            callableStatement.setString("@Address",Address);
            callableStatement.setString("@MobileNo",MobileNo);

            callableStatement.registerOutParameter("@RESULT", Types.VARCHAR);
            callableStatement.executeUpdate();

            Result = callableStatement.getString("@RESULT");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Result.toString();


    }


    public String SaveRecordRegister(int Mode, String txtEmailId, String strPassword, String CityId, String strUserName )
    {
        String Result = "";
        Connection con = ConnectionClass.CONN();

        String insertRegisterPage = "{call Add_Vendor_Register(?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            callableStatement = con.prepareCall(insertRegisterPage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            callableStatement.setInt("@Mode",Mode);
            callableStatement.setString("@UserName",strUserName);
            callableStatement.setString("@Password",strPassword);
            callableStatement.setString("@EmailID",txtEmailId);

            callableStatement.setString("@CityName",CityId);
            callableStatement.registerOutParameter("@RESULT", Types.VARCHAR);
            callableStatement.executeUpdate();

            Result = callableStatement.getString("@RESULT");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Result.toString();
    }

    /*
    private void BindProductList(){
        try {
            Connection con = ConnectionClass.CONN();
            if (con == null) {
                Toast.makeText(getApplicationContext(), "Error in connection with SQL server", Toast.LENGTH_LONG).show();
            }
            else {

                String Query = "EXEC BindProductList(?,?,?)";
                CallableStatement stmt = con.prepareCall(Query);
                int catid =1;

                stmt.setInt("@catId",catid);
                stmt.setInt("@SubCatID",0);
                stmt.setInt("@ChildsubCatId",0);

                ResultSet Result = stmt.executeQuery();


                while (Result.next()) {

                        CLas
                    txtUidNo.setText(Result.getString("UID_NO"));
                    txtSocCode.setText(Result.getString("SOC_CODE"));
                    txtGroCode.setText(Result.getString("GRO_CODE"));
                    txtVillCode.setText(Result.getString("VILLCODE"));
                    txtFarName.setText(Result.getString("FATHER_NAME"));


                    txtGroName.setText(Result.getString("G_NAME"));
                    txtVillName.setText(Result.getString("V_NAME"));
                    txtUidMobileNo.setText(Result.getString("MOBILENO"));
                    txtAccNo.setText(Result.getString("ACCOUNT_NO"));
                    txtIfscCode.setText(Result.getString("IFSC_CODE"));

                    txtBranName.setText(Result.getString("BRANCHNAME"));

                    txtSocName.setText(Result.getString("SOCIETY_NAME"));

                }


            }
        }

        catch (Exception ex)
        {

            Toast.makeText(getApplication(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    */
}
