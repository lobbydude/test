package com.drnds.titlelogy.model.client;

/**
 * Created by Ajithkumar on 8/4/2017.
 */

public class Upload {
    private String documentType;
    private String description;
    private String uploadedDate;
    private String doumentpath;
    private String orderid;
    private String orderdocid;


    public String getOrderdoc_Id() {
        return orderdocid;
    }

    public void setOrderdoc_Id(String orderid) {
        this.orderdocid = orderdocid;
    }

    public String getOrder_Id() {
        return orderid;
    }

    public void setOrder_Id(String orderid) {
        this.orderid = orderid;
    }

    public String getDoumentpath() {
        return doumentpath;
    }

    public void setDoumentpath(String doumentpath) {
        this.doumentpath = doumentpath;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(String uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
}
