package ru.logosph.myfinancemanager.ui.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
            GradientDrawable colorDrawable = (GradientDrawable) binding.colorCircle.getBackground();
            int colorFromCircle = colorDrawable.getColor().getDefaultColor();
            ColorPickerDialogFragment colorPickerDialogFragment = ColorPickerDialogFragment.newInstance(colorFromCircle);

            colorPickerDialogFragment.setOnCloseListener(color -> {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.OVAL);
                drawable.setColor(color);
                binding.colorCircle.setBackground(drawable);
            });
            colorPickerDialogFragment.show(getParentFragmentManager(), "colorPickerDialogFragment");
        });

        binding.changeColorButton.setOnClickListener(v -> {

        });

        return binding.getRoot();
    }

}
