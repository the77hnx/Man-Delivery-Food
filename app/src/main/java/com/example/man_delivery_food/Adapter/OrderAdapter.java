package com.example.man_delivery_food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.man_delivery_food.Model.Order;
import com.example.man_delivery_food.R;
import com.google.android.material.imageview.ShapeableImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Set data to views
        holder.orderIdTextView.setText("رقم الطلب: " + order.getOrderId());
        holder.numberItemsTextView.setText(order.getNumberOfItems() + " عناصر");
        holder.dateOrderTextView.setText(order.getOrderDate());
        holder.timeOrderTextView.setText(order.getOrderTime());
        holder.orderImageView.setImageResource(R.drawable.pizza); // Assuming you want to display a static image
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderIdTextView;
        private TextView numberItemsTextView;
        private TextView dateOrderTextView;
        private TextView timeOrderTextView;
        private ShapeableImageView orderImageView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id);
            numberItemsTextView = itemView.findViewById(R.id.number_items);
            dateOrderTextView = itemView.findViewById(R.id.date_order);
            timeOrderTextView = itemView.findViewById(R.id.time_order);
            orderImageView = itemView.findViewById(R.id.shapeableImageView);
        }
    }
}
