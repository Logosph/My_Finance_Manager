package ru.logosph.myfinancemanager.data.transaction_repository;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import ru.logosph.myfinancemanager.domain.models.TransactionItem;
import ru.logosph.myfinancemanager.domain.repository_interfaces.TransactionRepository;

public class TransactionRepositoryImpl implements TransactionRepository {
    @Override
    public void insert(Context context, TransactionItem transactionItem) {
        Thread thread = new Thread(() -> {
            TransactionDB transactionDB = TransactionDB.getInstance(context);
            TransactionDao transactionDao = transactionDB.transactionDao();

            transactionDao.insert(transactionItem);
        });

        thread.start();
    }

    @Override
    public LiveData<List<TransactionItem>> getAllTransactions(Context context, LifecycleOwner lifecycleOwner) {
        TransactionDB transactionDB = TransactionDB.getInstance(context);
        TransactionDao transactionDao = transactionDB.transactionDao();

        return transactionDao.getAllTransactions();
    }

    @Override
    public LiveData<List<TransactionItem>> getTransactionsByAccount(Context context, LifecycleOwner lifecycleOwner, String account) {
        TransactionDB transactionDB = TransactionDB.getInstance(context);
        TransactionDao transactionDao = transactionDB.transactionDao();

        return transactionDao.getTransactionsByAccount(account);
    }

    @Override
    public LiveData<List<TransactionItem>> getTransactionsByName(Context context, LifecycleOwner lifecycleOwner, String name) {
        TransactionDB transactionDB = TransactionDB.getInstance(context);
        TransactionDao transactionDao = transactionDB.transactionDao();

        return transactionDao.getTransactionsByName(name);
    }

    @Override
    public LiveData<List<TransactionItem>> getTransactionsByType(Context context, LifecycleOwner lifecycleOwner, Boolean isIncome) {
        TransactionDB transactionDB = TransactionDB.getInstance(context);
        TransactionDao transactionDao = transactionDB.transactionDao();

        return transactionDao.getTransactionsByType(isIncome);
    }
}
