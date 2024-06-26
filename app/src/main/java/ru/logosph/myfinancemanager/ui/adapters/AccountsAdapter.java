package ru.logosph.myfinancemanager.ui.adapters;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.databinding.ItemAccountBinding;
import ru.logosph.myfinancemanager.domain.models.AccountModel;
import ru.logosph.myfinancemanager.ui.view.HelperClasses.AccountDiffCallback;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder> {

    ArrayList<AccountModel> accounts = new ArrayList<>();
    AccountDiffCallback accountDiffCallback = new AccountDiffCallback();
    private int focusedItem = -1;

    public OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AccountsAdapter() {
    }

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

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int position1 = holder.getAdapterPosition();
                if (position1 != RecyclerView.NO_POSITION && focusedItem != position1) {
                    int oldFocusedItem = focusedItem;
                    focusedItem = position1;
                    notifyItemChanged(oldFocusedItem);
                    notifyItemChanged(focusedItem);
                    listener.onItemClick(position1);
                } else {
                    focusedItem = -1;
                    notifyItemChanged(position1);
                    listener.onItemClick(-1);
                }
            }
        });

        ObjectAnimator animatorFadeIn;
        if (focusedItem == position) {
            holder.binding.cardView.setBackgroundTintList(
                    ColorStateList.valueOf(
                            holder.itemView.getResources().getColor(
                                    R.color.md_theme_surfaceContainerHighest,
                                    holder.itemView.getContext().getTheme()
                            )
                    )
            );
            animatorFadeIn = ObjectAnimator.ofFloat(
                    holder.binding.cardView,
                    "elevation",
                    holder.binding.cardView.getElevation(),
                    15
            );
        } else {
            holder.binding.cardView.setBackgroundTintList(
                    ColorStateList.valueOf(
                            holder.itemView.getResources().getColor(
                                    R.color.md_theme_surfaceContainer,
                                    holder.itemView.getContext().getTheme()
                            )
                    )
            );
            animatorFadeIn = ObjectAnimator.ofFloat(
                    holder.binding.cardView,
                    "elevation",
                    holder.binding.cardView.getElevation(),
                    0
            );
        }
        animatorFadeIn.setDuration(500);
        animatorFadeIn.start();

        AccountModel account = accounts.get(position);
        holder.binding.accountName.setText(account.name);
        holder.binding.amountOfMoney.setText(String.valueOf(account.balance));
        if (account.icon != null) {
            holder.binding
                    .accountImage
                    .setImageResource(
                            holder.itemView
                                    .getResources()
                                    .getIdentifier(
                                            "icon_" + account.icon,
                                            "drawable",
                                            holder.itemView.getContext().getPackageName()
                                    )
                    );
        }
        holder.binding.accountImage.setColorFilter(account.color);
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AccountModel getAccount(int position) {
        return accounts.get(position);
    }

    public int getFocusedItem() {
        return focusedItem;
    }

    public void updateList(ArrayList<AccountModel> newAccounts) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return accounts.size();
            }

            @Override
            public int getNewListSize() {
                return newAccounts.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return accountDiffCallback.areItemsTheSame(accounts.get(oldItemPosition), newAccounts.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return accountDiffCallback.areContentsTheSame(accounts.get(oldItemPosition), newAccounts.get(newItemPosition));
            }
        });

        accounts.clear();
        accounts.addAll(newAccounts);
        focusedItem = -1;
        diffResult.dispatchUpdatesTo(this);

    }

}


