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
    List<AccountsItem> getAllAccounts();

    @Query("SELECT * FROM accounts_table WHERE name = :name")
    AccountsItem getAccountByName(String name);

    @Query("UPDATE accounts_table SET balance = :balance WHERE name = :name")
    int updateBalance(String name, double balance);

    @Query("DELETE FROM accounts_table WHERE name = :name")
    void delete(String name);

    @Query("UPDATE accounts_table SET name = :newName WHERE name = :oldName")
    void rename(String oldName, String newName);
}
