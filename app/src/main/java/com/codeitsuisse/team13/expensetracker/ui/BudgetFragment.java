package com.codeitsuisse.team13.expensetracker.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codeitsuisse.team13.expensetracker.db.Budget;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Chinmay on 12/09/2015.
 */
public class BudgetFragment extends android.support.v4.app.Fragment
{

	FloatingActionButton floatingActionButton;
	TextView currentBudget;
	TextView remainingBudget;
	private DecimalFormat df = new DecimalFormat("#0.00");


	public static android.support.v4.app.Fragment newInstance()
	{
		return new BudgetFragment();
	}

	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{

		View view = inflater.inflate(R.layout.fragment_budget, container, false);
		floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_budget);
		currentBudget = (TextView) view.findViewById(R.id.budget_amount);
		remainingBudget = (TextView) view.findViewById(R.id.budget_remaining_amount);

		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		Budget currentMonth = NavigationDrawerActivity.dbAdapter.getEntryBudget(month, year);
		double currentBudgetValue = 0;
		if (currentMonth != null)
		{
			currentBudgetValue = currentMonth.getBudget();
		}
		double totalExpenses = NavigationDrawerActivity.dbAdapter.totalExpenseMonth(month, year);
		currentBudget.setText(df.format(currentBudgetValue));
		remainingBudget.setText(df.format(currentBudgetValue - totalExpenses));

		floatingActionButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final AddBudgetFragment calendarFragment = AddBudgetFragment.newInstance();
				calendarFragment.show(getActivity().getSupportFragmentManager(), "dialog");
			}
		});
		return view;
	}
}
