package com.drnds.titlelogy.adapter.vendor;

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
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.drnds.titlelogy.activity.vendor.gridactivity.EditGridViewVendorActivity;
import com.drnds.titlelogy.adapter.client.RecyclergridviewAdapter;
import com.drnds.titlelogy.model.client.GridItem;
import com.drnds.titlelogy.model.vendor.VendorGridItem;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import android.util.SparseBooleanArray;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

import static com.drnds.titlelogy.R.id.chkSelected;
import static com.drnds.titlelogy.util.AppController.TAG;
import static com.drnds.titlelogy.util.Config.NewOrderFlag;
import static com.drnds.titlelogy.util.Config.Order_Filter;
import static com.drnds.titlelogy.util.Config.selectedArrays;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorRecyclegridviewAdapter extends RecyclerView.Adapter< VendorRecyclegridviewAdapter.MyViewHolder>implements Filterable {
    private ArrayList<VendorGridItem> vendorgridItemList;
    private ArrayList<VendorGridItem> mFilteredList;
    ArrayList<String> selectd = new ArrayList<String>();
    ArrayList<String> selectselection = new ArrayList<String>();
    private Context ctx;

    public  LinearLayout chkbox;
    private Context context;
    String orderid;

    SharedPreferences sp;
    public static final String VENDORGRID = "vendorgridadpter";

    //    for animation
    private int lastPosition = -1;


    public ArrayList<String> getArrayList(){
        return selectd;
    }


    public ArrayList<String> getSelectedArray(){
        return selectselection;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subclient,oderno,propertyaddress,producttype,state,county,status, barrowername,date;
        public CheckBox chkSelected;
        private List<VendorGridItem> vendorgridItemList=new ArrayList<VendorGridItem>();
        private  ItemClickListener itemClickListener;

        public ImageView cardmenu;
        //        private Button button;
        Context ctx;


        public MyViewHolder(View view,final Context ctx,final List<VendorGridItem> vendorgridItemList) {
            super(view);
            this.vendorgridItemList=vendorgridItemList;
            this.ctx=ctx;

            subclient = (TextView) view.findViewById(R.id.vendor_grid_subclient);
            oderno = (TextView) view.findViewById(R.id.vendor_grid_orderno);
            producttype = (TextView) view.findViewById(R.id.vendor_gridview_producttype);
            state = (TextView) view.findViewById(R.id.vendor_gridview_state);
            county = (TextView) view.findViewById(R.id.vendor_gridview_county);
            propertyaddress = (TextView) view.findViewById(R.id.vendor_gridview_address);
            status = (TextView) view.findViewById(R.id.vendor_grid_status);
            barrowername = (TextView) view.findViewById(R.id.vendor_gridview_borrowername);
            cardmenu=(ImageView)view.findViewById(R.id.vendorgridcardmenu);
            chkSelected = (CheckBox) view.findViewById(R.id.chkSelected);
            chkbox=(LinearLayout) view.findViewById(R.id.linear_chk);

            date = (TextView) view.findViewById(R.id.text_vgdate);

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
                    VendorGridItem vendorgriditem=vendorgridItemList.get(position);
                    orderid= vendorgriditem.getOrderId();
                    showPopupMenu(cardmenu,getAdapterPosition());
                    Logger.getInstance().Log("selected order id is : " +  vendorgriditem.getOrderId());


                }
            });


            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position=getAdapterPosition();
                    VendorGridItem vendorgriditem=vendorgridItemList.get(position);
                    orderid= vendorgriditem.getOrderId();
                    showPopupMenu(cardmenu,getAdapterPosition());
                    Logger.getInstance().Log("selected order id is : " +  vendorgriditem.getOrderId());

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
    public VendorRecyclegridviewAdapter(ArrayList<VendorGridItem>vendorgridItemList,Context context) {
        selectedArrays.clear();
        this. vendorgridItemList=   vendorgridItemList;
        this. mFilteredList=   vendorgridItemList;

        this.ctx=context;

    }



    @Override
    public VendorRecyclegridviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_grid_row, parent, false);
        ctx = parent.getContext();
        return new MyViewHolder(itemView,ctx,vendorgridItemList);


    }



    @Override
    public void onBindViewHolder(final VendorRecyclegridviewAdapter.MyViewHolder holder, final int position) {
        final int pos = position;
        VendorGridItem vendorgridItem = vendorgridItemList.get(position);
        System.out.println("Orderfilter"+Order_Filter);
        if(Order_Filter=="NEW_ORDERS"){
            chkbox.setVisibility(View.VISIBLE);
        }
        holder.date.setText(mFilteredList.get(position).getDate());
        holder.subclient.setText (mFilteredList.get(position).getSubclient());
        holder.oderno.setText( mFilteredList.get(position).getOderno());
        holder.status.setText(mFilteredList.get(position).getStatus());
        holder.producttype.setText(mFilteredList.get(position).getProducttype());
        holder.barrowername.setText(mFilteredList.get(position).getBarrowername());
        holder.state.setText(mFilteredList.get(position).getState());
        holder.county.setText(mFilteredList.get(position).getCounty());
        holder.propertyaddress.setText(mFilteredList.get(position).getPropertyaddress());



        holder.chkSelected.setChecked(vendorgridItemList.get(position).isSelected());

        holder.chkSelected.setTag(vendorgridItemList.get(position));



        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;
                VendorGridItem contact = (VendorGridItem) cb.getTag();

                contact.setSelected(cb.isChecked());
                vendorgridItemList.get(pos).setSelected(cb.isChecked());

                System.out.println("selectedOrdershiiii" + mFilteredList.get(pos).getOrderId());

                if (cb.isChecked()) {

//                     if (!selectd.isEmpty()){
//                         cb.setEnabled(false);
//                     }
//                     else {
//                         cb.setClickable(true);
//                     }

                    selectedArrays.add(mFilteredList.get(pos).getOrderId());

                    System.out.println("selected = " + selectedArrays);
                }else{
                    String removedOrderID = (String)mFilteredList.get(pos).getOrderId();
                    selectedArrays.remove(removedOrderID);
                    System.out.println("selected removed = " + removedOrderID);
                }


                System.out.println("final="+selectedArrays);


                Toast.makeText(
                        v.getContext(),
                        "Clicked on Checkbox: " + cb.getText() + " is "
                                + selectedArrays, Toast.LENGTH_SHORT).show();
            }
        });
//        for animation
        holder.setItemClickListener(new ItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                VendorGridItem vendorgridItem = mFilteredList.get(pos);
                Context context = v.getContext();
                Intent intent = new Intent(context,EditGridViewVendorActivity.class);
                sp = context.getApplicationContext().getSharedPreferences(
                        "vendorgridadpter", 0);
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("Order_Id",vendorgridItem.getOrderId());
                Logger.getInstance().Log("orderidadapter "+vendorgridItem.getOrderId());
                editor.putString("Sub_Client_Name", vendorgridItem.getSubclient());
                editor.putString("Order_Type", vendorgridItem.getProducttype());
                editor.putString("State_Name", vendorgridItem.getState());
                editor.putString("County_Name", vendorgridItem.getCounty());
                editor.putString("State", vendorgridItem.getStateid());
                editor.putString("Order_Priority", vendorgridItem.getOrderpriority());
                editor.putString("Order_Assign_Type", vendorgridItem.getCountytype());
                editor.putString("Progress_Status", vendorgridItem.getStatus());
                editor.putString("Barrower_Name", vendorgridItem.getBarrowername());
                editor.putString("Order_Number", vendorgridItem.getOderno());
                editor.putString("Order_Status", vendorgridItem.getOrdertask());
                Logger.getInstance().Log("Order_Status of " + vendorgridItem.getOrdertask());
                editor.putString("Sub_Client_Id", vendorgridItem.getSubclientid());

                editor.putString("Order_Status", vendorgridItem.getOrdertask());

                editor.commit();
                Logger.getInstance().Log("status888 " + vendorgridItem.getStatus());
                context.startActivity(intent);
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
        inflater.inflate(R.menu.vendor_grid_view_menu, popup.getMenu());
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

                case R.id.vengridmenu1:
                    hold(5);
                    return true;
                case R.id.vengridmenu2:
                    cancelled(4);
                    return true;
                case R.id.vengridmenu3:
                    clarification(1);
                    return true;
                case R.id.vengridmenu4:
                    workinprogress(1);
                    return true;
                case R.id.vengridmenu5:
                    completed(1);
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
                        Intent intent= new Intent(ctx, VendorNavigationActivity.class);
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
                        Intent intent= new Intent(ctx, VendorNavigationActivity.class);
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
                        Intent intent= new Intent(ctx, VendorNavigationActivity.class);
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


    //    for animation




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

                    mFilteredList = vendorgridItemList;
                } else {

                    ArrayList<VendorGridItem> filteredList = new ArrayList<>();

                    for (VendorGridItem vendorgridItem : vendorgridItemList) {

                        if (vendorgridItem.getSubclient().toLowerCase().contains(charString) ||
                                vendorgridItem.getOderno().toLowerCase().contains(charString) ||
                                vendorgridItem.getProducttype().toLowerCase().contains(charString)||
                                vendorgridItem.getPropertyaddress().toLowerCase().contains(charString)||
                                vendorgridItem.getState().toLowerCase().contains(charString)||
                                vendorgridItem.getCounty().toLowerCase().contains(charString)||
                                vendorgridItem.getCounty().toLowerCase().contains(charString)||
                                vendorgridItem.getBarrowername().toLowerCase().contains(charString))

                        {

                            filteredList.add(vendorgridItem);
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
                mFilteredList = (ArrayList<VendorGridItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface ItemClickListener {

        void onItemClick(View v,int pos);


    }




}


