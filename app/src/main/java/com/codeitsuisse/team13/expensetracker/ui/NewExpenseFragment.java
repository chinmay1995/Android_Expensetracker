package com.codeitsuisse.team13.expensetracker.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.codeitsuisse.team13.expensetracker.db.DBAdapter;
import com.codeitsuisse.team13.expensetracker.db.Expense;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Chinmay on 12/09/2015.
 */
public class NewExpenseFragment extends android.support.v4.app.DialogFragment
{

	@InjectView(R.id.button_cancel) protected Button cancelButton;

	@InjectView(R.id.button_done) protected Button doneButton;

	@InjectView(R.id.expense_amount) protected EditText expenseAmount;

	@InjectView(R.id.pay_later_button) protected RadioButton payLaterButton;

	@InjectView(R.id.pay_now_button) protected RadioButton payNowButton;

	@InjectView(R.id.category_spinner)
	protected Spinner categorySpinner;

	String expenseValue;
	String spinnerCategory = Expense.categories[0];

	DBAdapter dbAdapter;

	public static NewExpenseFragment newInstance()
	{
		final NewExpenseFragment fragment = new NewExpenseFragment();
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
	                         Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		dbAdapter = NavigationDrawerActivity.dbAdapter;
		View rootView = inflater.inflate(R.layout.new_expense_fragment, container, false);
		ButterKnife.inject(this, rootView);
		final MaterialCalendarView materialCalendarView =
				(MaterialCalendarView) rootView.findViewById(R.id.calendar_view);

		materialCalendarView.setSelectedDate(new Date());

		ArrayList<String> categoriesList = new ArrayList<String>();

		for(String c: Expense.categories) {
			categoriesList.add(c);
		}

		ArrayAdapter<String> categoryStrings = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
		                                                                categoriesList);

		categorySpinner.setAdapter(categoryStrings);

		categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				spinnerCategory = Expense.categories[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});

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
				if (expenseAmount.getText().toString().isEmpty() ||
						expenseAmount.getText().toString().equals("0"))
				{
					Toast.makeText(getActivity().getApplicationContext(), "Please Enter Amount",
					               Toast.LENGTH_LONG).show();

				}
				else
				{
					expenseValue = expenseAmount.getText().toString();

					double amount = Double.parseDouble(expenseValue);
					CalendarDay calendarDay = materialCalendarView.getSelectedDate();
					boolean liability;
					if (payLaterButton.isChecked())
					{
						liability = true;
					}
					else
					{
						liability = false;
					}
					int day = calendarDay.getDay();
					int month = calendarDay.getMonth() + 1;
					int year = calendarDay.getYear();
					Toast.makeText(getActivity(), day + " " + month + " " + year, Toast.LENGTH_LONG)
					     .show();
					String category = spinnerCategory;

					Expense expense = new Expense(category, day, month, year, amount, liability);
					dbAdapter.insertExpense(expense);
					dismiss();
				}
			}
		});

		return rootView;
	}

}
