package com.example.homefinder;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.homefinder.Urls.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HouseDetails extends AppCompatActivity {
    String whoSentOrder, houseID;
    NestedScrollView scrollHouseDetails;
    LinearLayout error_message_balance;
    TextView errorTxt;
    Urls urls;
    RelativeLayout bottomDetails,bottomCart;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        urls = new Urls();

        whoSentOrder = getIntent().getStringExtra("type");
        houseID = getIntent().getStringExtra("houseID");

        scrollHouseDetails = findViewById(R.id.scrollHouseDetails);
        error_message_balance = findViewById(R.id.error_message_balance);
        errorTxt = findViewById(R.id.errorTxt);
        bottomDetails = findViewById(R.id.bottomDetails);
        bottomCart = findViewById(R.id.bottomCart);


        /*going to use whosentorder to know which options to show the right options*/

        if ("Details".equals(whoSentOrder)) {
            bottomDetails.setVisibility(View.VISIBLE);
        } else {
            bottomCart.setVisibility(View.VISIBLE);
        }

        /*add the deleting option the cart options*/

        loadHouseDetails(houseID);
    }

    public void goBack(View view) {
        if ("Details".equals(whoSentOrder)) {
            startActivity(new Intent(HouseDetails.this, HouseList.class));
            finish();
        } else {
            startActivity(new Intent(HouseDetails.this, HouseCart.class));
            finish();
        }
    }

    public void openPlaceLocation(View view) {
        /*get the users coordinates and also the house's coordinates and then pass them */
        String userCoordinates = "2";
        String houseCoordinates = "";
        Intent cart = new Intent(HouseDetails.this, PlaceLocation.class);
        cart.putExtra("userCoordinates", userCoordinates);
        cart.putExtra("houseCoordinates", houseCoordinates);
        startActivity(cart);
    }

    /*load house details using the sent id*/
    private void loadHouseDetails(String selectedHouse) {
        final ProgressDialog progressDialog = new ProgressDialog(HouseDetails.this);
        progressDialog.setMessage("Loading results, please wait....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.LOAD_EXPENSES_RESULTS,
                response -> {
                    progressDialog.dismiss();
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);
                        if (tips.length() == 0) {
                            error_message_balance.setVisibility(View.VISIBLE);
                            scrollHouseDetails.setVisibility(View.GONE);
                        } else {
                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject inputsObjects = tips.getJSONObject(i);
                                scrollHouseDetails.setVisibility(View.VISIBLE);
                                String id = inputsObjects.getString("id");
                                String type = inputsObjects.getString("type");
                                String amount = inputsObjects.getString("amount");// for home consumption
                                String number = inputsObjects.getString("number");// for diary consumption
                                String employeename = inputsObjects.getString("employeename");// for diary consumption
                                String taskdone = inputsObjects.getString("taskdone");// for diary consumption
                                String date = inputsObjects.getString("date");
                                String froms = inputsObjects.getString("froms");

                            }

                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        scrollHouseDetails.setVisibility(View.GONE);
                        error_message_balance.setVisibility(View.VISIBLE);
                        errorTxt.setText(e.toString());
                        // Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            progressDialog.dismiss();
            scrollHouseDetails.setVisibility(View.GONE);
            error_message_balance.setVisibility(View.VISIBLE);
            //Toast.makeText(this, "Something went wrong, check your connection and try again please try again", Toast.LENGTH_SHORT).show();

        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("selected", selectedHouse);
//                params.put("userid", getID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}