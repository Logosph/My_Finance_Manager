package ru.logosph.myfinancemanager.domain.repository_interfaces;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ru.logosph.myfinancemanager.domain.models.AccountsItem;

public interface AccountRepository {

    MutableLiveData<ArrayList<AccountsItem>> accounts = new MutableLiveData<>();
    void loadAccountsFromDB(Context context, LifecycleOwner lifecycleOwner);

    LiveData<ArrayList<AccountsItem>> getAccounts();

    LiveData<AccountsItem> getOneAccount();

    void getAccountByName(Context context, LifecycleOwner lifecycleOwner, String name);

    void insert(Context context, AccountsItem accountsItem);
}
