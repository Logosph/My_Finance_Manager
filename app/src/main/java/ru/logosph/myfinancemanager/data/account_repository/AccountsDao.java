package ru.logosph.myfinancemanager.data.account_repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ru.logosph.myfinancemanager.domain.models.AccountsItem;

@Dao
public interface AccountsDao {
    @Insert
    long insert(AccountsItem accountsItem);

    @Query("SELECT * FROM accounts_table")
    LiveData<List<AccountsItem>> getAllAccounts();

    @Query("SELECT * FROM accounts_table WHERE name = :name")
    LiveData<AccountsItem> getAccountByName(String name);

    @Query("UPDATE accounts_table SET balance = :balance WHERE name = :name")
    int updateBalance(String name, double balance);
}
