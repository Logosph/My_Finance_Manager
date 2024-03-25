package ru.logosph.myfinancemanager.domain.models;

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
    public String color;
    public Integer icon;

    public AccountsItem(String name, Double balance, Double limit, String currency, String color, Integer icon) {
        this.name = name;
        this.balance = balance;
        this.limit = limit;
        this.currency = currency;
        this.color = color;
        this.icon = icon;
    }
}
