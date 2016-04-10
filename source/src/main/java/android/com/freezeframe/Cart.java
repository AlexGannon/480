package android.com.freezeframe;



import java.util.ArrayList;

public class Cart{

    private ArrayList<Eyewear> items;

    double totalPrice = 0;

    public Cart()
    {
        items = new ArrayList<Eyewear>();
    }

    public void addItem(Eyewear frame)
    {
        items.add(frame);
        totalPrice += frame.getPrice();
    }

    public void removeItem(String key)
    {
        String frameKey;
        for(int i = 0; i < items.size(); i++)
        {
            frameKey = items.get(i).getKey();
            if(frameKey != null && frameKey.equals(key)) {
                totalPrice -= items.get(i).getPrice();
                items.remove(i);
                break;
            }
        }
    }
    public ArrayList<Eyewear> getCart()
    {
        return items;
    }

    public void emptyCart()
    {
        items.clear();
        totalPrice = 0;
    }

    public double getPrice()
    {
        return totalPrice;
    }
}
