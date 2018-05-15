package com.nazri.restaurantfoodrecognizer.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nazri.restaurantfoodrecognizer.Interface.ItemClickListener;
import com.nazri.restaurantfoodrecognizer.R;

/**
 * Created by Matjeri on 2/12/2017.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOrderName, txtOrderTableNumber, txtTotalprice ;

    public OrderViewHolder (View itemView){
        super(itemView);

        txtOrderName = (TextView) itemView.findViewById(R.id.order_name);
        txtOrderTableNumber = (TextView) itemView.findViewById(R.id.order_tablenumber);
        txtTotalprice = (TextView) itemView.findViewById(R.id.order_totalprice);
    }

}