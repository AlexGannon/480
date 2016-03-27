package android.com.freezeframe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {
LinearLayout root = null;
    TextView usernameTv, nameEt = null;
    EditText emailEt, answerEt = null;
    Button button = null;
    String[] securityQuestions = {"Your oldest sibling's middle name?", "Street you lived on in third grade?",
            "Name of your favorite childhood friend?", "City where you met your spouse?", "Your favorite food as a child?"};

    Spinner spinner = null;
    boolean changed = false;
    Context context = null;
    String oldEmail;
    boolean emailChange = false, answerChange = false, questionChange = false;

    TextWatcher tw;

    String username, fullname, email, securityQuestion, securityAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        spinner = (Spinner) findViewById(R.id.spinner);
        usernameTv = (TextView) findViewById(R.id.tvNumber10);
        nameEt = (TextView) findViewById(R.id.tvNumber9);

        emailEt = (EditText) findViewById(R.id.tvNumber11);
        answerEt = (EditText) findViewById(R.id.tvNumber13);

        button = (Button) findViewById(R.id.button);

        username = MainActivity.currentUser.getUsername();
        fullname = MainActivity.currentUser.getFullName();
        email = MainActivity.currentUser.getEmail();
        securityQuestion = MainActivity.currentUser.getSecquestion();
        securityAnswer = MainActivity.currentUser.getSecanswer();
        oldEmail = email;



        usernameTv.setText(username);
        nameEt.setText(fullname);
        emailEt.setText(email);
        answerEt.setText(securityAnswer);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.myspinneritem, securityQuestions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for(int i = 0; i < securityQuestions.length; i++)
        {
            if(securityQuestions[i].equals(securityQuestion)) {
                spinner.setSelection(i);
                break;
            }
        }

        emailEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!emailChange)
                    emailChange = true;
            }

        });

        answerEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!answerChange)
                    answerChange = true;
            }

        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEt.getText().toString();
                securityQuestion = securityQuestions[spinner.getSelectedItemPosition()];
                securityAnswer = answerEt.getText().toString();

                if (oldEmail.equals(email))
                    emailChange = false;

                System.out.println("In Onclick Listener");
                new RegisterAsyncTask(v.getContext()).execute();
            }
        });
    }

    private TextWatcher generalTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!changed)
                changed = true;
        }

    };

    @Override
    public void onBackPressed() {

        if(changed)
        {
            Toast.makeText(context, "Text has been changes", Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
        this.finish();
    }

    public void setInfo()
    {

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
                JSONObject json = null;
                String responseString = "";

                if(emailChange)
                {
                URL url = new URL("https://pozzad-email-validator.p.mashape.com/emailvalidator/validateEmail/" + email + "?mashape-key=RPcIlPu8gxmshwzy4DvJMYW4y3v8p1utrTSjsnao1RrGXDfHO9");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                responseString = result.toString();
                System.out.println("The response string is: " + responseString);

                json = new JSONObject(responseString);
                System.out.println("The json response is: " + json);
                String isValid = json.get("isValid").toString();
                System.out.println("Email Validity: " + isValid);

                if(!isValid.equals("true"))
                    loginResult = "notValidEmail";
                }

                if(loginResult.equals("false")) {
                    System.out.println("In On background");
                    HttpPost httppost = new HttpPost("http://handy-implement-94801.appspot.com/updateinfo");
                    response = null;
                    json = null;

                    List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                    nameValuePairs.add(new BasicNameValuePair("user", username));
                    nameValuePairs.add(new BasicNameValuePair("email", email));
                    nameValuePairs.add(new BasicNameValuePair("question", securityQuestion));
                    nameValuePairs.add(new BasicNameValuePair("answer", securityAnswer));
                    if(emailChange)
                        nameValuePairs.add(new BasicNameValuePair("emailUpdated", "updated"));
                    else
                        nameValuePairs.add(new BasicNameValuePair("emailUpdated", "not"));

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
                //Update SharedPreferences to show changes
                MainActivity.currentUser.setEmail(email);
                MainActivity.currentUser.setSecquestion(securityQuestion);
                MainActivity.currentUser.setSecanswer(securityAnswer);

                Gson gson = new Gson();
                SharedPreferences sp = getSharedPreferences("FreezeFramePrefs", Context.MODE_PRIVATE);
                String json = gson.toJson(MainActivity.currentUser);
                sp.edit().putString("userInfo", json).commit();

                finish();
            }
            else if(result.equals("notValidEmail"))
                Toast.makeText(context, "Enter Not Valid Email", Toast.LENGTH_LONG).show();
            else if(result.equals("email"))
            {
                Toast.makeText(context, "Email Already In Use", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(context, "Error Occurred: " + result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
