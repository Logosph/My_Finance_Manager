package ru.logosph.myfinancemanager.domain.models;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

public class AccountModel {
    public String name;
    public Double balance;
    public Integer icon = null;
    public Integer color = 0;
    public Double limit = 0.0;

    public AccountModel(String name, Double balance, Integer icon, Integer color, Double limit) {
        this.name = name;
        this.balance = balance;
        this.icon = icon;
        this.color = color;
        this.limit = limit;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof AccountModel)) {
            return false;
        }

        AccountModel account = (AccountModel) obj;

        return account.name.equals(name) &&
                account.balance.equals(balance) &&
                account.icon.equals(icon) &&
                account.color.equals(color) &&
                account.limit.equals(limit);
    }
}
