package com.codeitsuisse.team13.expensetracker.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.codeitsuisse.team13.expensetracker.db.Expense;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Chinmay on 12/09/2015.
 */
public class ExpensesFragment extends Fragment {

	private MaterialCalendarView materialCalendarView;
	private int day;
	private int month;
	private int year;
	private ArrayList<ListViewData> listViewDatas;
	private ArrayList<Expense> expenses;
	private ListView listView;

	@Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.expenses_calender);
        //materialCalendarView.setSelectedDate(new Date());

		listViewDatas = new ArrayList<ListViewData>();

		listView = (ListView) view.findViewById(R.id.list_view_expenses);
        materialCalendarView.setOnDateChangedListener(new OnDateChangedListener()
        {
	        @Override
	        public void onDateChanged(MaterialCalendarView materialCalendarView,
	                                  CalendarDay calendarDay)
	        {
		        day = calendarDay.getDay();
		        month = calendarDay.getMonth() + 1;
		        year = calendarDay.getYear();

		        expenses = NavigationDrawerActivity.dbAdapter.getEntryExpense(day, month, year);
		        listViewDatas = new ArrayList<ListViewData>();
		        for(Expense e: expenses) {
			        listViewDatas.add(new ListViewData(e.getCategory(), e.getAmount()+"", !e.isLiability()));
		        }
		        ExpenseListAdapter expenseListAdapter =new ExpenseListAdapter(getActivity().getApplicationContext(),listViewDatas,R.layout.expense_listview_layout);
		        listView.setAdapter(expenseListAdapter);
		        expenseListAdapter.notifyDataSetChanged();
	        }
        });

		FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_expenses);
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
	        @Override
	        public void onClick(View v)
	        {

		        final NewExpenseFragment calendarFragment = NewExpenseFragment.newInstance();
		        calendarFragment.show(getActivity().getSupportFragmentManager(), "dialog");
	        }
        });


        /*listViewDatas.add(new ListViewData("Food", 7+"", true));
        listViewDatas.add(new ListViewData("Food", 7+"", false));
        listViewDatas.add(new ListViewData("Food", 7+"", true));
        listViewDatas.add(new ListViewData("Food", 7+"", false));
        listViewDatas.add(new ListViewData("Food", 7+"", true));
        listViewDatas.add(new ListViewData("Food", 7+"", false));

        listView.setAdapter(expenseListAdapter);*/
        setListViewHeightBasedOnChildren(listView);

        return view;
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}
