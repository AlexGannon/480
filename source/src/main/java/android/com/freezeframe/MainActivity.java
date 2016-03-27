package android.com.freezeframe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.face.FaceDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button glassesButton, logoutButton, profileButton, aboutButton, cartButton = null;
    static FaceDetector detector = null;
    static ArrayList<Eyewear> frames = new ArrayList<Eyewear>();
    ProgressDialog progress;
    static ArrayList<Eyewear> selectedFrames = new ArrayList<Eyewear>();
    static Cart shoppingCart;
    static User currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this);

        if(frames.isEmpty())
            new GetFramesAsyncTask().execute();

        glassesButton = (Button) findViewById(R.id.glassesbutton);
        logoutButton = (Button) findViewById(R.id.logout);
        profileButton = (Button) findViewById(R.id.profile);
        aboutButton = (Button) findViewById(R.id.about);
        cartButton = (Button) findViewById(R.id.cart);

        setShoppingCart(this);
        Intent i = getIntent();
        if(i.hasExtra("isLoggedIn"))
        {
            SharedPreferences sp = getSharedPreferences("FreezeFramePrefs", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            if (sp.contains("userInfo")) {
                String json = sp.getString("userInfo", null);
                currentUser = gson.fromJson(json, User.class);
            }
        }
        else
        {
            Gson gson = new Gson();
            SharedPreferences sp = getSharedPreferences("FreezeFramePrefs", Context.MODE_PRIVATE);
            String username = i.getStringExtra("username");
            String email = i.getStringExtra("email");
            String name = i.getStringExtra("name");
            String question = i.getStringExtra("question");
            String answer = i.getStringExtra("answer");
            currentUser = new User(username, "", question, answer, email, name);
            String json = gson.toJson(currentUser);
            sp.edit().putString("userInfo", json).commit();
        }

        glassesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .build();

                Intent i = new Intent(v.getContext(), PickGlassesActivity.class);
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

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MyProfileActivity.class);
                startActivity(i);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AboutActivity.class);
                startActivity(i);
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ShoppingCartActivity.class);
                startActivity(i);
            }
        });
    }


    private class GetFramesAsyncTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            System.out.println("In preExecute");
            progress.setMessage("Updating Inventory");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(Void... params) {


            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet("http://handy-implement-94801.appspot.com/getglasses");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);



                JSONObject json = null;

                JSONArray jsonArray = new JSONArray(responseString);

                System.out.println("The JsonArray Size Is: " + jsonArray.length());


                for(int i = 0; i < jsonArray.length(); i++)
                {
                    json = (JSONObject) jsonArray.get(i);

                    frames.add(new Eyewear(json.getString("url"), json.getString("fname"), json.getString("brand"), json.getString("about"), Double.parseDouble(json.getString("price")), Double.parseDouble(json.getString("ratio")), json.getString("key")) );
                    System.out.println(json.getString("key"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            progress.cancel();
            System.out.println("DONE HERE");

        }
    }

    public static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public void setShoppingCart(Context context)
    {
        SharedPreferences sp;
        Gson gson = new Gson();
        sp = context.getSharedPreferences("FreezeFrameShoppingCart", Context.MODE_PRIVATE);
        if (sp.contains("cart")) {
            String json = sp.getString("cart", null);
             shoppingCart = gson.fromJson(json, Cart.class);
            if(shoppingCart == null)
                shoppingCart = new Cart();
            System.out.println("Shopping cart not empty");
        }
        else {
            shoppingCart = new Cart();
            System.out.println("Shopping Cart Is Empty");
        }
    }
}
