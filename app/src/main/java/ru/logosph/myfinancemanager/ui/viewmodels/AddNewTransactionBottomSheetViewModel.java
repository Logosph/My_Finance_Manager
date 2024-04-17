package ru.logosph.myfinancemanager.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

import ru.logosph.myfinancemanager.data.account_repository.AccountRepositoryImpl;
import ru.logosph.myfinancemanager.data.transaction_repository.TransactionRepositoryImpl;
import ru.logosph.myfinancemanager.domain.models.TransactionItem;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;
import ru.logosph.myfinancemanager.domain.repository_interfaces.TransactionRepository;
import ru.logosph.myfinancemanager.domain.usecases.AddTransactionUseCase;

public class AddNewTransactionBottomSheetViewModel extends ViewModel {
    public Date date;
    public String account;
    public Boolean isIncome;
    public MutableLiveData<Boolean> complete = new MutableLiveData<>(false);


    public void addTransaction(String name, String amount, Context context, LifecycleOwner lifecycleOwner) {
        TransactionItem transactionItem = new TransactionItem(
                name,
                isIncome,
                date.toString(),
                Double.parseDouble(amount),
                account
        );
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        AccountRepository accountRepository = new AccountRepositoryImpl();
        AddTransactionUseCase.execute(transactionItem, context, transactionRepository, accountRepository);

    }
}
