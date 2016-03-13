package android.com.freezeframe;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {
    TextView description, brand, shape, model, price = null;
    LinearLayout ll = null;
    ImageView image = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        description = (TextView) findViewById(R.id.desc);
        brand = (TextView) findViewById(R.id.brand);
        shape = (TextView) findViewById(R.id.shape);
        model = (TextView) findViewById(R.id.model);
        price = (TextView) findViewById(R.id.price);

        image = (ImageView) findViewById(R.id.image);

        Intent i = getIntent();

        description.setText(i.getStringExtra("desc"));
        brand.setText(i.getStringExtra("brand"));
        model.setText(i.getStringExtra("model"));
        price.setText("$" + i.getStringExtra("price"));
        Picasso.with(this).load(i.getStringExtra("image")).into(image);


        description.setMovementMethod(new ScrollingMovementMethod());
    }
}
