package com.designtech9studio.puntersapp.Helpers;

import android.content.Intent;
import android.util.Log;

import com.designtech9studio.puntersapp.ConnectionClass;
import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.Model.ProfileModel;
import com.designtech9studio.puntersapp.Model.RechargeModel;
import com.designtech9studio.puntersapp.Model.VendorProfileModel;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class VendorDatabaseHelper {

    ConnectionClass connectionClass;
    Connection connection;

    public VendorDatabaseHelper() {
        connectionClass = new ConnectionClass();
        connection = connectionClass.CONN();
    }

/*Vender_Master	VendorID	1	NULL	NO	bigint
Vender_Master	VendorName	2	NULL	YES	varchar
Vender_Master	FirstName	3	NULL	YES	varchar
Vender_Master	LastName	4	NULL	YES	varchar
Vender_Master	CountryId	5	NULL	YES	int
Vender_Master	StateId	6	NULL	YES	int
Vender_Master	CityId	7	NULL	YES	int
Vender_Master	Address	8	NULL	YES	varchar
Vender_Master	MobileNo	9	NULL	YES	varchar
Vender_Master	Lat	10	NULL	YES	varchar
Vender_Master	Lot	11	NULL	YES	varchar
Vender_Master	CompanyName	12	NULL	YES	varchar
Vender_Master	GSTNo	13	NULL	YES	varchar
Vender_Master	AdharCardNo	14	NULL	YES	varchar
Vender_Master	Status	15	NULL	YES	int
Vender_Master	CreatedBy	16	NULL	YES	int
Vender_Master	CreatedDate	17	NULL	YES	datetime
Vender_Master	UpdatedBy	18	NULL	YES	int
Vender_Master	UpdatedDate	19	NULL	YES	datetime
Vender_Master	ImageUpload	20	NULL	YES	varchar
Vender_Master	Upload_AdharCard	21	NULL	YES	varchar
Vender_Master	Upload_PanCard	22	NULL	YES	varchar
Vender_Master	PanCardNo	23	NULL	YES	varchar

username varchar(200), password varchar(200), email varchar(200)
*/
    /*businessName, websiteLink, introduction, bankName ,bankAccountNumber, ifscCode, phone, gst, pan, aadhar;*/

    /*Wiil return revendor Id*/
    //username, email, password, phone, address, city, state, country, lat, log

    public ProfileModel getVendorProfile(String vendorId) {
        ProfileModel profileModel = new ProfileModel();
        try {
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "select * from  Vender_Master where vendorId = " + vendorId;
            System.out.println(query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                profileModel.setPhone(rs.getString("MobileNo"));
                profileModel.setEmail(rs.getString("email"));
                profileModel.setName(rs.getString("username"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profileModel;
    }


    public void updateVendorBankDetails(String bankname, String accNum, String ifsc,String vendorId) {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();


            String query = "update Vender_Master set bankName='"+bankname+"', bankAccountNumber = '" + accNum + "', ifscCode='" + ifsc + "' where vendorId = " + vendorId;
            System.out.println(query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            Log.i("Query", "Vendor Bank");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateVendorIdentityRecord(String gst, String pan, String aadhar,String vendorId) {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();


            String query = "update Vender_Master set GSTNo='"+gst+"', AdharCardNo = '" + aadhar + "', PanCardNo='" + pan + "' where vendorId = " + vendorId;
            System.out.println(query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            Log.i("Query", "Vendor GST");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateOnlineStatus(int isOnline,String vendorId) {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();


            String query = "update Vender_Master set is_available="+isOnline+" where vendorId = " + vendorId;
            System.out.println(query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            Log.i("Query", "Vendor Online");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isOnline(String vendorId) {
        int available = 0;
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();


            String query = "Select is_available from Vender_Master where vendorId = " + vendorId;
            System.out.println(query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            available = resultSet.getInt("is_available");
            Log.i("Query", "Vendor Online");


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return available == 1;
    }

    public void updateVendorProfileName(String first, String last, String vendorName, String company,String vendorId) {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();

            /*
            * Vender_Master	VendorName	2	NULL	YES	varchar
Vender_Master	FirstName	3	NULL	YES	varchar
Vender_Master	LastName	4	NULL	YES	varchar
Vender_Master	CountryId	5	NULL	YES	int
Vender_Master	StateId	6	NULL	YES	int
Vender_Master	CityId	7	NULL	YES	int
Vender_Master	Address	8	NULL	YES	varchar
Vender_Master	MobileNo	9	NULL	YES	varchar
Vender_Master	Lat	10	NULL	YES	varchar
Vender_Master	Lot	11	NULL	YES	varchar
Vender_Master	CompanyName	12	NULL	YES	varchar
*/

            String query = "update Vender_Master set FirstName='"+first+"', LastName = '" + last + "', VendorName='" + vendorName + "', CompanyName ='"+company+"' where vendorId = " + vendorId;
            System.out.println(query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            Log.i("Query", "Vendor ProfileName");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVendorAboutMeRecord(String business, String mobile, String website, String intro,String vendorId) {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();


            String query = "update Vender_Master set businessName='"+business+"', MobileNo = '" + mobile + "', websiteLink='" + website + "', introduction='"+ intro +"' where vendorId = " + vendorId;
            System.out.println(query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            Log.i("Query", "Vendor AboutMe");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int registerVendor(String username, String email, String password, String phone, String city, String sate, String country, String lat, String lot) {

        int vendorId = 0;
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();

            String countryId = getCountryId(country);
            String stateId = getStateId(sate, countryId);
            String cityId = getCityId(city, stateId);

            String query = "insert into Vender_Master (username, email, password, MobileNo, CityId, StateId, CountryId, lat, lot) values('" + username + "', '" +
                    email + "', '"+password+ "', '" + phone + "', "+ cityId  + ", " + stateId +  ", " + countryId + ", '"+lat+"', '" + lot + "' )";
            System.out.println(query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            Log.i("Query", "Vendor Registered");

            query = "select concat(VendorID, ' ') as DETAILS from Vender_Master order by vendorId desc";
            System.out.println(query);
            ps= connection.prepareStatement(query);

            String id = "0";
            ResultSet rs = ps.executeQuery();
            rs.next();
            id = rs.getString("DETAILS").trim();

            vendorId = Integer.valueOf(id);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendorId;
    }

    /*will return vendorId in userMaster table
     * It will validate vendor*/
    public int validateVendor(String username, String password) {
        int userId = 0;
        try {
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();

            String query = "";

            //query = "Select concat(UserId, ':' , RoleID) as DETAILS from UserMaster where UserName ='"+username+"' and password = '"+password+"' and role = "+Constant.VENDOR_ROLE ;
            query = "Select concat(VendorID, ' ') as DETAILS from Vender_Master where UserName ='" + username + "' and password = '" + password + "'";

            Log.i("Fetch_", query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            String details = "0";
            if (resultSet.next()) {
                details = resultSet.getString("DETAILS").trim();

            }
            userId = Integer.valueOf(details);
            Log.i("UserIdFetch", "ViaUsername " + userId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
    public String cityName(String userId) {
        String name = "";
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "select concat(CityID, ' ') as id from UserMaster where userId = " +userId;
            System.out.println("Query: " + query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            String cityId = "";
            resultSet.next();
            cityId = resultSet.getString("id").trim();

            query = "select * from CityMaster where cityId = " + cityId;
            System.out.println("Query: " + query);

            ps = connection.prepareStatement(query);
            resultSet = ps.executeQuery();
            resultSet.next();
            name = resultSet.getString("CityName");

            System.out.println("CityName: " + name);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
    public String userLatLng(String userId) {
        String userLatLng = "";
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "select concat(Lat, ',', Lot) as id from UserMaster where userId = " +userId;
            System.out.println("Query: " + query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            userLatLng = resultSet.getString("id").trim();

            System.out.println("User LatLng: " + userLatLng);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (userLatLng == null || userLatLng.contains("null")) {
            userLatLng ="0,0";
        }
        return userLatLng;
    }
    /*Vender_Master	GSTNo	13	NULL	YES	varchar
Vender_Master	AdharCardNo	14	NULL	YES	varchar
Vender_Master	Status	15	NULL	YES	int
Vender_Master	CreatedBy	16	NULL	YES	int
Vender_Master	CreatedDate	17	NULL	YES	datetime
Vender_Master	UpdatedBy	18	NULL	YES	int
Vender_Master	UpdatedDate	19	NULL	YES	datetime
Vender_Master	ImageUpload	20	NULL	YES	varchar
Vender_Master	Upload_AdharCard	21	NULL	YES	varchar
Vender_Master	Upload_PanCard	22	NULL	YES	varchar
Vender_Master	PanCardNo	23	NULL	YES	varchar*
businessName, websiteLink, introduction, bankName ,bankAccountNumber, ifscCode, phone, gst, pan, aadhar;*/

    public ArrayList<VendorProfileModel> getVendorDetailsForBooking(String city) {

        /*get vendor id, lat, lot, feedback point*/
        ArrayList<VendorProfileModel> profileModelArrayList = new ArrayList<>();

        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();

            String query = "Select concat(CityID, ' ') as id , CityName from CityMaster  where CityName = '"+city+"'";
            Log.i("Find", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            String cityId = "-1";
            if (rs.next()) {
                cityId = rs.getString("id").trim();
                Log.i("Address", "city id "+ cityId);
            }else {
                /*No vendor at user location id there*/
                return profileModelArrayList;
            }

            query = "Select concat(VendorID, ' ') as id, lat, lot, MobileNo from Vender_Master where cityId = " + cityId + " and is_available = 1";
            Log.i("Find", query);
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            /*username, email, password, MobileNo, CityId, StateId, CountryId, lat, lot*/
            while (rs.next()) {
                VendorProfileModel vendorProfileModel = new VendorProfileModel();
                String vendorId = rs.getString("id").trim();;
                String lat = rs.getString("lat");
                String lot = rs.getString("lot");
                String mobile = rs.getString("MobileNo");

                vendorProfileModel.setVendorId(vendorId);
                vendorProfileModel.setLat(lat);
                vendorProfileModel.setLot(lot);
                vendorProfileModel.setPhone(mobile);

                profileModelArrayList.add(vendorProfileModel);
                System.out.println("VendorId: " +vendorId + " lat: " + lat + " lot: " +lot + " Mobile: " + mobile);

            }

            /*Now we have find feedback points*/
            /* String query = "insert into feedbackTransaction(vendorId, customerName, customerMobileNo, ServiceId, comments, reviewPoints, CreatedDate) */
            for (VendorProfileModel vendorProfileModel: profileModelArrayList) {
                query = "select concat(VendorID, ' ') as id, reviewPoints from feedbackTransaction where vendorId = " + vendorProfileModel.getVendorId();
                Log.i("Find", query);

                ps = connection.prepareStatement(query);
                rs = ps.executeQuery();
                int points = 0;
                if (rs.next()) {
                    points = rs.getInt("reviewPoints");
                }
                vendorProfileModel.setFeebackpoints(points);
                System.out.println("vendorId: " + vendorProfileModel.getVendorId() + " points: " + points);
            }

            /*find vendor coins*/
            for (VendorProfileModel vendorProfileModel: profileModelArrayList) {
                query = "select coins from VendorPaymentCart where vendorId = " + vendorProfileModel.getVendorId() + " and ( PaymentMode = 'online'  or  ApprovedBy IS NOT NULL)";
                Log.i("Find", query);

                ps = connection.prepareStatement(query);
                rs = ps.executeQuery();
                int coins = 0;

                if (rs.next()) {
                    coins = rs.getInt("coins");
                }

                vendorProfileModel.setCoins(coins);
                System.out.println("vendorId: " + vendorProfileModel.getVendorId() + " Coins: " + coins);
            }
            /*vendor Category and subCategory*/
            for (VendorProfileModel vendorProfileModel: profileModelArrayList) {
                query = "select CatId, SubCatId from VendorWiseActivity  where vendorId = " + vendorProfileModel.getVendorId();
                Log.i("Find", query);

                ps = connection.prepareStatement(query);
                rs = ps.executeQuery();
                int catId = 0;
                int subId = 0;

                while (rs.next()) {
                    catId = rs.getInt("catId");
                    subId = rs.getInt("SubCatId");
                }

                vendorProfileModel.setCatId(catId);
                vendorProfileModel.setSubId(subId);

                System.out.println("vendorId: " + vendorProfileModel.getVendorId() + " CatdId: " + catId + " SubCatIs: " +subId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profileModelArrayList;

    }

    public void deleteVendor() {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "delete from Vender_Master where VendorID in(15, 16, 18)";
            Log.i("Find", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            Log.i("Queryy", "Deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public VendorProfileModel getVendorProfile(int vendorId) {
        VendorProfileModel vendorProfileModel = new VendorProfileModel();
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "Select * from vender_master where vendorId = "+vendorId;
            System.out.println(query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                vendorProfileModel.setGst(removeNull(rs.getString("GSTNo")));
                vendorProfileModel.setAadhar(removeNull(rs.getString("AdharCardNo")));
                vendorProfileModel.setPan(removeNull(rs.getString("PanCardNo")));
                vendorProfileModel.setBusinessName(removeNull(rs.getString("businessName")));
                vendorProfileModel.setWebsiteLink(removeNull(rs.getString("websiteLink")));
                vendorProfileModel.setIntroduction(removeNull(rs.getString("introduction")));
                vendorProfileModel.setBankName(removeNull(rs.getString("bankName")));
                vendorProfileModel.setIfscCode(removeNull(rs.getString("ifscCode")));
                vendorProfileModel.setBankAccountNumber(removeNull(rs.getString("bankAccountNumber")));
                vendorProfileModel.setPhone(removeNull(rs.getString("MobileNo")));
            }
            System.out.println(vendorProfileModel.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendorProfileModel;
    }
    String removeNull(String  s) {
        if (s == null)return "";
        return s;
    }



    public void alterVendor() {
        int userId = 0;
        try {
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();

            String query = "";

            //query = "Select concat(UserId, ':' , RoleID) as DETAILS from UserMaster where UserName ='"+username+"' and password = '"+password+"' and role = "+Constant.VENDOR_ROLE ;
            query = "alter table vender_master add username varchar(200), password varchar(200), email varchar(200)";

            Log.i("Fetch_", query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            Log.i("UserIdFetch", "ViaUsername " + userId);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    String getCountryId(String countryName) {
        try {
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "Select concat(CountryId, ' ') as id , countryName from CountryMaster  where CountryName = '" + countryName + "'";
            Log.i("Find", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            String countryId = "";
            String stateId = "", cityId = "";
            /*Getting country id*/
            if (rs.next()) {
                //fetch country id
                countryId = rs.getString("id").trim();
                Log.i("Address", "country id " + countryId);
            } else {
                //insert country
                String insertCountry = "insert into CountryMaster(CountryName, CreatedDate) values('" + countryName + "', GETDATE())";
                PreparedStatement p1 = connection.prepareStatement(insertCountry);
                p1.execute();

                rs = ps.executeQuery();
                rs.next();
                countryId = rs.getString("id").trim();

                Log.i("Adress", "New Country created Id:  " + countryId);
            }
            return countryId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    String getStateId(String stateName, String countryId) {
        try {
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String stateId = "", cityId = "";

            /*Getting state id*/
            String query = "Select concat(StateId, ' ') as id , StateName from StateMaster  where StateName = '" + stateName + "'";
            Log.i("Find", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                stateId = rs.getString("id").trim();
                Log.i("Address", "state id " + stateId);
            } else {
                //insert state
                String insertCountry = "insert into StateMaster(StateName, CountryId, CreatedDate) values('" + stateName + "', " + countryId + ", GETDATE())";
                PreparedStatement p1 = connection.prepareStatement(insertCountry);
                p1.execute();

                rs = ps.executeQuery();
                rs.next();
                stateId = rs.getString("id").trim();
                Log.i("Adress", "New State created Id:  " + stateId);
            }
            return stateId;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    String getCityId(String cityName, String stateId) {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String cityId = "";

            String query = "Select concat(CityID, ' ') as id , CityName from CityMaster  where CityName = '"+cityName+"'";
            Log.i("Find", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cityId = rs.getString("id").trim();
                Log.i("Address", "city id "+ cityId);
            }else {
                //insert state
                String insertCountry = "insert into CityMaster(CityName, StateId, CreatedDate) values('" + cityName +"', " + stateId +", GETDATE())";
                PreparedStatement p1 = connection.prepareStatement(insertCountry);
                p1.execute();

                rs = ps.executeQuery();
                rs.next();
                cityId = rs.getString("id").trim();
                Log.i("Adress", "New City created Id:  " + cityId);
            }
            return cityId;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void alterBooking() {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String s = "alter table CheckOutAddtoCart add isAccepted int";
            Log.i("Update", s);
            PreparedStatement preparedStatement = connection.prepareStatement(s);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void acceptBooking(String pkChkOut) {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "update CheckOutAddtoCart set isAccepted = 1 where pkChkOut = " + pkChkOut;
            System.out.println("Query " + query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            System.out.println("Query: Request accepted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void completeBooking(String pkChkOut) {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "update CheckOutAddtoCart set isCompleted = 1 where pkChkOut = " + pkChkOut;
            System.out.println("Query " + query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            System.out.println("Query: Request Completed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void alterCheckOutAddtoCart() {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "alter table CheckOutAddtoCart add isCompleted int";
            System.out.println("Query " + query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            System.out.println("Query: added");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void cancelBooking(String pkChkOut, String reason, String vendorId) {
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();



            String query = "update CheckOutAddtoCart set Cancel = 1, CanCelReason = '" +reason + "', CancelDate = getDate(), ";


            if (vendorId == null) {
                query+= " VendorID = NULL ";
            }else {
                query="update CheckOutAddtoCart set isAccepted = 0, VendorID = " + vendorId;
            }
            query+= " where pkChkOut = " + pkChkOut;

            System.out.println("Query " + query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            System.out.println("Query: Request accepted");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BookingModel> getBookingDetails(String vendorId) {

        List<BookingModel> result = new ArrayList<>();

        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();

            //convert(datetime,'"+ arrivalDateAndTime +"',5) )
            String tran_query = "select DATEDIFF(mi,convert(datetime, getdate(), 131), arrivalDateTime  ) as diff, MobileNo, MobileNo , date , timing, concat(arrivalDateTime, ' ' ) as D,  " +
                    " ServiceName, qty, rate, amount, concat(userId, '') as id, concat(pkChkOut, '') as tranid,StreetAddress, FirstName,  LastName, InvoiceNo, modeOfPayment, StreetAddress," +
                    " Cancel, CanCelReason, isAccepted, CancelDate, isCompleted " +
                    "from CheckOutAddtoCart where ServiceName IS NOT NULL and VendorID = " + vendorId + " " +
                    "order by arrivalDateTime";
            Log.i("fetchQuery", tran_query);
            PreparedStatement preparedStatement = connection.prepareStatement(tran_query);
            ResultSet rs = preparedStatement.executeQuery();



            while (rs.next()) {
                //id int Not Null IDENTITY(1, 1), UserId bigint, amount int, date varchar(30), timing varchar(30), street varchar(100))
                String timeDiff = rs.getString("diff");
                String customerMobile = rs.getString("MobileNo");
                String date = rs.getString("date");
                String timing = rs.getString("timing");
                String dateTime = rs.getString("D");
                String customerName = rs.getString("FirstName") + " " +rs.getString("LastName");
                String userId = rs.getString("id");
                String tranId = rs.getString("tranid");
                String serviceName = rs.getString("ServiceName");
                int qty = rs.getInt("qty");
                int rate = rs.getInt("rate");
                int amount = rs.getInt("amount");
                String invoice = rs.getString("InvoiceNo");
                String modeOfPayment = rs.getString("modeOfPayment");
                String streetAddress = rs.getString("StreetAddress");
                int cancel = rs.getInt("Cancel");
                String cancelReason = rs.getString("CanCelReason");
                int isAccepted = rs.getInt("isAccepted");
                Date canceldate = rs.getDate("CancelDate");
                int isCompleted = rs.getInt("isCompleted");



                BookingModel bookingModel = new BookingModel();
                bookingModel.setCustomerMobile(customerMobile);
                bookingModel.setDate(date);
                bookingModel.setTime(timing);
                bookingModel.setCustomerName(customerName);
                bookingModel.setUserId(Integer.valueOf(userId));
                bookingModel.setVendorId(Integer.valueOf(vendorId));
                bookingModel.setTran_id(Integer.valueOf(tranId));
                bookingModel.setServiceName(serviceName);
                bookingModel.setQty(Integer.valueOf(qty));
                bookingModel.setRate(rate);
                bookingModel.setAmount(amount);
                bookingModel.setInvoiceNo(invoice);
                bookingModel.setModeOfPayment(modeOfPayment);
                bookingModel.setAddress(streetAddress);

                bookingModel.setIsCancelled(cancel);
                bookingModel.setCancelReason(cancelReason);
                bookingModel.setIsAccepted(isAccepted);
                if (canceldate != null) {
                    bookingModel.setCancelDate(canceldate.toString());
                }

                /*If time */
                if (Integer.valueOf(timeDiff) > 0 && isCompleted!=1) {
                    /*ongoing*/
                    bookingModel.setOnGoing(true);

                }else{
                    /*transaction completed*/
                    bookingModel.setOnGoing(false);
                }

                System.out.println("TranId: " +tranId +" Min: " + timeDiff + " customer mobile: " + customerMobile + " Date: " +date + " time: " + timing + " DateTIme: " + dateTime + " CustomerName: " +customerName + " vendorId: " +vendorId);

                result.add(bookingModel);
            }

            for (BookingModel bookingModel: result) {
                String query = "select * from Vender_Master where VendorID = " + bookingModel.getVendorId();
                PreparedStatement ps = connection.prepareStatement(query);
                rs = ps.executeQuery();
                if (rs.next()) {
                        /*Vender_Master	VendorID	1	NULL	NO	bigint
Vender_Master	VendorName	2	NULL	YES	varchar
Vender_Master	FirstName	3	NULL	YES	varchar
Vender_Master	LastName	4	NULL	YES	varchar
Vender_Master	CountryId	5	NULL	YES	int
Vender_Master	StateId	6	NULL	YES	int
Vender_Master	CityId	7	NULL	YES	int
Vender_Master	Address	8	NULL	YES	varchar
Vender_Master	MobileNo	9	NULL	YES	varchar
Vender_Master	Lat	10	NULL	YES	varchar
Vender_Master	Lot	11	NULL	YES	varchar
Vender_Master	CompanyName	12	NULL	YES	varchar*/
                    String vendorName = rs.getString("FirstName") + " " + rs.getString("LastName");
                    String vendorMobile = rs.getString("MobileNo");

                    bookingModel.setVendorName(vendorName);
                    bookingModel.setVendorMobile(vendorMobile);
                    System.out.println("vendorName: " + vendorName + " vendorMobile: " + vendorMobile);
                }
            }

            for (BookingModel bookingModel: result) {
                String first = bookingModel.getCustomerName().split(" ")[0];
                String query = "select * from userMaster where MobileNo = '" + bookingModel.getCustomerMobile() + "' and FirstName = '" + first + "'";
                PreparedStatement ps = connection.prepareStatement(query);
                rs = ps.executeQuery();
                String lat = "", lng = "";
                if (rs.next()) {
                    lat = rs.getString("Lat");
                    lng = rs.getString("Lot");
                }
                System.out.println("Customer " + first + " lat " + lat + " lng: " + lng);
                double newLat=0.0, newLng=0.0;
                if (lat.equals("") || lat.equals("null"))newLat=0.0;
                else newLat = Double.parseDouble(lat);

                if (lng.equals("") || lng.equals("null")) newLng=0.0;
                else newLng = Double.parseDouble(lng);

                bookingModel.setCustomerLat(newLat);
                bookingModel.setCustomerLng(newLng);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    public String purchaseCoins(int VendorID, int CartAmount, String PaymentMode)
    {
        /*
        * @Mode int,
@PaymentID int,
@VendorID int,
@CartAmount decimal(18,2),
@PaymentMode varchar(50),
@STatus int,
@CreatedBy int,*/
        int Mode = 1;
        int PaymentID = 0;
        int STatus = 1;
        int CreatedBy = 1;
        double cartAmount = (double) CartAmount;
        int coins = CartAmount / 10;
        String Result = "";
        connectionClass = new ConnectionClass();
        /*connection = connectionClass.CONN();
        String insertVendorDetail = "{call AddVendorPaymentCart(?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            callableStatement = connection.prepareCall(insertVendorDetail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            callableStatement.setInt("@Mode",Mode);
            callableStatement.setInt("@PaymentID",PaymentID);
            callableStatement.setInt("@VendorID",VendorID);
            callableStatement.setDouble("@CartAmount",cartAmount);
            callableStatement.setString("@PaymentMode", PaymentMode);
            callableStatement.setInt("@STatus",STatus);
            callableStatement.setInt("@CreatedBy",CreatedBy);
            callableStatement.registerOutParameter("@RESULT", Types.VARCHAR);
            callableStatement.executeUpdate();

            Result = callableStatement.getString("@RESULT");
            System.out.println("Result: " + Result);

        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        try{
            String query = "Insert into History_VendorPaymentCart(VendorId, CartAmount," +
                    "PaymentMode, CreatedBy, CreatedDate,COINS, status)" +
                    "Values("+ VendorID + ","+CartAmount+", '"+PaymentMode+"', 1, Getdate(),"+coins+",1)";
            System.out.println("history query: " + query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();

            query = "Insert into VendorPaymentCart(VendorId, CartAmount, PaymentMode," +
                    "CreatedBy, CreatedDate,COINS, status) " +
                    "Values("+ VendorID + ","+CartAmount+", '"+PaymentMode+"', 1, Getdate(),"+coins+",1)";
            System.out.println("coins query: " + query);
            ps = connection.prepareStatement(query);
            ps.execute();
            System.out.println("Coins Data inserted");
/*
            query = "Update vender_Master set CartAmount = (isnull(VendorCartAMount,0)) + " +
                    cartAmount +
                    " where VendorID ="+VendorID;

            System.out.println("vendor updated query: " + query);
            ps = connection.prepareStatement(query);
            ps.execute();*/
            System.out.println("Coins Data inserted");


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Result.toString();


    }

    public int getVendorCoins(String vendorId) {
        int coins = 0;
        try {
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            /*where vendorId = " + vendorId + "and ( paymentMode='Online' or approvedBy is not null)*/
            String query = "select sum(coins) as sum from VendorPaymentCart where vendorId = " + vendorId + " and ( paymentMode='Online' or approvedBy is not null)";
            Log.i("query", query);
            PreparedStatement ps = connection.prepareStatement(query);
            System.out.println();
            ResultSet rs = ps.executeQuery();
            System.out.println("hello2");
            while (rs.next()) {
                coins = rs.getInt("sum");
                System.out.println("record found: "+coins + " vendorId: " + vendorId);

            }/*else {
                System.out.println("No record found");
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coins;
    }
    public ArrayList<RechargeModel> getVendorTransaction(String vendorId) {
        ArrayList<RechargeModel> result = new ArrayList<>();
        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            /*String query = "Insert into History_VendorPaymentCart(VendorId, CartAmount," +
                    "PaymentMode, CreatedBy, CreatedDate,COINS, status)" +*/
            String query = "select VendorId, CartAmount,PaymentMode, CreatedBy, CreatedDate,COINS from History_VendorPaymentCart where vendorId = " + vendorId;
            PreparedStatement ps = connection.prepareStatement(query);
            System.out.println();
            ResultSet rs = ps.executeQuery();
            //System.out.println("hello2");
            while (rs.next()) {
                double cartAmount = rs.getDouble("CartAmount");
                String paymentMode = rs.getString("PaymentMode");
                String timestamp = rs.getDate("CreatedDate").toString();
                int coins = rs.getInt("COINS");

                RechargeModel rechargeModel = new RechargeModel(cartAmount, paymentMode, timestamp, coins);
                result.add(rechargeModel);
                System.out.println(rechargeModel.toString());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setVendorActivity(int vendorId, int catId, int subCatId, int childId, double amount, String description, String uploadPic1, String uploadPic2, String uploadPic3) {
        /*
        * @Mode int,
@HeadId int,
@VendorId int,
@CatId int,
@SubCatId int,
@ChildSubId int,
@Amount decimal(18,2),
@Description text,
@UploadPic1 varchar(100),
@UploadPic2 varchar(100),
@UploadPic3 varchar(100),
@Status int,
@CreatedBy int,
@Result varchar(200) = null output  */
        int Mode = 1;
        int headId = 0;
        int STatus = 1;
        int CreatedBy = 1;
        String Result = "";
        connectionClass = new ConnectionClass();
        connection = connectionClass.CONN();
        /*String insertVendorDetail = "{call AddVendorWiseActivity(?,?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?)}";
        CallableStatement callableStatement = null;
        try {
            callableStatement = connection.prepareCall(insertVendorDetail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            callableStatement.setInt("@Mode",Mode);
            callableStatement.setInt("@HeadId",headId);
            callableStatement.setInt("@VendorID", Integer.valueOf(vendorId));
            callableStatement.setInt("@CatId", catId);

            callableStatement.setInt("@SubCatId", subCatId);

            callableStatement.setInt("@ChildSubId", childId);
            callableStatement.setDouble("@Amount",amount);
            callableStatement.setString("@Description", description);
            callableStatement.setString("@Description", description);

            callableStatement.setString("@UploadPic1", uploadPic1);
            callableStatement.setString("@UploadPic2", uploadPic2);
            callableStatement.setString("@UploadPic3", uploadPic3);

            callableStatement.setInt("@STatus",STatus);
            callableStatement.setInt("@CreatedBy",CreatedBy);
            callableStatement.registerOutParameter("@RESULT", Types.VARCHAR);
            callableStatement.executeUpdate();

            Result = callableStatement.getString("@RESULT");
            System.out.println("Result: " + Result);

        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        try{
            /*Select @Count = (Select Count(*) from VendorWiseActivity Where VendorId = @VendorId
   and CatId = @CatId and subCatId = @SubCatId and ChildSubId = @ChildSubId) */
            String query = "select * from VendorWiseActivity where VendorId  = " + vendorId +" and CatId = "+catId + " and subCatId = "+subCatId;
            System.out.println(query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                /*Reord present*/
            }else{
                /*Insert into VendorWiseActivity (VendorID, CatId, SubCatId, ChildSubId, Amount,  Description, UploadPic1, UploadPic2, UploadPic3, Status, CreatedBy, CreatedDate)
     Values(@VendorId, @CatId, @SubCatId, @ChildSubId, @Amount, @Description,
     @UploadPic1, @UploadPic2, @UploadPic3, @Status, @CreatedBy, Getdate())  */

                query = "Insert into VendorWiseActivity (VendorID, CatId, SubCatId, ChildSubId, Amount,  Description, UploadPic1, UploadPic2, UploadPic3, Status, CreatedBy, CreatedDate) values("+
                        vendorId + ", " +catId +", "+subCatId + ", "+childId + ", " +amount +", '" + description + "', '"+uploadPic1+"', '"+uploadPic2 + "', '"+uploadPic3+"', 1, 1, getDate())";
                System.out.println(query);
                ps = connection.prepareStatement(query);
                ps.execute();

                query = "select * from VendorWiseActivity order by HeadID desc";
                System.out.println(query);
                ps = connection.prepareStatement(query);
                rs = ps.executeQuery();

                int id = 0;
                if (rs.next()) {
                    /*Reord present*/
                    id = rs.getInt("HeadId");
                    System.out.println("HeadId:  " +id);
                }

                String activityCode = "Act00-"+catId+"-"+id;

               /* Update VendorWiseActivity Set ActivityCode = 'Act00-' + Cast(CatId as varchar)
                        + '-' + Cast(@@Identity as varchar)
                where HeadID = @@Identity*/
                query = "Update VendorWiseActivity Set ActivityCode = '" + activityCode + "' where headId = " + id;
                System.out.println(query);
                ps = connection.prepareStatement(query);
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void printVendorWise(String vendorId) {

        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            /*String query = "Insert into History_VendorPaymentCart(VendorId, CartAmount," +
                    "PaymentMode, CreatedBy, CreatedDate,COINS, status)" +*/
            String query = "select * from VendorWiseActivity where vendorId = " + vendorId;
            PreparedStatement ps = connection.prepareStatement(query);
            System.out.println();
            ResultSet rs = ps.executeQuery();
            //System.out.println("hello2");
            if (rs.next()) {
               String description = rs.getString("Description");
                System.out.println("Description: " + description);

            }else{
                System.out.println("No Record Found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}
