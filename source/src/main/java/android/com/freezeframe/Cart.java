package android.com.freezeframe;



import java.util.ArrayList;

public class Cart{

    private ArrayList<Eyewear> items;

    public Cart()
    {
        items = new ArrayList<Eyewear>();
    }

    public void addItem(Eyewear frame)
    {
        items.add(frame);
    }

    public void removeItem(String key)
    {
        String frameKey;
        for(int i = 0; i < items.size(); i++)
        {
            frameKey = items.get(i).getKey();
            if(frameKey != null && frameKey.equals(key)) {
                items.remove(i);
                break;
            }
        }
    }
    public ArrayList<Eyewear> getCart()
    {
        return items;
    }
}
