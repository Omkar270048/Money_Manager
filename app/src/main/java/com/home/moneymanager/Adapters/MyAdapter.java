package com.home.moneymanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.home.moneymanager.Data.Product;
import com.home.moneymanager.R;

import java.util.AbstractList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    Context context;
    AbstractList<Product> list;

    public MyAdapter(Context context, AbstractList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Product product = list.get(position);
        holder.name.setText(product.getName());
        holder.cost.setText(product.getCost());
        holder.date.setText(product.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, cost, date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.p_name);
            cost = itemView.findViewById(R.id.p_cost);
            date = itemView.findViewById(R.id.p_time);
        }
    }
}
