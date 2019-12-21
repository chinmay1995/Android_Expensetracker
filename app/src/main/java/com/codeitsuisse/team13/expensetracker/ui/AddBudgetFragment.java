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
import android.widget.Toast;

import com.codeitsuisse.team13.expensetracker.db.Budget;

import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Chinmay on 12/09/2015.
 */
public class AddBudgetFragment extends android.support.v4.app.DialogFragment
{
	@InjectView(R.id.button_cancel) protected Button cancelButton;

	@InjectView(R.id.button_done) protected Button doneButton;

	@InjectView(R.id.add_budget) protected EditText addBudget;
	String budgetValue;
	private DecimalFormat df = new DecimalFormat("#0.00");

	public static AddBudgetFragment newInstance()
	{
		final AddBudgetFragment fragment = new AddBudgetFragment();
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

		View rootView = inflater.inflate(R.layout.add_budget, container, false);
		ButterKnife.inject(this, rootView);

		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		Budget currentMonth = NavigationDrawerActivity.dbAdapter.getEntryBudget(month, year);

		if (currentMonth != null)
		{
			addBudget.setText(df.format(currentMonth.getBudget()));
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
				if (!addBudget.getText().toString().isEmpty())
				{
					budgetValue = addBudget.getText().toString();
					System.out.println(budgetValue);
					double newBudget = Double.parseDouble(budgetValue);
					System.out.println(newBudget);

					Calendar calendar = Calendar.getInstance();
					int month = calendar.get(Calendar.MONTH) + 1;
					int year = calendar.get(Calendar.YEAR);

					Budget budget = new Budget(month, year, newBudget);
					NavigationDrawerActivity.dbAdapter.insertOrUpdateBudget(budget);

					((NavigationDrawerActivity) getActivity()).changeSection(1);
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
