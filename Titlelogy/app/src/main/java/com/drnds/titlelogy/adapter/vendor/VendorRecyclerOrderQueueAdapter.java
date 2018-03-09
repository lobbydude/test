package com.drnds.titlelogy.adapter.vendor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.NavigationActivity;
import com.drnds.titlelogy.activity.vendor.VendorNavigationActivity;
import com.drnds.titlelogy.activity.vendor.orderqueueactivity.EditOrderVendorActivity;
import com.drnds.titlelogy.adapter.client.RecyclerOrderQueueAdapter;
import com.drnds.titlelogy.model.client.OrderQueue;
import com.drnds.titlelogy.model.vendor.VendorOrderQueue;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import es.dmoral.toasty.Toasty;

import static com.drnds.titlelogy.util.AppController.TAG;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorRecyclerOrderQueueAdapter extends RecyclerView.Adapter<VendorRecyclerOrderQueueAdapter.MyViewHolder>implements Filterable {

    private List<VendorOrderQueue> vendorList;
    private List<VendorOrderQueue> mFilteredList;
    private Activity activity;
    private Context ctx;
    public static final String VENDORORDER = "Vendor";
    private int lastPosition = -1;
    private ProgressDialog pDialog;
    String orderid;

    public SharedPreferences sp;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subclient, orderno, status, producttype, borrowername, statename, countyname, propertyaddress,date;
        private List<VendorOrderQueue> vendorList = new ArrayList<VendorOrderQueue>();
        Context ctx;
        private  ItemClickListener itemClickListener;
        public ImageView cardmenu;

        public MyViewHolder(View view,final Context ctx,final List<VendorOrderQueue> vendorList) {
            super(view);
            this.vendorList = vendorList;
            this.ctx=ctx;

            date = (TextView) view.findViewById(R.id.text_vdate);
            subclient = (TextView) view.findViewById(R.id.vendor_text_subclient);
            orderno = (TextView) view.findViewById(R.id.vendor_text_orderno);
            status = (TextView) view.findViewById(R.id.vendor_status);
            producttype = (TextView) view.findViewById(R.id.vendor_producttype);
            borrowername = (TextView) view.findViewById(R.id.vendor_borrowername);
            statename = (TextView) view.findViewById(R.id.vendor_statename);
            countyname = (TextView) view.findViewById(R.id.vendor_countyname);
            propertyaddress = (TextView) view.findViewById(R.id.vendor_property_address);
            cardmenu=(ImageView)view.findViewById(R.id.vendorcardmenu);


            subclient.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            orderno.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            producttype.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            statename.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            countyname.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            propertyaddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            status.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            borrowername.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            view.setOnClickListener(this);


            cardmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    VendorOrderQueue vendororderqueue=vendorList.get(position);
                    orderid=vendororderqueue.getOrder_Id();
                    showPopupMenu(cardmenu,getAdapterPosition());
                    Logger.getInstance().Log("selected order id is : " + vendororderqueue.getOrder_Id());


                }
            });


            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position=getAdapterPosition();
                    VendorOrderQueue vendororderqueue=vendorList.get(position);
                    orderid=vendororderqueue.getOrder_Id();
                    showPopupMenu(cardmenu,getAdapterPosition());
                    Logger.getInstance().Log("selected order id is : " + vendororderqueue.getOrder_Id());

                    return true;
                }
            });


        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }
    }

    public VendorRecyclerOrderQueueAdapter(ArrayList<VendorOrderQueue> vendorList,Context context) {
        this.vendorList = vendorList;
        this.mFilteredList = vendorList;
        pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        this.ctx = context;
    }

    @Override
    public VendorRecyclerOrderQueueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_orderqueue_row, parent, false);

        ctx = parent.getContext();


        return new VendorRecyclerOrderQueueAdapter.MyViewHolder(itemView, ctx, vendorList);
    }

    @Override
    public void onBindViewHolder(final VendorRecyclerOrderQueueAdapter.MyViewHolder holder, final int position) {
        VendorOrderQueue vendor = vendorList.get(position);
        holder.date.setText(vendorList.get(position).getDate());
        holder.subclient.setText(mFilteredList.get(position).getSubclient());
        holder.orderno.setText(mFilteredList.get(position).getOderno());
        holder.status.setText(mFilteredList.get(position).getStatus());
        holder.producttype.setText(mFilteredList.get(position).getProducttype());
        holder.borrowername.setText(mFilteredList.get(position).getBarrowername());
        holder.statename.setText(mFilteredList.get(position).getState());
        holder.countyname.setText(mFilteredList.get(position).getCounty());
        holder.propertyaddress.setText(mFilteredList.get(position).getPropertyaddress());
        holder.setItemClickListener(new ItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                VendorOrderQueue vendororderQueue = mFilteredList.get(pos);
                Context context = v.getContext();
                Intent intent = new Intent(context, EditOrderVendorActivity.class);
                sp = context.getApplicationContext().getSharedPreferences(
                        VENDORORDER, 0);

                SharedPreferences.Editor editor = sp.edit();

                editor.putString("Order_Id", vendororderQueue.getOrder_Id());
                editor.putString("Sub_Client_Name", vendororderQueue.getSubclient());
                editor.putString("State_Name", vendororderQueue.getState());
                editor.putString("County_Name", vendororderQueue.getCounty());
                editor.putString("State", vendororderQueue.getStateID());
                editor.putString("Order_Type", vendororderQueue.getProducttype());
                editor.putString("Order_Status", vendororderQueue.getOrdertask());
                editor.putString("Order_Priority", vendororderQueue.getOrderPriority());
                editor.putString("Order_Assign_Type", vendororderQueue.getCountytype());
                editor.putString("Progress_Status", vendororderQueue.getStatus());
                editor.putString("Sub_Client_Id", vendororderQueue.getSubId());
                editor.putString("Order_Number", vendororderQueue.getOderno());
                editor.putString("Clinet_Id", vendororderQueue.getClintId());
                editor.commit();
                Logger.getInstance().Log("gfdfg"+vendororderQueue.getClintId());
                ((Activity) ctx).startActivityForResult(intent,1005);
//                context.startActivity(intent);

            }
        });

        holder.cardmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.cardmenu,position);
            }
        });
    }

    private void showPopupMenu(View view,int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.vendor_orderqueue_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }


    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        public MyMenuItemClickListener(int positon) {
            this.position=positon;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.venmenu1:
                    cancelled(5);
                    return true;
                case R.id.venmenu2:
                    clarification(4);
                    return true;
                case R.id.venmenu3:
                    completed(1);
                    return true;
                case R.id.venmenu4:
                    hold(1);
                    return true;
                case R.id.venmenu5:
                    workinprogress(1);
                    return true;
                default:
            }
            return false;
        }
    }


    private void hold(final int position){
        Logger.getInstance().Log("in update client id");
        showDialog();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, VendorNavigationActivity.class);
                        ctx.startActivity(intent);
                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();
                // Check for error node in json
                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Order_Id", orderid);
                params.put("Order_Progress_Id", "5");
                Logger.getInstance().Log("parabola: " + params);



                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }


    private void completed(final int position){
        Logger.getInstance().Log("in update client id");
        showDialog();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, VendorNavigationActivity.class);
                        ctx.startActivity(intent);
                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();
                // Check for error node in json
                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Order_Id", orderid);
                params.put("Order_Progress_Id", "3");
                Logger.getInstance().Log("parabola: " + params);



                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }



    private void workinprogress(final int position){
        Logger.getInstance().Log("in update client id");
        showDialog();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, VendorNavigationActivity.class);
                        ctx.startActivity(intent);
                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();
                // Check for error node in json
                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Order_Id", orderid);
                params.put("Order_Progress_Id", "14");
                Logger.getInstance().Log("parabola: " + params);



                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }





    private void cancelled(final int position) {
        Logger.getInstance().Log("in update client id");
        showDialog();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, VendorNavigationActivity.class);
                        ctx.startActivity(intent);
                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();
                // Check for error node in json
                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Order_Id", orderid);
                params.put("Order_Progress_Id", "4");
                Logger.getInstance().Log("parabola: " + params);



                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



    private void clarification(final int position) {
        Logger.getInstance().Log("in update client id");
        showDialog();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, VendorNavigationActivity.class);
                        ctx.startActivity(intent);
                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();
                // Check for error node in json
                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Order_Id", orderid);
                params.put("Order_Progress_Id", "1");
                Logger.getInstance().Log("parabola: " + params);



                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }




    //    for animation


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = vendorList;
                } else {

                    ArrayList<VendorOrderQueue> filteredList = new ArrayList<>();

                    for (VendorOrderQueue vendororderQueue : vendorList) {

                        if (vendororderQueue.getSubclient().toLowerCase().contains(charString) ||
                                vendororderQueue.getOderno().toLowerCase().contains(charString) ||
                                vendororderQueue.getProducttype().toLowerCase().contains(charString) ||
                                vendororderQueue.getPropertyaddress().toLowerCase().contains(charString) ||
                                vendororderQueue.getState().toLowerCase().contains(charString) ||
                                vendororderQueue.getCounty().toLowerCase().contains(charString) ||
                                vendororderQueue.getBarrowername().toLowerCase().contains(charString) ||
                                vendororderQueue.getStatus().toLowerCase().contains(charString)) {

                            filteredList.add(vendororderQueue);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<VendorOrderQueue>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface ItemClickListener {

        void onItemClick(View v,int pos);


    }
}