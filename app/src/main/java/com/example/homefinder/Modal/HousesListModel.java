package com.example.homefinder.Modal;

public class HousesListModel {

    String housename, houseprice, houseCoordinates, houseid, houseLocation, housebedrooms, housebathrooms,houseDescription,houseImage;

    public HousesListModel(String housename, String houseprice, String houseCoordinates,
                           String houseid, String houseLocation, String housebedrooms, String housebathrooms, String houseDescription, String houseImage) {
        this.housename = housename;
        this.houseprice = houseprice;
        this.houseCoordinates = houseCoordinates;
        this.houseid = houseid;
        this.houseLocation = houseLocation;
        this.housebedrooms = housebedrooms;
        this.housebathrooms = housebathrooms;
        this.houseDescription = houseDescription;
        this.houseImage = houseImage;
    }

    public String getHousename() {
        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename;
    }

    public String getHouseprice() {
        return houseprice;
    }

    public void setHouseprice(String houseprice) {
        this.houseprice = houseprice;
    }

    public String getHouseCoordinates() {
        return houseCoordinates;
    }

    public void setHouseCoordinates(String houseCoordinates) {
        this.houseCoordinates = houseCoordinates;
    }

    public String getHouseid() {
        return houseid;
    }

    public void setHouseid(String houseid) {
        this.houseid = houseid;
    }

    public String getHouseLocation() {
        return houseLocation;
    }

    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
    }

    public String getHousebedrooms() {
        return housebedrooms;
    }

    public void setHousebedrooms(String housebedrooms) {
        this.housebedrooms = housebedrooms;
    }

    public String getHousebathrooms() {
        return housebathrooms;
    }

    public void setHousebathrooms(String housebathrooms) {
        this.housebathrooms = housebathrooms;
    }

    public String getHouseDescription() {
        return houseDescription;
    }

    public void setHouseDescription(String houseDescription) {
        this.houseDescription = houseDescription;
    }

    public String getHouseImage() {
        return houseImage;
    }

    public void setHouseImage(String houseImage) {
        this.houseImage = houseImage;
    }
}
