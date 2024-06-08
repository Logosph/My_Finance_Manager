package ru.logosph.myfinancemanager.ui.viewmodels;

import androidx.lifecycle.ViewModel;

public class StatsViewModel extends ViewModel {
    private String name = null;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
