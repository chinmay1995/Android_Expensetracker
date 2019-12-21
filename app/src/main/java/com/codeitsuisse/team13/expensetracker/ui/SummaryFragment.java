package com.codeitsuisse.team13.expensetracker.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codeitsuisse.team13.expensetracker.db.Budget;
import com.codeitsuisse.team13.expensetracker.db.DBAdapter;
import com.codeitsuisse.team13.expensetracker.db.Expense;
import com.codeitsuisse.team13.expensetracker.db.Savings;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Chinmay on 12/09/2015.
 */
public class SummaryFragment  extends android.support.v4.app.Fragment{

	public static android.support.v4.app.Fragment newInstance() {
		return new SummaryFragment();
	}

	FloatingActionButton floatingActionButton;
	private LinearLayout expenseBudgetLinearLayout, savingsLinearLayout;
	private TextView expenseFillTextView, expenseEmptyTextView, savingsTextView;
	private PieChart mPieChart;
	private DBAdapter dbAdapter;

	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbAdapter = NavigationDrawerActivity.dbAdapter;
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
		if(container == null)
			System.out.println("cont nul");
		View view = inflater.inflate(R.layout.fragment_summary, container, false);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_summary);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NewExpenseFragment calendarFragment = NewExpenseFragment.newInstance();
                calendarFragment.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });
		expenseBudgetLinearLayout = (LinearLayout) view.findViewById(R.id.expense_linearlayout);
		expenseFillTextView = (TextView)view.findViewById(R.id.expense_fill_bar);
		expenseEmptyTextView = (TextView)view.findViewById(R.id.expense_empty_bar);
		savingsTextView = (TextView)view.findViewById(R.id.savings_output);

		DBAdapter dbAdapter = NavigationDrawerActivity.dbAdapter;
		//ArrayList<Expense> expenses = dbAdapter.getEntryExpense(date.getTime().getMonth()+1, date.getTime().getYear());
		double actualExpense = 0, budget = 0, assumedSavings = 0, actualSavings = 0;
		actualExpense = dbAdapter.totalExpenseMonth(dbAdapter.getMonth(), dbAdapter.getYear());
		Budget temp = dbAdapter.getEntryBudget(dbAdapter.getMonth(), dbAdapter.getYear());
		if (temp != null)
			budget = temp.getBudget();
		if(expenseFillTextView == null)
			System.out.println("lp npe");
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);

		int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
		double width = (2*screenWidth)/3.0;
		if(actualExpense >= budget) {
			params.width = LinearLayout.LayoutParams.MATCH_PARENT;
			expenseFillTextView.setLayoutParams(params);
		}
		else{
			params.width =(int)((actualExpense/budget)*width);
			expenseFillTextView.setLayoutParams(params);

			params.width = (int)(width - (int)((actualExpense/budget)*width));
			expenseFillTextView.setLayoutParams(params);
		}

		Savings savingsTemp = dbAdapter.getEntrySavings(dbAdapter.getMonth(), dbAdapter.getYear());
		if (savingsTemp != null)
			assumedSavings = savingsTemp.getSavings();
		actualSavings = budget - actualExpense;

		if (budget == 0)
		{
			savingsTextView.setText("Your budget is set as Rs. 0.00 :p");
		}
		else if (actualSavings < 0)
		{
			savingsTextView.setText("You have spent more than the planned budget :(");
		}
		else if (actualSavings > assumedSavings)
		{
			savingsTextView.setText("You are currently Rs. " + (actualSavings-assumedSavings) + " ahead of your planned savings ;)");
		}
		else if (assumedSavings > actualSavings)
		{
			savingsTextView.setText("You are currently Rs. " + (actualSavings-assumedSavings) + " behind of your planned savings :/");
		}
		else if (assumedSavings == actualSavings)
		{
			savingsTextView.setText("Your current savings Rs. " + assumedSavings + " is same as what you had planned :)");
		}

		mPieChart = (PieChart) view.findViewById(R.id.piechart);

		HashMap<String, Double> summary = dbAdapter.getSummaryView(dbAdapter.getMonth(), dbAdapter.getYear());
		double total = dbAdapter.totalExpenseMonth(dbAdapter.getMonth(), dbAdapter.getYear());
		Set set = summary.entrySet();
		Iterator i = set.iterator();
		while(i.hasNext())
		{
			Map.Entry me =  (Map.Entry)i.next();
			String category = (String) me.getKey();
			double percent = ((Double) me.getValue() / total) * 100.0;
			System.out.println((Double) me.getValue());
			mPieChart.addPieSlice(new PieModel(category, (float) percent, Color.parseColor((String) Expense.categoriesColor.get(category))));
		}

		mPieChart.startAnimation();


		return view;
	}
}
