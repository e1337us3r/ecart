package com.apolets.main;

public class Merchant {
    private int id;
    private String companyName;
    private String address;
    private int telNum;
    private int balance;

    public Merchant(int id, String companyName, String address, int telNum, int balance) {
        this.id = id;
        this.companyName = companyName;
        this.address = address;
        this.telNum = telNum;
        this.balance = balance;
    }


    public int getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getAddress() {
        return address;
    }

    public int getTelNum() {
        return telNum;
    }

    public int getBalance() {
        return balance;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTelNum(int telNum) {
        this.telNum = telNum;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }


}
