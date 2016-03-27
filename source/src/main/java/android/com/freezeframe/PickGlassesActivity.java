package android.com.freezeframe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PickGlassesActivity extends AppCompatActivity {
    ImageView imageView = null;
    Bitmap bmp = null;
    ListView lv = null;
    ArrayList<String> myList = new ArrayList<String>();
    LinearLayout ll = null;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_glasses);
        MainActivity.selectedFrames.clear();

        ll = (LinearLayout) findViewById(R.id.ll);

        LinearLayout temp = null;

        for(int i = 0; i < MainActivity.frames.size(); i++)
        {
            temp = (LinearLayout) getLayoutInflater().inflate(R.layout.frame_preview, null);
            ImageView imageView = (ImageView) temp.findViewById(R.id.imageone);
            Picasso.with(this).load(MainActivity.frames.get(i).getUrl()).into(imageView);
            final int currentValue = i;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Current Value: " + currentValue);
                    if (!v.isSelected())
                    {
                        MainActivity.selectedFrames.add(MainActivity.frames.get(currentValue));
                        v.setSelected(true);
                        count++;
                    } else
                    {
                        MainActivity.selectedFrames.remove(MainActivity.frames.get(currentValue));
                        v.setSelected(false);
                        count--;
                    }

                }
            });
            ll.addView(temp);
        }

        Button button = (Button) getLayoutInflater().inflate(R.layout.tryonbutton, null);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0)
                {
                    Toast.makeText(v.getContext(), "Select At Least One Frame", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(v.getContext(), GlassActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        ll.addView(button);




    }



}
