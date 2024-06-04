package ru.logosph.myfinancemanager.domain.repository_interfaces;

import android.content.Context;

import java.util.ArrayList;

import ru.logosph.myfinancemanager.domain.models.AccountsItem;

public interface AccountRepository {

    ArrayList<AccountsItem> accounts = new ArrayList<>();

    void loadAccountsFromDB(Context context);

    ArrayList<AccountsItem> getAccounts();

    AccountsItem getOneAccount();

    void updateBalanceByName(Context context, String name, double balance);

    void getAccountByName(Context context, String name);

    void insert(Context context, AccountsItem accountsItem);

    void delete(Context context, AccountsItem accountsItem);
}
