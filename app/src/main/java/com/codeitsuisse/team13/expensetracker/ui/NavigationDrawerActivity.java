package com.codeitsuisse.team13.expensetracker.ui;

import android.os.Bundle;
import android.view.KeyEvent;

import com.codeitsuisse.team13.expensetracker.db.DBAdapter;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by Chinmay on 12/09/2015.
 */
public class NavigationDrawerActivity extends MaterialNavigationDrawer {

	public static DBAdapter dbAdapter;
    @Override
    public void init(Bundle savedInstanceState) {
	    dbAdapter = new DBAdapter(this);
        enableToolbarElevation();
        this.setDrawerHeaderImage(R.drawable.rect_logo);
        this.addSection(newSection("Summary", new SummaryFragment()));
        this.addSection(newSection("Budget", new BudgetFragment()));
        this.addSection(newSection("Expenses", new ExpensesFragment()));
        this.addSection(newSection("Savings", new SavingsFragment()));
        this.disableLearningPattern();
    }

	public void changeSection(int sec)
	{
		if (sec == 3)
			this.setFragment(new SavingsFragment(), "Savings");
		else
			this.setFragment(new BudgetFragment(), "Budget");
	}
}
