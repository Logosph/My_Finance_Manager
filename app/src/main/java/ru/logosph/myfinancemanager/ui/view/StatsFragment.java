package ru.logosph.myfinancemanager.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.jetbrains.annotations.Nullable;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.databinding.FragmentStatsBinding;

public class StatsFragment extends Fragment {
    FragmentStatsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater, container, false);

        // Navigation example
//        binding.button.setOnClickListener(v -> {
//            NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//            NavController navController = navHostFragment.getNavController();
//            navController.navigate(R.id.action_statsFragment_to_mainFragment);
//        });

        return binding.getRoot();
    }
}
