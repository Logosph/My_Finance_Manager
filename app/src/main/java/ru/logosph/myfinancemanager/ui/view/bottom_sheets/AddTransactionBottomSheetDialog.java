package ru.logosph.myfinancemanager.ui.view.bottom_sheets;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.logosph.myfinancemanager.databinding.FragmentAddTransactionBottomSheetBinding;
import ru.logosph.myfinancemanager.ui.viewmodels.AddNewTransactionBottomSheetViewModel;

public class AddTransactionBottomSheetDialog extends BottomSheetDialogFragment {

    FragmentAddTransactionBottomSheetBinding binding;
    AddNewTransactionBottomSheetViewModel viewModel;
    OnDismissListener listener;

    public interface OnDismissListener {
        void onDismiss();
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddTransactionBottomSheetBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddNewTransactionBottomSheetViewModel.class);

        Bundle arguments = getArguments();
        if (arguments != null) {
            try {
                binding.typeOfTransactionTextView.setText(arguments.getString("type"));
                viewModel.isIncome = arguments.getString("type").equals("Income");
            } catch (Exception e) {
                binding.typeOfTransactionTextView.setText("Error. Please, try again.");
            }
            try {
                binding.forAccountTextView.setText(arguments.getString("account"));
                viewModel.account = arguments.getString("account");
            } catch (Exception e) {
                binding.forAccountTextView.setText("Error. Please, try again.");
            }
        }

        binding.selectDateButton.setText(new SimpleDateFormat("dd.MM.yyyy").format(viewModel.date));
        binding.selectDateButton.setOnClickListener(v -> {

            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                viewModel.date = new Date(selection);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                binding.selectDateButton.setText(sdf.format(viewModel.date));
            });
            datePicker.show(getParentFragmentManager(), "datePicker");

        });

        binding.submitTransactionButton.setOnClickListener(v -> {
            String name = null;
            String amount = null;
            int errors = 0;

            try {
                name = binding.nameEditText.getText().toString();
            } catch (NullPointerException e) {
                binding.fromAccountLayout.setError("Name is required");
                errors++;
            }

            try {
                amount = binding.amountEditText.getText().toString();
            } catch (NullPointerException e) {
                binding.amountInputLayout.setError("Amount is required");
                errors++;
            }

            if (errors > 0) {
                return;
            }
            viewModel.addTransaction(name, amount, getContext());
            viewModel.complete.observe(getViewLifecycleOwner(), complete -> {
                if (complete) {
                    if (listener != null) {
                        listener.onDismiss();
                    }
                    dismiss();
                }
            });
        });

        return binding.getRoot();
    }



}
