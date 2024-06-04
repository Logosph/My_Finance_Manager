package ru.logosph.myfinancemanager.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.logosph.myfinancemanager.data.account_repository.AccountRepositoryImpl;
import ru.logosph.myfinancemanager.domain.models.AccountModel;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;
import ru.logosph.myfinancemanager.domain.usecases.DeleteAccountUseCase;
import ru.logosph.myfinancemanager.domain.usecases.LoadAccountsUseCase;

public class MainFragmentViewModel extends ViewModel {

    public MutableLiveData<ArrayList<AccountModel>> accounts = new MutableLiveData<>();
    public Boolean isFabMenuOpen = false;

    public void getAccounts(Context context) {
        new Thread(() -> {
            // Load the accounts from the database
            AccountRepository accountRepository = new AccountRepositoryImpl();
            LoadAccountsUseCase.execute(context, accountRepository);
            ArrayList<AccountModel> accountModels = LoadAccountsUseCase.getAccounts();
            accounts.postValue(accountModels);
        }).start();
    }

    public void deleteAccount(Context context, String account) {
        new Thread(() -> {
            AccountRepository accountRepository = new AccountRepositoryImpl();
            DeleteAccountUseCase.execute(context, accountRepository, account);
            getAccounts(context);
        }).start();
    }
}
