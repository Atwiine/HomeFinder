package com.example.homefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.homefinder.Adapter.HousesListAdapter;
import com.example.homefinder.Modal.HousesListModel;
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
    TextView errorTxt;



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

/*load houses*/

        recyclerView = findViewById(R.id.recyclerview_houses_list);
        mData = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HousesListAdapter(this, mData);
        recyclerView.setAdapter(adapter);

        loadHouses();
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
            //Toast.makeText(this, "Something went wrong, check your connection and try again please try again", Toast.LENGTH_SHORT).show();

        });
        Volley.newRequestQueue(HouseList.this).add(stringRequest);
    }


}