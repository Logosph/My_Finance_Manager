package ru.logosph.myfinancemanager.ui.view;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.databinding.FragmentBottomSheetBinding;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    FragmentBottomSheetBinding binding;
    boolean state = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false);

        binding.colorCircle.setOnClickListener(v -> {
            ColorPickerDialogFragment colorPickerDialogFragment = new ColorPickerDialogFragment();
            colorPickerDialogFragment.show(getParentFragmentManager(), "colorPickerDialogFragment");
        });

        binding.changeColorButton.setOnClickListener(v -> {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            if (state) {
                drawable.setColor(getResources().getColor(R.color.md_theme_errorContainer_highContrast, null));
                binding.colorCircle.setBackground(drawable);
                state = false;
            } else {
                drawable.setColor(getResources().getColor(R.color.md_theme_primary, null));
                binding.colorCircle.setBackground(drawable);
                state = true;
            }
        });

        return binding.getRoot();
    }

}
