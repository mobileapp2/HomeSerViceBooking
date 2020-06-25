package com.designtech9studio.puntersapp.Model;

public class RechargeModel {

    double cartAmount;
    String paymentMode, timeStamp;
    int coins;

    public RechargeModel(double cartAmount, String paymentMode, String timeStamp, int coins) {
        this.cartAmount = cartAmount;
        this.paymentMode = paymentMode;
        this.timeStamp = timeStamp;
        this.coins = coins;
    }

    @Override
    public String toString() {
        return "RechargeModel{" +
                "cartAmount=" + cartAmount +
                ", paymentMode='" + paymentMode + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", coins=" + coins +
                '}';
    }

    public double getCartAmount() {
        return cartAmount;
    }

    public void setCartAmount(double cartAmount) {
        this.cartAmount = cartAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
    /*String query = "Insert into History_VendorPaymentCart(VendorId, CartAmount," +
                    "PaymentMode, CreatedBy, CreatedDate,COINS, status)" +*/


}
