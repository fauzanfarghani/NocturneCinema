package com.example.nocturnecinema.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nocturnecinema.R;
import com.example.nocturnecinema.models.Transaction;

import java.util.List;

import android.widget.ImageView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private Context context;
    private List<Transaction> transactionList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onUpdateClick(Transaction transaction);
        void onDeleteClick(Transaction transaction);
    }

    public TransactionAdapter(Context context, List<Transaction> transactionList, OnItemClickListener listener) {
        this.context = context;
        this.transactionList = transactionList;
        this.listener = listener;
    }

    public void updateList(List<Transaction> newList) {
        transactionList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.tvTitle.setText(transaction.getFilmTitle());
        holder.tvPrice.setText("Price: " + transaction.getFilmPrice());
        holder.tvQty.setText("Quantity: " + transaction.getQuantity());
        
        // New fields
        holder.tvRating.setText("Rating: " + transaction.getFilmRating());
        holder.tvCountry.setText("Country: " + transaction.getFilmCountry());
        
        // Load Image
        com.example.nocturnecinema.utils.ImageLoader.loadImage(transaction.getFilmCoverUrl(), holder.ivCover); 

        holder.btnUpdate.setOnClickListener(v -> listener.onUpdateClick(transaction));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(transaction));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice, tvQty, tvRating, tvCountry;
        ImageView ivCover;
        Button btnUpdate, btnDelete;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_trans_title);
            tvPrice = itemView.findViewById(R.id.tv_trans_price);
            tvQty = itemView.findViewById(R.id.tv_trans_qty);
            tvRating = itemView.findViewById(R.id.tv_trans_rating);
            tvCountry = itemView.findViewById(R.id.tv_trans_country);
            ivCover = itemView.findViewById(R.id.iv_trans_cover);
            btnUpdate = itemView.findViewById(R.id.btn_update_trans);
            btnDelete = itemView.findViewById(R.id.btn_delete_trans);
        }
    }
}
