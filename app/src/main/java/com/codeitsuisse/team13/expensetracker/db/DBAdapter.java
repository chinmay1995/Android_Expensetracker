package com.codeitsuisse.team13.expensetracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dell on 9/12/2015.
 */
public class DBAdapter {

    private static final String DATABASE_NAME = "expenseDatabase.db";
    /* private static final String EXPENSE_DATABASE_TABLE = "expenseTable";
     private static final String SAVINGS_DATABASE_TABLE = "expenseTable";
     private static final String BUDGET_DATABASE_TABLE = "expenseTable";*/
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase database;
    private MyDbHelper myDbHelper;

    private Context context;

    public DBAdapter(Context context){
        this.context = context;
        myDbHelper = new MyDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public int getDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    public int getMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }


    public int getYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }



    //Budget function starts here
    private long insertBudget(Budget budget) throws SQLException {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Budget.BUDGET_NAME, budget.getBudget());
        contentValues.put(Budget.MONTH_NAME, budget.getMonth());
        contentValues.put(Budget.YEAR_NAME, budget.getYear());

        return myDbHelper.getWritableDatabase().insert(Budget.BudgetTable, null, contentValues);
    }



    public Budget getEntryBudget(int month, int year) {


        Cursor cursor = myDbHelper.getReadableDatabase().query(Budget.BudgetTable,
                null, "month = " + month + " and year = " + year, null, null, null, null);
        if(cursor == null){
            Toast.makeText(context, "budget is null"+month+" "+year, Toast.LENGTH_LONG).show();
            return null;
        }

        Budget budget = null;
        if(cursor.moveToFirst()){
            budget = new Budget(cursor.getInt(cursor.getColumnIndex(Budget.MONTH_NAME)),
                    cursor.getInt(cursor.getColumnIndex(Budget.YEAR_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(Budget.BUDGET_NAME)));
            Log.i("btag","budget is not null");
        }

        return budget;
    }

    public long insertOrUpdateBudget(Budget budget){

       /* Cursor cursor = myDbHelper.getReadableDatabase().query(Budget.BudgetTable, null, "month = "+budget.getMonth()+" and year = "
        + budget.getYear(), null, null, null, null, null);*/
        /*Cursor cursor = myDbHelper.getReadableDatabase().rawQuery("select * from "+Budget.BudgetTable+" where " +
                "month = "+budget.getMonth()+ " year = "+ budget.getYear(), null);*/

        Cursor cursor = myDbHelper.getReadableDatabase().rawQuery("select * from " + Budget.BudgetTable
                + " where month = "+budget.getMonth() + " and year = "+budget.getYear(), null);
        if(cursor == null)
            return 0;
        if(cursor.moveToFirst()){
            ContentValues contentValues = new ContentValues();
            contentValues.put(Budget.BUDGET_NAME, budget.getBudget());
            contentValues.put(Budget.YEAR_NAME, budget.getYear());
            contentValues.put(Budget.MONTH_NAME, budget.getMonth());
            Toast.makeText(context, budget.getMonth()+" "+budget.getYear() + " " + budget.getBudget(), Toast.LENGTH_LONG).show();
            return myDbHelper.getWritableDatabase().update(Budget.BudgetTable, contentValues, "month = "+budget.getMonth()+" and year = "
                    + budget.getYear() , null);
        }
        else {
            long ans = insertBudget(budget);
            Toast.makeText(context, budget.getMonth()+" "+budget.getYear() + " " + budget.getBudget(), Toast.LENGTH_LONG).show();
            return ans;
        }
    }


    //Expense functions starts here
    public long insertExpense(Expense expense) throws SQLException {

        ContentValues contentValues = new ContentValues();
        //expense.setKey(Expense.currentKey++);
        contentValues.put(Expense.AMOUNT_NAME, expense.getAmount());
        contentValues.put(Expense.MONTH_NAME, expense.getMonth());
        contentValues.put(Expense.YEAR_NAME, expense.getYear());
        contentValues.put(Expense.DAY_NAME, expense.getDay());
        contentValues.put(Expense.CATEGORY_NAME, expense.getCategory());
        contentValues.put(Expense.LIABILITY_NAME, expense.isLiability() ? 1 : 0);
        long ans = myDbHelper.getWritableDatabase().insert(Expense.ExpenseTable, null,
                                                           contentValues);
		Toast.makeText(context, expense.getCategory() + " " + expense.isLiability() + " " + expense.getDay(), Toast.LENGTH_LONG).show();
	    return ans;
    }



    public boolean removeExpense(Expense expense) {

        return myDbHelper.getWritableDatabase().delete(Expense.ExpenseTable, Expense.KEY_ID +
                "=" + expense.getKey(), null) > 0;
    }


    public ArrayList<Expense> getAllEntriesExpense () {
        ArrayList<Expense> expenses = new ArrayList<Expense>();
        Cursor cursor =  myDbHelper.getReadableDatabase().query(Expense.ExpenseTable,
                null,
                null, null, null, null, null);

        // new String[]{Expense.KEY_ID, Expense.CATEGORY_NAME, Expense.DAY_NAME, Expense.MONTH_NAME, Expense.YEAR_NAME, Expense.AMOUNT_NAME, Expense.LIABILITY_NAME}

        if(cursor == null)
            return expenses;
        if(!cursor.moveToFirst())
            return expenses;
        do
        {
            Expense expense = new Expense();
            expense.setKey(cursor.getInt(cursor.getColumnIndex(Expense.KEY_ID)));
            expense.setAmount(cursor.getFloat(cursor.getColumnIndex(Expense.AMOUNT_NAME)));
            expense.setCategory(cursor.getString(cursor.getColumnIndex(Expense.CATEGORY_NAME)));
            //expense.setCategory(cursor.getString(1));
            expense.setDay(cursor.getInt(cursor.getColumnIndex(Expense.DAY_NAME)));
            expense.setMonth(cursor.getInt(cursor.getColumnIndex(Expense.MONTH_NAME)));
            expense.setYear(cursor.getInt(cursor.getColumnIndex(Expense.YEAR_NAME)));
            expense.setLiability(cursor.getInt(cursor.getColumnIndex(Expense.LIABILITY_NAME)) == 1? true: false);

            //expense.setLiability(cursor.getInt(6) == 1 ? true: false);
            expenses.add(expense);
        }while(cursor.moveToNext());
        return expenses;
    }

    public HashMap<String, Double> getSummaryView(int month, int year) {

        ArrayList<Expense> expenses = getEntryExpense(month, year);
        HashMap<String, Double> hm = new HashMap<String, Double>();
        for(Expense e: expenses) {
            if(hm.containsKey(e.getCategory())) {
                hm.put(e.getCategory(), new Double(hm.get(e.getCategory()) + e.getAmount()));
            } else {
                hm.put(e.getCategory(), new Double(e.getAmount()));
            }
        }
        return hm;
    }

    public double totalExpenseMonth(int month, int year) {
        HashMap<String, Double> hm = getSummaryView(month, year);
        double total = 0;
        Set set = hm.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext())
        {
            Map.Entry me =  (Map.Entry)i.next();
            total += (Double)me.getValue();
        }
        return total;
    }


    public ArrayList<Expense> getEntryExpense(int month, int year) {

        ArrayList<Expense> total = getAllEntriesExpense();
        ArrayList<Expense> expenses = new ArrayList<Expense>();

        for(Expense e: total) {
            if(e.getMonth() == month && e.getYear() == year) {
                expenses.add(e);
            }
        }

        return expenses;
    }

    public ArrayList<Expense> getEntryExpense(int day, int month, int year) {

        ArrayList<Expense> total = getAllEntriesExpense();
        ArrayList<Expense> expenses = new ArrayList<Expense>();

        for(Expense e: total) {
            if(e.getMonth() == month && e.getYear() == year && e.getDay() == day) {
                expenses.add(e);
            }
        }

        return expenses;
    }


    //Savings function starts here
    private long insertSavings(Savings savings) throws SQLException {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Savings.SAVINGS_NAME, savings.getSavings());
        contentValues.put(Savings.MONTH_NAME, savings.getMonth());
        contentValues.put(Savings.YEAR_NAME, savings.getYear());

        return myDbHelper.getWritableDatabase().insert(Savings.SavingsTable, null, contentValues);
    }

    public Savings getEntrySavings(int month, int year) {


        Cursor cursor = myDbHelper.getReadableDatabase().query(Savings.SavingsTable,
                null, "month = " + month + " and year = " + year, null, null, null, null);
        if(cursor == null){
            Toast.makeText(context, "savings is null "+month+" "+year, Toast.LENGTH_LONG).show();
            return null;
        }
        Savings savings = null;
        if(cursor.moveToFirst()){
            savings = new Savings(cursor.getInt(cursor.getColumnIndex(Savings.MONTH_NAME)),
                    cursor.getInt(cursor.getColumnIndex(Savings.YEAR_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(Savings.SAVINGS_NAME)));
            Log.i("stag","savings is not null");
        }

        return savings;
    }

    public long insertOrUpdateSavings(Savings savings){

        Cursor cursor = myDbHelper.getReadableDatabase().query(Savings.SavingsTable, null, "month = " + savings.getMonth()
                + " and year = "
                + savings.getYear()
                , null, null, null, null, null);
        if(cursor == null)
            return 0;
        if(cursor.moveToFirst()){
            ContentValues contentValues = new ContentValues();
            contentValues.put(Savings.SAVINGS_NAME, savings.getSavings());
            contentValues.put(Savings.MONTH_NAME, savings.getMonth());
            contentValues.put(Savings.YEAR_NAME, savings.getYear());
            return myDbHelper.getWritableDatabase().update(Savings.SavingsTable, contentValues, "month = " + savings.getMonth() + " and year = "
                    + savings.getYear() , null);
        }
        else {
            long ans = insertSavings(savings);
            Toast.makeText(context, savings.getMonth()+" "+savings.getYear(), Toast.LENGTH_LONG).show();
            return ans;
        }
    }

	public boolean resetDatabase() {
		database.execSQL("delete from " + Budget.BudgetTable);
		database.execSQL("delete from " + Savings.SavingsTable);
		database.execSQL("delete from " + Expense.ExpenseTable);
		return true;
	}


    /*public int updateEntry(long _rowIndex, MyObject _myObject) {
        String where = KEY_ID + “=” + _rowIndex;
        ContentValues contentValues = new ContentValues();
// TODO fill in the ContentValue based on the new object
        return db.update(DATABASE_TABLE, contentValues, where, null);
    }*/



    private static class MyDbHelper extends SQLiteOpenHelper {
        public MyDbHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
            //super(context, name, factory, version);
            super(context,DATABASE_NAME,null,1);
        }

        // Called when no database exists in
        // disk and the helper class needs
        // to create a new one.
        @Override
        public void onCreate(SQLiteDatabase _db) {

            _db.execSQL("DROP TABLE IF EXISTS " + Budget.BudgetTable);
            _db.execSQL(Budget.CREATE_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " + Expense.ExpenseTable);
            _db.execSQL(Expense.CREATE_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " + Savings.SavingsTable);
            _db.execSQL(Savings.CREATE_TABLE);

        }
        // Called when there is a database version mismatch meaning that
        // the version of the database on disk needs to be upgraded to
        // the current version.
        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
                              int _newVersion) {
            // Log the version upgrade.
            Log.w("TaskDBAdapter", "Upgrading from version " +
                    _oldVersion + " to " +
                    _newVersion +
                    ", which will destroy all old data");
            // Upgrade the existing database to conform to the new version.
            // Multiple previous versions can be handled by comparing
            // _oldVersion and _newVersion values.
            // The simplest case is to drop the old table and create a
            // new one.

            _db.execSQL("DROP TABLE IF EXISTS " + Budget.CREATE_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " + Expense.CREATE_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " + Savings.CREATE_TABLE);

            // Create a new one.
            onCreate(_db);
        }
    }

    public void close() {
        myDbHelper.close();
    }
}
