package android.com.freezeframe;

import android.graphics.Bitmap;

public class Eyewear {
    String image = "";
    String name = "", brand = "", description = "";
    double price = 0;

    public Eyewear(String image, String name, String brand, String description, double price)
    {
        this.image = image;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
    }

    public String getImage()
    {
        return image;
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


}
