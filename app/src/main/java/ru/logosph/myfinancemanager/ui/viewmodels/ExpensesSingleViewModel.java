package ru.logosph.myfinancemanager.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.logosph.myfinancemanager.data.currency_repository.CurrencyRepositoryImpl;
import ru.logosph.myfinancemanager.data.transaction_repository.TransactionRepositoryImpl;
import ru.logosph.myfinancemanager.domain.models.TransactionItem;
import ru.logosph.myfinancemanager.domain.models.TransactionsAndDateItems;
import ru.logosph.myfinancemanager.domain.repository_interfaces.CurrencyRepository;
import ru.logosph.myfinancemanager.domain.repository_interfaces.TransactionRepository;
import ru.logosph.myfinancemanager.domain.usecases.CalculateDollarsUseCase;
import ru.logosph.myfinancemanager.domain.usecases.LoadExpensesUseCase;
import ru.logosph.myfinancemanager.domain.usecases.LoadIncomesByAccountName;

public class ExpensesSingleViewModel extends ViewModel {

    private final MutableLiveData<List<TransactionsAndDateItems>> transactions = new MutableLiveData<>();
    private final MutableLiveData<LoadingState> loadingState = new MutableLiveData<>(LoadingState.DEFAULT);
    private int total = 0;
    private String accountName;
    private double totalInDollars = 0;

    public void setTotalInDollars(double totalInDollars) {
        this.totalInDollars = totalInDollars;
    }

    public double getTotalInDollars() {
        return totalInDollars;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public LiveData<LoadingState> getLoadingState() {
        return loadingState;
    }

    public LiveData<List<TransactionsAndDateItems>> getTransactions() {
        return transactions;
    }

    public void loadTransactions(Context context) {
        new Thread(() -> {
            try {
                loadingState.postValue(LoadingState.LOADING);
                TransactionRepository repo = new TransactionRepositoryImpl();
                List<TransactionsAndDateItems> data = LoadExpensesUseCase.execute(accountName, context, repo);
                transactions.postValue(data);
                for (TransactionsAndDateItems transactionsAndDateItems : data) {
                    if (transactionsAndDateItems instanceof TransactionItem) {
                        total += (int) ((TransactionItem)transactionsAndDateItems).getAmount();
                    }
                }

                CurrencyRepository currencyRepository = new CurrencyRepositoryImpl();
                totalInDollars = Math.round(CalculateDollarsUseCase.execute(currencyRepository, total) * 100.0) / 100.0;

                loadingState.postValue(LoadingState.SUCCESS);
            } catch (Exception e) {
                loadingState.postValue(LoadingState.ERROR);
            }
        }).start();
    }

    public enum LoadingState {
        DEFAULT,
        LOADING,
        SUCCESS,
        ERROR
    }
}
