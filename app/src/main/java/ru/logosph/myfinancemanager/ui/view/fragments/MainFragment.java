package ru.logosph.myfinancemanager.ui.view.fragments;

import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.data.account_repository.AccountRepositoryImpl;
import ru.logosph.myfinancemanager.databinding.FragmentMainBinding;
import ru.logosph.myfinancemanager.ui.adapters.AccountsAdapter;
import ru.logosph.myfinancemanager.ui.view.bottom_sheets.AddNewBalanceBottomSheetDialogFragment;
import ru.logosph.myfinancemanager.ui.view.bottom_sheets.AddTransactionBottomSheetDialog;
import ru.logosph.myfinancemanager.ui.viewmodels.AddNewTransactionBottomSheetViewModel;
import ru.logosph.myfinancemanager.ui.viewmodels.MainFragmentViewModel;

public class MainFragment extends Fragment {

    FragmentMainBinding binding;
    MainFragmentViewModel viewModel;
    AccountsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);
        AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();

        if (viewModel.isFabMenuOpen) {
            binding.fabAddAccount.hide();
            binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_app_bar_selected);
        } else {
            binding.fabAddAccount.show();
            binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_normal);
        }

        // Navigation example
//        binding.button.setOnClickListener(v -> {
//            NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//            NavController navController = navHostFragment.getNavController();
//            navController.navigate(R.id.action_mainFragment_to_statsFragment);
//        });

        viewModel.accounts.observe(getViewLifecycleOwner(), accounts -> {
            adapter = new AccountsAdapter(accounts);
            adapter.setOnItemClickListener(position -> {
                TransitionManager.beginDelayedTransition(binding.bottomAppBar, new Slide());
                if (position != -1) {
                    binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_app_bar_selected);
                    binding.fabAddAccount.hide();
                    viewModel.isFabMenuOpen = true;
                } else {
                    binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_normal);
                    binding.fabAddAccount.show();
                    viewModel.isFabMenuOpen = false;
                }
            });
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        });

        viewModel.getAccounts(getContext(), getViewLifecycleOwner());

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && binding.fabAddAccount.isShown() && !viewModel.isFabMenuOpen) {
                    binding.fabAddAccount.hide();
                } else if (dy < 0 && !binding.fabAddAccount.isShown() && !viewModel.isFabMenuOpen) {
                    binding.fabAddAccount.show();
                }
            }
        });

        binding.fabAddAccount.setOnClickListener(v -> {
            AddNewBalanceBottomSheetDialogFragment bottomSheetDialogFragment = new AddNewBalanceBottomSheetDialogFragment();
            bottomSheetDialogFragment.show(getParentFragmentManager(), bottomSheetDialogFragment.getTag());
        });

        binding.bottomAppBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.more) {
                Snackbar.make(binding.getRoot(), "More", Snackbar.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.add) {
                AddTransactionBottomSheetDialog bottomSheetDialogFragment = new AddTransactionBottomSheetDialog();
                Bundle args = new Bundle();
                args.putString("type", "Income");
                args.putString("account", adapter.getAccount(adapter.getFocusedItem()).name);
                bottomSheetDialogFragment.setArguments(args);
                bottomSheetDialogFragment.show(getParentFragmentManager(), bottomSheetDialogFragment.getTag());
                return true;
            } else if (itemId == R.id.remove) {
                AddTransactionBottomSheetDialog bottomSheetDialogFragment = new AddTransactionBottomSheetDialog();
                Bundle args = new Bundle();
                args.putString("type", "Expense");
                args.putString("account", adapter.getAccount(adapter.getFocusedItem()).name);
                bottomSheetDialogFragment.setArguments(args);
                bottomSheetDialogFragment.show(getParentFragmentManager(), bottomSheetDialogFragment.getTag());
                return true;
            } else if (itemId == R.id.transfer) {
                Snackbar.make(binding.getRoot(), "Transfer money", Snackbar.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.statistics) {
                Snackbar.make(binding.getRoot(), "Statistics", Snackbar.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.filter) {
                Snackbar.make(binding.getRoot(), "Filter", Snackbar.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.limit) {
                Snackbar.make(binding.getRoot(), "Limit", Snackbar.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });

        return binding.getRoot();
    }
}
