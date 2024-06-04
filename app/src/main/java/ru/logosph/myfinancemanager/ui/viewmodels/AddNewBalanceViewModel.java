package ru.logosph.myfinancemanager.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.logosph.myfinancemanager.data.account_repository.AccountRepositoryImpl;
import ru.logosph.myfinancemanager.domain.enums.AddNewBalanceStates;
import ru.logosph.myfinancemanager.domain.models.AccountsItem;
import ru.logosph.myfinancemanager.domain.usecases.AddNewAccountUseCase;

public class AddNewBalanceViewModel extends ViewModel {

    volatile public MutableLiveData<AddNewBalanceStates> addNewBalanceState = new MutableLiveData<>();

    public void addNewBalance(
            double balance,
            double limit,
            int color,
            int icon,
            String name,
            LifecycleOwner lifecycleOwner,
            Context context) {
        AddNewAccountUseCase addNewAccountUseCase = new AddNewAccountUseCase();

        new Thread(() -> {
            AddNewBalanceStates state = addNewAccountUseCase.execute(
                    new AccountsItem(
                            name,
                            balance,
                            limit,
                            "rub",
                            color,
                            icon
                    ),
                    new AccountRepositoryImpl(),
                    context

            );
            addNewBalanceState.postValue(state);
        }).start();
    }
}
