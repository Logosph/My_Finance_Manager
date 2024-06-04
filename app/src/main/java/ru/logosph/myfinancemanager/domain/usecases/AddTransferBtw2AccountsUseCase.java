package ru.logosph.myfinancemanager.domain.usecases;

import android.content.Context;

import java.util.Date;

import ru.logosph.myfinancemanager.domain.models.TransactionItem;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;
import ru.logosph.myfinancemanager.domain.repository_interfaces.TransactionRepository;

public class AddTransferBtw2AccountsUseCase {
    public static void execute(
            String name,
            String fromAccount,
            String toAccount,
            double amount,
            Date date,
            Context context,
            TransactionRepository transactionRepository,
            AccountRepository accountRepository
    ) {
        TransactionItem transactionItemFrom = new TransactionItem(
                name,
                false,
                date.toString(),
                amount,
                fromAccount
        );

        TransactionItem transactionItemTo = new TransactionItem(
                name,
                true,
                date.toString(),
                amount,
                toAccount
        );

        transactionRepository.insert(context, transactionItemFrom);
        transactionRepository.insert(context, transactionItemTo);

        accountRepository.getAccountByName(context, fromAccount);
        accountRepository.updateBalanceByName(
                context,
                fromAccount,
                accountRepository.getOneAccount().getBalance() - amount
        );

        accountRepository.getAccountByName(context, toAccount);
        accountRepository.updateBalanceByName(
                context,
                toAccount,
                accountRepository.getOneAccount().getBalance() + amount
        );

    }
}
