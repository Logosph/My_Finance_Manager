package ru.logosph.myfinancemanager.domain.models;

public class DateItem implements TransactionsAndDateItems {
    private String date;

    public DateItem(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
