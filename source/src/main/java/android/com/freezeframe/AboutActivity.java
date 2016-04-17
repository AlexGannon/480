package android.com.freezeframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutActivity extends AppCompatActivity {
TextView mainText, bottomText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mainText = (TextView) findViewById(R.id.about);
        bottomText = (TextView) findViewById(R.id.bottom);

    }
}
