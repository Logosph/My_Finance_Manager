package ru.logosph.myfinancemanager.ui.view.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.databinding.FragmentChooseIconDialogBinding;

public class ChooseIconDialog extends DialogFragment {

    FragmentChooseIconDialogBinding binding;
    private int color = -1;

    public void setColor(int color) {
        this.color = color;
    }

    public interface ChooseIconDialogListener {
        void onIconChosen(int icon);
    }

    private ChooseIconDialogListener listener;

    public void setListener(ChooseIconDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());

        binding = FragmentChooseIconDialogBinding.inflate(getLayoutInflater());

        builder.setView(binding.getRoot());

        for (int i = 1; i < 22; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(getResources().getIdentifier("icon_" + i, "drawable", getContext().getPackageName()));
            imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(150, 150));
            int finalI = i;
            imageView.setOnClickListener(v -> {
                listener.onIconChosen(finalI);
                dismiss();
            });
            if (color != -1) {
                imageView.setColorFilter(color);
            }
            binding.iconFlexableViewGroup.addView(imageView);

        }

        return builder.create();
    }
}
