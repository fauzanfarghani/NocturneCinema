package com.example.nocturnecinema.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nocturnecinema.R;
import com.example.nocturnecinema.adapters.TransactionAdapter;
import com.example.nocturnecinema.db.DatabaseHelper;
import com.example.nocturnecinema.models.Transaction;

import java.util.List;

public class TransactionFragment extends Fragment implements TransactionAdapter.OnItemClickListener {

    private RecyclerView rvTransactions;
    private DatabaseHelper dbHelper;
    private TransactionAdapter adapter;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        
        rvTransactions = view.findViewById(R.id.rv_transactions);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        dbHelper = new DatabaseHelper(getContext());
        
        SharedPreferences prefs = getContext().getSharedPreferences("NocturnePrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("USER_ID", -1);

        loadTransactions();
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadTransactions();
    }

    private void loadTransactions() {
        if (userId != -1) {
            List<Transaction> list = dbHelper.getUserTransactions(userId);
            if (adapter == null) {
                adapter = new TransactionAdapter(getContext(), list, this);
                rvTransactions.setAdapter(adapter);
            } else {
                adapter.updateList(list);
            }
        }
    }

    @Override
    public void onUpdateClick(Transaction transaction) {
        showUpdateDialog(transaction);
    }

    @Override
    public void onDeleteClick(Transaction transaction) {
        showDeleteDialog(transaction);
    }
    
    private void showUpdateDialog(Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_quantity, null);
        builder.setView(view);

        final android.widget.TextView tvQty = view.findViewById(R.id.tv_dialog_qty);
        android.widget.Button btnMinus = view.findViewById(R.id.btn_dialog_minus);
        android.widget.Button btnPlus = view.findViewById(R.id.btn_dialog_plus);
        
        // Use an array to hold the mutable integer effectively inside lambda
        final int[] currentQty = {transaction.getQuantity()};
        tvQty.setText(String.valueOf(currentQty[0]));

        btnMinus.setOnClickListener(v -> {
            if (currentQty[0] > 1) {
                currentQty[0]--;
                tvQty.setText(String.valueOf(currentQty[0]));
            }
        });

        btnPlus.setOnClickListener(v -> {
            currentQty[0]++;
            tvQty.setText(String.valueOf(currentQty[0]));
        });

        builder.setPositiveButton("Update", (dialog, which) -> {
            dbHelper.updateTransaction(transaction.getId(), currentQty[0]);
            loadTransactions();
            Toast.makeText(getContext(), "Transaction Updated", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
    
    private void showDeleteDialog(Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_delete_confirmation, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        
        // Transparent background for rounded corners effect if parent has it, 
        // but since we use a rectangular dark background in XML, this is optional but good practice.
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        android.widget.Button btnCancel = view.findViewById(R.id.btn_dialog_cancel);
        android.widget.Button btnDelete = view.findViewById(R.id.btn_dialog_delete);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnDelete.setOnClickListener(v -> {
            dbHelper.deleteTransaction(transaction.getId());
            loadTransactions();
            Toast.makeText(getContext(), "Transaction Deleted", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
