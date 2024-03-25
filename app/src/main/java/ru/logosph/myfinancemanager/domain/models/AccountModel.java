package ru.logosph.myfinancemanager.domain.models;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class AccountModel {
    public String name;
    public Double balance;
    public Drawable icon = null;
    public String color = "#000000";

    public AccountModel(String name, Double balance) {
        this.name = name;
        this.balance = balance;
    }

    public AccountModel(String name, Double balance, Drawable icon) {
        this.name = name;
        this.balance = balance;
        this.icon = icon;
    }

    public AccountModel(String name, Double balance, String color) {
        this.name = name;
        this.balance = balance;
        this.color = color;
    }

    public AccountModel(String name, Double balance, Drawable icon, String color) {
        this.name = name;
        this.balance = balance;
        this.icon = icon;
        this.color = color;
    }

}
