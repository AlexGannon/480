package android.com.freezeframe;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ForgotActivity extends AppCompatActivity {
    EditText usernameEt, emailEt = null;
    EditText answerEt = null;
    Button button = null;
    TextView nametv, emailtv, secquestv, questionDisplay = null;
    String question;
    String answer;
    boolean validAccount = false;
    int count = 0;
    String username;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        usernameEt = (EditText) findViewById(R.id.username);
        emailEt = (EditText) findViewById(R.id.email);
        answerEt = (EditText) findViewById(R.id.answer);
        button = (Button) findViewById(R.id.button);
        questionDisplay = (TextView) findViewById(R.id.question);

        nametv = (TextView) findViewById(R.id.unametext);
        emailtv = (TextView) findViewById(R.id.emailtext);
        secquestv = (TextView) findViewById(R.id.textview);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validAccount) {
                    username = usernameEt.getText().toString();
                    email = emailEt.getText().toString();
                    if (username.isEmpty() || email.isEmpty())
                        Toast.makeText(v.getContext(), "No Empty Fields", Toast.LENGTH_LONG).show();
                    else
                        new ForgotAsyncTask(v.getContext(), true).execute();
                }
                else
                {
                    String userAnswer = answerEt.getText().toString();
                    if(userAnswer.isEmpty())
                        Toast.makeText(v.getContext(), "Enter Answer First", Toast.LENGTH_LONG).show();
                    else
                    {
                        if(userAnswer.equals(answer))
                        {
                            Intent i = new Intent(v.getContext(), ResetPasswordActivity.class);
                            i.putExtra("username", username);
                            startActivity(i);
                            finish();
                        }
                        else {
                            LoginActivity.loginAttempts++;
                            if(LoginActivity.loginAttempts == 5)
                            {
                                Toast.makeText(v.getContext(), "Too many attempts: account locked", Toast.LENGTH_LONG).show();
                                new ForgotAsyncTask(v.getContext(), false).execute();
                            }
                            else
                                Toast.makeText(v.getContext(), "Incorrect Answer: " + (5-LoginActivity.loginAttempts) + " Attempts Remaining", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

    }

    private class ForgotAsyncTask extends AsyncTask<Void, Void, String>
    {
        Context context = null;
        String status;
        boolean getInfo;


        public ForgotAsyncTask(Context context, boolean getInfo)
        {
            super();
            this.context = context;
            this.getInfo = getInfo;
        }

        @Override
        protected String doInBackground(Void... params) {

            if(getInfo) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://handy-implement-94801.appspot.com/forgot");

                HttpResponse response;
                JSONObject json;


                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("user", username));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("type", "getQuestion"));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");

                    json = new JSONObject(responseString);
                    status = json.get("status").toString();
                    System.out.println("The status is: " + status);

                    if (status.equals("valid")) {
                        question = json.get("question").toString();
                        answer = json.get("answer").toString();
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


            }
            else
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://handy-implement-94801.appspot.com/forgot");

                HttpResponse response;
                JSONObject json;


                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("user", username));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("type", "lockAccount"));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");

                    json = new JSONObject(responseString);
                    status = json.get("status").toString();

                    if (status.equals("valid")) {
                        question = json.get("question").toString();
                        answer = json.get("answer").toString();
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
            }
            return status;
        }

        @Override
        protected void onPostExecute(String result)
        {

            if(getInfo && result.equals("valid")) {
                nametv.setVisibility(View.GONE);
                usernameEt.setVisibility(View.GONE);
                emailtv.setVisibility(View.GONE);
                emailEt.setVisibility(View.GONE);

                secquestv.setVisibility(View.VISIBLE);
                questionDisplay.setVisibility(View.VISIBLE);

                questionDisplay.setText(question);
                answerEt.setVisibility(View.VISIBLE);

                button.setText("Reset Password");
                validAccount = true;
            }
            else if(result.equals("invalid"))
            {
                Toast.makeText(context, "Incorrect username or email", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("locked"))
            {
                Toast.makeText(context, "This account has been locked", Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }

        @Override
        protected void onPreExecute()
        {

        }
    }
}
