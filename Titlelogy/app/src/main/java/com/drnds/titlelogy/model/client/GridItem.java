package com.drnds.titlelogy.model.client;

/**
 * Created by ajithkumar on 6/27/2017.
 */

public class GridItem {
    private String subclient;
    private String oderno;
    private String status;
    private String producttype;
    private String state;
    private String ordertask;
    private String barrowername;
    private String subId;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getOrdertask() {
        return ordertask;
    }

    public void setOrdertask(String ordertask) {
        this.ordertask = ordertask;
    }

    public String getOrderpriority() {
        return orderpriority;
    }

    public void setOrderpriority(String orderpriority) {
        this.orderpriority = orderpriority;
    }

    public String getCountytype() {
        return countytype;
    }

    public void setCountytype(String countytype) {
        this.countytype = countytype;
    }




    private String orderpriority;
    private String countytype;


    private String orderId;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String county;
    private String propertyaddress;

    public String getSubclient() {
        return subclient;
    }

    public void setSubclient(String subclient) {
        this.subclient = subclient;
    }

    public String getOderno() {
        return oderno;
    }

    public void setOderno(String oderno) {
        this.oderno = oderno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPropertyaddress() {
        return propertyaddress;
    }

    public void setPropertyaddress(String propertyaddress) {
        this.propertyaddress = propertyaddress;
    }

    public String getBarrowername() {
        return barrowername;
    }

    public void setBarrowername(String barrowername) {
        this.barrowername = barrowername;
    }



}
