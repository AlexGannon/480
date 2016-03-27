package android.com.freezeframe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity {
    LinearLayout root, test = null;
    TextView emptyTv = null;
    Eyewear frame;
    Button button = null;
    ArrayList<Eyewear> temp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        final DecimalFormat precision = new DecimalFormat("#.00");
        root = (LinearLayout) findViewById(R.id.root);
        LinearLayout item = null;
        ImageView preview = null;
        final TextView model, brand, price = null;
        button = (Button) findViewById(R.id.button);
        emptyTv = (TextView) findViewById(R.id.empty);


        ArrayList<String> usedFrames = new ArrayList<String>();
        ArrayList<Eyewear> cart = MainActivity.shoppingCart.getCart();
        temp = new ArrayList<Eyewear>();


        for(int i = 0; i < cart.size(); i++)
        {
            temp.add(cart.get(i));
        }

        if(cart.size() == 0)
        {
            emptyTv.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);
        }
        else
        {
            int qty;
            for (int i = 0; i < temp.size(); i++) {
                frame = temp.get(i);
                qty = 1;
                if (!usedFrames.contains(frame.getKey())) {

                    usedFrames.add(frame.getKey());

                    for (int j = i + 1; j < temp.size(); j++) {
                        if (temp.get(j).getKey().equals(frame.getKey())) {
                            qty++;
                        }
                    }

                    final int myQty = qty;

                    item = (LinearLayout) getLayoutInflater().inflate(R.layout.shoppingcartitem, null);
                    test = (LinearLayout) item.findViewById(R.id.myll);
                    final Eyewear myFrame = frame;
                    test.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), DetailActivity.class);
                            i.putExtra("fromCart", "true");
                            i.putExtra("frame", myFrame);
                            startActivity(i);
                        }
                    });
                    Picasso.with(this).load(frame.getUrl()).into((ImageView) item.findViewById(R.id.image));
                    ((TextView) item.findViewById(R.id.model)).setText(frame.getName());
                    final TextView priceEt = (TextView) item.findViewById(R.id.price);
                    priceEt.setText("$" + precision.format(frame.getPrice() * qty));
                    ((TextView) item.findViewById(R.id.brand)).setText(frame.getBrand());
                    final TextView qtyTv = (TextView) item.findViewById(R.id.qty);
                    qtyTv.setText("x " + qty);
                    final String myKey = frame.getKey();
                    final double myPrice = frame.getPrice();

                    item.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(final View v) {


                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Remove Item")
                                    .setMessage("Are you sure you want to remove this item from the cart?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int newAmount = Integer.parseInt(qtyTv.getText().toString().substring(2, qtyTv.length())) - 1;

                                            if (newAmount == 0) {
                                                root.removeView(v);

                                                if (root.getChildCount() == 0) {
                                                    button.setVisibility(View.INVISIBLE);
                                                    emptyTv.setVisibility(View.VISIBLE);
                                                    MainActivity.shoppingCart.removeItem(myKey);
                                                    saveCart(v.getContext());
                                                    Toast.makeText(v.getContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    MainActivity.shoppingCart.removeItem(myKey);
                                                    saveCart(v.getContext());
                                                    Toast.makeText(v.getContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                qtyTv.setText("x " + newAmount);
                                                priceEt.setText("$" + precision.format((Double.parseDouble(priceEt.getText().toString().substring(1, priceEt.length()))) - (myPrice)));
                                                MainActivity.shoppingCart.removeItem(myKey);
                                                saveCart(v.getContext());
                                                Toast.makeText(v.getContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .show();


                            return false;
                        }
                    });

                    root.addView(item);
                }


                if (cart.size() == 0) {
                    button.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void saveCart(Context context)
    {
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.shoppingCart);

        SharedPreferences sp;
        sp = context.getSharedPreferences("FreezeFrameShoppingCart", Context.MODE_PRIVATE);
        sp.edit().putString("cart", json).commit();
    }

}