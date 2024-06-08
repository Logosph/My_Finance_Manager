package ru.logosph.myfinancemanager.domain.usecases;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.logosph.myfinancemanager.domain.models.DateItem;
import ru.logosph.myfinancemanager.domain.models.TransactionItem;
import ru.logosph.myfinancemanager.domain.models.TransactionsAndDateItems;
import ru.logosph.myfinancemanager.domain.repository_interfaces.TransactionRepository;

public class LoadExpensesUseCase {
    public static List<TransactionsAndDateItems> execute(String name, Context context, TransactionRepository repo) throws ParseException {
        ArrayList<TransactionsAndDateItems> transactions = new ArrayList<>();
        List<TransactionItem> transactionItems = repo.getTransactionsByAccount(context, name);
        // Sort transactions by date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        transactionItems.sort((o1, o2) -> {
            try {
                return simpleDateFormat.parse(o2.getDate()).compareTo(simpleDateFormat.parse(o1.getDate()));
            } catch (ParseException | NullPointerException e) {
                throw new RuntimeException(e);
            }
        });

        if (transactionItems.isEmpty()) {
            return transactions;
        }

        // Insert DateItem before first transaction of the day
        SimpleDateFormat russianSDF = new SimpleDateFormat("dd MMMM yyyy");
        Date currentDate = null;
        for (TransactionItem transactionItem : transactionItems) {
            if (transactionItem.getIsIncome()) continue;
            Date date = simpleDateFormat.parse(transactionItem.getDate());
            if (currentDate == null || !currentDate.equals(date)) {
                currentDate = date;
                transactions.add(new DateItem(russianSDF.format(currentDate)));
            }
            transactions.add(transactionItem);
        }

        return transactions;
    }
}
