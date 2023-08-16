package com.example.homefinder.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.homefinder.HouseList;
import com.example.homefinder.Modal.HousesListModel;
import com.example.homefinder.R;
import com.example.homefinder.Urls.Urls;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousesListAdapter extends RecyclerView.Adapter<HousesListAdapter.HouseViewHolder> implements Filterable {
    Context context;
    public static List<HousesListModel> mData;
    Urls urls;
    List<HousesListModel> markets_filter;

    private final Filter examplefilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HousesListModel> filterexample = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterexample.addAll(markets_filter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (HousesListModel marketsModel : markets_filter) {
                    if (marketsModel.getHouseLocation().toLowerCase().contains(filterPattern)) {
                        filterexample.add(marketsModel);
                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = filterexample;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mData.clear();
            mData.addAll((Collection<? extends HousesListModel>) results.values);
            notifyDataSetChanged();
        }
    };


    public HousesListAdapter(Context context, List<HousesListModel> mData) {
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

        String imageUrl = urls.https + "admin/assets/uploads/rentals/" + housesListModel.getHouseImage();
        try {

            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.houseImage);
        } catch (Exception e) {
            e.printStackTrace();
        }




    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    public void filterList(ArrayList<HousesListModel> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public  class HouseViewHolder extends RecyclerView.ViewHolder {

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
//            addCart = itemView.findViewById(R.id.addCart);
            houseImage = itemView.findViewById(R.id.houseImage);
            card_house = itemView.findViewById(R.id.card_house);


            /*onclick on the card*/
            card_house.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hId = houseid.getText().toString();
                    String order = "Details";
                    Intent cart = new Intent(context, HouseDetails.class);
                    cart.putExtra("whoSentOrder",order);
                    cart.putExtra("houseID",hId);
                    cart.putExtra("housename", mData.get(getAdapterPosition()).getHousename());
                    cart.putExtra("houseprice", mData.get(getAdapterPosition()).getHouseprice());
                    cart.putExtra("houseCoordinates", mData.get(getAdapterPosition()).getHouseCoordinates());
                    cart.putExtra("houseid", mData.get(getAdapterPosition()).getHouseid());
                    cart.putExtra("houseLocation", mData.get(getAdapterPosition()).getHouseLocation());
                    cart.putExtra("housebedrooms", mData.get(getAdapterPosition()).getHousebedrooms());
                    cart.putExtra("housebathrooms", mData.get(getAdapterPosition()).getHousebathrooms());
                    cart.putExtra("houseDescription", mData.get(getAdapterPosition()).getHouseDescription());
                    cart.putExtra("image_url", urls.https + "admin/assets/uploads/rentals/" + mData.get(getAdapterPosition()).getHouseImage());
                    context.startActivity(cart);
                }
            });

        }
    }

}
