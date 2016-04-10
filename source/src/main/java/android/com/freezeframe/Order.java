package android.com.freezeframe;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Order implements Serializable {
    //Shipping Information
    String shipAddress, shipCity, shipZip, shipState, shipName;
    //Prescription Information
    String rightSphere, leftSphere, rightCylinder, leftCylinder, rightAxis, leftAxis;
    //BillingInformation
    String card, cvv, exp;
    //Billing Address
    String billAddress, billCity, billZip, billState, billName;

    //Constructor with shipping information
    public Order(String shipAddress, String shipCity, String shipZip, String shipState, String shipName)
    {
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.shipZip = shipZip;
        this.shipState = shipState;
        this.shipName = shipName;
    }

    public void setPrescInfo(String rightSphere, String leftSphere, String rightCylinder, String leftCylinder, String rightAxis, String leftAxis)
    {
        this.rightSphere = rightSphere;
        this.leftSphere = leftSphere;
        this.rightCylinder = rightCylinder;
        this.leftCylinder = leftCylinder;
        this.rightAxis = rightAxis;
        this.leftAxis = leftAxis;
    }

    public void setBillingInfo(String card, String cvv, String exp, String billAddress, String billCity, String billName, String billState, String billZip)
    {
        this.card = card;
        this.cvv = cvv;
        this.exp = exp;
        this.billAddress = billAddress;
        this.billCity = billCity;
        this.billName = billName;
        this.billState = billState;
        this.billZip = billZip;
    }

    public void setCardInfo(String card, String cvv, String exp, String billName)
    {
        this.card = card;
        this.cvv = cvv;
        this.exp = exp;
        this.billName = billName;
        billAddress = shipAddress;
        billCity = shipCity;
        billState = shipState;
        billZip = shipZip;

    }


    public String getShippingInfo()
    {
        return shipName + " : " + shipAddress + " ," + shipCity + " ," + shipState + " " + shipZip;
    }

    public String getPrescInfo()
    {
        return "Right Sphere: " + rightSphere + " Right Cylinder: " + rightCylinder + " Right Axis: " + rightAxis +
                " Left Sphere: " + leftSphere + " Left Cylinder: " + leftCylinder + " Left Axis: " + leftAxis;
    }

    public String getCardNumber()
    {
        return card;
    }

    public String getCardExp()
    {
        return exp;
    }

    public String getCardCVV()
    {
        return cvv;
    }

    public String getBillingName()
    {
        return billName;
    }

    public String getBillingAddress()
    {
        return billName + " : " + billAddress + " ," + billCity + " ," + billState + " " + billZip;
    }
}
