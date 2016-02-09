package android.com.freezeframe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    Context context = this;
    ImageView imageView = null;
    String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;

        Animation a = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        a.setDuration(1800);

        ((ImageView) findViewById(R.id.imageView)).setAnimation(a);
        SharedPreferences sharedpreferences = getSharedPreferences("FreezeFramePrefs", Context.MODE_PRIVATE);
        username = sharedpreferences.getString("username", null);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(username == null) {

                    Intent i = new Intent(context, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(context, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 2200);
    }
}
