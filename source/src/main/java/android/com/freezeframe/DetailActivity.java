package android.com.freezeframe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class DetailActivity extends AppCompatActivity {
    TextView description, brand, shape, model, price = null;
    LinearLayout ll = null;
    ImageView image = null;
    Button button = null;
    Eyewear frame = null;
    boolean isFromCart = false;
    MenuItem cartIcon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        description = (TextView) findViewById(R.id.desc);
        brand = (TextView) findViewById(R.id.brand);
        shape = (TextView) findViewById(R.id.shape);
        model = (TextView) findViewById(R.id.model);
        price = (TextView) findViewById(R.id.price);
        DecimalFormat precision = new DecimalFormat("#.00");
        Intent intent = getIntent();
        frame = (Eyewear) intent.getSerializableExtra("frame");



        image = (ImageView) findViewById(R.id.image);

        button = (Button) findViewById(R.id.button);

        if(intent.hasExtra("fromCart")) {

            button.setVisibility(View.INVISIBLE);
            isFromCart = true;
        }

        //description.setText(i.getStringExtra("desc"));
        //brand.setText(i.getStringExtra("brand"));
        //model.setText(i.getStringExtra("model"));
        //price.setText("$" + i.getStringExtra("price"));


        description.setText(frame.getDescription());
        brand.setText(frame.getBrand());
        model.setText(frame.getName());
        price.setText("$" + precision.format(frame.getPrice()));

        //Picasso.with(this).load(i.getStringExtra("image")).into(image);
        Picasso.with(this).load(frame.getUrl()).into(image);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.shoppingCart.addItem(frame);

                Gson gson = new Gson();
                String json = gson.toJson(MainActivity.shoppingCart);

                SharedPreferences sp;
                sp = v.getContext().getSharedPreferences("FreezeFrameShoppingCart", Context.MODE_PRIVATE);
                sp.edit().putString("cart", json).commit();
                Toast.makeText(v.getContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
            }
        });


        description.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailmenu, menu);
        cartIcon = menu.findItem(R.id.action_cart);
        if(isFromCart)
            cartIcon.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                // User chose the "Settings" item, show the app settings UI...
                Intent i = new Intent(this, ShoppingCartActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
