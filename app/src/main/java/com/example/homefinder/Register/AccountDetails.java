package com.example.homefinder.Register;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.homefinder.MainActivity;
import com.example.homefinder.R;
import com.example.homefinder.Urls.SessionManager;
import com.example.homefinder.Urls.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountDetails extends AppCompatActivity {

    EditText username, password;
    SessionManager sessionManager;
    Urls urls;
    String getID, farmname, dd;
    TextView userid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountdetails);
        urls = new Urls();
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(SessionManager.ID);


        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        userid = findViewById(R.id.userid);

        /*load the user profile*/
        loadProfile(getID);
    }

    public void openLogin(View view) {
        startActivity(new Intent(AccountDetails.this, Login.class));
    }

    public void update(View view) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                AccountDetails.this);
        alertDialog2.setTitle("Confirm to proceed to update profile");
        alertDialog2.setMessage("Make sure you double check your details");
        alertDialog2.setPositiveButton("YES",
                (dialog, which) -> {
                    update();
                });
        alertDialog2.setNegativeButton("NO",
                (dialog, which) -> dialog.cancel());
        alertDialog2.show();

    }


    private void loadProfile(final String userids) {

        final ProgressDialog loading = new ProgressDialog(AccountDetails.this);
        loading.setMessage("Fetching user profile please wait...");
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.LOAD_USER_PROFILE_URL,
                response -> {
                    Log.i("tagconvertstr", "[" + response + "]");
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject jsonObject = new JSONObject(response);
                        @SuppressLint("NotConstructor") String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");
                        if (success.equals("1")) {
                            Toast.makeText(AccountDetails.this, "Profile details loaded successfully", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.i("tagconvertstr", "[" + response + "]");
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("userid");
                                String fullname = object.getString("fullnames");
                                String password1 = "0"+object.getString("phone");
                                username.setText(fullname);
                                password.setText(password1);
                                userid.setText(id);
                            }
                            loading.dismiss();

                        } else if (success.equals("0")) {
                            loading.dismiss();
//                            login.setVisibility(View.VISIBLE);
                            Toast.makeText(AccountDetails.this, "Error account not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading.dismiss();
//                        login.setVisibility(View.VISIBLE);
                        Toast.makeText(AccountDetails.this, "failed to fetch user profile, please try again ", Toast.LENGTH_LONG).show();
                    }
                },

                error -> {
                    loading.dismiss();
//                    login.setVisibility(View.VISIBLE);
                    Toast.makeText(AccountDetails.this, "failed to fetch user profile please check your internet connection and try again ", Toast.LENGTH_SHORT).show();
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", userids);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void update() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait ...");
        dialog.show();

        final String full_names = this.username.getText().toString().trim();
        final String pass = this.password.getText().toString().trim();
        String userids = userid.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATE_USER_PROFILE_URL,
                response -> {
                    try {

                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

                            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                    AccountDetails.this);
                            alertDialog2.setTitle("Profile updated successfully");
                            alertDialog2.setMessage("You are about to be logged out, please log in again with your logged profile");
                            alertDialog2.setPositiveButton("YES",
                                    (dialogs, which) -> {
                                        sessionManager.logout();
                                    });
                            alertDialog2.show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Profile was not updated unsuccessfully, please try again", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Profile was not updated unsuccessfully, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", full_names);
                params.put("password", pass);
                params.put("userid", userids);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void goBack(View view) {
        startActivity(new Intent(AccountDetails.this, MainActivity.class));
        finish();
    }
}