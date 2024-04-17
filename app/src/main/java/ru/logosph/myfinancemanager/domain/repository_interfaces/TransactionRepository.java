package ru.logosph.myfinancemanager.domain.repository_interfaces;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import ru.logosph.myfinancemanager.domain.models.TransactionItem;

public interface TransactionRepository {

    void insert(Context context, TransactionItem transactionItem);
    LiveData<List<TransactionItem>> getAllTransactions(Context context, LifecycleOwner lifecycleOwner);
    LiveData<List<TransactionItem>> getTransactionsByAccount(Context context, LifecycleOwner lifecycleOwner, String account);
    LiveData<List<TransactionItem>> getTransactionsByName(Context context, LifecycleOwner lifecycleOwner, String name);
    LiveData<List<TransactionItem>> getTransactionsByType(Context context, LifecycleOwner lifecycleOwner, Boolean isIncome);



}
