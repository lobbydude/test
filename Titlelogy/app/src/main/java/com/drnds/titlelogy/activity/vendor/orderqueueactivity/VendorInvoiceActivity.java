package com.drnds.titlelogy.activity.vendor.orderqueueactivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.gridactivity.GridInvoiceActivity;
import com.drnds.titlelogy.activity.client.orderqueueactivity.EditOrderActivity;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.drnds.titlelogy.adapter.client.RecyclerOrderQueueAdapter.ORDERQUEUE;

public class VendorInvoiceActivity extends AppCompatActivity {


    private EditText inputordercost, inputsearchcost, inputcopycost, inputnoofpages, inputinvoicedate;
    Button submit, upload;
    SharedPreferences sp, pref;
    private String Order_Id;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_invoice);


        inputordercost = (EditText) findViewById(R.id.input_venordercost);
        inputsearchcost = (EditText) findViewById(R.id.input_vensearchcost);
        inputcopycost = (EditText) findViewById(R.id.input_vencopycost);
        inputnoofpages = (EditText) findViewById(R.id.input_vennoofpages);
        inputinvoicedate = (EditText) findViewById(R.id.input_veninvoicedate);

        submit = (Button) findViewById(R.id.button_venorderinvoicesubmit);
        checkInternetConnection();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sp = getApplicationContext().getSharedPreferences(
                ORDERQUEUE, 0);

        Order_Id = sp.getString("Order_Id", "");

        getOrderdetails();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();   // checking internet connection

                checkInternetConnection();


                Submit();

            }
        });


    }


    public String getorderID() {


        return sp.getString("Order_Id", "");
    }


    public String getClientId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Client_Id", "");


    }


    public String getVendorUserId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Vendor_User_Id", "");


    }

    public String getSubprocessId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Sub_Client_Id", "");


    }


    public void  getOrderdetails() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.INVOICE_URL +getorderID(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("View_Invoice_Details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        String Order_Cost=details.getString("Order_Cost");
                        inputordercost.setText(Order_Cost);
                        String Search_Cost=details.getString("Search_Cost");
                        inputsearchcost.setText(Search_Cost);
                        String Copy_Cost=details.getString("Copy_Cost");
                        inputcopycost.setText(Copy_Cost);
                        String No_Of_Pages=details.getString("No_Of_Pages");
                        inputnoofpages.setText(No_Of_Pages);
                        String Invoice_Date=details.getString("Invoice_Date");
                        inputinvoicedate.setText(Invoice_Date);
                        Logger.getInstance().Log("set Order cost " + Order_Cost);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }




    public void  Submit(){
        showDialog();
        final String Order_Cost = inputordercost.getText().toString().trim();
        final String  Search_Cost = inputsearchcost.getText().toString().trim();
        final String  Copy_Cost = inputcopycost.getText().toString().trim();
        final String  No_Of_Pages = inputnoofpages.getText().toString().trim();
        final String  Invoice_Date = inputinvoicedate.getText().toString().trim();

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.INVOICE_EDIT_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();

                Logger.getInstance().Log("sucess string");
                try {

                    boolean  error = response.getBoolean("error");

                    Logger.getInstance().Log("in error response"+error);
                    // Check for error node in json
                    if (!error)
                    {
                        Toasty.success(VendorInvoiceActivity.this.getApplicationContext(),"Updated  Sucessfully...", Toast.LENGTH_SHORT).show();
                        hideDialog();

                        onBackPressed();
                    }else {
                        Toasty.success(VendorInvoiceActivity.this.getApplicationContext(),"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Order_Id",getorderID());
                params.put("Clinet_Id",getClientId());
//                Logger.getInstance().Log("Id .... is"+getClientId());
                params.put("Order_Cost",Order_Cost);
                params.put("Search_Cost",Search_Cost);
                params.put("Copy_Cost",Copy_Cost);
                params.put("No_Of_Pages",No_Of_Pages);
                params.put("Invoice_Date",Invoice_Date);
                params.put("Vendor_User_Id",getVendorUserId());
                params.put("Subprocess_ID",getSubprocessId());
                Logger.getInstance().Log("Id .... is"+getVendorUserId());
                Logger.getInstance().Log("Id .... is"+getSubprocessId());





                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(customRequest);
    }


    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            TastyToast.makeText( VendorInvoiceActivity.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
            return false;
        }
        return false;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
