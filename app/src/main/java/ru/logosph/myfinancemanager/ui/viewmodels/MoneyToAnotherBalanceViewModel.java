package ru.logosph.myfinancemanager.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;
import java.util.List;

import ru.logosph.myfinancemanager.data.account_repository.AccountRepositoryImpl;
import ru.logosph.myfinancemanager.data.transaction_repository.TransactionRepositoryImpl;
import ru.logosph.myfinancemanager.domain.models.AccountModel;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;
import ru.logosph.myfinancemanager.domain.repository_interfaces.TransactionRepository;
import ru.logosph.myfinancemanager.domain.usecases.AddTransferBtw2AccountsUseCase;
import ru.logosph.myfinancemanager.domain.usecases.LoadAccountsUseCase;

public class MoneyToAnotherBalanceViewModel extends ViewModel {

    public MutableLiveData<List<AccountModel>> accounts = new MutableLiveData<>();
    public MutableLiveData<LoadAccountsStates> loadAccountsStates = new MutableLiveData<>(LoadAccountsStates.LOADING);
    public MutableLiveData<LoadAccountsStates> addTransferStates = new MutableLiveData<>(LoadAccountsStates.DEFAULT);
    public Date date = null;


    public List<AccountModel> getAccounts() {
        return accounts.getValue();
    }

    public void loadAccounts(Context context) {
        loadAccountsStates.postValue(LoadAccountsStates.LOADING);
        try {
            new Thread(() -> {
                AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();

                accounts.postValue(LoadAccountsUseCase.execute(context, accountRepository));
                loadAccountsStates.postValue(LoadAccountsStates.SUCCESS);
            }).start();
        } catch (Exception e) {
            loadAccountsStates.postValue(LoadAccountsStates.ERROR);
        }
    }

    public void addTransfer(
            String fromAccount,
            String toAccount,
            double amount,
            Context context
    ) {
        new Thread(() -> {
            try {
                addTransferStates.postValue(LoadAccountsStates.LOADING);
                TransactionRepository transactionRepository = new TransactionRepositoryImpl();
                AccountRepository accountRepository = new AccountRepositoryImpl();
                AddTransferBtw2AccountsUseCase.execute(
                        "Transfer",
                        fromAccount,
                        toAccount,
                        amount,
                        date,
                        context,
                        transactionRepository,
                        accountRepository
                );
                addTransferStates.postValue(LoadAccountsStates.SUCCESS);
            } catch (Exception e) {
                addTransferStates.postValue(LoadAccountsStates.ERROR);
            }
        }).start();
    }

    public enum LoadAccountsStates {
        DEFAULT,
        LOADING,
        SUCCESS,
        ERROR
    }
}


