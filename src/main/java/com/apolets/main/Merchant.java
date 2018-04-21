package com.apolets.main;

public class Merchant {
    private static String companyName;
    private static String address;
    private static String telNum;

    public static String getCompanyName() {
        return companyName;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        Merchant.address = address;
    }

    public static String getTelNum() {
        return telNum;
    }

    public static void setTelNum(String telNum) {
        Merchant.telNum = telNum;
    }



}
