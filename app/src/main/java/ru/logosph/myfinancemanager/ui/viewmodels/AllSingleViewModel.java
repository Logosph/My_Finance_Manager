package ru.logosph.myfinancemanager.ui.viewmodels;

import android.content.Context;

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
import ru.logosph.myfinancemanager.domain.usecases.LoadTransactionsByAccountUseCase;

public class AllSingleViewModel extends ViewModel {
    private String name = null;
    public MutableLiveData<List<TransactionsAndDateItems>> transactions = new MutableLiveData<>();
    public MutableLiveData<LoadingStates> loadingState = new MutableLiveData<>(LoadingStates.DEFAULT);
    private boolean isInDollar = false;
    private double totalExpenses = 0;
    private double totalIncome = 0;
    private double totalExpensesInDollars = 0;
    private double totalIncomeInDollars = 0;

    public void setInDollar(boolean inDollar) {
        isInDollar = inDollar;
    }

    public boolean getInDollar() {
        return isInDollar;
    }

    public double getTotalExpensesInDollars() {
        return totalExpensesInDollars;
    }

    public double getTotalIncomeInDollars() {
        return totalIncomeInDollars;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void loadTransactions(Context context, String name) {
        new Thread(() -> {
            try {
                loadingState.postValue(LoadingStates.LOADING);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TransactionRepository transactionRepository = new TransactionRepositoryImpl();
                List<TransactionsAndDateItems> loadedTransactions = LoadTransactionsByAccountUseCase.execute(name, context, transactionRepository);
                transactions.postValue(loadedTransactions);

                if (loadedTransactions.isEmpty()) {
                    loadingState.postValue(LoadingStates.SUCESS);
                    return;
                }
                for (TransactionsAndDateItems transactionItem : loadedTransactions) {
                    if (transactionItem instanceof TransactionItem) {
                        if (!((TransactionItem) transactionItem).getIsIncome()) {
                            totalExpenses -= ((TransactionItem) transactionItem).getAmount();
                        } else {
                            totalIncome += ((TransactionItem) transactionItem).getAmount();
                        }
                    }
                }

                CurrencyRepository currencyRepository = new CurrencyRepositoryImpl();
                totalExpensesInDollars = CalculateDollarsUseCase.execute(currencyRepository, (int) totalExpenses);
                totalIncomeInDollars = CalculateDollarsUseCase.execute(currencyRepository, (int) totalIncome);

                loadingState.postValue(LoadingStates.SUCESS);
            } catch (Exception e) {
                loadingState.postValue(LoadingStates.ERROR);
            }
        }).start();
    }


    public enum LoadingStates {
        DEFAULT,
        LOADING,
        SUCESS,
        ERROR
    }
}
