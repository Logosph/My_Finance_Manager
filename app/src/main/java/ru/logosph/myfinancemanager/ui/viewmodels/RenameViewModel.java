package ru.logosph.myfinancemanager.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.logosph.myfinancemanager.data.account_repository.AccountRepositoryImpl;
import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;
import ru.logosph.myfinancemanager.domain.usecases.RenameUseCase;

public class RenameViewModel extends ViewModel {

    private MutableLiveData<RenameResult> renameResult = new MutableLiveData<>(RenameResult.DEFAULT);

    public LiveData<RenameResult> getRenameResult() {
        return renameResult;
    }

    public void rename(String oldName, String newName, Context context) {
        new Thread(() -> {
            try {
                renameResult.postValue(RenameResult.LOADING);
                AccountRepository accountRepository = new AccountRepositoryImpl();
                RenameUseCase.execute(oldName, newName, context, accountRepository);
                renameResult.postValue(RenameResult.SUCCESS);
            } catch (Exception e) {
                renameResult.postValue(RenameResult.ERROR);
            }
        }).start();
    }

    public enum RenameResult {
        DEFAULT,
        LOADING,
        SUCCESS,
        ERROR
    }

}
