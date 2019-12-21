package com.codeitsuisse.team13.expensetracker.db;

import java.util.HashMap;

/**
 * Created by Dell on 9/12/2015.
 */
public class Expense {

    public static final String KEY_ID="expense_id";
    public static final String CATEGORY_NAME="category";
    public static final int CATEGORY_COLUMN = 1;

    public static final String DAY_NAME="day";
    public static final int DAY_COLUMN = 2;

    public static final String MONTH_NAME="month";
    public static final int MONTH_COLUMN = 3;

    public static final String YEAR_NAME="year";
    public static final int YEAR_COLUMN = 4;

    public static final String AMOUNT_NAME="amount";
    public static final int AMOUNT_COLUMN = 5;


    public static final String LIABILITY_NAME="liability";
    public static final int LIABILITY = 6;

    public static final String ExpenseTable = "expense";

    public static final String[] categories = {"Food", "Transport", "Housing", "Bills", };
    public static HashMap<String, String> categoriesColor;
    public static final String[] colors = {"#FE6DA8", "#56B7F1", "#CDA67F", "#FED70E"};

    static {
        categoriesColor = new HashMap<String, String>();
        for(int i = 0; i < categories.length; ++i) {
            categoriesColor.put(categories[i], colors[i]);
        }
    }

    public Expense(String category,  int day, int month, int year, double amount, boolean liability) {
        this.category = category;
        this.day = day;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.liability = liability;
    }

    public Expense(){

    }

    public static final String CREATE_TABLE = "create table expense ( "
            + KEY_ID + " integer primary key autoincrement, "
            + "category varchar(20), "
            + "day numeric(2, 0), "
            + "month numeric(2, 0), "
            + "year numeric(4, 0), "
            + "amount numeric(10, 2), " +
              "liability numeric(1,0) )";

    private String category;
    private int key;
    private int day, month, year;
    private double amount;
    private boolean liability;



    public boolean isLiability() {
        return liability;
    }

    public void setLiability(boolean liability) {
        this.liability = liability;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }


    @Override
    public String toString() {
        return "Expense{" +
                "category='" + category + '\'' +
                ", key=" + key +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", amount=" + amount +
                ", liability=" + liability +
                '}';
    }
}
