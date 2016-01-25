package android.com.freezeframe;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    Button button = null;
    TextView loginTv = null;
    String[] securityQuestions = {"Your oldest sibling's middle name?", "Street you lived on in third grade?",
            "Name of your favorite childhood friend?", "City where you met your spouse?", "Your favorite food as a child?"};
    String username, email, fname, lname, password, secQuestion, secAnswer;
    Spinner spinner = null;
    EditText etFirstName, etLastName, etEmail, etUsername, etPassword, etSecurityQuestion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, securityQuestions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        etFirstName = (EditText) findViewById(R.id.fname);
        etLastName = (EditText) findViewById(R.id.lname);
        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        etEmail = (EditText) findViewById(R.id.email);
        etSecurityQuestion = (EditText) findViewById(R.id.answer);

        button = (Button) findViewById(R.id.button);
        loginTv = (TextView) findViewById(R.id.login);


        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                email = etEmail.getText().toString();
                fname = etFirstName.getText().toString();
                lname = etLastName.getText().toString();
                password = etPassword.getText().toString();
                secQuestion = spinner.getSelectedItem().toString();
                secAnswer = etSecurityQuestion.getText().toString();

                //Check for empty fields
                if(username.isEmpty() || email.isEmpty() || fname.isEmpty() || lname.isEmpty() || password.isEmpty() || secAnswer.isEmpty())
                    Toast.makeText(v.getContext(), "No Empty Fields", Toast.LENGTH_LONG).show();
                else if(password.length() < 6)
                    Toast.makeText(v.getContext(), "Password Must Be At Least 6 Characters In Length", Toast.LENGTH_LONG).show();
                else
                    new RegisterAsyncTask(v.getContext()).execute();




            }
        });
    }
    private class RegisterAsyncTask extends AsyncTask<Void, Void, String>
    {
        Context context = null;

        public RegisterAsyncTask(Context context)
        {
            super();
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String loginResult = "false";

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = null;

            try
            {
            StringBuilder result = new StringBuilder();


                URL url = new URL("https://pozzad-email-validator.p.mashape.com/emailvalidator/validateEmail/" + email + "?mashape-key=RPcIlPu8gxmshwzy4DvJMYW4y3v8p1utrTSjsnao1RrGXDfHO9");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            JSONObject json = null;
            String responseString = result.toString();
            System.out.println("The response string is: " + responseString);

            json = new JSONObject(responseString);
            System.out.println("The json response is: " + json);
            String isValid = json.get("isValid").toString();
            System.out.println("Email Validity: " + isValid);

                if(!isValid.equals("true"))
                    loginResult = "notValidEmail";

                if(loginResult.equals("false")) {
                    HttpPost httppost = new HttpPost("http://handy-implement-94801.appspot.com/register");

                    response = null;
                    json = null;

                    List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                    nameValuePairs.add(new BasicNameValuePair("user", username));
                    nameValuePairs.add(new BasicNameValuePair("pass", password));
                    nameValuePairs.add(new BasicNameValuePair("email", email));
                    nameValuePairs.add(new BasicNameValuePair("name", fname + " " + lname));
                    nameValuePairs.add(new BasicNameValuePair("question", secQuestion));
                    nameValuePairs.add(new BasicNameValuePair("answer", secAnswer));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                    // Execute HTTP Post Request

                    response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    entity = response.getEntity();
                    responseString = EntityUtils.toString(entity, "UTF-8");

                    json = new JSONObject(responseString);
                    loginResult = json.get("reson").toString();
                    System.out.println(loginResult);
                }

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
                Intent i = new Intent(context, MainActivity.class);
                startActivity(i);
                finish();
            }
            else if(result.equals("notValidEmail"))
                Toast.makeText(context, "Please Enter Valid a Email", Toast.LENGTH_LONG).show();
            else if(result.equals("email"))
            {
                Toast.makeText(context, "Email already in use", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("username"))
            {
                Toast.makeText(context, "Username already in use", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(context, "Try again", Toast.LENGTH_LONG).show();
            }
        }
    }
}
