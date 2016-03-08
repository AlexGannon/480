package android.com.freezeframe;

import android.graphics.Bitmap;

public class Eyewear {
    String image = "";
    String name = "", brand = "", description = "";
    double price = 0;
    Bitmap bitmap = null;
    double ratio = 0;

    public Eyewear(String image, String name, String brand, String description, double price, double ratio)
    {
        this.image = image;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.ratio = ratio;
    }

    public Eyewear(Bitmap bitmap, double ratio)
    {
        this.bitmap = bitmap;
        this.ratio = ratio;
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

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public double getRatio()
    {
        return ratio;
    }


}
