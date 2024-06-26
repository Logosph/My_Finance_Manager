package ru.logosph.myfinancemanager.ui.view.bottom_sheets;

import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.databinding.FragmentAddNewBalanceBottomSheetBinding;
import ru.logosph.myfinancemanager.domain.enums.AddNewBalanceStates;
import ru.logosph.myfinancemanager.ui.view.dialogs.ChooseIconDialog;
import ru.logosph.myfinancemanager.ui.view.dialogs.ColorPickerDialogFragment;
import ru.logosph.myfinancemanager.ui.viewmodels.AddNewBalanceViewModel;

public class AddNewBalanceBottomSheetDialogFragment extends BottomSheetDialogFragment {

    FragmentAddNewBalanceBottomSheetBinding binding;
    OnDissmissListener listener;


    public interface OnDissmissListener {
        void onDismiss();
    }

    public void setOnDismissListener(OnDissmissListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddNewBalanceBottomSheetBinding.inflate(inflater, container, false);
        AddNewBalanceViewModel viewModel = new ViewModelProvider(this).get(AddNewBalanceViewModel.class);
        
        
        binding.colorCircle.setOnClickListener(v -> {
            GradientDrawable colorDrawable = (GradientDrawable) binding.colorCircle.getBackground();
            int colorFromCircle = colorDrawable.getColor().getDefaultColor();
            ColorPickerDialogFragment colorPickerDialogFragment = ColorPickerDialogFragment.newInstance(colorFromCircle);

            colorPickerDialogFragment.setOnCloseListener(color -> {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.OVAL);
                drawable.setColor(color);
                binding.colorCircle.setBackground(drawable);
                binding.iconImageView.setColorFilter(color);
            });
            colorPickerDialogFragment.show(getParentFragmentManager(), "colorPickerDialogFragment");
        });

        setEditText(binding.initialBalanceEditText);
        setEditText(binding.limitEditText);
        setEditText(binding.balanceNameEditText);

        binding.addNewBalanceButton.setOnClickListener(v -> {
            double balance = 0, limit = 0;
            int color, icon = 0;
            String name;
            int errors = 0;

            try {
                balance = Double.parseDouble(binding.initialBalanceEditText.getText().toString());
            } catch (NumberFormatException e) {
                binding.initialBalanceInputLayout.setError(getResources().getString(R.string.enter_balance_error));
                errors++;
            }
            
            try {
                limit = Double.parseDouble(binding.limitEditText.getText().toString());
            } catch (NumberFormatException e) {
                binding.limitInputLayout.setError(getResources().getString(R.string.enter_limit_error));
                errors++;
            }

            if (viewModel.iconId != -1) {
                icon = viewModel.iconId;
            } else {
                Snackbar.make(binding.getRoot(), getResources().getString(R.string.choose_icon_error), Snackbar.LENGTH_LONG).show();
                errors++;
            }

            name = binding.balanceNameEditText.getText().toString();
            if (name.isEmpty()) {
                binding.balanceNameInputLayout.setError(getResources().getString(R.string.enter_name_error));
                errors++;
            }
            color = ((GradientDrawable) binding.colorCircle.getBackground()).getColor().getDefaultColor();

            if (errors == 0) {
                viewModel.addNewBalanceState.observe(getViewLifecycleOwner(), addNewBalanceStates -> {
                    if (addNewBalanceStates == AddNewBalanceStates.SUCCESS) {
                        if (listener != null) {
                            listener.onDismiss();
                        }
                        dismiss();
                    } else if (addNewBalanceStates == AddNewBalanceStates.ALREADY_EXISTS) {
                        Snackbar.make(binding.getRoot(), getResources().getString(R.string.balance_already_exists), Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(binding.getRoot(), getResources().getString(R.string.error), Snackbar.LENGTH_LONG).show();
                    }
                });
                viewModel.addNewBalance(balance, limit, color, icon, name, getViewLifecycleOwner(), getContext());
            }
        });

        binding.iconImageView.setOnClickListener(v -> {

            ChooseIconDialog chooseIconDialog = new ChooseIconDialog();
            chooseIconDialog.setListener(iconId -> {
                viewModel.iconId = iconId;
                binding.iconImageView.setImageResource(getResources().getIdentifier("icon_" + iconId, "drawable", getContext().getPackageName()));
            });
            chooseIconDialog.setColor(((GradientDrawable) binding.colorCircle.getBackground()).getColor().getDefaultColor());
            chooseIconDialog.show(getParentFragmentManager(), "chooseIconDialog");
        });

        return binding.getRoot();
    }

    public void setEditText(TextInputEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editText.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setError(null);
            }
        });
    }
}
