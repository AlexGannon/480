package android.com.freezeframe;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Eyewear implements Serializable{
    String name = "", brand = "", description = "";
    double price = 0;
    String url = "";
    double ratio = 0;
    String key = "";

    public Eyewear(String url, String name, String brand, String description, double price, double ratio, String key)
    {
        this.url = url;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.ratio = ratio;
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public String getBrand()
    {
        return brand;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUrl()
    {
        return url;
    }

    public double getRatio()
    {
        return ratio;
    }

    public double getPrice()
    {
        return price;
    }

    public String getKey()
    {
        return key;
    }


}
