package ru.logosph.myfinancemanager.domain.usecases;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ru.logosph.myfinancemanager.domain.models.AccountModel;
import ru.logosph.myfinancemanager.domain.models.AccountsItem;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;

public class LoadAccountsUseCase {

    public MutableLiveData<ArrayList<AccountModel>> accounts = new MutableLiveData<>();

    public void execute(Context context, LifecycleOwner lifecycleOwner, AccountRepository accountRepository) {
        accountRepository.getAccounts().observe(lifecycleOwner, accountsItems -> {
            ArrayList<AccountModel> accountModels = new ArrayList<>();
            for (AccountsItem accountsItem : accountsItems) {
                accountModels.add(convertToAccountModel(accountsItem));
            }
            accounts.setValue(accountModels);
        });
        accountRepository.loadAccountsFromDB(context, lifecycleOwner);
    }

    private AccountModel convertToAccountModel(AccountsItem accountsItem) {
        return new AccountModel(accountsItem.getName(), accountsItem.getBalance(), accountsItem.getIcon(), accountsItem.getColor(), accountsItem.getLimit());
    }


}
