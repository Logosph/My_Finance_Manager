package ru.logosph.myfinancemanager.domain.models;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

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
}
