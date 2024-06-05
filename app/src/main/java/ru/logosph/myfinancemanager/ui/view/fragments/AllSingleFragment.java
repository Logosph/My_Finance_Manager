package ru.logosph.myfinancemanager.ui.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.AbstractMap;
import java.util.Arrays;

import ru.logosph.myfinancemanager.databinding.FragmentAllSingleBinding;

public class AllSingleFragment extends Fragment {

    FragmentAllSingleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllSingleBinding.inflate(inflater, container, false);

        binding.analyticalPieChart.setDataChart(
                Arrays.asList(
                        new AbstractMap.SimpleEntry<>(4, "Свои проекты"),
                        new AbstractMap.SimpleEntry<>(6, "Соместные проекты"),
                        new AbstractMap.SimpleEntry<>(6, "Проекты поддержанные группой людей"),
                        new AbstractMap.SimpleEntry<>(2, "Неизвестные проекты")
                )
        );

        return binding.getRoot();
    }
}
