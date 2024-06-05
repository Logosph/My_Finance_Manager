package ru.logosph.myfinancemanager.data.account_repository;

import android.content.Context;


import java.util.ArrayList;

import ru.logosph.myfinancemanager.data.transaction_repository.TransactionRepositoryImpl;
import ru.logosph.myfinancemanager.domain.models.AccountsItem;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;

public class AccountRepositoryImpl implements AccountRepository {

    public ArrayList<AccountsItem> accounts;
    private AccountsItem oneAccount = null;

    @Override
    public void loadAccountsFromDB(Context context) {
        AccountsDB accountsDB = AccountsDB.getInstance(context);
        AccountsDao accountsDao = accountsDB.accountsDao();

        accounts = new ArrayList<>(accountsDao.getAllAccounts());
    }

    @Override
    public ArrayList<AccountsItem> getAccounts() {
        return accounts;
    }

    @Override
    public AccountsItem getOneAccount() {
        return oneAccount;
    }

    @Override
    public void updateBalanceByName(Context context, String name, double balance) {
        AccountsDB accountsDB = AccountsDB.getInstance(context);
        AccountsDao accountsDao = accountsDB.accountsDao();

        accountsDao.updateBalance(name, balance);
    }

    @Override
    public void getAccountByName(Context context, String name) {
        AccountsDB accountsDB = AccountsDB.getInstance(context);
        AccountsDao accountsDao = accountsDB.accountsDao();

        oneAccount = accountsDao.getAccountByName(name);
    }



    @Override
    public void insert(Context context, AccountsItem accountsItem) {
        AccountsDB accountsDB = AccountsDB.getInstance(context);
        AccountsDao accountsDao = accountsDB.accountsDao();

        accountsDao.insert(accountsItem);
    }

    @Override
    public void delete(Context context, AccountsItem accountsItem) {
        AccountsDB accountsDB = AccountsDB.getInstance(context);
        AccountsDao accountsDao = accountsDB.accountsDao();

        accountsDao.delete(accountsItem.name);

        TransactionRepositoryImpl transactionRepository = new TransactionRepositoryImpl();
        transactionRepository.deleteByAccount(context, accountsItem.name);
    }

    @Override
    public void rename(Context context, String oldName, String newName) {
        AccountsDB accountsDB = AccountsDB.getInstance(context);
        AccountsDao accountsDao = accountsDB.accountsDao();

        accountsDao.rename(oldName, newName);

        TransactionRepositoryImpl transactionRepository = new TransactionRepositoryImpl();
        transactionRepository.renameAccount(context, oldName, newName);
    }

}
