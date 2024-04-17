package ru.logosph.myfinancemanager.data.account_repository;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ru.logosph.myfinancemanager.domain.models.AccountsItem;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;

public class AccountRepositoryImpl implements AccountRepository {

    public MutableLiveData<ArrayList<AccountsItem>> accounts = new MutableLiveData<>();
    private MutableLiveData<AccountsItem> oneAccount = new MutableLiveData<>();

    @Override
    public void loadAccountsFromDB(Context context, LifecycleOwner lifecycleOwner) {
        AccountsDB accountsDB = AccountsDB.getInstance(context);
        AccountsDao accountsDao = accountsDB.accountsDao();

        accountsDao.getAllAccounts().observe(lifecycleOwner, accountsItems -> {
            accounts.setValue((ArrayList<AccountsItem>) accountsItems);
        });
    }

    @Override
    public LiveData<ArrayList<AccountsItem>> getAccounts() {
        return accounts;
    }

    @Override
    public LiveData<AccountsItem> getOneAccount() {
        return oneAccount;
    }

    @Override
    public void updateBalanceByName(Context context, String name, double balance) {
        AccountsDB accountsDB = AccountsDB.getInstance(context);
        AccountsDao accountsDao = accountsDB.accountsDao();

        Thread thread = new Thread(() -> accountsDao.updateBalance(name, balance));
    }

    @Override
    public void getAccountByName(Context context, LifecycleOwner lifecycleOwner, String name) {
        AccountsDB accountsDB = AccountsDB.getInstance(context);
        AccountsDao accountsDao = accountsDB.accountsDao();

        accountsDao.getAccountByName(name).observe(lifecycleOwner, accountsItem -> {
            oneAccount.setValue(accountsItem);
        });

    }



    @Override
    public void insert(Context context, AccountsItem accountsItem) {
        AccountsDB accountsDB = AccountsDB.getInstance(context);
        AccountsDao accountsDao = accountsDB.accountsDao();

        accountsDao.insert(accountsItem);
    }

}
