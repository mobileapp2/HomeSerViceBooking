package com.designtech9studio.puntersapp.Helpers;

import android.util.Log;

import com.designtech9studio.puntersapp.Activity.Profile;
import com.designtech9studio.puntersapp.ConnectionClass;
import com.designtech9studio.puntersapp.Model.AddressModel;
import com.designtech9studio.puntersapp.Model.BannerModel;
import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.CategoryMasterModel;
import com.designtech9studio.puntersapp.Model.ProfileModel;
import com.designtech9studio.puntersapp.Model.SubCategoryModel;
import com.designtech9studio.puntersapp.Model.UserModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper {

    final String TAG = "MainPageSQL";

    ConnectionClass connectionClass;
    Connection connection;
    public DataBaseHelper() {
    }

    public ResultSet fetchQuery(String query) {
        try{

            PreparedStatement ps = connection.prepareStatement(query);

            Log.e("query",query);
            return ps.executeQuery();
        }catch (SQLException sql) {
            Log.i("SQLException", sql.getMessage());
        }finally {
            return null;
        }
    }
/*
* CountryMaster	CountryId	1	NULL	NO	bigint
CountryMaster	CountryName	2	NULL	YES	varchar
CountryMaster	Status	3	NULL	YES	int
CountryMaster	CreatedBy	4	NULL	YES	int
CountryMaster	CreatedDate	5	NULL	YES	datetime
CountryMaster	UpdatedBy	6	NULL	YES	int
CountryMaster	UpdatedDate	7	NULL	YES	datetime
*/
/*StateMaster	StateId	1	NULL	NO	bigint
StateMaster	CountryId	2	NULL	YES	int
StateMaster	StateName	3	NULL	YES	varchar
StateMaster	Status	4	NULL	YES	int
StateMaster	CreatedBy	5	NULL	YES	int
StateMaster	CreatedDate	6	NULL	YES	datetime
StateMaster	UpdatedBy	7	NULL	YES	int
StateMaster	UpdatedDate	8	NULL	YES	datetime
*/
    public void setAddress(AddressModel model, String userId) {
        try{
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();

            String query = "Select concat(CountryId, ' ') as id , countryName from CountryMaster  where CountryName = '"+model.getCountryName()+"'";
            Log.i("Find", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            String countryId = "";
            String stateId="", cityId = "";
            /*Getting country id*/
            if (rs.next()) {
                //fetch country id
                countryId = rs.getString("id").trim();
                Log.i("Address", "country id " + countryId);
            }else{
                //insert country
                String insertCountry = "insert into CountryMaster(CountryName, CreatedDate) values('" + model.getCountryName() +"', GETDATE())";
                PreparedStatement p1 = connection.prepareStatement(insertCountry);
                p1.execute();

                rs = ps.executeQuery();
                rs.next();
                countryId = rs.getString("id").trim();

                Log.i("Adress", "New Country created Id:  " + countryId);
            }

            /*Getting state id*/
            query = "Select concat(StateId, ' ') as id , StateName from StateMaster  where StateName = '"+model.getStateName()+"'";
            Log.i("Find", query);
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                stateId = rs.getString("id").trim();
                Log.i("Address", "state id "+ stateId);
            }else {
                //insert state
                String insertCountry = "insert into StateMaster(StateName, CountryId, CreatedDate) values('" + model.getStateName() +"', " + countryId +", GETDATE())";
                PreparedStatement p1 = connection.prepareStatement(insertCountry);
                p1.execute();

                rs = ps.executeQuery();
                rs.next();
                stateId = rs.getString("id").trim();
                Log.i("Adress", "New State created Id:  " + stateId);
            }


            /*get city id*/

            /*CityMaster	CityID	1	NULL	NO	bigint
CityMaster	StateId	2	NULL	YES	int
CityMaster	CityName	3	NULL	YES	varchar
CityMaster	Status	4	NULL	YES	int
CityMaster	CreatedBY	5	NULL	YES	int
CityMaster	CreatedDate	6	NULL	YES	datetime
CityMaster	UpdatedBy	7	NULL	YES	int
CityMaster	UPdatedate	8	NULL	YES	datetime
*/
            query = "Select concat(CityID, ' ') as id , CityName from CityMaster  where CityName = '"+model.getCityName()+"'";
            Log.i("Find", query);
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                cityId = rs.getString("id").trim();
                Log.i("Address", "city id "+ cityId);
            }else {
                //insert state
                String insertCountry = "insert into CityMaster(CityName, StateId, CreatedDate) values('" + model.getCityName() +"', " + stateId +", GETDATE())";
                PreparedStatement p1 = connection.prepareStatement(insertCountry);
                p1.execute();

                rs = ps.executeQuery();
                rs.next();
                cityId = rs.getString("id").trim();
                Log.i("Adress", "New City created Id:  " + cityId);
            }

            /*Update userId*/
            /*UserMaster	StreetAddress1	7	NULL	YES	varchar
UserMaster	StreetAddress2	8	NULL	YES	varchar
UserMaster	LandMark	9	NULL	YES	varchar
UserMaster	CityId	10	NULL	YES	int
UserMaster	StateID	11	NULL	YES	int
UserMaster	CountryID	12	NULL	YES	int
UserMaster	Lat	13	NULL	YES	varchar
UserMaster	Lot	14	NULL	YES	varchar
UserMaster	Status	15	NULL	YES	int
UserMaster	CreatedBy	16	NULL	YES	int
UserMaster	CreatedDate	17	NULL	YES	datetime
UserMaster	UpdatedBy	18	NULL	YES	int
UserMaster	UpdatedDate	19	NULL	YES	datetime
*/
            String updateQuery = "update UserMaster set StreetAddress1 = '" + model.getStreetAddress1() + "' ,"+
                    "StreetAddress2 = '"+model.getStreetAddress2()+"', " +
                    "LandMark = '"+model.getLandmark() +  "', " +
                    "cityId = "+ cityId + ", "+
                    "stateId = " + stateId + ", " +
                    "countryid = " + countryId + ", "+
                    "lat = '"+model.getLat() + "', " +
                    "lot = '"+model.getLot() + "', "+
                    "UpdatedDate = GETDATE() " +
                    "where userId = " +userId;
            Log.i("UpdateQuery", updateQuery);
            ps = connection.prepareStatement(updateQuery);
            ps.execute();
            Log.i("Address", "Updated");


            //    connection.close();

            /*query = "Select concat(CityID, ' ', stateId, ' ', CountryID, ' ' , lat, ' ', lot) as id from  UserMaster   where userId = "+userId+"";
            Log.i("Find", query);
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                cityId = rs.getString("id").trim();
                Log.i("Address", " Upadte Prove "+ cityId);
            }*/


        }catch (Exception e) {
            System.out.println("Find "+e.getMessage());
        }



    }

    /*UserTransaction	Id	1	NULL	NO	int
UserTransaction	ApplicationUserId	2	NULL	YES	int
UserTransaction	Amount	3	NULL	YES	decimal
UserTransaction	TransName	4	NULL	NO	varchar
UserTransaction	Type	5	NULL	NO	varchar
UserTransaction	IsDistroy	6	((0))	NO	bit
UserTransaction	CreatedDate	7	NULL	NO	datetime
UserTransaction	ModifiedDate	8	NULL	NO	datetime
UserTransaction	IsActive	9	((1))	NO	bit
*/

    /*TranChildSubCategory	TranId	1	NULL	NO	bigint
TranChildSubCategory	CHildId	2	NULL	YES	int
TranChildSubCategory	Amount	3	NULL	YES	decimal
TranChildSubCategory	CreatedBy	4	NULL	YES	int
TranChildSubCategory	CreatedDate	5	NULL	YES	datetime
*/

    public void updateTransactionAppTable() {
        try{
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            /*create entry in trnsaction table*/

            //String query = "create table transactionApp(id int Not Null IDENTITY(1, 1), UserId bigint, amount int, date datetime);";Log.i("Query", tranQuery);
            /*ALTER TABLE table_name
ADD column_name datatype;*/
            String tranQuery = "alter table transactionApp add modeOfPayment varchar(200), status varchar(100)";
            Log.i("Querry", tranQuery);
            PreparedStatement ps = connection.prepareStatement(tranQuery);
            ps.execute();
            Log.i("Query", "Altered table");

            tranQuery = "update transactionApp set modeOfPayment='CASH', status='completed'";
            Log.i("Querry", tranQuery);

            ps = connection.prepareStatement(tranQuery);
            ps.execute();
            Log.i("Query", "Updated");


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setFeedBack(int vendorId, String custName, String mobile, String serviceId, String comments, String points){
        try {
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();

            String query = "insert into feedbackTransaction(vendorId, customerName, customerMobileNo, ServiceId, comments, reviewPoints, CreatedDate) values("+vendorId+", '"+custName+"', '"+
            mobile+"', "+serviceId + ", '"+comments +"', "+points + ", getdate())";
            Log.i("Query", query);

            //query = "select addFeedBackTransaction("+vendorId+", "+custName +", "+mobile+", "+serviceId+", "+comments+", " + points +") from dual";
            //Log.i("Query", query);
            PreparedStatement ps = connection.prepareStatement(query);
            //ResultSet rs = ps.executeQuery();
            ps.execute();

            Log.i("Query", "inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

   /* public void setBookingTable(String userId, List<CatSubChildModel> cartDataModels, int total, String selectedDate, String selectedTime, String address, String mode, String status) {

        try{
            Log.i("BOOKING ", "SETTING");
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            *//*create entry in trnsaction table*//*

            String amount = String.valueOf(total);
            Log.i("ModeeOfPayment", mode);
            //String query = "create table transactionApp(id int Not Null IDENTITY(1, 1), UserId bigint, amount int, date datetime);";modeOfPayment status
            String tranQuery = "insert into transactionApp(UserId, Amount, date, timing, street, modeOfPayment, status) values("+userId+ ", " + amount + ", '"+selectedDate+"', '"+selectedTime +"', '"+address+"', '"+mode+"', '"+ status +"' )";
            Log.i("Query", tranQuery);
            PreparedStatement ps = connection.prepareStatement(tranQuery);
            ps.execute();
            Log.i("Query", "inserted");

            *//*Get tran id*//*
            String fetchtranID = "select id from transactionApp order by id desc;";
            ps = connection.prepareStatement(fetchtranID);
            ResultSet rs = ps.executeQuery();

            rs.next();
            int tranId = rs.getInt("id");

            for (CatSubChildModel cart: cartDataModels) {
                String childId = String.valueOf(cart.getSubChildId());
                String qty = String.valueOf(cart.getQty());
                String bookingQuery = "insert into booking(id, childId, qty) values("+tranId + ", " + childId + ", " + qty + ")";
                Log.i("Query", bookingQuery);
                ps = connection.prepareStatement(bookingQuery);
                ps.execute();
                Log.i("QueryBook", "Booking insrted");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }*/
    /*UserId	EmailId	MobileNo	FirstName	LastName	StreetAddress	GrossAmount	TotalAmount	TaxAmount	Cancel	CanCelReason	CancelDate	CreatedBy	CreatedDate	InvoiceNo
    Year_Session	UniqueNo	VendorID	VendorServiceId modeOfPayment date varchar(100), timing varchar(100), arrivalDateTime datetime
     */

    public void alterTransaction() {
        try {
            Log.i("BOOKING ", "SETTING");
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String countQuery = "alter table TransactionCheckOutAddtoCart add serviceName varchar(100)";
            Log.i("BillCOunt", countQuery);
            PreparedStatement ps = connection.prepareStatement(countQuery);
            ps.execute();
            System.out.println("Altered completed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setBookingTable(String userId, List<CatSubChildModel> cartDataModels,String selectedDate, String selectedTime, String address, String mode, String vendorId) {

        try{
            Log.i("BOOKING ", "SETTING");
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            /*create entry in trnsaction table*/

            /*Get user profile*/
            ProfileModel profileModel  = getProfileData(userId);
            String name[] = profileModel.getName().split(" ");
            String firstName ="", lastName = "";
            if (name.length>0) {
                firstName = name[0];
                if (firstName.length()>1)
                    lastName = name[1];
            }
            String email = profileModel.getEmail();
            String mobile = profileModel.getPhone();
            int taxAMount = 0;

            String countQuery = "Select count(*) as count from CheckOutAddtoCart";
            Log.i("BillCOunt", countQuery);
            PreparedStatement ps = connection.prepareStatement(countQuery);
            ResultSet rs = ps.executeQuery();
            int  billCount= -1;
            rs.next();
            billCount = rs.getInt("count");

            Log.i("BillCOunt", ""+billCount);
            String yearSession = "2019-2020 ";
            String arrivalDateAndTime = selectedDate + " " + selectedTime;
            Log.i("ArrivalDateTIme", arrivalDateAndTime);


            int countIndex = 1;
            for (CatSubChildModel cart: cartDataModels) {

                String childId = String.valueOf(cart.getSubChildId());
                String qty = String.valueOf(cart.getQty());
                int amount = (int)cart.getAmount();
                int gross= amount * Integer.valueOf(qty);
                int totalAmount = gross + taxAMount;
                String serviceName = cart.getSubChildName();
                String invoiceNumber = "Inv-"+yearSession+"-"+billCount + countIndex;
                countIndex++;

                /*SubChildCatID varchar(200), qty int, Rate int, Amount int, ServiceName varchar(200)*/
                /*SubChildCatID, qty , Rate , Amount , ServiceName */

                String bookingQuery = "insert into CheckOutAddtoCart(UserId,EmailId, MobileNo, FirstName, LastName, StreetAddress, GrossAmount, TotalAmount, TaxAmount, CreatedDate, InvoiceNo, Year_Session," +
                        "VendorID, modeOfPayment, date, timing, arrivalDateTime, SubChildCatID, qty , Rate , Amount , ServiceName) values" +
                        "("+userId + ", '" + email + "', '" + mobile + "', '"+firstName+"', '" + lastName + "', '" + address + "', " + gross + ", "+totalAmount + ", " + taxAMount + ", getDate(), '" +
                        invoiceNumber + "', '" + yearSession + "', "+vendorId + ", '" + mode + "', '"+selectedDate+"', '" + selectedTime + "', convert(datetime,'"+ arrivalDateAndTime +"',5), " +
                        childId + ", " + qty + ", "+amount + ", " + gross + ", '"+serviceName+"')";

                Log.i("Query", bookingQuery);
                ps = connection.prepareStatement(bookingQuery);
                ps.execute();
                Log.i("QueryBook", "Booking insrted");

                /*TranID	fkChkOut	UserId	Sno	SubChildCatID	Qty	Rate	Amount	Cancel	CanCelReason
                CancelDate	CreatedBy	CreatedDate	Year_Session	VendorID	VendorServiceId	ServiceName
                 TransactionCheckOutAddtoCart
*/
                String itemsOfBooking = "insert into TransactionCheckOutAddtoCart(UserId, SubChildCatID, Qty, rate, amount, CreatedDate, Year_Session, VendorID, ServiceName) values( "+
                        userId + ", " + childId + ", " + qty + ", " + amount + ", " + gross + ", getDate(), '" + yearSession + "', " + vendorId + ", '"+serviceName+ "')";


                Log.i("Query", itemsOfBooking);

                ps = connection.prepareStatement(itemsOfBooking);
                ps.execute();
                Log.i("QueryBook", "Booking Items Inserted");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /*public List<BookingModel> getBookingDetails(String userID) {

        List<BookingModel> result = new ArrayList<>();

        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();

            String tran_query = "select * from transactionApp where userId = " + userID +" order by date";
            Log.i("fetchQuery", tran_query);
            PreparedStatement preparedStatement = connection.prepareStatement(tran_query);
            ResultSet rs = preparedStatement.executeQuery();



            while (rs.next()) {
                //id int Not Null IDENTITY(1, 1), UserId bigint, amount int, date varchar(30), timing varchar(30), street varchar(100))
                BookingModel model = new BookingModel();
                int id = rs.getInt("id");
                int amount = rs.getInt("amount");
                String address = rs.getString("street");
                String timings = rs.getString("timing");
                String date = rs.getString("date");
                String modeOfPayment = rs.getString("modeOfPayment");

                model.setAddress(address);
                model.setAmount(amount);
                model.setDate(date);
                model.setTran_id(id);
                model.setTime(timings);
                model.setModeOfPayment(modeOfPayment);

                System.out.println("Booking " + model.toString());
                result.add(model);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }*/

    public void alterBooking2() {
        try{
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            //String query = "alter table CheckOutAddtoCart add date varchar(100), timing varchar(100)";
            //String query = "alter table CheckOutAddtoCart add arrivalDateTime datetime";
            String query = "alter table CheckOutAddtoCart add SubChildCatID varchar(200), qty int, Rate int, Amount int, ServiceName varchar(200)";

            Log.i("Query", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            Log.i("Querry", "altered");

            /*query = "insert into CheckOutAddtoCart(arrivalDateTime) values(convert(datetime,'18-06-12 10:34:09 PM',5))";
            Log.i("Query", query);
            ps = connection.prepareStatement(query);
            ps.execute();*/

            Log.i("Querry", "altered");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*UserId	EmailId	MobileNo	FirstName	LastName	StreetAddress	GrossAmount	TotalAmount	TaxAmount	Cancel	CanCelReason	CancelDate	CreatedBy	CreatedDate	InvoiceNo
    Year_Session	UniqueNo	VendorID	VendorServiceId modeOfPayment date varchar(100), timing varchar(100), arrivalDateTime datetime
    SubChildCatID varchar(200), qty int, Rate int, Amount int, ServiceName varchar(200)
     CheckOutAddtoCart
     */
    /*TranID	fkChkOut	UserId	Sno	SubChildCatID	Qty	Rate	Amount	Cancel	CanCelReason
                CancelDate	CreatedBy	CreatedDate	Year_Session	VendorID	VendorServiceId	ServiceName
                 TransactionCheckOutAddtoCart
*/

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

    public List<BookingModel> getBookingDetails(String userID) {

        List<BookingModel> result = new ArrayList<>();

        try{
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();

            //convert(datetime,'"+ arrivalDateAndTime +"',5) )
            String tran_query = "select DATEDIFF(mi,convert(datetime, getdate(), 131), arrivalDateTime  ) as diff, MobileNo, MobileNo , date , timing, concat(arrivalDateTime, ' ' ) as D,  " +
                    " ServiceName, qty, rate, amount, concat(VendorID, '') as id, concat(pkChkOut, '') as tranid,StreetAddress, FirstName,  LastName, InvoiceNo, modeOfPayment, StreetAddress " +
                    "from CheckOutAddtoCart where ServiceName IS NOT NULL and userId = " + userID + " " +
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
                String vendorId = rs.getString("id");
                String tranId = rs.getString("tranid");
                String serviceName = rs.getString("ServiceName");
                int qty = rs.getInt("qty");
                int rate = rs.getInt("rate");
                int amount = rs.getInt("amount");
                String invoice = rs.getString("InvoiceNo");
                String modeOfPayment = rs.getString("modeOfPayment");
                String streetAddress = rs.getString("StreetAddress");


                BookingModel bookingModel = new BookingModel();
                bookingModel.setCustomerMobile(customerMobile);
                bookingModel.setDate(date);
                bookingModel.setTime(timing);
                bookingModel.setCustomerName(customerName);
                int VenID_INT = 0;
                if (!vendorId.equals("")) {
                    VenID_INT = Integer.parseInt(vendorId);
                }
                bookingModel.setVendorId(Integer.valueOf(VenID_INT));
                bookingModel.setTran_id(Integer.valueOf(tranId));
                bookingModel.setServiceName(serviceName);
                bookingModel.setQty(Integer.valueOf(qty));
                bookingModel.setRate(rate);
                bookingModel.setAmount(amount);
                bookingModel.setInvoiceNo(invoice);
                bookingModel.setModeOfPayment(modeOfPayment);
                bookingModel.setAddress(streetAddress);

                /*If time */
                if (Integer.valueOf(timeDiff) > 0) {
                    /*ongoing*/
                    bookingModel.setOnGoing(true);

                }else{
                    /*transaction completed*/
                    bookingModel.setOnGoing(false);
                }

                System.out.println("Min: " + timeDiff + " customer mobile: " + customerMobile + " Date: " +date + " time: " + timing + " DateTIme: " + dateTime + " CustomerName: " +customerName + " vendorId: " +vendorId);

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
                    String vendorUserName = rs.getString("username");

                    bookingModel.setVendorName(vendorName);
                    bookingModel.setVendorMobile(vendorMobile);
                    bookingModel.setVendorUserName(vendorUserName);

                    System.out.println("vendorName: " + vendorName + " vendorMobile: " + vendorMobile + " vendoruserName: " + vendorUserName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }


   /* public void createBookingtable() {
        try{
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "create table booking(book_id int Not Null IDENTITY(1, 1), id int, childId bigint, qty int);";
            PreparedStatement ps = connection.prepareStatement(query);
            Log.i("Create table", query);
            ps.execute();
            Log.i("", "created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
     /*public void createTranTable() {
        try{
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();

            String dropQuery = "drop table transactionApp";
            Log.i("Query", dropQuery);
            PreparedStatement ps1 = connection.prepareStatement(dropQuery);
            ps1.execute();

            Log.i("Database", "Table dropped");
            String query = "create table transactionApp(id int Not Null IDENTITY(1, 1), UserId bigint, amount int, date varchar(30), timing varchar(30), street varchar(100));";
            PreparedStatement ps = connection.prepareStatement(query);
            Log.i("Create table", query);
            ps.execute();
            Log.i("insertQuery", "created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    public void setUpdatedProfile(String userId, String firstname, String lastname, String email, String mobile) {
        try{
/*
* UserMaster	UserId	1	NULL	NO	bigint
UserMaster	UserCode	2	NULL	YES	varchar
UserMaster	UserName	3	NULL	YES	varchar
UserMaster	Password	4	NULL	YES	varchar
UserMaster	RoleID	5	NULL	YES	bigint
UserMaster	MobileNo	6	NULL	YES	varchar
UserMaster	StreetAddress1	7	NULL	YES	varchar
UserMaster	StreetAddress2	8	NULL	YES	varchar
UserMaster	LandMark	9	NULL	YES	varchar
UserMaster	CityId	10	NULL	YES	int
UserMaster	StateID	11	NULL	YES	int
UserMaster	CountryID	12	NULL	YES	int
UserMaster	Lat	13	NULL	YES	varchar
UserMaster	Lot	14	NULL	YES	varchar
UserMaster	Status	15	NULL	YES	int
UserMaster	CreatedBy	16	NULL	YES	int
UserMaster	CreatedDate	17	NULL	YES	datetime
UserMaster	UpdatedBy	18	NULL	YES	int
UserMaster	UpdatedDate	19	NULL	YES	datetime
UserMaster	FirstName	20	NULL	YES	varchar
UserMaster	LastName	21	NULL	YES	varchar
UserMaster	EmailID	22	NULL	YES	varchar
*/
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "update userMaster set FirstName='" + firstname +"', "+
                    "LastName='"+lastname+"', " +
                    "EmailID='"+email+"', " +
                    "MobileNo='" + mobile + "' where userId = " +userId;
            Log.i("Query", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            Log.i("Record", "Updated");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CategoryMasterModel> getMainPageServicesData() {

        List<CategoryMasterModel> catList = new ArrayList<>();
        Log.i("Find", "getMainPageServicesData");
        try{
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();

            String query = "Select * from CategoryMaster where Status=1;";
            Log.i("Find", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            Log.i("FInd", "found 2");

            if (rs!=null) {
                Log.i(TAG, "Found Not null");

                while (rs.next()) {
                    Log.i(TAG, "Found 2");
                    int catId = rs.getInt("CatId");
                    String catName = rs.getString("CatName");
                    String catImage = rs.getString("ImageUpload");
                    //String catImage = "1234";
                    String description = rs.getString("Description");
                    int mainPage = rs.getInt("MainPage");

                    if (mainPage == 1) {
                        CategoryMasterModel model = new CategoryMasterModel(catId, catName, catImage, description, mainPage);
                        catList.add(model);
                        System.out.println(TAG + model.toString());
                    }



                    //System.out.println("Category Name: "+ catName + "\tMainPage: "+mainPage+"\tCatId: "+ catId+"\tDescription: "+description +"\tImage: "+catImage);
                }
                connection.close();
            }

        }catch (Exception e) {
            System.out.println("Find "+e.getMessage());
        }
        return catList;
    }
    public ProfileModel getProfileData(String userId) {

        ProfileModel profileModel = new ProfileModel();
        try {
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "Select * from UserMaster where userId = "+userId;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String FirstName = resultSet.getString("FirstName");
                String LastName = resultSet.getString("LastName");
                String EmailID = resultSet.getString("EmailID");
                String mobileNumber = resultSet.getString("MobileNo");

                if (EmailID==null || EmailID.equals("null"))EmailID="";
                if (mobileNumber==null || mobileNumber.equals("null"))mobileNumber="";
                if (LastName==null || LastName.equals("null"))LastName="";
                if (FirstName==null || FirstName.equals("null"))FirstName="";

                profileModel.setEmail(EmailID);
                profileModel.setName(FirstName + " " + LastName);
                profileModel.setPhone(mobileNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profileModel;
    }
/*
* UserMaster	UserId	1	NULL	NO	bigint
UserMaster	UserCode	2	NULL	YES	varchar
UserMaster	UserName	3	NULL	YES	varchar
UserMaster	Password	4	NULL	YES	varchar
UserMaster	RoleID	5	NULL	YES	bigint
UserMaster	MobileNo	6	NULL	YES	varchar
UserMaster	StreetAddress1	7	NULL	YES	varchar
UserMaster	StreetAddress2	8	NULL	YES	varchar
UserMaster	LandMark	9	NULL	YES	varchar
UserMaster	CityId	10	NULL	YES	int
UserMaster	StateID	11	NULL	YES	int
UserMaster	CountryID	12	NULL	YES	int
UserMaster	Lat	13	NULL	YES	varchar
UserMaster	Lot	14	NULL	YES	varchar
UserMaster	Status	15	NULL	YES	int
UserMaster	CreatedBy	16	NULL	YES	int
UserMaster	CreatedDate	17	NULL	YES	datetime
UserMaster	UpdatedBy	18	NULL	YES	int
UserMaster	UpdatedDate	19	NULL	YES	datetime
UserMaster	FirstName	20	NULL	YES	varchar
UserMaster	LastName	21	NULL	YES	varchar
UserMaster	EmailID	22	NULL	YES	varchar
*/
    //Tab;e UserMaster

    public void registerUser(UserModel userModel) {
        String result="";
        try {
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            // String query = "Select * from UserMaster";
            String username = userModel.getUserName();
            String pass = userModel.getPassword();
            int rollId = userModel.getRoleId();
            String firstName = userModel.getFirstName();
            String lastname = userModel.getLastName();
            String email = userModel.getEmailId();
            String query = "";
            if (email==null)
            query = "insert into UserMaster(UserName,  Password, RoleID , CreatedDate, FirstName, LastName, MobileNo) values ( '"+ username + "', '" +pass +"', " + rollId + ", GETDATE(), '" +
                    firstName + "', '" + lastname +"', '"+userModel.getMobile()+"' )";
            Log.i("InsertQuery", query);


            //String query = "Select concat(UserId, ':' , RoleID, ':', MobileNo) as DETAILS from UserMaster;";
            //String query = "Select concat(UserId, ':' , RoleID) as DETAILS from UserMaster;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            Log.i("Data", "Inserted master");
            //printUser(userModel.getMobile());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int  registerUserViaMail(UserModel userModel) {
        int result=0;
        try {
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            // String query = "Select * from UserMaster";
            int rollId = userModel.getRoleId();
            String firstName = userModel.getFirstName();
            String lastname = userModel.getLastName();
            String email = userModel.getEmailId();
            String query = "";

            query = "insert into UserMaster(EmailID,  RoleID , CreatedDate, FirstName, LastName) values('"+email + "', " + rollId +
                    ", GETDATE(), '"+firstName+"', '"+lastname + "')";
            Log.i("InsertQuery", query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            query = "Select concat(UserId, ':' , RoleID) as DETAILS from UserMaster order by UserId desc";
            //String query = "Select concat(UserId, ':' , RoleID, ':', MobileNo) as DETAILS from UserMaster;";
            //String query = "Select concat(UserId, ':' , RoleID) as DETAILS from UserMaster;";

            Log.i("UserQuery", query);
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String details = resultSet.getString("DETAILS");
                String s[] = details.split(Constant.SEPARATOR);
                if (s==null) {
                    System.out.println("No USER INFO");
                }else if (s.length>=1) {
                    System.out.println("User info Id: "+ s[0]);
                    result  = Integer.valueOf(s[0]);
                }
            }
            Log.i("UserIdFetch", "ViaEmail " + result);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public int getIdViaMail(String email) {
        int userId = 0;
        try {
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();

            String query = "";
            query = "Select concat(UserId, ':' , RoleID) as DETAILS from UserMaster where emailId ='"+email+"' ";
            Log.i("Fetch_ID_Email", query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String details = resultSet.getString("DETAILS");
                String s[] = details.split(Constant.SEPARATOR);
                if (s==null) {
                    System.out.println("No USER INFO");
                }else if (s.length>=1) {
                    System.out.println("User info Id: "+ s[0]);
                    userId  = Integer.valueOf(s[0]);
                }
            }
            Log.i("UserIdFetch", "ViaEmail " + userId);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;



    }
    /*
* UserMaster	UserId	1	NULL	NO	bigint
UserMaster	UserCode	2	NULL	YES	varchar
UserMaster	UserName	3	NULL	YES	varchar
UserMaster	Password	4	NULL	YES	varchar
UserMaster	RoleID	5	NULL	YES	bigint
UserMaster	MobileNo	6	NULL	YES	varchar
UserMaster	StreetAddress1	7	NULL	YES	varchar
UserMaster	StreetAddress2	8	NULL	YES	varchar
UserMaster	LandMark	9	NULL	YES	varchar
UserMaster	CityId	10	NULL	YES	int
UserMaster	StateID	11	NULL	YES	int
UserMaster	CountryID	12	NULL	YES	int
UserMaster	Lat	13	NULL	YES	varchar
UserMaster	Lot	14	NULL	YES	varchar
UserMaster	Status	15	NULL	YES	int
UserMaster	CreatedBy	16	NULL	YES	int
UserMaster	CreatedDate	17	NULL	YES	datetime
UserMaster	UpdatedBy	18	NULL	YES	int
UserMaster	UpdatedDate	19	NULL	YES	datetime
UserMaster	FirstName	20	NULL	YES	varchar
UserMaster	LastName	21	NULL	YES	varchar
UserMaster	EmailID	22	NULL	YES	varchar
*/
    public void printUser(String mobile) {
        String result = "";
        try {
            connectionClass = new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "Select * from UserMaster mobileNo='" +mobile+ "'; ";
            Log.i("Query", query);
            //String query = "Select * from UserMaster mobileNo like '%\" +mobile+ \"%'\";;
            //String query = "Select concat(UserId, ':' , RoleID, ':', MobileNo) as DETAILS from UserMaster;";
            //String query = "Select concat(UserId, ':' , RoleID) as DETAILS from UserMaster;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId = 1;

                //int userId = resultSet.getInt(0);
                String userCode = resultSet.getString(1);
                String userName = resultSet.getString("UserName");
                String userPassword = resultSet.getString("Password");
                String FirstName = resultSet.getString("FirstName");
                String LastName = resultSet.getString("LastName");
                String EmailID = resultSet.getString("EmailID");
                String mobileNumber = resultSet.getString("MobileNo");
                int userRoleId = 1;
//                int userRoleId = resultSet.getInt(4);
                String streetAddress1 = resultSet.getString(6);
                String streetAddress2 = resultSet.getString(7);
                String landMark = resultSet.getString(8);
                /*int CityId = resultSet.getInt(9);
                int StateID = resultSet.getInt(10);
                int CountryID = resultSet.getInt(11);
                String Lat = resultSet.getString(12);*/
                String Lot = resultSet.getString(13);
                int Status = resultSet.getInt(14);
                int CreatedBy = resultSet.getInt(15);
                Date Createddate = resultSet.getDate(16);
//                int UpdatedBy = resultSet.getInt(17);
                Date UpdatedDate = resultSet.getDate(18);
                //System.out.println("Role: "+ userRoleId + " userName: " + userName);
                /*System.out.println(userId + " " +userCode + " " +userName + " " +userPassword + " " +userRoleId + " " +mobileNumber + " " +streetAddress1 + " " +streetAddress2 + " " +
                        landMark + " " +CityId + " " +StateID + " " +CountryID + " " +Lat + " " +Lot + " " +Status + " " +CreatedBy + " " +Createddate + " " +
                        UpdatedBy + " " +UpdatedDate + " " +FirstName + " " +LastName+ " " +EmailID );*/
                System.out.println("Username: "+ userName + "Password: " + userPassword+" FirstName: " + FirstName + " LastName: " + LastName + " " + " Email" + EmailID + " Mobile: " + mobileNumber);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printAllUser() {
        String result="";
        try {
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            // String query = "Select * from UserMaster";
            String query = "Select * from UserMaster";
            //String query = "Select concat(UserId, ':' , RoleID, ':', MobileNo) as DETAILS from UserMaster;";
            //String query = "Select concat(UserId, ':' , RoleID) as DETAILS from UserMaster;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId = 1;

                //int userId = resultSet.getInt(0);
                String userCode = resultSet.getString(1);
                String userName = resultSet.getString(2);
                String userPassword = resultSet.getString(3);
                int userRoleId = 1;
//                int userRoleId = resultSet.getInt(4);
                String mobileNumber = resultSet.getString(5);
                String streetAddress1 = resultSet.getString(6);
                String streetAddress2 = resultSet.getString(7);
                String landMark = resultSet.getString(8);
                //int CityId = resultSet.getInt(9);
                //int StateID = resultSet.getInt(10);
                //int CountryID = resultSet.getInt(11);
                String Lat = resultSet.getString(12);
                String Lot = resultSet.getString(13);
//                int Status = resultSet.getInt(14);
                int CreatedBy = resultSet.getInt(15);
                Date Createddate = resultSet.getDate(16);
//                int UpdatedBy = resultSet.getInt(17);
                Date UpdatedDate = resultSet.getDate(18);
                String FirstName = resultSet.getString(19);
                String LastName = resultSet.getString(20);
                String EmailID = resultSet.getString(21);
                //System.out.println("Role: "+ userRoleId + " userName: " + userName);
                /*System.out.println(userId + " " +userCode + " " +userName + " " +userPassword + " " +userRoleId + " " +mobileNumber + " " +streetAddress1 + " " +streetAddress2 + " " +
                        landMark + " " +CityId + " " +StateID + " " +CountryID + " " +Lat + " " +Lot + " " +Status + " " +CreatedBy + " " +Createddate + " " +
                        UpdatedBy + " " +UpdatedDate + " " +FirstName + " " +LastName+ " " +EmailID );*/

                userName = resultSet.getString("UserName");
                userPassword = resultSet.getString("Password");
                FirstName = resultSet.getString("FirstName");
                LastName = resultSet.getString("LastName");
                EmailID = resultSet.getString("EmailID");
                mobileNumber = resultSet.getString("MobileNo");
                System.out.println("FirstName: " +FirstName + " LastName: " +LastName+ " " + " Email" +EmailID+ " Mobile: " +mobileNumber);
                System.out.println("Street1: " +streetAddress1 + " Street2: " + streetAddress2 + " landmark: " + landMark);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public String getUserIdAndRole(String mobile) {


        String result="";
        try {
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
           // String query = "Select * from UserMaster";
            String query = "Select concat(UserId, ':' , RoleID) as DETAILS from UserMaster where mobileNo like '%" +mobile+ "%'";
            //String query = "Select concat(UserId, ':' , RoleID, ':', MobileNo) as DETAILS from UserMaster;";
            //String query = "Select concat(UserId, ':' , RoleID) as DETAILS from UserMaster;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId = 1;
                String details = resultSet.getString("DETAILS");
                String s[] = details.split(Constant.SEPARATOR);
                if (s==null) {
                    System.out.println("No USER INFO");
                }else if (s.length==1) {
                    System.out.println("User info Id: "+ s[0]);

                }else if (s.length==2){
                    System.out.println("User info Id: "+ s[0] + " RoleID: "+ s[1]+ " Mobile: ");
                    connection.close();
                    return s[0]+Constant.SEPARATOR+ s[1];
                }else{
                    System.out.println("User info Id: "+ s[0] + " RoleID: "+ s[1]+ " Mobile: "+s[2]);

                }
                //int userId = resultSet.getInt(0);
               /* String userCode = resultSet.getString(1);
                String userName = resultSet.getString(2);
                String userPassword = resultSet.getString(3);
                int userRoleId = 1;
//                int userRoleId = resultSet.getInt(4);
                String mobileNumber = resultSet.getString(5);
                String streetAddress1 = resultSet.getString(6);
                String streetAddress2 = resultSet.getString(7);
                String landMark = resultSet.getString(8);
                int CityId = resultSet.getInt(9);
                int StateID = resultSet.getInt(10);
                int CountryID = resultSet.getInt(11);
                String Lat = resultSet.getString(12);
                String Lot = resultSet.getString(13);
                int Status = resultSet.getInt(14);
                int CreatedBy = resultSet.getInt(15);
                Date Createddate = resultSet.getDate(16);
                int UpdatedBy = resultSet.getInt(17);
                Date UpdatedDate = resultSet.getDate(18);
                String FirstName = resultSet.getString(19);
                String LastName = resultSet.getString(20);
                String EmailID = resultSet.getString(21);*/
                //System.out.println("Role: "+ userRoleId + " userName: " + userName);
                /*System.out.println(userId + " " +userCode + " " +userName + " " +userPassword + " " +userRoleId + " " +mobileNumber + " " +streetAddress1 + " " +streetAddress2 + " " +
                        landMark + " " +CityId + " " +StateID + " " +CountryID + " " +Lat + " " +Lot + " " +Status + " " +CreatedBy + " " +Createddate + " " +
                        UpdatedBy + " " +UpdatedDate + " " +FirstName + " " +LastName+ " " +EmailID );*/

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;


    }
/*RoleMaster	RoleID	1	NULL	NO	int
RoleMaster	RoleName	2	NULL	YES	varchar
RoleMaster	Status	3	NULL	YES	int
*/
    public void getRoleDetails() {
        try {
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "Select * from RoleMaster";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId=1;
                //int userId = resultSet.getInt(0);
                String roleName = resultSet.getString(1);
                String roleStatus = resultSet.getString(2);
                System.out.println("ROLE "+ userId+ " " + roleName + " " + roleStatus);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/*StateMaster	StateId	1	NULL	NO	bigint
StateMaster	CountryId	2	NULL	YES	int
StateMaster	StateName	3	NULL	YES	varchar
StateMaster	Status	4	NULL	YES	int
StateMaster	CreatedBy	5	NULL	YES	int
StateMaster	CreatedDate	6	NULL	YES	datetime
StateMaster	UpdatedBy	7	NULL	YES	int
StateMaster	UpdatedDate	8	NULL	YES	datetime
*/
    public void getStateDetails() {
        try {
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "Select * from StateMaster";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId=1;
                //int userId = resultSet.getInt(0);
                int countryId = resultSet.getInt(1);
                String StateName = resultSet.getString(2);
                System.out.println("Sate "+ userId+ " " + countryId + " " + StateName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*
    * CountryMaster	CountryId	1	NULL	NO	bigint
CountryMaster	CountryName	2	NULL	YES	varchar
CountryMaster	Status	3	NULL	YES	int
CountryMaster	CreatedBy	4	NULL	YES	int
CountryMaster	CreatedDate	5	NULL	YES	datetime
CountryMaster	UpdatedBy	6	NULL	YES	int
*/
    public void getCountryDetails() {
        try {
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "Select * from CountryMaster";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId=1;
                //int userId = resultSet.getInt(2);
                int countryId = resultSet.getInt(1);
                String StateName = resultSet.getString(1);
                System.out.println("country "+ userId+ " " + countryId + " " + StateName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public List<CategoryMasterModel> getAllServiceData() {

        List<CategoryMasterModel> catList = new ArrayList<>();
        Log.i("Find", "getMainPageServicesData");
        try{
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();

            String query = "Select * from CategoryMaster where status=1;";
            Log.i("Find", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            Log.i("FInd", "found 2");

            if (rs!=null) {
                Log.i(TAG, "Found Not null");

                while (rs.next()) {
                    Log.i(TAG, "Found 2");
                    int catId = rs.getInt("CatId");
                    String catName = rs.getString("CatName");
                    String catImage = rs.getString("ImageUpload");
                    //String catImage = "1234";
                    String description = rs.getString("Description");
                    int mainPage = rs.getInt("MainPage");
                    CategoryMasterModel model = new CategoryMasterModel(catId, catName, catImage, description, mainPage);
                    catList.add(model);
                    System.out.println(TAG + model.toString());





                    //System.out.println("Category Name: "+ catName + "\tMainPage: "+mainPage+"\tCatId: "+ catId+"\tDescription: "+description +"\tImage: "+catImage);
                }

            }
            connection.close();


        }catch (Exception e) {
            System.out.println("Find "+e.getMessage());
        }
        return catList;
    }


    /*BannerImage	BannerID	1	NULL	NO	bigint
BannerImage	BannerName	2	NULL	YES	varchar
BannerImage	ImageUpload	3	NULL	YES	varchar
BannerImage	Status	4	NULL	YES	int
BannerImage	MainPageSHow	5	NULL	YES	int
BannerImage	CreatedBy	6	NULL	YES	int
BannerImage	CreatedDate	7	NULL	YES	datetime
BannerImage	UpdatedBy	8	NULL	YES	int
BannerImage	UpdatedDate	9	NULL	YES	datetime
*/

    public List<BannerModel> getBannerData() {
        List<BannerModel> result = new ArrayList<>();
        try{
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "Select *  from BannerImage where Status=1;";
            Log.i("Query", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs!=null) {
                Log.i(TAG, "Found Not null");

                while (rs.next()) {

                    int id=0;
                    //int id = rs.getInt(0);

                    String name = rs.getString("BannerName");
                    String image = rs.getString("ImageUpload");
                    image = ImageFullUrl.getFullImageURL(image);
                    //String image = "";
                    BannerModel model = new BannerModel(id, name, image);
                    result.add(model);
                    System.out.println("bannerId: "+id + " Name: "+name +" image: "+image);
                    //System.out.println("Category Name: "+ catName + "\tMainPage: "+mainPage+"\tCatId: "+ catId+"\tDescription: "+description +"\tImage: "+catImage);
                }
            }else{
                Log.i("Query", "have no record");
            }
            connection.close();


        }catch (Exception e) {
            System.out.println("getBannerData->Exception: " + e.getMessage());
        }

        return result;

    }

    /*SubID
CatID
SubCategoryName
Status
CreatedBy
CreatedDate
UpdatedBy
UpdatedDate
ImageUpload
MainPage
*/
//Table name:    SubCategory

    public List<SubCategoryModel> getSubCategoryData(String catId) {

        List<SubCategoryModel> result = new ArrayList<>();
        try{
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "Select *  from SubCategory where catId = '"+catId +"' ;";
            Log.i("Query", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs!=null) {

                while (rs.next()) {

                    int id = rs.getInt("SubID");
                    int catIdDB = rs.getInt("CatID");
                    String name = rs.getString("SubCategoryName");
                    int status = rs.getInt("Status");
                    String image = rs.getString("ImageUpload");
                    int mainPage = rs.getInt("MainPage");

                    image = ImageFullUrl.getFullImageURL(image);
                    System.out.println("SUBCat: "+id+ " catId: "+catIdDB+" Name: "+name  +" Image: "+image + "\tMainPage: " + mainPage);
                    SubCategoryModel model = new SubCategoryModel(id, catIdDB, name, status, image, mainPage);
                    result.add(model);
                    //System.out.println("Category Name: "+ catName + "\tMainPage: "+mainPage+"\tCatId: "+ catId+"\tDescription: "+description +"\tImage: "+catImage);
                }
            }else{
                Log.i("Query", "have no record");
            }
            connection.close();


        }catch (Exception e) {
            System.out.println("getSubCategoryData->Exception: " + e.getMessage());
        }

        return result;

    }
    public List<SubCategoryModel> getAllSubCategoryData() {

        List<SubCategoryModel> result = new ArrayList<>();
        try{
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "Select *  from SubCategory;";
            Log.i("Query", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            boolean flag= false;
            if (rs!=null) {
                while (rs.next()) {
                    flag = true;
                    int id = rs.getInt("SubID");
                    int catIdDB = rs.getInt("CatID");
                    String name = rs.getString("SubCategoryName");
                    int status = rs.getInt("Status");
                    String image = rs.getString("ImageUpload");
                    int mainPage = rs.getInt("MainPage");

                    if (image !=null)
                    image = ImageFullUrl.getFullImageURL(image);

                    if (name!=null && image!=null )
                    System.out.println("SUBCat: "+id+ " catId: "+catIdDB+" Name: "+name  +" Image: "+image + "\tMainPage: " + mainPage);
                    SubCategoryModel model = new SubCategoryModel(id, catIdDB, name, status, image, mainPage);
                    result.add(model);
                    //System.out.println("Category Name: "+ catName + "\tMainPage: "+mainPage+"\tCatId: "+ catId+"\tDescription: "+description +"\tImage: "+catImage);
                }
                if (!flag) {
                    System.out.println("Query: " + query + " -> No record found");
                }
            }else{
                Log.i("Query", "have no record");
            }
            connection.close();


        }catch (Exception e) {
            System.out.println("getSubCategoryData->Exception: " + e.getMessage());
        }

        return result;


    }

    /*ChildId
CatId
SubCatId
ChildSubCatId
ChildSubCatName
Amount
ChildDescription
Status
CreatedBy
CreatedDate
UpdatedBy
UpdatedDate
ImageUpload
*/
    //table ChildSubCategory
    public List<CatSubChildModel> getCatSubChildData(String catId) {
        List<CatSubChildModel> result = new ArrayList<>();
        try{
            connectionClass= new ConnectionClass();
            connection = connectionClass.CONN();
            String query = "Select *  from ChildSubCategory where catId = '"+catId +"' ;";
            Log.i("Query", query);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs!=null) {
                Log.i(TAG, "Found Not null");

                while (rs.next()) {

                    int id = rs.getInt("ChildId");
                    int subCatId = rs.getInt("SubCatId");
                    int catIdDB = rs.getInt("CatId");
                    String name = rs.getString("ChildSubCatName");
                    double amount = rs.getDouble("Amount");
                    int status = rs.getInt("Status");
                    String description = rs.getString("ChildDescription");
                    String image = rs.getString("ImageUpload");


                    image = ImageFullUrl.getFullImageURL(image);

                   // System.out.println("SUBCat: "+id+ " catId: "+catIdDB+" Name: "+name  +" Image: "+image + " Decription: " + description +" amount: "+amount);
                    //int subChildId, int catId, int subCatId, String subChildName, double amount, String childDescription, String childImage
                    CatSubChildModel model = new CatSubChildModel(id, catIdDB, subCatId, name, amount, description, image);
                    result.add(model);
                    System.out.println("childId: "+id+" Child Name: "+ name + " Amount: "+amount+"CatId: "+ catId+"\tDescription: "+description +"\tImage: "+image);
                }
            }else{
                Log.i("Query", "have no record");
            }
            connection.close();


        }catch (Exception e) {
            System.out.println("getCatSubChildData->Exception: " + e.getMessage());
        }

        return result;


    }

}
