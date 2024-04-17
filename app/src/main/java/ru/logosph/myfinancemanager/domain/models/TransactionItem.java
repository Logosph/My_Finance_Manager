package ru.logosph.myfinancemanager.domain.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaction_table")
public class TransactionItem {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Boolean isIncome;
    private String date;
    private double amount;
    private String account;

    public TransactionItem(String name, Boolean isIncome, String date, double amount, String account) {
        this.name = name;
        this.isIncome = isIncome;
        this.date = date;
        this.amount = amount;
        this.account = account;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsIncome() {
        return isIncome;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getAccount() {
        return account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsIncome(Boolean isIncome) {
        this.isIncome = isIncome;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAccount(String account) {
        this.account = account;
    }




}

