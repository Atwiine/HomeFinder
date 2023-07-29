package com.example.homefinder.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.homefinder.HouseDetails;
import com.example.homefinder.Modal.HousesListModel;
import com.example.homefinder.R;
import com.example.homefinder.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousesCartAdapter extends RecyclerView.Adapter<HousesCartAdapter.HouseViewHolder> {
    Context context;
    public static List<HousesListModel> mData;
    Urls urls;
    //    SessionManager sessionManager;
    String getTYPE;
    String getId, idPost, teacher;

    public HousesCartAdapter(Context context, List<HousesListModel> mData) {
        this.context = context;
        this.mData = mData;
        urls = new Urls();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_house_card, null, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, final int position) {



        HousesListModel housesListModel = mData.get(position);
        holder.housename.setText(mData.get(position).getHousename());
        holder.houseprice.setText(mData.get(position).getHouseprice());
        holder.houseCoordinates.setText(mData.get(position).getHouseCoordinates());
        holder.houseid.setText(mData.get(position).getHouseid());
        holder.houseLocation.setText(mData.get(position).getHouseLocation());
        holder.housebedrooms.setText(mData.get(position).getHousebedrooms());
        holder.housebathrooms.setText(mData.get(position).getHousebathrooms());
        holder.houseDescription.setText(mData.get(position).getHouseDescription());

        String imageUrl = urls.https + "house_images/" + housesListModel.getHouseImage();
        try {

            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.houseImage);
        } catch (Exception e) {
            e.printStackTrace();
        }



        /*adding house in cart OPTIONS*/
        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String hid = holder.houseid.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.ADD_HOUSE_CART,
                        response -> {
                            try {
                                Log.i("tagconvertstr", "[" + response + "]");
                                JSONObject object = new JSONObject(response);
                                String success = object.getString("success");
                                if (success.equals("1")) {
                                    Log.i("tagconvertstr", "[" + response + "]");

                                    Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Not added to cart, please try again " + e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }, error -> {
                    Toast.makeText(context, "Not added to cart, please check your network and try again", Toast.LENGTH_LONG).show();

                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", hid);
                        return params;

                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }

        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void clear() {
        int size = mData.size();
        if (size > 0) {
            mData.subList(0, size).clear();

            notifyItemRangeRemoved(0, size);
        }
    }

    public class HouseViewHolder extends RecyclerView.ViewHolder {

        TextView housename, houseprice, houseCoordinates, houseid, houseLocation, housebedrooms, housebathrooms,houseDescription;
        RelativeLayout addCart;
   ImageView houseImage;
        MaterialCardView card_house;

        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);
            housename = itemView.findViewById(R.id.housename);
            houseprice = itemView.findViewById(R.id.houseprice);
            houseCoordinates = itemView.findViewById(R.id.houseCoordinates);
            houseid = itemView.findViewById(R.id.houseid);
            houseLocation = itemView.findViewById(R.id.houseLocation);
            housebedrooms = itemView.findViewById(R.id.housebedrooms);
            housebathrooms = itemView.findViewById(R.id.housebathrooms);
            houseDescription = itemView.findViewById(R.id.houseDescription);
            addCart = itemView.findViewById(R.id.addCart);
            houseImage = itemView.findViewById(R.id.houseImage);
            card_house = itemView.findViewById(R.id.card_house);

            /*onclick on the card*/
            card_house.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hId = houseid.getText().toString();
                    String order = "Cart";
                    Intent cart = new Intent(context, HouseDetails.class);
                    cart.putExtra("whoSentOrder",order);
                    cart.putExtra("houseID",hId);
                    context.startActivity(cart);
                }
            });

        }
    }
}
