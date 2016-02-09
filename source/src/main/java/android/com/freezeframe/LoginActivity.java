package android.com.freezeframe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button button = null;
    TextView registerTv, forgotTv = null;
    EditText editTextPassword, editTextUsername = null;
    CheckBox checkBox = null;
    static int loginAttempts = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = (Button) findViewById(R.id.button);
        registerTv = (TextView) findViewById(R.id.register);

        checkBox = (CheckBox) findViewById(R.id.checkbox);

        editTextPassword = (EditText) findViewById(R.id.password);
        editTextUsername = (EditText) findViewById(R.id.username);

        forgotTv = (TextView) findViewById(R.id.forgot);

        forgotTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ForgotActivity.class);
                startActivity(i);
            }
        });


        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if(username.isEmpty() || password.isEmpty())
                    Toast.makeText(v.getContext(), "Username or Password Empty", Toast.LENGTH_SHORT).show();
                else
                    new RegisterAsyncTask(username, password, v.getContext()).execute();
            }
        });
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, String>
    {
        String username, password;
        Context context = null;

        public RegisterAsyncTask(String username, String password, Context context)
        {
            super();
            this.username = username;
            this.password = password;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String loginResult = "false";

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://handy-implement-94801.appspot.com/login");

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
            loginResult = json.get("reson").toString();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return loginResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(result.equals("success"))
            {
                if(checkBox.isChecked())
                {
                    SharedPreferences sharedpreferences = getSharedPreferences("FreezeFramePrefs", Context.MODE_PRIVATE);
                    sharedpreferences.edit().putString("username", editTextUsername.getText().toString()).commit();
                }
                Intent i = new Intent(context, MainActivity.class);
                startActivity(i);
                finish();
            }
            else if(result.equals("locked"))
            {
                Toast.makeText(context, "Account has been locked", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_LONG).show();
                editTextPassword.setText("");
            }
        }
    }
}
