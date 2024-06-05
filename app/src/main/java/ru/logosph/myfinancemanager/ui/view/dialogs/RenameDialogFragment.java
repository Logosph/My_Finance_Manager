package ru.logosph.myfinancemanager.ui.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import ru.logosph.myfinancemanager.databinding.FragmentRenameDialogBinding;
import ru.logosph.myfinancemanager.ui.viewmodels.RenameViewModel;

public class RenameDialogFragment extends DialogFragment {

    FragmentRenameDialogBinding binding;
    RenameViewModel viewModel;

    public interface OnDismissListener {
        void onDismiss();
    }

    private OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        binding = FragmentRenameDialogBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(RenameViewModel.class);
        builder.setView(binding.getRoot());

        try {
            String oldName = getArguments().getString("oldName");
        } catch (NullPointerException e) {
            Snackbar.make(requireView(), "Error", Snackbar.LENGTH_SHORT).show();
            dismiss();
        }

        binding.cancelButton.setOnClickListener(v -> dismiss());
        binding.accountNameTextView.setText(getArguments().getString("oldName"));

        binding.saveButton.setOnClickListener(v -> {
            try {
                String newName = binding.newNameEditText.getText().toString();
            } catch (NullPointerException e) {
                binding.newNameInputLayout.setError("Enter new name");
                return;
            }

            viewModel.rename(
                    getArguments().getString("oldName"),
                    binding.newNameEditText.getText().toString(),
                    requireContext()
            );
        });

        viewModel.getRenameResult().observe(this, renameResult -> {
            switch (renameResult) {
                case LOADING:
                    binding.progressIndicator.setVisibility(View.VISIBLE);
                    binding.blocking.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressIndicator.setVisibility(View.GONE);
                    binding.blocking.setVisibility(View.GONE);
                    onDismissListener.onDismiss();
                    dismiss();
                    break;
                case ERROR:
                    binding.progressIndicator.setVisibility(View.GONE);
                    binding.blocking.setVisibility(View.GONE);
                    Snackbar.make(requireView(), "Error", Snackbar.LENGTH_SHORT).show();
                    dismiss();
                    break;
            }
        });


        return builder.create();
    }
}
