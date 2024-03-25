package ru.logosph.myfinancemanager.data.account_repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.logosph.myfinancemanager.domain.models.AccountsItem;

@Database(entities = {AccountsItem.class}, version = 1)
public abstract class AccountsDB extends RoomDatabase {
    private static AccountsDB instance;

    public abstract AccountsDao accountsDao();

    public static synchronized AccountsDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AccountsDB.class, "accounts_database")
                                .fallbackToDestructiveMigration()
                                .build();
        }
        return instance;
    }

}
