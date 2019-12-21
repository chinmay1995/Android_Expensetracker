package com.codeitsuisse.team13.expensetracker.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codeitsuisse.team13.expensetracker.db.Budget;
import com.codeitsuisse.team13.expensetracker.db.Savings;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Chinmay on 12/09/2015.
 */
public class SavingsFragment extends Fragment {
    FloatingActionButton floatingActionButton ;

	TextView savingsAmount;
	TextView currentSavingsAmount;
	private DecimalFormat df = new DecimalFormat("#0.00");

	@Override
    public View onCreateView( final LayoutInflater inflater,  final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_savings, container, false);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_savings);
		savingsAmount = (TextView) view.findViewById(R.id.savings_amount);
		currentSavingsAmount = (TextView) view.findViewById(R.id.current_savings_amount);

		int month = NavigationDrawerActivity.dbAdapter.getMonth();
		int year = NavigationDrawerActivity.dbAdapter.getYear();

		Savings currentSavingsTarget = NavigationDrawerActivity.dbAdapter.getEntrySavings(month,
		                                                                                  year);
		Budget currentBudget =  NavigationDrawerActivity.dbAdapter.getEntryBudget(month, year);

		double budgetValue = 0;
		if(currentBudget != null) {
			budgetValue = currentBudget.getBudget();
		}

		double totalExpenses = NavigationDrawerActivity.dbAdapter.totalExpenseMonth(month, year);
		double currentSavings = budgetValue - totalExpenses;

		double savingsTarget = 0;
		if(currentSavingsTarget != null) {
			savingsTarget = currentSavingsTarget.getSavings();
		}

		savingsAmount.setText(df.format(savingsTarget));
		currentSavingsAmount.setText(df.format(currentSavings));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AddSavingsFragment calendarFragment = AddSavingsFragment.newInstance();
                calendarFragment.show(getActivity().getSupportFragmentManager(), "dialog");
	            //container.invalidate();
	            //container.requestLayout();
	            //container.recomputeViewAttributes(savingsAmount);
	            //container.recomputeViewAttributes(currentSavingsAmount);
            }
        });
        return view;
    }

}
