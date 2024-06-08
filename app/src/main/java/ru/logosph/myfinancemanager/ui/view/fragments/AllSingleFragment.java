package ru.logosph.myfinancemanager.ui.view.fragments;

import android.animation.ObjectAnimator;
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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.databinding.FragmentAllSingleBinding;
import ru.logosph.myfinancemanager.ui.adapters.TransactionsSingleAdapter;
import ru.logosph.myfinancemanager.ui.view.HelperClasses.SlideInTopAnimator;
import ru.logosph.myfinancemanager.ui.viewmodels.AllSingleViewModel;

public class AllSingleFragment extends Fragment {

    FragmentAllSingleBinding binding;
    AllSingleViewModel viewModel;
    TransactionsSingleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllSingleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AllSingleViewModel.class);

        binding.blocking.setOnClickListener(v -> {
        });

        adapter = new TransactionsSingleAdapter(new ArrayList<>());
        binding.transactionsRecyclerView.setItemAnimator(new SlideInTopAnimator());
        binding.transactionsRecyclerView.setAdapter(adapter);
        binding.transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.analyticalPieChart.setTextFormatter(value -> value >= 0 ? "+" + value + " ₽" : "-" + value + " ₽");

        try {
            viewModel.setName(getArguments().getString("name"));
        } catch (NullPointerException e) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.mainFragment);
        }

        if (viewModel.loadingState.getValue() == AllSingleViewModel.LoadingStates.SUCESS) {
            binding.progressIndicator.setVisibility(View.GONE);
            binding.blocking.setVisibility(View.GONE);
        } else {
            viewModel.loadTransactions(requireContext(), viewModel.getName());
        }

        viewModel.loadingState.observe(getViewLifecycleOwner(), loadingStates -> {
            switch (loadingStates) {
                case DEFAULT:
                    binding.progressIndicator.setVisibility(View.GONE);
                    binding.blocking.setVisibility(View.GONE);
                    break;
                case LOADING:
                    binding.progressIndicator.setVisibility(View.VISIBLE);
                    binding.blocking.setVisibility(View.VISIBLE);
                    break;
                case SUCESS:
                    binding.blocking.setVisibility(View.GONE);
                    binding.progressIndicator.setVisibilityAfterHide(View.GONE);
                    binding.progressIndicator.hide();
                    binding.analyticalPieChart.setDataChart(
                            Arrays.asList(
                                    new AbstractMap.SimpleEntry<>((int) viewModel.getTotalExpenses(), "Expenses"),
                                    new AbstractMap.SimpleEntry<>((int) viewModel.getTotalIncome(), "Income")
                            )
                    );
                    adapter.updateData(viewModel.transactions.getValue());
                    break;
                case ERROR:
                    binding.blocking.setVisibility(View.GONE);
                    binding.progressIndicator.setVisibilityAfterHide(View.GONE);
                    binding.progressIndicator.hide();
                    Snackbar.make(binding.getRoot(), "Some error occurred. Try again later.", Snackbar.LENGTH_LONG).show();
                    break;
            }
        });

        binding.analyticalPieChart.setOnClickListener(v -> {
            if (viewModel.getInDollar()) {
                binding.analyticalPieChart.setTextFormatter(value -> value >= 0 ? "+" + value + " ₽" : "-" + value + " ₽");

                binding.analyticalPieChart.setDataChart(
                        Arrays.asList(
                                new AbstractMap.SimpleEntry<>((int) viewModel.getTotalExpenses(), "Expenses"),
                                new AbstractMap.SimpleEntry<>((int) viewModel.getTotalIncome(), "Income")
                        )
                );
                viewModel.setInDollar(false);
            } else {
                binding.analyticalPieChart.setTextFormatter(value -> value >= 0 ? "+" + value + " $" : "-" + value + " $");

                binding.analyticalPieChart.setDataChart(
                        Arrays.asList(
                                new AbstractMap.SimpleEntry<>((int) viewModel.getTotalExpensesInDollars(), "Expenses"),
                                new AbstractMap.SimpleEntry<>((int) viewModel.getTotalIncomeInDollars(), "Income")
                        )
                );
                viewModel.setInDollar(true);
            }
            // Создайте анимацию вращения
            ObjectAnimator rotation = ObjectAnimator.ofFloat(binding.analyticalPieChart, "rotationY", 0.0f, 360.0f);

            // Создайте анимацию масштабирования
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.analyticalPieChart, "scaleX", 1.0f, 0.8f, 1.0f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.analyticalPieChart, "scaleY", 1.0f, 0.8f, 1.0f);

            // Установите длительность анимаций
            rotation.setDuration(700);
            scaleX.setDuration(700);
            scaleY.setDuration(700);

            // Начните анимации
            rotation.start();
            scaleX.start();
            scaleY.start();
        });

        return binding.getRoot();
    }
}
