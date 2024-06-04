package ru.logosph.myfinancemanager.ui.view.bottom_sheets;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import kotlin.time.AbstractLongTimeSource;
import ru.logosph.myfinancemanager.databinding.FragmentMoneyToAnotherBalanceBottomSheetBinding;
import ru.logosph.myfinancemanager.ui.viewmodels.MoneyToAnotherBalanceViewModel;

public class MoneyToAnotherBalanceBSFragment extends BottomSheetDialogFragment {

    FragmentMoneyToAnotherBalanceBottomSheetBinding binding;
    MoneyToAnotherBalanceViewModel viewModel;

    public interface OnDismissListener {
        void onDismiss();
    }

    private OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMoneyToAnotherBalanceBottomSheetBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MoneyToAnotherBalanceViewModel.class);

        // * Getting the name of the account from the arguments
        viewModel.loadAccounts(requireContext());
        final String name;
        String vulnerable = "";
        try {
            vulnerable = getArguments().getString("name");
        } catch (NullPointerException e) {
            Snackbar.make(binding.getRoot(), "Error loading account", Snackbar.LENGTH_SHORT).show();
            dismiss();
        }
        name = vulnerable;
        binding.selectedAccountTextView.setText(name);

        // * Obtaining the list of accounts
        viewModel.accounts.observe(getViewLifecycleOwner(), accounts -> {

            String[] accountsNames = new String[accounts.size() + 1];
            accountsNames[0] = "<nothing>";
            for (int i = 0; i < accounts.size(); i++) {
                accountsNames[i + 1] = accounts.get(i).name;
            }
            binding.toAccountAutoCompleteTextView.setSimpleItems(accountsNames);
        });

        viewModel.addTransferStates.observe(getViewLifecycleOwner(), addTransferStates -> {
            switch (addTransferStates) {
                case DEFAULT:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.blockingView.setVisibility(View.GONE);
                    break;
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.blockingView.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.blockingView.setVisibility(View.GONE);
                    onDismissListener.onDismiss();
                    dismiss();
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.blockingView.setVisibility(View.GONE);
                    Snackbar.make(binding.getRoot(), "Error transferring money", Snackbar.LENGTH_SHORT).show();
                    break;
            }
        });

        // * Removing the error message when the user selects an account
        binding.toAccountAutoCompleteTextView.setOnClickListener(v -> {
            binding.toAccountInputLayout.setError(null);
        });

        // * Adding progress bar and blocking view when loading accounts
        viewModel.loadAccountsStates.observe(getViewLifecycleOwner(), loadAccountsStates -> {
            switch (loadAccountsStates) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.blockingView.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.blockingView.setVisibility(View.GONE);
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.blockingView.setVisibility(View.GONE);
                    Snackbar.make(binding.getRoot(), "Error loading accounts", Snackbar.LENGTH_SHORT).show();
                    dismiss();
                    break;
            }
        });

        binding.amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.amountInputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.amountInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.amountInputLayout.setError(null);
            }
        });

        // * Processing the date picker button click
        binding.datePickerButton.setOnClickListener(v -> {
            binding.datePickerButton.setError(null);
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                viewModel.date = new Date(selection);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                binding.datePickerButton.setText(sdf.format(viewModel.date));
            });
            datePicker.show(getParentFragmentManager(), "datePicker");
        });

        // * Processing the transfer button click
        binding.transferButton.setOnClickListener(v -> {
            String toAccount = binding.toAccountAutoCompleteTextView.getText().toString();
            if (toAccount.equals("<nothing>") || toAccount.isEmpty()) {
                binding.toAccountInputLayout.setError("Select account");
            } else if (toAccount.equals(name)) {
                binding.toAccountInputLayout.setError("Cannot transfer to the same account");
            }
            if (viewModel.date == null) {
                binding.datePickerButton.setError("Select date");
            }
            String amount = binding.amountEditText.getText().toString();
            Integer amountInt = 0;
            if (amount.isEmpty()) {
                binding.amountInputLayout.setError("Enter amount");
            } else {
                amountInt = Integer.parseInt(amount);
            }


            // * Processing the transfer
            if (
                    !toAccount.equals("<nothing>") &&
                    !toAccount.isEmpty() &&
                    viewModel.date != null &&
                    !amount.isEmpty() &&
                    !toAccount.equals(name)
            ) {
                viewModel.addTransfer(
                        name,
                        toAccount,
                        amountInt,
                        requireContext()
                );
            }
        });

        binding.blockingView.setOnClickListener(v -> {});

        return binding.getRoot();
    }
}
