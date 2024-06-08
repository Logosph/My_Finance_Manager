package ru.logosph.myfinancemanager.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.databinding.ItemDateBinding;
import ru.logosph.myfinancemanager.databinding.ItemTransactionBinding;
import ru.logosph.myfinancemanager.domain.models.DateItem;
import ru.logosph.myfinancemanager.domain.models.TransactionItem;
import ru.logosph.myfinancemanager.domain.models.TransactionsAndDateItems;

public class TransactionsSingleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_DATE = 0;
    private static final int TYPE_TRANSACTION = 1;

    private List<TransactionsAndDateItems> data;

    public void updateData(List<TransactionsAndDateItems> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyItemRangeRemoved(0, data.size());
    }

    public TransactionsSingleAdapter(List<TransactionsAndDateItems> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        // Check if current item is a date item
        if (isPositionDate(position)) {
            return TYPE_DATE;
        } else {
            return TYPE_TRANSACTION;
        }
    }

    public boolean isPositionDate(int position) {
        return data.get(position) instanceof DateItem;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
            return new DateViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
            return new TransactionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DateViewHolder) {
            ((DateViewHolder) holder).bindDate((DateItem) data.get(position));
        } else if (holder instanceof TransactionViewHolder) {
            ((TransactionViewHolder) holder).bindTransaction((TransactionItem) data.get(position));
        }
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {

        ItemDateBinding binding;

        public DateViewHolder(View itemView) {
            super(itemView);
            binding = ItemDateBinding.bind(itemView);
        }

        public void bindDate(DateItem dateItem) {
            binding.tvDate.setText(dateItem.getDate());
        }
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        ItemTransactionBinding binding;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            binding = ItemTransactionBinding.bind(itemView);
        }

        public void bindTransaction(TransactionItem transactionItem) {
            binding.name.setText(transactionItem.getName());
            if (transactionItem.getIsIncome()) {
                binding.amount.setTextColor(Color.parseColor("#43d143"));
                binding.amount.setText("+" + transactionItem.getAmount() + " ₽");
            } else {
                binding.amount.setTextColor(Color.parseColor("#c70000"));
                binding.amount.setText("-" + transactionItem.getAmount() + " ₽");
            }
        }
    }
}
