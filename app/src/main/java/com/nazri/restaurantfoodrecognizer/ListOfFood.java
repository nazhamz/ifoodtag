package com.nazri.restaurantfoodrecognizer;

import android.content.DialogInterface;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import com.nazri.restaurantfoodrecognizer.Common.Common;
import com.nazri.restaurantfoodrecognizer.Database.Database;
import com.nazri.restaurantfoodrecognizer.Model.Order;
import com.nazri.restaurantfoodrecognizer.Model.Request;
import com.nazri.restaurantfoodrecognizer.ViewHolder.CartAdapter;


public class ListOfFood extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    FButton btnAddQueue;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_food);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init
        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnAddQueue = (FButton)findViewById(R.id.btnAddToQueue);

        btnAddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cart.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(ListOfFood.this, "Sorry, your list is empty.", Toast.LENGTH_SHORT).show();
            }
        });

        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListOfFood.this);
        alertDialog.setMessage("Enter your Table Name: ");

        final EditText editTable_number = new EditText(ListOfFood.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editTable_number.setLayoutParams(lp);
        alertDialog.setView(editTable_number);    //Add edit Text to alert dialog
        alertDialog.setIcon(R.drawable.ic_assignment_turned_in_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Create a new Request
                Request request = new Request(
                        Common.currentUser.getIc(),
                        Common.currentUser.getName(),
                        editTable_number.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );
                //Submit to Firebase
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                //Delete
                new Database(getBaseContext()).cleanQueue();
                Toast.makeText(ListOfFood.this, "Thank You. You can check your Receipt now.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //calculate total price
        float total = 0.0f;
        for (Order order:cart)
            total += (Float.parseFloat(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en","MY");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.DELETE))
            deleteList(item.getOrder());
        return true;
    }

    public void deleteList(int position){
        //remove item
        cart.remove(position);
        //then, delete oll data from sqlite
        new Database(this).cleanQueue();
        //update new data from list
        for (Order item:cart)
            new Database(this).addToQueue(item);

        //refresh
        loadListFood();
    }
}



