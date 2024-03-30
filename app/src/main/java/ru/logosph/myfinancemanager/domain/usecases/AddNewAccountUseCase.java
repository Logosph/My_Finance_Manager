package ru.logosph.myfinancemanager.domain.usecases;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ru.logosph.myfinancemanager.domain.enums.AddNewBalanceStates;
import ru.logosph.myfinancemanager.domain.models.AccountsItem;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;

public class AddNewAccountUseCase {

    MutableLiveData<AddNewBalanceStates> addNewBalanceState = new MutableLiveData<>();

    public LiveData<AddNewBalanceStates> getAddNewBalanceState() {
        return addNewBalanceState;
    }

    public void execute(AccountsItem accountsItem, LifecycleOwner lifecycleOwner, AccountRepository accountRepository, Context context) {

        accountRepository.getOneAccount().observe(lifecycleOwner, accountsItem1 -> {
            if (accountsItem1 == null) {
                new Thread(() -> {
                    accountRepository.insert(context, accountsItem);
                    addNewBalanceState.postValue(AddNewBalanceStates.SUCCESS);
                }).start();
            } else {
                addNewBalanceState.setValue(AddNewBalanceStates.ALREADY_EXISTS);
            }
        });
        accountRepository.getAccountByName(context, lifecycleOwner, accountsItem.getName());
    }
}
