package ru.logosph.myfinancemanager.domain.repository_interfaces;

import android.content.Context;

import java.util.List;

import ru.logosph.myfinancemanager.domain.models.TransactionItem;

public interface TransactionRepository {

    void insert(Context context, TransactionItem transactionItem);
    List<TransactionItem> getAllTransactions(Context context);
    List<TransactionItem> getTransactionsByAccount(Context context, String account);
    List<TransactionItem> getTransactionsByName(Context context, String name);
    List<TransactionItem> getTransactionsByType(Context context, Boolean isIncome);
    void deleteByAccount(Context context, String account);
    void renameAccount(Context context, String oldName, String newName);

}