package com.iberdrola.clientes.presentation.views.customviews.horizontalrecyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iberdrola.clientes.R;

import java.util.List;

public class GraficaRecyclerViewAdapter extends RecyclerView.Adapter<GraficaRecyclerViewAdapter.ViewHolder> {

    private List<String> dataShow;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public GraficaRecyclerViewAdapter(Context context, List<String> dataShow) {
        this.mInflater = LayoutInflater.from(context);
        this.dataShow = dataShow;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_grafica_item_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data = dataShow.get(position);
        holder.myTextView.setText(data);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return dataShow.size();
    }

    @SuppressLint("NewApi")
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnScrollChangeListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return dataShow.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}