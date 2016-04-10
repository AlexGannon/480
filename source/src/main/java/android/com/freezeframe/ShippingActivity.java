package android.com.freezeframe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ShippingActivity extends AppCompatActivity {
    EditText nameEt, addressEt, cityEt, zipEt;
    Spinner spinner = null;
    Button button = null;
    String name, address, city, zip, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        nameEt = (EditText) findViewById(R.id.name);
        addressEt = (EditText) findViewById(R.id.address);
        cityEt = (EditText) findViewById(R.id.city);
        zipEt = (EditText) findViewById(R.id.zip);
        spinner = (Spinner) findViewById(R.id.spinner);
        button = (Button) findViewById(R.id.button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, R.layout.myspinneritem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEt.getText().toString();
                address  = addressEt.getText().toString();
                city = cityEt.getText().toString();
                zip = zipEt.getText().toString();
                state = (String) spinner.getSelectedItem();

                if(name.equals(""))
                    Toast.makeText(v.getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                else if(address.equals("") || address.length() < 5)
                    Toast.makeText(v.getContext(), "Enter Address", Toast.LENGTH_SHORT).show();
                else if(city.equals(""))
                    Toast.makeText(v.getContext(), "Enter City", Toast.LENGTH_SHORT).show();
                else if(zip.equals("") || zip.length() != 5)
                    Toast.makeText(v.getContext(), "Enter Zip Code", Toast.LENGTH_SHORT).show();
                else
                {
                    Intent i = new Intent(v.getContext(), PrescActivity.class);
                    i.putExtra("order", new Order(address, city, zip, state, name));
                    startActivity(i);
                }
            }
        });

    }
}
