package ru.logosph.myfinancemanager.ui.view.HelperClasses;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import ru.logosph.myfinancemanager.domain.models.AccountModel;
import ru.logosph.myfinancemanager.domain.models.AccountsItem;

public class AccountDiffCallback extends DiffUtil.ItemCallback<AccountModel> {

    @Override
    public boolean areItemsTheSame(@NonNull AccountModel oldItem, @NonNull AccountModel newItem) {
        return oldItem.name == newItem.name;
    }

    @Override
    public boolean areContentsTheSame(@NonNull AccountModel oldItem, @NonNull AccountModel newItem) {
        return oldItem.equals(newItem);
    }
}
