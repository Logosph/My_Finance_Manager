package ru.logosph.myfinancemanager.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ru.logosph.myfinancemanager.data.account_repository.AccountRepositoryImpl;
import ru.logosph.myfinancemanager.domain.models.AccountModel;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;
import ru.logosph.myfinancemanager.domain.usecases.LoadAccountsUseCase;

public class MainFragmentViewModel extends androidx.lifecycle.ViewModel {

    public MutableLiveData<ArrayList<AccountModel>> accounts = new MutableLiveData<>();

    public void getAccounts(Context context, LifecycleOwner lifecycleOwner) {
        LoadAccountsUseCase loadAccountsUseCase = new LoadAccountsUseCase();
        AccountRepository accountRepository = new AccountRepositoryImpl();
        loadAccountsUseCase.accounts.observe(lifecycleOwner, accountModels -> {
            accounts.setValue(accountModels);
        });
        loadAccountsUseCase.execute(context, lifecycleOwner, accountRepository);
    }
}
