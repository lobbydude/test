package com.drnds.titlelogy.adapter.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
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
import com.drnds.titlelogy.activity.client.createuseractivity.CreateUserActivity;
import com.drnds.titlelogy.activity.client.gridactivity.EditGridViewActivity;
import com.drnds.titlelogy.activity.client.orderqueueactivity.EditOrderActivity;
import com.drnds.titlelogy.model.client.GridItem;
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

import static com.drnds.titlelogy.R.id.status;
import static com.drnds.titlelogy.util.AppController.TAG;

/**
 * Created by ajithkumar on 6/27/2017.
 */

public  class RecyclergridviewAdapter extends RecyclerView.Adapter< RecyclergridviewAdapter.MyViewHolder>implements Filterable {
    private ArrayList<GridItem> gridItemList;
    private ArrayList<GridItem> mFilteredList;

    private Context ctx;

    SharedPreferences sharedpreferences;
    public static final String GRID = "gridadpter";
    RecyclerView mRecyclerView;
//    private ProgressDialog pDialog;
    String orderid,orderstatuscheck;
    String orderstatus = "CANCELLED";


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subclient,oderno,propertyaddress,producttype,state,county,status, barrowername,date;
        private List<GridItem> gridItemList=new ArrayList<GridItem>();
        private   ItemClickListener itemClickListener;

        public ImageView cardmenu;
        //        private Button button;
        Context ctx;

        public MyViewHolder(View view,final Context ctx,final List<GridItem> gridItemList) {
            super(view);
            this.gridItemList=gridItemList;
            this.ctx=ctx;

            subclient = (TextView) view.findViewById(R.id.grid_subclient);
            oderno = (TextView) view.findViewById(R.id.grid_orderno);
            producttype = (TextView) view.findViewById(R.id.gridview_producttype);
            state = (TextView) view.findViewById(R.id.gridview_state);
            county = (TextView) view.findViewById(R.id.gridview_county);
            propertyaddress = (TextView) view.findViewById(R.id.gridview_address);
            status = (TextView) view.findViewById(R.id.grid_status);
            barrowername = (TextView) view.findViewById(R.id.gridview_borrowername);
            date= (TextView) view.findViewById(R.id.text_gdate);
            cardmenu=(ImageView)view.findViewById(R.id.gridcardmenu);

            subclient.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            oderno.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            producttype.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            state.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            county.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            propertyaddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            status.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            barrowername.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            view.setOnClickListener(this);


            cardmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    GridItem gridItem=gridItemList.get(position);
                    orderid= gridItem.getOrderId();
                    showPopupMenu(cardmenu,getAdapterPosition());
                    Logger.getInstance().Log("selected order id is : " +  gridItem.getOrderId());


                }
            });


            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position=getAdapterPosition();
                    GridItem griditem=gridItemList.get(position);
                    orderid=griditem.getOrderId();
                    orderstatuscheck=griditem.getStatus();
                    showPopupMenu(cardmenu,getAdapterPosition());
                    Logger.getInstance().Log("selected order id is : " + griditem.getOrderId());

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
    public RecyclergridviewAdapter(ArrayList<GridItem>gridItemList,Context context) {
        this. gridItemList=   gridItemList;
        this. mFilteredList=   gridItemList;

//        pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        this.ctx=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_row, parent, false);

        ctx = parent.getContext();

        return new MyViewHolder(itemView,ctx,gridItemList);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        holder.date.setText (gridItemList.get(position).getDate());

        holder.subclient.setText (mFilteredList.get(position).getSubclient());

        holder.oderno.setText( mFilteredList.get(position).getOderno());
        holder.producttype.setText( mFilteredList.get(position).getProducttype());
        holder.propertyaddress.setText( mFilteredList.get(position).getPropertyaddress());

        holder.state .setText( mFilteredList.get(position).getState());
        holder.county.setText( mFilteredList.get(position).getCounty());
        holder.status.setText( mFilteredList.get(position).getStatus());
        holder.barrowername.setText( mFilteredList.get(position).getBarrowername());


        holder.setItemClickListener(new RecyclergridviewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                GridItem gridItem = mFilteredList.get(pos);
                Context context = v.getContext();
                Intent intent=new Intent(context, EditGridViewActivity.class);

                sharedpreferences = context.getSharedPreferences(GRID, Context.MODE_PRIVATE);
                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedpreferences.edit();




                //Adding values to editor

                editor.putString("Order_Id",gridItem.getOrderId());
                editor.putString("Sub_Client_Name", gridItem.getSubclient());
                editor.putString("Product_Type", gridItem.getProducttype());
                editor.putString("State", gridItem.getState());
                editor.putString("County", gridItem.getCounty());
                editor.putString("Order_Priority", gridItem.getOrderpriority());
                editor.putString("Order_Assign_Type", gridItem.getCountytype());
                editor.putString("Progress_Status", gridItem.getStatus());
                editor.putString("Barrower_Name", gridItem.getBarrowername());
                editor.putString("Order_Status", gridItem.getOrdertask());
                editor.putString("Sub_Client_Id", gridItem.getSubId());
                Logger.getInstance().Log("subid22 " + gridItem.getSubId());
                editor.commit();
                context.startActivity(intent);
//            intent.putExtra("Order_Id",orderQueue.getOrder_Id());

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
        inflater.inflate(R.menu.grid_view_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }


    private boolean validate() {

        if(orderstatus.equals(orderstatuscheck)){
        }
        return true;
    }





    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        public MyMenuItemClickListener(int positon) {
            this.position=positon;
        }



        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.gridmenu1:
                    hold(5);
                    return true;
                case R.id.gridmenu2:
//                    if(validate()){
//                        menuItem.setVisible(true);
//                    }
                    cancelled(4);
                    return true;
                case R.id.gridmenu3:
                    clarification(1);
                    return true;
                case R.id.gridmenu4:
                    workinprogress(1);
                    return true;
                default:
            }
            return false;
        }
    }


    private void hold(final int position){
        Logger.getInstance().Log("in update client id");
//        showDialog();

//        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, NavigationActivity.class);
                        ctx.startActivity(intent);
//                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
//                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                hideDialog();
                // Check for error node in json
                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                hideDialog();
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


    private void workinprogress(final int position){
        Logger.getInstance().Log("in update client id");
//        showDialog();

//        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, NavigationActivity.class);
                        ctx.startActivity(intent);
//                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
//                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                hideDialog();
                // Check for error node in json
                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                hideDialog();
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



    private void completed(final int position){
        Logger.getInstance().Log("in update client id");
//        showDialog();

//        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, NavigationActivity.class);
                        ctx.startActivity(intent);
//                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
//                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                hideDialog();
                // Check for error node in json
                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                hideDialog();
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




    private void cancelled(final int position) {
        Logger.getInstance().Log("in update client id");
//        showDialog();

//        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, NavigationActivity.class);
                        ctx.startActivity(intent);
//                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
//                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                hideDialog();
                // Check for error node in json
                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                hideDialog();
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



//    private void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }



    private void clarification(final int position) {
        Logger.getInstance().Log("in update client id");
//        showDialog();

//        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, NavigationActivity.class);
                        ctx.startActivity(intent);
//                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
//                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                hideDialog();
                // Check for error node in json
                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                hideDialog();
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


    @Override
    public int getItemCount() {
        return  mFilteredList.size();
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = gridItemList;
                } else {

                    ArrayList<GridItem> filteredList = new ArrayList<>();

                    for (GridItem gridItem : gridItemList) {

                        if (gridItem.getSubclient().toLowerCase().contains(charString) ||
                                gridItem.getOderno().toLowerCase().contains(charString) ||
                                gridItem.getProducttype().toLowerCase().contains(charString)||
                                gridItem.getPropertyaddress().toLowerCase().contains(charString)||
                                gridItem.getState().toLowerCase().contains(charString)||
                                gridItem.getCounty().toLowerCase().contains(charString)||
                                gridItem.getCounty().toLowerCase().contains(charString)||
                                gridItem.getBarrowername().toLowerCase().contains(charString))
                        {

                            filteredList.add(gridItem);
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
                mFilteredList = (ArrayList<GridItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ItemClickListener {

        void onItemClick(View v,int pos);


    }



}

