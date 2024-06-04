package ru.logosph.myfinancemanager.domain.models;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "accounts_table")
public class AccountsItem {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public Double balance;
    public Double limit;
    public String currency;
    public Integer color;
    public Integer icon;

    public AccountsItem(String name, Double balance, Double limit, String currency, Integer color, Integer icon) {
        this.name = name;
        this.balance = balance;
        this.limit = limit;
        this.currency = currency;
        this.color = color;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getLimit() {
        return limit;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getColor() {
        return color;
    }

    public Integer getIcon() {
        return icon;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        AccountsItem accountsItem = (AccountsItem) obj;
        return id == accountsItem.id &&
                name.equals(accountsItem.name) &&
                balance.equals(accountsItem.balance) &&
                limit.equals(accountsItem.limit) &&
                currency.equals(accountsItem.currency) &&
                color.equals(accountsItem.color) &&
                icon.equals(accountsItem.icon);
    }
}
