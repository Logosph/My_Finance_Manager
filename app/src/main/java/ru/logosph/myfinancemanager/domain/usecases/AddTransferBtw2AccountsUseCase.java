package ru.logosph.myfinancemanager.domain.usecases;

import android.content.Context;

import java.text.SimpleDateFormat;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        TransactionItem transactionItemFrom = new TransactionItem(
                name,
                false,
                simpleDateFormat.format(date),
                amount,
                fromAccount
        );

        TransactionItem transactionItemTo = new TransactionItem(
                name,
                true,
                simpleDateFormat.format(date),
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
