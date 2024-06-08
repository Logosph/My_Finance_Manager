package ru.logosph.myfinancemanager.ui.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import ru.logosph.myfinancemanager.databinding.FragmentIncomeSingleBinding;
import ru.logosph.myfinancemanager.ui.adapters.TransactionsSingleAdapter;
import ru.logosph.myfinancemanager.ui.view.HelperClasses.SlideInTopAnimator;
import ru.logosph.myfinancemanager.ui.viewmodels.IncomeSingleViewModel;

public class IncomeSingleFragment extends Fragment {

    FragmentIncomeSingleBinding binding;
    IncomeSingleViewModel viewModel;
    TransactionsSingleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIncomeSingleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(IncomeSingleViewModel.class);

        try {
            viewModel.setAccountName(getArguments().getString("name"));
        } catch (NullPointerException e) {
            Navigation.findNavController(binding.getRoot()).popBackStack();
        }

        adapter = new TransactionsSingleAdapter(new ArrayList<>());
        binding.recyclerView.setItemAnimator(new SlideInTopAnimator());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.blocking.setOnClickListener(v -> {
        });

        binding.progress.setVisibilityAfterHide(View.GONE);

        viewModel.getLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            switch (loadingState) {
                case DEFAULT:
                    binding.blocking.setVisibility(View.GONE);
                    binding.progress.setVisibility(View.GONE);
                    break;
                case LOADING:
                    binding.blocking.setVisibility(View.VISIBLE);
                    binding.progress.show();
                    break;
                case SUCCESS:
                    binding.blocking.setVisibility(View.GONE);
                    binding.progress.hide();
                    adapter.updateData(viewModel.getTransactions().getValue());
                    binding.incomeRub.setText("+" + viewModel.getTotal() + " ₽");
                    binding.incomeUsd.setText("+" + viewModel.getTotalInDollars() + " $");
                    break;
                case ERROR:
                    binding.blocking.setVisibility(View.GONE);
                    binding.progress.setVisibility(View.GONE);
                    Snackbar.make(binding.getRoot(), "Some error occurred", Snackbar.LENGTH_SHORT).show();
                    break;
            }
        });

        if (viewModel.getLoadingState().getValue() == IncomeSingleViewModel.LoadingState.SUCCESS) {
            binding.progress.setVisibility(View.GONE);
            binding.blocking.setVisibility(View.GONE);
        } else {
            viewModel.loadTransactions(getContext());
        }



        return binding.getRoot();
    }
}
