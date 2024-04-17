package ru.logosph.myfinancemanager.data.transaction_repository;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ru.logosph.myfinancemanager.domain.models.TransactionItem;

@Dao
interface TransactionDao {
    @Insert
    long insert(TransactionItem transactionItem);

    @Query("SELECT * FROM transaction_table")
    LiveData<List<TransactionItem>> getAllTransactions();

    @Query("SELECT * FROM transaction_table WHERE name = :name")
    LiveData<List<TransactionItem>> getTransactionsByName(String name);

    @Query("SELECT * FROM transaction_table WHERE account = :account")
    LiveData<List<TransactionItem>> getTransactionsByAccount(String account);

    @Query("SELECT * FROM transaction_table WHERE isIncome = :isIncome")
    LiveData<List<TransactionItem>> getTransactionsByType(Boolean isIncome);

}
