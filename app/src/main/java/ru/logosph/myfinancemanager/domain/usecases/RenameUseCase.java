package ru.logosph.myfinancemanager.domain.usecases;

import android.content.Context;

import ru.logosph.myfinancemanager.domain.repository_interfaces.AccountRepository;

public class RenameUseCase {

    public static void execute(
            String oldName,
            String newName,
            Context context,
            AccountRepository accountRepository
    ) {
        accountRepository.rename(context, oldName, newName);
    }
}
