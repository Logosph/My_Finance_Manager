package ru.logosph.myfinancemanager.ui.view.fragments;

import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import ru.logosph.myfinancemanager.R;
import ru.logosph.myfinancemanager.databinding.FragmentMainBinding;
import ru.logosph.myfinancemanager.ui.adapters.AccountsAdapter;
import ru.logosph.myfinancemanager.ui.view.bottom_sheets.AddNewBalanceBottomSheetDialogFragment;
import ru.logosph.myfinancemanager.ui.view.bottom_sheets.AddTransactionBottomSheetDialog;
import ru.logosph.myfinancemanager.ui.view.bottom_sheets.MoneyToAnotherBalanceBSFragment;
import ru.logosph.myfinancemanager.ui.view.dialogs.ChooseIconDialog;
import ru.logosph.myfinancemanager.ui.view.dialogs.RenameDialogFragment;
import ru.logosph.myfinancemanager.ui.viewmodels.MainFragmentViewModel;

/**
 * MainFragment is the main screen of the application.
 * It displays a list of accounts and provides options to add new accounts or transactions.
 */
public class MainFragment extends Fragment {

    FragmentMainBinding binding;
    MainFragmentViewModel viewModel;
    AccountsAdapter adapter;

    /**
     * This method is called to have the fragment instantiate its user interface view.
     * It inflates the layout for the fragment, sets up the ViewModel, RecyclerView and listeners.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);
        // Get the ViewModel for this fragment
        viewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

        // Set up the FloatingActionButton and BottomAppBar
        binding.fabAddAccount.setVisibility(viewModel.isFabMenuOpen ? View.GONE : View.VISIBLE);
        binding.bottomAppBar.replaceMenu(viewModel.isFabMenuOpen ? R.menu.menu_bottom_app_bar_selected : R.menu.menu_bottom_bar_normal);

        adapter = new AccountsAdapter();
        adapter.setOnItemClickListener(position -> {
                    // Handle item click
                    TransitionManager.beginDelayedTransition(binding.bottomAppBar, new Slide());
                    viewModel.isFabMenuOpen = position != -1;
                    if (viewModel.isFabMenuOpen) {
                        binding.fabAddAccount.hide();
                        binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_app_bar_selected);
                    } else {
                        binding.fabAddAccount.show();
                        binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_normal);
                    }
                });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        
        // Observe the accounts LiveData from the ViewModel
        viewModel.accounts.observe(getViewLifecycleOwner(), accounts -> {
            // Set up the RecyclerView adapter
            adapter.listener.onItemClick(-1);
            // Set the adapter and layout manager for the RecyclerView
            adapter.updateList(accounts);

        });

        // Get the accounts from the ViewModel
        viewModel.getAccounts(getContext());

        // Set up the scroll listener for the RecyclerView
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Hide the FloatingActionButton when scrolling down
                if (!viewModel.isFabMenuOpen) {
                    if (dy > 0) {
                        binding.fabAddAccount.hide();
                    } else {
                        binding.fabAddAccount.show();
                    }
                }
            }
        });

        // Set up the click listener for the FloatingActionButton
        binding.fabAddAccount.setOnClickListener(v -> {
            // Show the AddNewBalanceBottomSheetDialogFragment when the FloatingActionButton is clicked
            AddNewBalanceBottomSheetDialogFragment bottomSheetDialogFragment = new AddNewBalanceBottomSheetDialogFragment();
            bottomSheetDialogFragment.setOnDismissListener(() -> viewModel.getAccounts(MainFragment.this.getContext()));
            bottomSheetDialogFragment.show(getParentFragmentManager(), bottomSheetDialogFragment.getTag());
        });

        // Set up the menu item click listener for the BottomAppBar
        binding.bottomAppBar.setOnMenuItemClickListener(item -> {
            // Handle menu item clicks
            int itemId = item.getItemId();
            if (itemId == R.id.more) {
                View menuItemView = binding.bottomAppBar.findViewById(R.id.more);
                PopupMenu popupMenu = new PopupMenu(getContext(), menuItemView);
                popupMenu.getMenuInflater().inflate(R.menu.menu_more_for_card, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    int menuItemId = menuItem.getItemId();
                    if (menuItemId == R.id.deleteAccount) {
                        viewModel.deleteAccount(getContext(), adapter.getAccount(adapter.getFocusedItem()).name);
                        return true;
                    } else if (menuItemId == R.id.renameAccount) {
                        RenameDialogFragment bottomSheetDialogFragment = new RenameDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("oldName", adapter.getAccount(adapter.getFocusedItem()).name);
                        bottomSheetDialogFragment.setArguments(args);
                        bottomSheetDialogFragment.setOnDismissListener(() -> viewModel.getAccounts(getContext()));
                        bottomSheetDialogFragment.show(getParentFragmentManager(), bottomSheetDialogFragment.getTag());
                        return true;
                    } else if (menuItemId == R.id.statisticsAccount) {
                        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavController navController = navHostFragment.getNavController();
                        Bundle args = new Bundle();
                        args.putString("account", adapter.getAccount(adapter.getFocusedItem()).name);
                        navController.navigate(R.id.action_mainFragment_to_statsFragment, args);
                        return true;
                    } else {
                        return false;
                    }
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    popupMenu.setForceShowIcon(true);
                }
                popupMenu.show();
                return true;
            } else if (itemId == R.id.add) {
                // Show the AddTransactionBottomSheetDialog when the "Add" menu item is clicked
                AddTransactionBottomSheetDialog bottomSheetDialogFragment = new AddTransactionBottomSheetDialog();
                Bundle args = new Bundle();
                args.putString("type", "Income");
                args.putString("account", adapter.getAccount(adapter.getFocusedItem()).name);
                bottomSheetDialogFragment.setArguments(args);
                bottomSheetDialogFragment.setOnDismissListener(() -> viewModel.getAccounts(getContext()));
                bottomSheetDialogFragment.show(getParentFragmentManager(), bottomSheetDialogFragment.getTag());

                return true;
            } else if (itemId == R.id.remove) {
                // Show the AddTransactionBottomSheetDialog when the "Remove" menu item is clicked
                AddTransactionBottomSheetDialog bottomSheetDialogFragment = new AddTransactionBottomSheetDialog();
                Bundle args = new Bundle();
                args.putString("type", "Expense");
                args.putString("account", adapter.getAccount(adapter.getFocusedItem()).name);
                bottomSheetDialogFragment.setArguments(args);
                bottomSheetDialogFragment.setOnDismissListener(() -> viewModel.getAccounts(getContext()));
                bottomSheetDialogFragment.show(getParentFragmentManager(), bottomSheetDialogFragment.getTag());
                return true;
            } else if (itemId == R.id.transfer) {
                MoneyToAnotherBalanceBSFragment bottomSheetDialogFragment = new MoneyToAnotherBalanceBSFragment();
                bottomSheetDialogFragment.setOnDismissListener(() -> {
                    viewModel.getAccounts(getContext());
                });

                Bundle args = new Bundle();
                args.putString("name", adapter.getAccount(adapter.getFocusedItem()).name);
                bottomSheetDialogFragment.setArguments(args);

                bottomSheetDialogFragment.show(getParentFragmentManager(), bottomSheetDialogFragment.getTag());
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


        // Return the root view for this fragment
        return binding.getRoot();
    }
}