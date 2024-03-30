package ru.logosph.myfinancemanager.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.databinding.ItemAccountBinding;
import ru.logosph.myfinancemanager.domain.models.AccountModel;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder> {

    ArrayList<AccountModel> accounts = new ArrayList<>();

    public AccountsAdapter(ArrayList<AccountModel> accounts) {
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountsViewHolder(
                ItemAccountBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder holder, int position) {
        AccountModel account = accounts.get(position);
        holder.binding.accountName.setText(account.name);
        holder.binding.amountOfMoney.setText(String.valueOf(account.balance));
        if (account.icon != null) {
            // holder.binding.accountImage.setImageDrawable(account.icon);
            holder.binding.accountImage.setColorFilter(account.color);
        } else {
            holder.binding.accountImage.setImageResource(R.drawable.account_circle);
            holder.binding.accountImage.setColorFilter(account.color);
        }
        holder.binding.limit.setText(holder.itemView.getResources().getString(R.string.left_text) + " " + String.valueOf(account.limit));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    class AccountsViewHolder extends RecyclerView.ViewHolder {
        ItemAccountBinding binding;

        public AccountsViewHolder(ItemAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
