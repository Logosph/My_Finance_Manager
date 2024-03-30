package ru.logosph.myfinancemanager.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import ru.logosph.myfinancemanager.data.account_repository.AccountRepositoryImpl;
import ru.logosph.myfinancemanager.databinding.FragmentMainBinding;
import ru.logosph.myfinancemanager.ui.adapters.AccountsAdapter;
import ru.logosph.myfinancemanager.ui.viewmodels.MainFragmentViewModel;

public class MainFragment extends Fragment {

    FragmentMainBinding binding;
    MainFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);
        AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();

        // Navigation example
//        binding.button.setOnClickListener(v -> {
//            NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//            NavController navController = navHostFragment.getNavController();
//            navController.navigate(R.id.action_mainFragment_to_statsFragment);
//        });

        viewModel.accounts.observe(getViewLifecycleOwner(), accounts -> {
            AccountsAdapter adapter = new AccountsAdapter(accounts);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        });

        viewModel.getAccounts(getContext(), getViewLifecycleOwner());

        binding.fabAddAccount.setOnClickListener(v -> {
            AddNewBalanceBottomSheetDialogFragment bottomSheetDialogFragment = new AddNewBalanceBottomSheetDialogFragment();
            bottomSheetDialogFragment.show(getParentFragmentManager(),  bottomSheetDialogFragment.getTag());
        });

        return binding.getRoot();
    }
}
