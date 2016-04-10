package android.com.freezeframe;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BillingActivity extends AppCompatActivity {
    EditText addressEt, cityEt, zipEt, nameEt, cardEt, cvvEt, expEt = null;
    Spinner stateSpinner = null;
    CheckBox cb = null;
    Button button = null;
    Order order = null;
    ProgressDialog progress;
    Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        order = (Order) getIntent().getExtras().getSerializable("order");

        addressEt = (EditText) findViewById(R.id.address);
        cityEt = (EditText) findViewById(R.id.city);
        zipEt = (EditText) findViewById(R.id.zip);
        nameEt = (EditText) findViewById(R.id.name);
        cvvEt = (EditText) findViewById(R.id.cvv);
        expEt = (EditText) findViewById(R.id.expiration);
        cardEt = (EditText) findViewById(R.id.card);

        stateSpinner = (Spinner) findViewById(R.id.spinner);

        cb = (CheckBox) findViewById(R.id.checkbox);

        button = (Button) findViewById(R.id.button);

        progress = new ProgressDialog(this);
        context = this;

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    addressEt.setEnabled(false);
                    cityEt.setEnabled(false);
                    zipEt.setEnabled(false);
                    stateSpinner.setEnabled(false);
                }
                else
                {
                    addressEt.setEnabled(true);
                    cityEt.setEnabled(true);
                    zipEt.setEnabled(true);
                    stateSpinner.setEnabled(true);
                }


            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, cvv, exp, card, address, city, state, zip;

                name = nameEt.getText().toString();
                cvv = cvvEt.getText().toString();
                exp = expEt.getText().toString();
                card = cardEt.getText().toString();

                if(!cb.isChecked())
                {
                    address = addressEt.getText().toString();
                    city = cityEt.getText().toString();
                    zip = zipEt.getText().toString();
                    state = (String) stateSpinner.getSelectedItem();
                    if(name.equals(""))
                        Toast.makeText(v.getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                    else if(cvv.equals("") || cvv.length() != 3)
                        Toast.makeText(v.getContext(), "Enter CVV", Toast.LENGTH_SHORT).show();
                    else if(exp.equals("") || exp.length() != 5)
                            Toast.makeText(v.getContext(), "Enter Expiration Date", Toast.LENGTH_SHORT).show();
                    else if(card.equals("") || card.length() < 10)
                        Toast.makeText(v.getContext(), "Enter Card Number", Toast.LENGTH_SHORT).show();
                    else if(address.equals("") || address.length() < 6)
                        Toast.makeText(v.getContext(), "Enter Address", Toast.LENGTH_SHORT).show();
                    else if(city.equals(""))
                        Toast.makeText(v.getContext(), "Enter City", Toast.LENGTH_SHORT).show();
                    else if(zip.equals("")  || zip.length() != 5)
                        Toast.makeText(v.getContext(), "Enter Zip Code", Toast.LENGTH_SHORT).show();
                    else
                    {
                        order.setBillingInfo(card, cvv, exp, address, city, name, state, zip);
                        new OrderAsyncTask().execute();
                    }

                }
                else
                {
                    if(name.equals(""))
                        Toast.makeText(v.getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                    else if(cvv.equals("") || cvv.length() != 3)
                        Toast.makeText(v.getContext(), "Enter CVV", Toast.LENGTH_SHORT).show();
                    else if(exp.equals("") || exp.length() != 5)
                        Toast.makeText(v.getContext(), "Enter Expiration Date", Toast.LENGTH_SHORT).show();
                    else if(card.equals("") || card.length() < 10)
                        Toast.makeText(v.getContext(), "Enter Card Number", Toast.LENGTH_SHORT).show();
                    else
                    {
                        order.setCardInfo(card, cvv, exp, name);
                        new OrderAsyncTask().execute();
                    }
                }


            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, R.layout.myspinneritem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);
    }

    private class OrderAsyncTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            progress.setMessage("Placing Order");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(Void... params)
        {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://handy-implement-94801.appspot.com/placeorder");
                HttpResponse response;
                JSONObject json;
                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                String result = "";

                ArrayList<String> items = new ArrayList<String>();
                ArrayList<Eyewear> cart = MainActivity.shoppingCart.getCart();
                for(int i = 0; i < cart.size(); i++)
                {
                    items.add(cart.get(i).getKey());
                }

                Gson gson = new Gson();

                nameValuePairs.add(new BasicNameValuePair("shippingInfo", order.getShippingInfo()));
                nameValuePairs.add(new BasicNameValuePair("prescInfo", order.getPrescInfo()));
                nameValuePairs.add(new BasicNameValuePair("cardNumber", order.getCardNumber()));
                nameValuePairs.add(new BasicNameValuePair("cardExp", order.getCardExp()));
                nameValuePairs.add(new BasicNameValuePair("cardCVV", order.getCardCVV()));
                nameValuePairs.add(new BasicNameValuePair("billingName", order.getBillingName()));
                nameValuePairs.add(new BasicNameValuePair("billingAddress", order.getBillingAddress()));
                nameValuePairs.add(new BasicNameValuePair("items", gson.toJson(items)));
                nameValuePairs.add(new BasicNameValuePair("username", MainActivity.currentUser.getUsername()));



            try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    response = httpclient.execute(httppost);



                    HttpEntity entity = response.getEntity();
                    System.out.println("Response is: " + response);
                    String responseString = EntityUtils.toString(entity, "UTF-8");

                    json = new JSONObject(responseString);
                    result = json.get("reson").toString();


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(result.equals("nope"))
            {
                Toast.makeText(context, "Unable To Place Order", Toast.LENGTH_SHORT).show();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Order Placed")
                        .setMessage("The order has been successfully placed. The order number is: " + result)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(context, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

            progress.cancel();
            MainActivity.shoppingCart.emptyCart();


        }
    }
}
