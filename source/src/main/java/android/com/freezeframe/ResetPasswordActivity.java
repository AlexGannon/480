package android.com.freezeframe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText pass, conf = null;
    Button button = null;
    String password, confPassword;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        pass = (EditText) findViewById(R.id.password);
        conf = (EditText) findViewById(R.id.confirm);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = pass.getText().toString();
                confPassword = conf.getText().toString();
                username = getIntent().getStringExtra("username");

                if (!password.equals(confPassword))
                    Toast.makeText(v.getContext(), "Password do not match", Toast.LENGTH_LONG).show();
                else if (password.length() < 6)
                    Toast.makeText(v.getContext(), "Password must be at least 6 characters in length", Toast.LENGTH_LONG).show();
                else
                    new PasswordAsyncTask(v.getContext()).execute();

            }
        });
    }

    private class PasswordAsyncTask extends AsyncTask<Void, Void, String> {
        Context context;

        public PasswordAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://handy-implement-94801.appspot.com/reset");
            String result = null;
            HttpResponse response;
            JSONObject json;


            List<NameValuePair> nameValuePairs = new ArrayList<>(1);
            nameValuePairs.add(new BasicNameValuePair("user", username));
            nameValuePairs.add(new BasicNameValuePair("pass", password));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                // Execute HTTP Post Request

                response = httpclient.execute(httppost);


                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");

                json = new JSONObject(responseString);
                result = json.get("result").toString();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(context, "Password has been reset", Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(context, "Could not reset password", Toast.LENGTH_LONG).show();
            }
        }
    }
}
