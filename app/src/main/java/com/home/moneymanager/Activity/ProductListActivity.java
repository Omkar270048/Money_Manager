package com.home.moneymanager.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.home.moneymanager.Data.Product;
import com.home.moneymanager.Adapters.MyAdapter;
import com.home.moneymanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProductListActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    MyAdapter adapter;
    ArrayList<Product> list;
    FirebaseAuth mAuth;
    TextView totalPrice, tvNoData;
    Spinner spinner;
    ArrayList<String> spinnerArraylist;
    ArrayAdapter<String> spinnerAdapter;
    //ArrayList<Integer> costs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

       // getSupportActionBar().hide();// removing default status bar

        spinner = findViewById(R.id.spinner);
        tvNoData = findViewById(R.id.tv_no_data);
        totalPrice = findViewById(R.id.total_price);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        recyclerView = findViewById(R.id.product_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new MyAdapter(this, list);
        recyclerView.setAdapter(adapter);
        reference = db.getReference().child("UserInfo").child(uid).child("Product");
        //costs = new ArrayList<>();

        // code for spinner
        spinnerArraylist = new ArrayList<>();
        spinnerArraylist.add("Today");
        spinnerArraylist.add("This Month");
        spinnerArraylist.add("Last Month");
        spinnerArraylist.add("All");

        spinnerAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item, spinnerArraylist);
        spinner.setAdapter(spinnerAdapter);

//        getData();
//       reference.addValueEventListener(new ValueEventListener() {
//           @Override
//           public void onDataChange(@NonNull DataSnapshot snapshot) {
//               for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                   Product Model = dataSnapshot.getValue(Product.class);
//                   if(getMonth(Model.getTime()).equals("24")) {
//                       list.add(Model);
////                   Toast.makeText(ProductListActivity.this, getMonth(Model.getTime()), Toast.LENGTH_SHORT).show();
//                       costs.add(Integer.valueOf(Model.getCost())); //get cost
//                   }
//               }
//               adapter.notifyDataSetChanged();
//               if(list.isEmpty()){
//                   Toast.makeText(ProductListActivity.this, "NO DATA", Toast.LENGTH_SHORT).show();
//                   tvNoData.setVisibility(View.VISIBLE);
//                   tvNoData.getBackground().setAlpha(128);
//               }else{
//                   tvNoData.setVisibility(View.GONE);
//
//
//               }
//               totalPrice.setText(totalCost(costs));
//           }
//
//           @Override
//           public void onCancelled(@NonNull DatabaseError error) {
//
//           }
//       });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: //today
                        getData("0");
                        break;
                    case 1: // this month
                        getData("1");
                        break;
                    case 2:// last month
                        getData("2");
                        break;
                    case 3: // all
                        getData("3");
                        break;
                    default:
                        getData("0");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String totalCost(ArrayList<Integer> costs) {
        int total = 0;

        for (int i=0; i<costs.size(); i++){
            total += costs.get(i);
        }
//        Toast.makeText(ProductListActivity.this, "Total:" + total, Toast.LENGTH_SHORT).show();
        String val = String.valueOf(total);
        return val;
    }

    private String getMonth(String m){ // for fetching product uploading month from database
        String s = "";
        for(int i=0; i<m.length(); i++){
            if(i==3 || i==4){ //3,4
               s += m.charAt(i);
            }
        }
        return s;
    }

    private String getYear(String m){ // for fetching product uploading year from database
        String s = "";
        for(int i=0; i<m.length(); i++){
            if(i==6 || i==7 || i==8 || i==9){  //6,7,8,9
                s += m.charAt(i);
            }
        }
        return s;
    }

    private String getDay(String m){ // for fetching product uploading day from database
        String s = "";
        for(int i=0; i<m.length(); i++){
            if(i==0 || i==1){ //0,1
                s += m.charAt(i);
            }
        }
        return s;
    }

    private void getData(String flag){
        list.clear();
        //adapter.notifyDataSetChanged();
        int thisMonth = Integer.valueOf(getPresentMonth());
        int lastMonth = thisMonth - 1;
        if(lastMonth == 0){lastMonth = 12;}
        String lm = String.valueOf(lastMonth);
        ArrayList<Integer> costs= new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Product Model = dataSnapshot.getValue(Product.class);
                    if(getYear(Model.getTime()).equals(getPresentYear()) &&
                            getDay(Model.getTime()).equals(getTodayDate()) && flag.equals("0")) {
                        list.add(Model);
                        costs.add(Integer.valueOf(Model.getCost())); //get cost

                    }else if(getYear(Model.getTime()).equals(getPresentYear()) &&
                            getMonth(Model.getTime()).equals(getPresentMonth()) && flag.equals("1")){
                        list.add(Model);
                        costs.add(Integer.valueOf(Model.getCost())); //get cost

                    }else if(getYear(Model.getTime()).equals(getPresentYear()) &&
                            getMonth(Model.getTime()).equals(lm) && flag.equals("2")){
                        list.add(Model);
                        costs.add(Integer.valueOf(Model.getCost())); //get cost

                    }else if(flag.equals("3")){
                        list.add(Model);
                        costs.add(Integer.valueOf(Model.getCost())); //get cost
                    }
                }
                if(list.isEmpty()){
                    list.clear();
                    Toast.makeText(ProductListActivity.this, "NO DATA", Toast.LENGTH_SHORT).show();
                    tvNoData.setVisibility(View.VISIBLE);
                    tvNoData.getBackground().setAlpha(128);
                }else{
                    tvNoData.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();

                totalPrice.setText(totalCost(costs));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductListActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void checkListStatus() {
//        if(list.isEmpty()){
//            list.clear();
//            Toast.makeText(ProductListActivity.this, "NO DATA", Toast.LENGTH_SHORT).show();
//            tvNoData.setVisibility(View.VISIBLE);
//            tvNoData.getBackground().setAlpha(128);
//        }else{
//            tvNoData.setVisibility(View.GONE);
//        }
//    }

    private String getTodayDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        Date date = new Date();
//        System.out.println(formatter.format(date));
        //Toast.makeText(UploadDataActivity.this,formatter.format(date) , Toast.LENGTH_SHORT).show();
        return formatter.format(date);
    }

    private String getPresentMonth(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        Date date = new Date();
        return formatter.format(date);
    }

    private String getPresentYear(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return formatter.format(date);
    }

}