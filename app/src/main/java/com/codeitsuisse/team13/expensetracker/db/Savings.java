package com.codeitsuisse.team13.expensetracker.db;

/**
 * Created by Dell on 9/12/2015.
 */
public class Savings {

    public static final String KEY_ID="_id";
    // The name and column index of each column in your database.
    public static final String KEY_SAVINGS="savings";

    public static final int MONTH_COLUMN = 1;
    public static final String MONTH_NAME="month";

    public static final String YEAR_NAME="year";
    public static final int YEAR_COLUMN = 2;

    public static final String SAVINGS_NAME = "savings";
    public static final int SAVINGS_COLUMN = 3;

    public static final String SavingsTable = "savingsTable";

    public Savings(int month, int year, double savings) {
        this.month = month;
        this.year = year;
        this.savings = savings;
    }

    public Savings(){

    }

    public static final String CREATE_TABLE = "create table savingsTable ( "
            + "month numeric(2, 0),"
            + "year numeric(4, 0),"
            + "savings numeric(10, 2)," +
            "primary key(month, year) )";


    private int month, year;
    private double savings;

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

    public double getSavings() {
        return savings;
    }

    public void setSavings(double savings) {
        this.savings = savings;
    }

    @Override
    public String toString() {
        return "Savings{" +
                "month=" + month +
                ", year=" + year +
                ", savings=" + savings +
                '}';
    }
}
