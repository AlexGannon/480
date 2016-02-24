package android.com.freezeframe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity {
    Button glassesButton, logoutButton = null;
    static FaceDetector detector = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glassesButton = (Button) findViewById(R.id.glassesbutton);
        logoutButton = (Button) findViewById(R.id.logout);

        glassesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .build();

                Intent i = new Intent(v.getContext(), GlassActivity.class);
                startActivity(i);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences("FreezeFramePrefs", Context.MODE_PRIVATE);
                sharedpreferences.edit().clear().commit();
                Intent i = new Intent(v.getContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
