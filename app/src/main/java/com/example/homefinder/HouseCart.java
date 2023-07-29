package com.example.homefinder;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.homefinder.Adapter.HousesCartAdapter;
import com.example.homefinder.Adapter.HousesListAdapter;
import com.example.homefinder.Modal.HousesListModel;
import com.example.homefinder.Urls.SessionManager;
import com.example.homefinder.Urls.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseCart extends AppCompatActivity {
    Urls urls;
    NestedScrollView scrollHouseDetails;
    LinearLayout error_message_balance,no_message_balance;
    TextView errorTxt,totalCart;
    SessionManager sessionManager;
    String getID;
    RecyclerView recyclerView;
    List<HousesListModel> mData;
    HousesCartAdapter adapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_cart);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);

        urls = new Urls();
        scrollHouseDetails = findViewById(R.id.scrollHouseDetails);
        error_message_balance = findViewById(R.id.error_message_balance);
        errorTxt = findViewById(R.id.errorTxt);
        no_message_balance = findViewById(R.id.no_message_balance);
        totalCart = findViewById(R.id.totalCart); // getting the total number in the cart

        /*load cart houses*/

        recyclerView = findViewById(R.id.recyclerview_houses_cart);
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HousesCartAdapter(this, mData);
        recyclerView.setAdapter(adapter);

        loadHouseCart(getID);
    }

    public void openDetails(View view) {
        /*get the house id to send the details part*/
        String hId = "2";
        String order = "Cart";
        Intent cart = new Intent(HouseCart.this,HouseDetails.class);
        cart.putExtra("whoSentOrder",order);
        cart.putExtra("houseID",hId);
        startActivity(cart);
    }

    public void goBack(View view) {
/*figure out who last accessed cart options*/

    }

    private void loadHouseCart(String selected) {
        final ProgressDialog progressDialog = new ProgressDialog(HouseCart.this);
        progressDialog.setMessage("Loading results, please wait....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.LOAD_HOUSES_CART,
                response -> {
                    progressDialog.dismiss();
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);
                        if (tips.length() == 0) {
                            no_message_balance.setVisibility(View.VISIBLE);
                            scrollHouseDetails.setVisibility(View.GONE);
                        } else {
                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject inputsObjects = tips.getJSONObject(i);

                                String housename = inputsObjects.getString("housename");
                                String houseprice = inputsObjects.getString("houseprice");
                                String houseCoordinates = inputsObjects.getString("houseCoordinates");
                                String houseid = inputsObjects.getString("houseid");
                                String houseLocation = inputsObjects.getString("houseLocation");
                                String housebedrooms = inputsObjects.getString("housebedrooms");
                                String housebathrooms = inputsObjects.getString("housebathrooms");
                                String houseDescription = inputsObjects.getString("houseDescription");
                                String houseImage = inputsObjects.getString("houseImage");

                                HousesListModel inputsModel =
                                        new HousesListModel(housename, houseprice, houseCoordinates, houseid,
                                                houseLocation, housebedrooms, housebathrooms,houseDescription,houseImage
                                        );
                                mData.add(inputsModel);
                            }
                            adapter = new HousesCartAdapter(HouseCart.this, mData);
                            recyclerView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        error_message_balance.setVisibility(View.VISIBLE);
                        errorTxt.setText(e.toString());
                        // Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            progressDialog.dismiss();
            error_message_balance.setVisibility(View.VISIBLE);
            //Toast.makeText(this, "Something went wrong, check your connection and try again please try again", Toast.LENGTH_SHORT).show();

        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("houseid", getID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}