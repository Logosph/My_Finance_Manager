package ru.logosph.myfinancemanager.domain.usecases;

import android.content.Context;

import ru.logosph.myfinancemanager.domain.models.AccountsItem;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;

public class DeleteAccountUseCase {

    public static void execute(Context context, AccountRepository accountRepository, String account) {
        accountRepository.getAccountByName(context, account);
        AccountsItem accountsItem = accountRepository.getOneAccount();
        accountRepository.delete(context, accountsItem);
    }
}
