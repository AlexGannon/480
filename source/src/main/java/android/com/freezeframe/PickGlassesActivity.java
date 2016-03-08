package android.com.freezeframe;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.BitSet;

public class PickGlassesActivity extends AppCompatActivity {
    LinearLayout ll = null;
    Button button = null;
    ProgressBar pb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_glasses);


        pb = (ProgressBar) findViewById(R.id.progress);
        //new RegisterAsyncTask().execute();

       //populateList();
        setListTwo();
        pb.setVisibility(View.GONE);

        //ll.addView(button);
        System.out.println("END HERE");

    }



    private class RegisterAsyncTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            System.out.println("In preExecute");
        }

        @Override
        protected String doInBackground(Void... params) {


            try
            {
                /*
                StringBuilder result = new StringBuilder();
                URL url = new URL("http://handy-implement-94801.appspot.com/getglasses");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                */
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet("http://handy-implement-94801.appspot.com/getglasses");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);



                JSONObject json = null;
                //String responseString = result.toString();
                JSONArray jsonArray = new JSONArray(responseString);

                System.out.println("The JsonArray Size Is: " + jsonArray.length());

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    json = (JSONObject) jsonArray.get(i);
                    //MainActivity.frames.add(new Eyewear(json.get("image").toString(), "name", "brand", "desc", 100.00));
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
            System.out.println("In PostExecute");
            ll = (LinearLayout) findViewById(R.id.ll);
            LinearLayout myLayout;
            ImageView imageViewOne, imageViewTwo;

            button = (Button) getLayoutInflater().inflate(R.layout.tryonbutton, null);
            TextView tv;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            for(int i = 0; i < MainActivity.frames.size(); i++)
            {
                myLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.frame_preview, null);

                imageViewOne = (ImageView) myLayout.findViewById(R.id.imageone);

                byte[] n = org.apache.commons.codec.binary.Base64.decodeBase64((MainActivity.frames.get(i).getImage()).getBytes());
                imageViewOne.setImageBitmap(BitmapFactory.decodeByteArray(n, 0, n.length, options));
                //imageViewOne.setImageResource(R.drawable.glasses13);


                imageViewOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setSelected(!v.isSelected());
                    }
                });

                ll.addView(myLayout);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
                }
            });


            ll.addView(button);
        }
    }

    public void populateList()
    {
        ll = (LinearLayout) findViewById(R.id.ll);
        LinearLayout myLayout;
        ImageView imageViewOne, imageViewTwo;

        button = (Button) getLayoutInflater().inflate(R.layout.tryonbutton, null);
        TextView tv;
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = null;
        options.inMutable = true;
        options.inSampleSize = 8;


        byte[] n = null;
        org.apache.commons.codec.binary.Base64 base64= new org.apache.commons.codec.binary.Base64();
        System.out.println("START HERE");

        for(int i = 0; i < MainActivity.frames.size(); i++)
        {
            myLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.frame_preview, null);

            imageViewOne = (ImageView) myLayout.findViewById(R.id.imageone);

            n = base64.decodeBase64((MainActivity.frames.get(i).getImage()).getBytes());
            bitmap = BitmapFactory.decodeByteArray(n, 0, n.length, options);
            imageViewOne.setImageBitmap(bitmap);
            //imageViewOne.setImageResource(R.drawable.glasses13);


            imageViewOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                }
            });
            ll.addView(myLayout);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setListTwo()
    {
        ll = (LinearLayout) findViewById(R.id.ll);
        LinearLayout myLayout;
        ImageView imageViewOne, imageViewTwo;

        button = (Button) getLayoutInflater().inflate(R.layout.tryonbutton, null);
        System.out.println("START HERE");

        for(int i = 0; i < MainActivity.frames.size(); i++)
        {
            myLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.frame_preview, null);

            imageViewOne = (ImageView) myLayout.findViewById(R.id.imageone);


            imageViewOne.setImageBitmap(MainActivity.frames.get(i).getBitmap());
            //imageViewOne.setImageResource(R.drawable.glasses13);


            imageViewOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                }
            });
            ll.addView(myLayout);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        ll.addView(button);
    }



}
