package ru.logosph.myfinancemanager.domain.usecases;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ru.logosph.myfinancemanager.domain.enums.AddNewBalanceStates;
import ru.logosph.myfinancemanager.domain.models.AccountsItem;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;

public class AddNewAccountUseCase {

    public AddNewBalanceStates execute(AccountsItem accountsItem, AccountRepository accountRepository, Context context) {

        accountRepository.getAccountByName(context, accountsItem.getName());
        AccountsItem accountsItemTaken = accountRepository.getOneAccount();
        if (accountsItemTaken == null) {
            accountRepository.insert(context, accountsItem);
            return AddNewBalanceStates.SUCCESS;
        } else {
            return AddNewBalanceStates.ALREADY_EXISTS;
        }
    }
}
