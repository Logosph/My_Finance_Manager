package ru.logosph.myfinancemanager.domain.usecases;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.logosph.myfinancemanager.domain.models.AccountModel;
import ru.logosph.myfinancemanager.domain.models.AccountsItem;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;

public class LoadAccountsUseCase {

    private static ArrayList<AccountModel> accounts;

    public static List<AccountModel> execute(Context context, AccountRepository accountRepository) {
        accountRepository.loadAccountsFromDB(context);
        ArrayList<AccountsItem> accountsItems = accountRepository.getAccounts();
        ArrayList<AccountModel> accountModels = new ArrayList<>();
        for (AccountsItem accountsItem : accountsItems) {
            accountModels.add(convertToAccountModel(accountsItem));
        }
        accounts = accountModels;
        return accountModels;
    }

    private static AccountModel convertToAccountModel(AccountsItem accountsItem) {
        return new AccountModel(accountsItem.getName(), accountsItem.getBalance(), accountsItem.getIcon(), accountsItem.getColor(), accountsItem.getLimit());
    }

    public static ArrayList<AccountModel> getAccounts() {
        return accounts;
    }
}
