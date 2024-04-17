package ru.logosph.myfinancemanager.data.transaction_repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.logosph.myfinancemanager.domain.models.TransactionItem;

@Database(entities = {TransactionItem.class}, version = 1)
public abstract class TransactionDB extends RoomDatabase {
    public abstract TransactionDao transactionDao();

    private static TransactionDB instance;

    public static synchronized TransactionDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TransactionDB.class, "transaction_database")
                                .fallbackToDestructiveMigration()
                                .build();
        }
        return instance;
    }

}
