package com.example.homefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.homefinder.Adapter.HousesListAdapter;
import com.example.homefinder.Modal.HousesListModel;
import com.example.homefinder.Register.Login;
import com.example.homefinder.Urls.SessionManager;
import com.example.homefinder.Urls.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseList extends AppCompatActivity {
    Urls urls;
    RecyclerView recyclerView;
    List<HousesListModel> mData;
    HousesListAdapter adapter;
    NestedScrollView scrollHouseDetails;
    LinearLayout error_message_balance,no_message_balance;
    TextView errorTxt,totalCart;
    EditText search;
    String countCart = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_list);

        urls = new Urls();


        scrollHouseDetails = findViewById(R.id.scrollHouseDetails);
        error_message_balance = findViewById(R.id.error_message_balance);
        errorTxt = findViewById(R.id.errorTxt);
        no_message_balance = findViewById(R.id.no_message_balance);
        totalCart = findViewById(R.id.totalCart); // getting the total number in the cart

        /*searching option*/
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setFocusableInTouchMode(true);
                search.setFocusable(true);
            }
        });
        search.setFocusableInTouchMode(false);
        search.setFocusable(false);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

/*load houses*/

        recyclerView = findViewById(R.id.recyclerview_houses_list);
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HousesListAdapter(this, mData);
        recyclerView.setAdapter(adapter);

        loadHouses();
        /*count cart*/
        checkCart();
    }

    public void openDetails(View view) {
        /*get the house id to send the details part*/

    }




    private void loadHouses() {
        final ProgressDialog progressDialog = new ProgressDialog(HouseList.this);
        progressDialog.setMessage("Loading results, please wait....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.LOAD_HOUSES,
                response -> {
                    progressDialog.dismiss();
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);
                        if (tips.length() == 0) {
                            no_message_balance.setVisibility(View.VISIBLE);
                            scrollHouseDetails.setVisibility(View.GONE);
                        } else {

                            scrollHouseDetails.setVisibility(View.VISIBLE);

                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject inputsObjects = tips.getJSONObject(i);

                                String housename = inputsObjects.getString("housename");
                                String houseprice = inputsObjects.getString("houseprice");
                                String houseCoordinatesLat = inputsObjects.getString("houseCoordinatesLat");
                                String houseCoordinatesLong =  inputsObjects.getString("houseCoordinatesLong");
                                String houseid = inputsObjects.getString("houseid");
                                String houseLocation = inputsObjects.getString("houseLocation");
                                String housebedrooms = inputsObjects.getString("housebedrooms");
                                String housebathrooms = inputsObjects.getString("housebathrooms");
                                String houseDescription = inputsObjects.getString("houseDescription");
                                String houseImage = inputsObjects.getString("houseImage");

                                String houseCoordinates = houseCoordinatesLat + houseCoordinatesLong;

                                /*get the total*/
//                                totalCart.setText(tips.length());

                                HousesListModel inputsModel =
                                        new HousesListModel(housename, houseprice, houseCoordinates, houseid,
                                                houseLocation, housebedrooms, housebathrooms,houseDescription,houseImage
                                        );
                                mData.add(inputsModel);
                            }
                            adapter = new HousesListAdapter(HouseList.this, mData);
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
            errorTxt.setText(error.toString());

            //Toast.makeText(this, "Something went wrong, check your connection and try again please try again", Toast.LENGTH_SHORT).show();

        });
        Volley.newRequestQueue(HouseList.this).add(stringRequest);
    }


    private void filter(String text) {
        ArrayList<HousesListModel> filteredList = new ArrayList<>();

        for (HousesListModel item : mData) {
            if (item.getHouseLocation().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
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
                                totalCart.setText(countCart);
//                                Toast.makeText(HouseDetails.this, "asd " + countCart, Toast.LENGTH_SHORT).show();
                            }

                        } else if (success.equals("0")) {
                            countCart = "0";
                            totalCart.setText(countCart);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        countCart = "";
                        totalCart.setText(countCart);
//                        Toast.makeText(this, "asdaaaaa" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            countCart = " ";
            totalCart.setText(countCart);
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


    public void openCart(View view) {
        String order = "List";
        Intent cart = new Intent(HouseList.this, HouseCart.class);
        cart.putExtra("whoSentOrder", order);
        startActivity(cart);

    }

    public void openMore(View view) {
        startActivity(new Intent(HouseList.this,MoreHouseList.class));
    }
}