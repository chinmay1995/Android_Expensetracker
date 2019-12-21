package com.codeitsuisse.team13.expensetracker.db;

/**
 * Created by Dell on 9/12/2015.
 */
public class Budget {

    public static final String MONTH_NAME="month";
    public static final int MONTH_COLUMN = 1;

    public static final String YEAR_NAME="year";
    public static final int YEAR_COLUMN = 2;

    public static final String BUDGET_NAME="budget";
    public static final int BUDGET_COLUMN = 3;

    public static final String BudgetTable = "budgetTable";

    public static final String CREATE_TABLE = "create table budgetTable ( "
            + "month numeric(2, 0), "
            + "year numeric(4, 0), "
            + "budget numeric(10, 2), " +
              "primary key(month, year) )";

    private int month, year;
    private double budget;

    public Budget(int month, int year, double budget) {
        this.month = month;
        this.year = year;
        this.budget = budget;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "month=" + month +
                ", year=" + year +
                ", budget=" + budget +
                '}';
    }
}
