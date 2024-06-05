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

        TransactionDB transactionDB = TransactionDB.getInstance(context);
        TransactionDao transactionDao = transactionDB.transactionDao();

        transactionDao.insert(transactionItem);


    }

    @Override
    public List<TransactionItem> getAllTransactions(Context context, LifecycleOwner lifecycleOwner) {
        TransactionDB transactionDB = TransactionDB.getInstance(context);
        TransactionDao transactionDao = transactionDB.transactionDao();

        return transactionDao.getAllTransactions();
    }

    @Override
    public List<TransactionItem> getTransactionsByAccount(Context context, LifecycleOwner lifecycleOwner, String account) {
        TransactionDB transactionDB = TransactionDB.getInstance(context);
        TransactionDao transactionDao = transactionDB.transactionDao();

        return transactionDao.getTransactionsByAccount(account);
    }

    @Override
    public List<TransactionItem> getTransactionsByName(Context context, LifecycleOwner lifecycleOwner, String name) {
        TransactionDB transactionDB = TransactionDB.getInstance(context);
        TransactionDao transactionDao = transactionDB.transactionDao();

        return transactionDao.getTransactionsByName(name);
    }

    @Override
    public List<TransactionItem> getTransactionsByType(Context context, LifecycleOwner lifecycleOwner, Boolean isIncome) {
        TransactionDB transactionDB = TransactionDB.getInstance(context);
        TransactionDao transactionDao = transactionDB.transactionDao();

        return transactionDao.getTransactionsByType(isIncome);
    }

    @Override
    public void deleteByAccount(Context context, String account) {
        TransactionDB transactionDB = TransactionDB.getInstance(context);
        TransactionDao transactionDao = transactionDB.transactionDao();

        transactionDao.deleteByAccount(account);
    }

    @Override
    public void renameAccount(Context context, String oldName, String newName) {
        TransactionDB transactionDB = TransactionDB.getInstance(context);
        TransactionDao transactionDao = transactionDB.transactionDao();

        transactionDao.renameAccount(oldName, newName);
    }
}
