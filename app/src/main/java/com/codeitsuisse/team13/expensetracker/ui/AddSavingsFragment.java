package com.codeitsuisse.team13.expensetracker.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codeitsuisse.team13.expensetracker.db.Budget;
import com.codeitsuisse.team13.expensetracker.db.Savings;

import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Chinmay on 13/09/2015.
 */
public class AddSavingsFragment extends android.support.v4.app.DialogFragment
{
	@InjectView(R.id.button_cancel) protected Button cancelButton;

	@InjectView(R.id.button_done) protected Button doneButton;

	@InjectView(R.id.add_savings) protected EditText addSavings;

	String savingsValue;
	private DecimalFormat df = new DecimalFormat("#0.00");

	public static AddSavingsFragment newInstance()
	{
		final AddSavingsFragment fragment = new AddSavingsFragment();
		final Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		final Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.0f;
		dialog.getWindow().setAttributes(lp);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);

		final View rootView = inflater.inflate(R.layout.add_savings, container, false);
		ButterKnife.inject(this, rootView);

		int month = NavigationDrawerActivity.dbAdapter.getMonth();
		int year = NavigationDrawerActivity.dbAdapter.getYear();

		Savings currentMonth = NavigationDrawerActivity.dbAdapter.getEntrySavings(month, year);

		if (currentMonth != null)
		{
			addSavings.setText(df.format(currentMonth.getSavings()));
		}

		cancelButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});

		doneButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!addSavings.getText().toString().isEmpty())
				{
					savingsValue = addSavings.getText().toString();

					double newSavings = Double.parseDouble(savingsValue);
					int month = NavigationDrawerActivity.dbAdapter.getMonth();
					int year = NavigationDrawerActivity.dbAdapter.getYear();

					Savings savings = new Savings(month, year, newSavings);
					NavigationDrawerActivity.dbAdapter.insertOrUpdateSavings(savings);
					((NavigationDrawerActivity) getActivity()).changeSection(3);

					/*View previous = rootView.getRootView();
					TextView target = (TextView) previous.findViewById(R.id.savings_amount);
					TextView current = (TextView) previous.findViewById(R.id.current_savings_amount);

					target.setText(savingsValue);

					Budget currentBudget =  NavigationDrawerActivity.dbAdapter.getEntryBudget(month, year);

					double budgetValue = 0;
					if(currentBudget != null) {
						budgetValue = currentBudget.getBudget();
					}

					double totalExpenses = NavigationDrawerActivity.dbAdapter.totalExpenseMonth(month, year);
					double currentSavings = budgetValue - totalExpenses;

					current.setText(currentSavings + "");*/
					dismiss();
				}
				else
				{
					Toast.makeText(getActivity().getApplicationContext(), "Please Enter Amount",
					               Toast.LENGTH_LONG).show();
				}
			}
		});

		return rootView;
	}

}
