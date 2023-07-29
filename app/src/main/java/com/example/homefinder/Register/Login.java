package com.example.homefinder.Register;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {
    EditText username,password;
    Urls urls;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = findViewById(R.id.password);
        sessionManager = new SessionManager(this);
        urls = new Urls();
    }

    public void openRegister(View view) {
        startActivity(new Intent(Login.this,Register.class));
    }

    public void openMain(View view) {
        String pass = password.getText().toString().trim();

        if (!pass.isEmpty()) {
            Logins(pass);

        } else {
            password.setError("Enter phone number");
        }
    }


    private void Logins(final String password) {

        final ProgressDialog loading = new ProgressDialog(Login.this);
        loading.setMessage("logging in please wait...");
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.LOGIN_URL,
                response -> {
                    Log.i("tagconvertstr", "[" + response + "]");
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONObject jsonObject = new JSONObject(response);
                        @SuppressLint("NotConstructor") String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");
                        if (success.equals("1")) {
                            Toast.makeText(Login.this, "login success", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.i("tagconvertstr", "[" + response + "]");
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("userid");
                                String fullname = object.getString("fullnames");
                                String password1 = "0"+object.getString("phone");

                                sessionManager.createSession( password1,fullname, id);
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.putExtra("username", fullname);
                                    startActivity(intent);
                                    finish();

                                }
                                loading.dismiss();

                        } else if (success.equals("0")) {
                            loading.dismiss();
//                            login.setVisibility(View.VISIBLE);
                            Toast.makeText(Login.this, "login was error account not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading.dismiss();
//                        login.setVisibility(View.VISIBLE);
                        Toast.makeText(Login.this, "failed to login, please try again ", Toast.LENGTH_LONG).show();
                    }
                },

                error -> {
                    loading.dismiss();
//                    login.setVisibility(View.VISIBLE);
                    Toast.makeText(Login.this, "login error please check your internet connection and try again ", Toast.LENGTH_SHORT).show();
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}