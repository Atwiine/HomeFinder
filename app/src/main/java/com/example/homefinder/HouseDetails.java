package com.example.homefinder;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.homefinder.Urls.SessionManager;
import com.example.homefinder.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

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
    RelativeLayout bottomDetails,bottomCart,bottomRent;
    ImageView image_house;
    TextView txtheading, txtlocation ,txtbedrooms ,txtbathrooms,
            houseDescription,houseCoordinates,txtpricecart,houseprice,txtprice,txtpricerent;
    String imgurl;
    MaterialCardView card_cart,card_open_cart,card_open_rent;
    SessionManager sessionManager;
    String getID;
    String countCart = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);

        /*count cart*/
        checkCart();

        scrollHouseDetails = findViewById(R.id.scrollHouseDetails);
        error_message_balance = findViewById(R.id.error_message_balance);
        errorTxt = findViewById(R.id.errorTxt);
        bottomDetails = findViewById(R.id.bottomDetails);
        bottomCart = findViewById(R.id.bottomCart);
        txtheading = findViewById(R.id.txtheading);
        txtlocation = findViewById(R.id.txtlocation);
        txtbedrooms = findViewById(R.id.txtbedrooms);
        txtbathrooms = findViewById(R.id.txtbathrooms);
        houseDescription = findViewById(R.id.houseDescription);
        image_house = findViewById(R.id.image_house);
        houseCoordinates = findViewById(R.id.houseCoordinates);
        houseprice = findViewById(R.id.houseprice);
        txtprice = findViewById(R.id.txtprice);
        txtpricecart = findViewById(R.id.txtpricecart);
        card_cart = findViewById(R.id.card_cart);
        card_open_cart = findViewById(R.id.card_open_cart);
        txtpricerent = findViewById(R.id.txtpricerent);
        card_open_rent = findViewById(R.id.card_open_rent);
        bottomRent = findViewById(R.id.bottomRent);


        whoSentOrder = getIntent().getStringExtra("whoSentOrder");
        houseID = getIntent().getStringExtra("houseID");
        txtprice.setText(getIntent().getStringExtra("houseprice"));
        txtpricecart.setText(getIntent().getStringExtra("houseprice"));
        txtpricerent.setText(getIntent().getStringExtra("houseprice"));
        houseprice.setText(getIntent().getStringExtra("houseprice"));
        houseCoordinates.setText(getIntent().getStringExtra("houseCoordinates"));
        txtlocation.setText(getIntent().getStringExtra("houseLocation"));
        txtbedrooms.setText(getIntent().getStringExtra("housebedrooms"));
        txtbathrooms.setText(getIntent().getStringExtra("housebathrooms"));
        houseDescription.setText(getIntent().getStringExtra("houseDescription"));
        txtheading.setText(getIntent().getStringExtra("housename"));

        imgurl = getIntent().getStringExtra("image_url");

        Glide.with(this)
                .load(imgurl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
                .into(image_house);



        /*going to use whosentorder to know which options to show the right options*/

        if (whoSentOrder.equals("Details")) {
            /*checking the cart to see if the house has already been added in and then show the go to cart option*/
            checkCartForHouse(houseID,getID);

            scrollHouseDetails.setVisibility(View.VISIBLE);
            bottomDetails.setVisibility(View.VISIBLE); // adding to cart
            bottomCart.setVisibility(View.GONE);
        } else {
            scrollHouseDetails.setVisibility(View.VISIBLE);
            bottomRent.setVisibility(View.VISIBLE); // going to the cart page
            bottomDetails.setVisibility(View.GONE);
        }

        /*adding house in cart OPTIONS*/

        card_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(HouseDetails.this, "asd " + countCart, Toast.LENGTH_SHORT).show();
//
//                /*check if the cart is full or not*/
                if (countCart.isEmpty() || countCart.equals("null") ){
                    Toast.makeText(HouseDetails.this, "something went , please try again.", Toast.LENGTH_SHORT).show();
                }else  if (countCart.equals("0") || countCart.equals("1")) {
                    final String hid = houseID;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.ADD_HOUSE_CART,
                            response -> {
                                try {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    JSONObject object = new JSONObject(response);
                                    String success = object.getString("success");
                                    if (success.equals("1")) {
                                        Log.i("tagconvertstr", "[" + response + "]");

                                        Toast.makeText(HouseDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                        bottomCart.setVisibility(View.VISIBLE); // going to the cart page
                                        bottomDetails.setVisibility(View.GONE);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(HouseDetails.this, "Not added to cart, please try again " + e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }, error -> {
                        Toast.makeText(HouseDetails.this, "Not added to cart, please check your network and try again"
                                + error.toString(), Toast.LENGTH_LONG).show();

                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("houseid", hid);
                            params.put("userid", "1");
                            return params;

                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(HouseDetails.this);
                    requestQueue.add(stringRequest);
                }
                else  if (countCart.equals("2")) {
                    Toast.makeText(HouseDetails.this, "Cart is full, proceed to see whats in your cart", Toast.LENGTH_SHORT).show();
                    bottomCart.setVisibility(View.VISIBLE); // going to the cart page
                    bottomDetails.setVisibility(View.GONE);
                }


            }

        });

        /*opening the cart options */
        card_open_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order = "Details";
                Intent cart = new Intent(HouseDetails.this, HouseCart.class);
                cart.putExtra("whoSentOrder", order);
                startActivity(cart);

//                startActivity(new Intent(HouseDetails.this,HouseCart.class));
            }
        });

        /* calling to book*/
        card_open_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:0754840755"));
                try {
                    startActivity(callIntent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });


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
    private void checkCartForHouse(String selectedHouse,String user) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.CHECK_CART,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);
                        if (tips.length() == 0) {
                            bottomCart.setVisibility(View.GONE);
                            scrollHouseDetails.setVisibility(View.VISIBLE);
                            bottomDetails.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject inputsObjects = tips.getJSONObject(i);
                                String carthouseid = inputsObjects.getString("houseid");

                                /**
                                 * check if the houseid from the cart is the same as the house that
                                 * has been seleced and if so, hide the add to cart option and
                                 * show the option to go to cart*/

                                    if (carthouseid.equals(houseID)) {
                                        bottomDetails.setVisibility(View.GONE);
                                        bottomCart.setVisibility(View.VISIBLE);
                                        scrollHouseDetails.setVisibility(View.VISIBLE);
                                    } else {
                                        bottomCart.setVisibility(View.GONE);
                                        scrollHouseDetails.setVisibility(View.VISIBLE);
                                        bottomDetails.setVisibility(View.VISIBLE);
                                    }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        error_message_balance.setVisibility(View.VISIBLE);
                        errorTxt.setText(e.toString());
                        // Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            error_message_balance.setVisibility(View.VISIBLE);
            errorTxt.setText(error.toString());
            //Toast.makeText(this, "Something went wrong, check your connection and try again please try again", Toast.LENGTH_SHORT).show();

        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("houseid", selectedHouse);
                params.put("userid", "1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*checking if the cart has a max of two houses*/
    private void checkCart() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.COUNT_CART,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject jsonObject = new JSONObject(response);
                        @SuppressLint("NotConstructor") String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");
                        if (success.equals("1")) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.i("tagconvertstr", "[" + response + "]");
                                JSONObject object = jsonArray.getJSONObject(i);

                                countCart =  object.getString("value_sum");
//                                Toast.makeText(HouseDetails.this, "asd " + countCart, Toast.LENGTH_SHORT).show();
                            }

                        } else if (success.equals("0")) {
                            countCart = "0";
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        countCart = "";
//                        Toast.makeText(this, "asdaaaaa" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            countCart = " ";
//            Toast.makeText(this, "asdaaaaa5555", Toast.LENGTH_SHORT).show();

        }){
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", "1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}