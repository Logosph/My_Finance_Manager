package ru.logosph.myfinancemanager.ui.adapters;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.RenderEffect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.databinding.ItemAccountBinding;
import ru.logosph.myfinancemanager.domain.models.AccountModel;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder> {

    ArrayList<AccountModel> accounts = new ArrayList<>();
    private int focusedItem = -1;

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
            holder.binding.cardView.setBackgroundTintList (
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
            holder.binding.cardView.setBackgroundTintList (
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AccountModel getAccount(int position) {
        return accounts.get(position);
    }

    public int getFocusedItem() {
        return focusedItem;
    }
}


