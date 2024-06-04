package ru.logosph.myfinancemanager.domain.usecases;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import ru.logosph.myfinancemanager.domain.models.AccountsItem;
import ru.logosph.myfinancemanager.domain.models.TransactionItem;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;
import ru.logosph.myfinancemanager.domain.repository_interfaces.TransactionRepository;

public class AddTransactionUseCase {
    public static void execute(
            TransactionItem transactionItem,
            Context context,
            @NonNull TransactionRepository transactionRepository,
            @NonNull AccountRepository accountRepository
    ) {
        transactionRepository.insert(context, transactionItem);
        accountRepository.getAccountByName(context, transactionItem.getAccount());
        AccountsItem accountsItem = accountRepository.getOneAccount();

        if (transactionItem.getIsIncome()) {
            accountRepository.updateBalanceByName(
                    context,
                    transactionItem.getAccount(),
                    accountsItem.getBalance() + transactionItem.getAmount()
            );
        } else {
            accountRepository.updateBalanceByName(
                    context,
                    transactionItem.getAccount(),
                    accountsItem.getBalance() - transactionItem.getAmount()
            );
        }
    }
}
