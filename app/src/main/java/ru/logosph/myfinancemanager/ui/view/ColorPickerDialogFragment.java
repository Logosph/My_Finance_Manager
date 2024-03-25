package ru.logosph.myfinancemanager.ui.view;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;

import ru.logosph.myfinancemanager.databinding.FragmentColorPickerDialogBinding;

/**
 * This class represents a dialog fragment for a color picker.
 * It allows the user to select a color using sliders for hue, saturation, and value (HSV).
 * The selected color is displayed in a color icon and its hex value is shown in an EditText.
 */
public class ColorPickerDialogFragment extends DialogFragment {

    /**
     * Interface for handling the closing of the dialog.
     * This interface needs to be implemented by the class that wants to receive the color selected in the dialog.
     */
    public interface OnDialogCloseListener {
        /**
         * Method that will be called when the dialog is closed.
         * @param color The color selected in the dialog.
         */
        void onClose(int color);
    }

    // Listener for the OnDialogClose event
    private OnDialogCloseListener closeListener;

    /**
     * Sets the listener for the OnDialogClose event.
     * @param listener The listener that will handle the event.
     */
    public void setOnCloseListener(OnDialogCloseListener listener) {
        this.closeListener = listener;
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     * @param color The initial color to be set in the dialog.
     * @return A new instance of fragment ColorPickerDialogFragment.
     */
    public static ColorPickerDialogFragment newInstance(int color) {
        ColorPickerDialogFragment fragment = new ColorPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt("color", color);
        fragment.setArguments(args);
        return fragment;
    }

    // Binding object for the color picker dialog fragment layout
    FragmentColorPickerDialogBinding binding;

    /**
     * Called to create the dialog.
     *
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Create a dialog builder
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());

        // Inflate the layout for the color picker dialog fragment
        binding = FragmentColorPickerDialogBinding.inflate(getLayoutInflater());

        // Set the view for the dialog
        builder.setView(binding.getRoot());

        // If there is a saved instance state, restore the slider values and update the color icon
        if (savedInstanceState != null) {
            binding.hueSlider.setValue(savedInstanceState.getFloat("hue"));
            binding.saturationSlider.setValue(savedInstanceState.getFloat("saturation"));
            binding.valueSlider.setValue(savedInstanceState.getFloat("value"));
            updateColorIcon();
        } else {
            // If there is an argument, restore the slider values and update the color icon
            if (getArguments() != null) {
                int color = getArguments().getInt("color");
                float[] hsv = new float[3];
                Color.colorToHSV(color, hsv);
                binding.hueSlider.setValue((int)hsv[0]);
                binding.saturationSlider.setValue((int)(hsv[1] * 100));
                binding.valueSlider.setValue((int)(hsv[2] * 100));
                updateColorIcon();
            }
        }


        // Set an OnClickListener for the confirm button
        binding.confirmButton.setOnClickListener(v -> {
            // If a closeListener has been set
            if (closeListener != null) {
                // Call the onClose method of the closeListener
                // Pass the currently selected color (converted from HSV to RGB) as an argument
                closeListener.onClose(Color.HSVToColor(new float[]{binding.hueSlider.getValue(), binding.saturationSlider.getValue() / 100f, binding.valueSlider.getValue() / 100f}));
            }
            // Dismiss the dialog
            dismiss();
        });

        // Set an OnClickListener for the cancel button
        binding.cancelButton.setOnClickListener(v -> {
            // Dismiss the dialog without returning any color
            dismiss();
        });
        // Add a text watcher to the hex color EditText
        binding.hexColorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // If the text was changed programmatically, return
                if ("programmatically".equals(binding.hexColorEditText.getTag())) {
                    return;
                }

                // If the text is empty, return
                if (charSequence.length() == 0) {
                    return;
                }

                // Convert the hex color to HSV and update the sliders
                try {
                    String hex = charSequence.toString();
                    if (hex.startsWith("#")) {
                        if (hex.length() < 7) {
                            hex += "0000000".substring(hex.length());
                        } else if (hex.length() > 7) {
                            binding.hexColorEditText.setText(hex.substring(0, 7));
                        }
                        int color = Color.parseColor(hex);
                        float[] hsv = new float[3];
                        Color.colorToHSV(color, hsv);
                        binding.hueSlider.setValue((int) hsv[0]);
                        binding.saturationSlider.setValue((int) (hsv[1] * 100));
                        binding.valueSlider.setValue((int) (hsv[2] * 100));
                    } else {
                        binding.hexColorEditText.setText("#");
                    }
                } catch (Exception e) {
                    Log.e("ColorPicker", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Setup the EditTexts and Sliders for hue, saturation, and value
        setupEditText(binding.hueEditText, binding.hueSlider, 0, 360);
        setupEditText(binding.saturationEditText, binding.saturationSlider, 0, 100);
        setupEditText(binding.valueEditText, binding.valueSlider, 0, 100);

        // Setup the Sliders and their backgrounds
        setupSlider(binding.hueSlider, this::updateHueBackground, binding.hueEditText);
        setupSlider(binding.saturationSlider, this::updateSaturationBackground, binding.saturationEditText);
        setupSlider(binding.valueSlider, this::updateValueBackground, binding.valueEditText);

        // Create and return the dialog
        return builder.create();
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it can later be reconstructed in a new instance of its process is restarted.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("hue", binding.hueSlider.getValue());
        outState.putFloat("saturation", binding.saturationSlider.getValue());
        outState.putFloat("value", binding.valueSlider.getValue());
    }

    /**
     * Updates the color of the color picker icon based on the current HSV values.
     */
    private void updateColorIcon() {
        Drawable iconDrawable = binding.colorPickerIcon.getDrawable();
        if (iconDrawable != null) {
            iconDrawable.setTint(Color.HSVToColor(new float[]{binding.hueSlider.getValue(), binding.saturationSlider.getValue() / 100f, binding.valueSlider.getValue() / 100f}));
            binding.colorPickerIcon.setImageDrawable(iconDrawable);
        }
    }

    /**
     * Updates the background of the hue slider based on the current HSV values.
     */
    private void updateHueBackground() {
        int width = binding.hueSlider.getWidth();
        int height = binding.hueSlider.getHeight();
        int[] colors = new int[width];
        for (int i = 0; i < width; i++) {
            colors[i] = Color.HSVToColor(new float[]{i * 360f / width, binding.saturationSlider.getValue() / 100f, binding.valueSlider.getValue() / 100f});
        }
        int[] colors2 = new int[width * height];
        for (int i = 0; i < height; i++) {
            System.arraycopy(colors, 0, colors2, i * width, width);
        }
        Bitmap bitmap = Bitmap.createBitmap(colors2, width, height, Bitmap.Config.ARGB_8888);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        binding.hueSlider.setBackground(drawable);
    }

    /**
     * Updates the background of the saturation slider based on the current HSV values.
     */
    private void updateSaturationBackground() {
        int width = binding.saturationSlider.getWidth();
        int height = binding.saturationSlider.getHeight();
        int[] colors = new int[width];
        for (int i = 0; i < width; i++) {
            colors[i] = Color.HSVToColor(new float[]{binding.hueSlider.getValue(), i * 1f / width, binding.valueSlider.getValue() / 100f});
        }
        int[] colors2 = new int[width * height];
        for (int i = 0; i < height; i++) {
            System.arraycopy(colors, 0, colors2, i * width, width);
        }
        Bitmap bitmap = Bitmap.createBitmap(colors2, width, height, Bitmap.Config.ARGB_8888);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        binding.saturationSlider.setBackground(drawable);
    }

    /**
     * Updates the background of the value slider based on the current HSV values.
     */
    private void updateValueBackground() {
        int width = binding.valueSlider.getWidth();
        int height = binding.valueSlider.getHeight();
        int[] colors = new int[width];
        for (int i = 0; i < width; i++) {
            colors[i] = Color.HSVToColor(new float[]{binding.hueSlider.getValue(), binding.valueSlider.getValue() / 100f, i * 1f / width});
        }
        int[] colors2 = new int[width * height];
        for (int i = 0; i < height; i++) {
            System.arraycopy(colors, 0, colors2, i * width, width);
        }
        Bitmap bitmap = Bitmap.createBitmap(colors2, width, height, Bitmap.Config.ARGB_8888);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        binding.valueSlider.setBackground(drawable);
    }

    /**
     * Sets up a slider with a change listener and a text watcher.
     *
     * @param slider           The slider to setup.
     * @param updateBackground The method to call to update the slider's background.
     * @param editText         The EditText associated with the slider.
     */
    private void setupSlider(Slider slider, Runnable updateBackground, EditText editText) {
        slider.post(() -> {
            updateBackground.run();
            editText.setText(String.valueOf((int) slider.getValue()));
            slider.addOnChangeListener((s, value, fromUser) -> {
                if (slider.getTag() != null) {
                    updateColorIcon();
                    updateAllBackgrounds();
                    return;
                }
                updateColorIcon();
                updateAllBackgrounds();
                editText.setTag("programmatically");
                editText.setText(String.valueOf((int) value));
                editText.setTag(null);
            });
        });
    }

    /**
     * Updates all slider backgrounds.
     */
    private void updateAllBackgrounds() {
        updateHueBackground();
        updateSaturationBackground();
        updateValueBackground();
    }

    /**
     * Sets up an EditText with a text watcher.
     *
     * @param editText The EditText to setup.
     * @param slider   The Slider associated with the EditText.
     * @param min      The minimum value for the EditText.
     * @param max      The maximum value for the EditText.
     */
    private void setupEditText(EditText editText, Slider slider, int min, int max) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ("programmatically".equals(editText.getTag())) {
                    Log.d("ColorPicker", "programmatically");
                    return;
                }
                try {
                    if (charSequence.length() == 0) {
                        return;
                    }
                    int value = Integer.parseInt(charSequence.toString());
                    if (value >= min && value <= max) {
                        slider.setTag("programmatically");
                        slider.setValue(value);
                        slider.setTag(null);
                    } else if (value < min) {
                        slider.setTag("programmatically");
                        editText.setText(String.valueOf(min));
                        slider.setValue(min);
                        slider.setTag(null);
                    } else {
                        slider.setTag("programmatically");
                        editText.setText(String.valueOf(max));
                        slider.setValue(max);
                        slider.setTag(null);
                    }
                } catch (Exception e) {
                    editText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}