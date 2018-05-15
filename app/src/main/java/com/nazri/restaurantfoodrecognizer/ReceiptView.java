package com.nazri.restaurantfoodrecognizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nazri.restaurantfoodrecognizer.Common.Common;
import com.nazri.restaurantfoodrecognizer.Model.Request;
import com.nazri.restaurantfoodrecognizer.Model.Order;
import com.nazri.restaurantfoodrecognizer.ViewHolder.OrderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ReceiptView extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    private ListView listView;


    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference requests;
    List<Order> foodlists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_view);


        foodlists = new ArrayList<>();

        //firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getName());


    }
    private void loadOrders(String name){
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("name")
                .equalTo(name)

        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {

                viewHolder.txtOrderName.setText("Customer Name : " + model.getName());
                viewHolder.txtOrderTableNumber.setText("Table Name : " + model.getTable_number());
                viewHolder.txtTotalprice.setText("Grand Total                 " + model.getTotal());

            }
        };
        recyclerView.setAdapter(adapter);
    }
}
