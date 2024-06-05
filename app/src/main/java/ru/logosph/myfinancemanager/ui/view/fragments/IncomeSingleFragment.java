package ru.logosph.myfinancemanager.ui.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.logosph.myfinancemanager.databinding.FragmentIncomeSingleBinding;

public class IncomeSingleFragment extends Fragment {

    FragmentIncomeSingleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIncomeSingleBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
