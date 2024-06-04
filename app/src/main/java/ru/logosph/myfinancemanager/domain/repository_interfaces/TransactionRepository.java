package ru.logosph.myfinancemanager.domain.repository_interfaces;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import ru.logosph.myfinancemanager.domain.models.TransactionItem;

public interface TransactionRepository {

    void insert(Context context, TransactionItem transactionItem);
    List<TransactionItem> getAllTransactions(Context context, LifecycleOwner lifecycleOwner);
    List<TransactionItem> getTransactionsByAccount(Context context, LifecycleOwner lifecycleOwner, String account);
    List<TransactionItem> getTransactionsByName(Context context, LifecycleOwner lifecycleOwner, String name);
    List<TransactionItem> getTransactionsByType(Context context, LifecycleOwner lifecycleOwner, Boolean isIncome);
    void deleteByAccount(Context context, String account);


}
