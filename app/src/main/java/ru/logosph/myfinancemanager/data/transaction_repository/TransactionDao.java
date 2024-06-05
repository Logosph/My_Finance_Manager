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
    List<TransactionItem> getAllTransactions();

    @Query("SELECT * FROM transaction_table WHERE name = :name")
    List<TransactionItem> getTransactionsByName(String name);

    @Query("SELECT * FROM transaction_table WHERE account = :account")
    List<TransactionItem> getTransactionsByAccount(String account);

    @Query("SELECT * FROM transaction_table WHERE isIncome = :isIncome")
    List<TransactionItem> getTransactionsByType(Boolean isIncome);

    // Удалить ВСЕ записи у которых account = :account
    @Query("DELETE FROM transaction_table WHERE account = :account")
    void deleteByAccount(String account);

    @Query("UPDATE transaction_table SET account = :newName WHERE account = :oldName")
    void renameAccount(String oldName, String newName);

}
