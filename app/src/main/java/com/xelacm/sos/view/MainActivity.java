package com.xelacm.sos.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.xelacm.sos.R;
import com.xelacm.sos.adapters.GpsTracker;
import com.xelacm.sos.adapters.MenuAdapter;
import com.xelacm.sos.adapters.RecyclerItemClickListener;
import com.xelacm.sos.models.MenuModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity  {

    RecyclerView recyclerview;
    RequestQueue queue;
    private GpsTracker gpsTracker;
    private FusedLocationProviderClient client;
    String apiUrl = "http://header.safaricombeats.co.ke/dxl/";
    private static final String TAG = "Debug";
    URL ImageUrl = null;
    InputStream is = null;
    Bitmap bmImg = null;
    ImageView imageView= null;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = findViewById(R.id.rec);






        MenuModel[] myListData = new MenuModel[]{
                new MenuModel("Airtime Top up", android.R.drawable.ic_menu_call),
                new MenuModel("Data Calls SMS & Airtime", android.R.drawable.ic_menu_call),
                new MenuModel("Tunukiwa Offers", android.R.drawable.ic_input_add),
                new MenuModel("Ask Zuri", android.R.drawable.ic_dialog_dialer),
                new MenuModel("Send Money", android.R.drawable.ic_dialog_alert),
                new MenuModel("Lipa Na M-PESA", android.R.drawable.ic_dialog_map),
                new MenuModel("Send Money", android.R.drawable.ic_dialog_alert),
                new MenuModel("SOS CALL", android.R.drawable.ic_menu_call),

        };


        MenuAdapter adapter = new MenuAdapter(myListData);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerview.setAdapter(adapter);
        recyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, recyclerview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (position == 0) {
                            // create object of MyAsyncTasks class and execute it
                           GetMsisdn();

                        } else {
                            Toast.makeText(MainActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    public void GetMsisdn() {

       if (getLocation()==1){
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String msisdn = null;
                if (!response.isEmpty()) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response);
                        Log.d("##MSISDN", obj.toString());
                        msisdn = obj.getJSONObject("ServiceResponse").getJSONObject("ResponseBody").getJSONObject("Response").getString("Msisdn");
                        goToUrl("http://192.168.43.82:3000/");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Error getting MSISDN. Try again", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("error", error.toString());
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
        // display a progress dialog for good user experiance
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    }

    public int getLocation(){
        int ret = 0;
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            Log.d("########Location","Lat: "+latitude+"   Lng:"+longitude);
            ret = 1;
        }else{
            gpsTracker.showSettingsAlert();
            ret = 0;
        }
        return ret;
    }

    private void goToUrl (String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", url);
        progressDialog.dismiss();
        startActivity(intent);
    }

}