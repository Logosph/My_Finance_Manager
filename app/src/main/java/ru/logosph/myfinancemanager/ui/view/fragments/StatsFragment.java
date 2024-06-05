package ru.logosph.myfinancemanager.ui.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.Nullable;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.databinding.FragmentStatsBinding;
import ru.logosph.myfinancemanager.ui.viewmodels.StatsViewModel;

public class StatsFragment extends Fragment {
    FragmentStatsBinding binding;
    StatsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(StatsViewModel.class);

        try {
            String name = getArguments().getString("account");
            if (name == null) {
                throw new Exception("Name is null");
            }
            viewModel.setName(name);
        } catch (Exception e) {
            Log.d("MyTag", "onCreateView: " + e.getMessage());
            Navigation.findNavController(binding.getRoot()).popBackStack();
        }

        binding.topAppBar.setTitle(viewModel.getName());
        Log.d("MyTag", "onCreateView: " + viewModel.getName());

        // Создаем адаптер
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(), viewModel.getName());
        binding.viewPager.setAdapter(viewPagerAdapter);

        // Связываем TabLayout и ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setIcon(R.drawable.savings);
                            tab.setText(R.string.all);
                            break;
                        case 1:
                            tab.setIcon(R.drawable.profit);
                            tab.setText(R.string.income);
                            break;
                        case 2:
                            tab.setIcon(R.drawable.expenses);
                            tab.setText(R.string.expenses);
                            break;
                    }
                }
        ).attach();

        return binding.getRoot();
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        private String name;

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String name) {
            super(fragmentActivity);
            this.name = name;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Возвращаем фрагмент в зависимости от позиции
            Bundle bundle = new Bundle();
            bundle.putString("name", this.name);
            switch (position) {
                case 0:
                    AllSingleFragment allSingleFragment = new AllSingleFragment();
                    allSingleFragment.setArguments(bundle);
                    return allSingleFragment;
                case 1:
                    IncomeSingleFragment incomeSingleFragment = new IncomeSingleFragment();
                    incomeSingleFragment.setArguments(bundle);
                    return incomeSingleFragment;
                case 2:
                    ExpensesSingleFragment expensesSingleFragment = new ExpensesSingleFragment();
                    expensesSingleFragment.setArguments(bundle);
                    return expensesSingleFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 3; // количество вкладок
        }
    }
}