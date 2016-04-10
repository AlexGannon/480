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

public class PrescActivity extends AppCompatActivity {
    Spinner sphereRSpin, sphereLSpin, cylinderRSpin, cylinderLSpin, axisRSpin, axisLSpin;
    Button button = null;
    String[] axisvals;
    Order order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presc);

        order = (Order) getIntent().getExtras().getSerializable("order");

        axisvals = new String[181];

        for(int i = 0; i < axisvals.length; i++)
        {
            axisvals[i] = "" + i;
        }

        sphereRSpin = (Spinner) findViewById(R.id.spherer);
        sphereLSpin = (Spinner) findViewById(R.id.spherel);

        cylinderRSpin = (Spinner) findViewById(R.id.cylinderr);
        cylinderLSpin = (Spinner) findViewById(R.id.cylinderl);

        axisRSpin = (Spinner) findViewById(R.id.axisr);
        axisLSpin = (Spinner) findViewById(R.id.axisl);



        //Adapter for sphere
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sphere_values, R.layout.myspinneritem);
        adapter.setDropDownViewResource(R.layout.myspinneritem);
        sphereRSpin.setAdapter(adapter);
        sphereLSpin.setAdapter(adapter);

        //Adapter for cylinder
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.cylinder_values, R.layout.myspinneritem);
        adapter2.setDropDownViewResource(R.layout.myspinneritem);
        cylinderRSpin.setAdapter(adapter2);
        cylinderLSpin.setAdapter(adapter2);


        //Adapter for axis
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.myspinneritem, axisvals);
        adapter3.setDropDownViewResource(R.layout.myspinneritem);
        axisRSpin.setAdapter(adapter3);
        axisLSpin.setAdapter(adapter3);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), BillingActivity.class);
                order.setPrescInfo(sphereRSpin.getSelectedItem().toString(), sphereLSpin.getSelectedItem().toString(), cylinderRSpin.getSelectedItem().toString(), cylinderLSpin.getSelectedItem().toString(), axisRSpin.getSelectedItem().toString(), axisLSpin.getSelectedItem().toString());
                i.putExtra("order", order);
                startActivity(i);
            }
        });


    }
}
