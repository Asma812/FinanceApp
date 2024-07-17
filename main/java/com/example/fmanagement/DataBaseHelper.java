package com.example.fmanagement;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Finance.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_PAYMENTS = "payments";

    // Column names
    private static final String COLUMN_PAYMENT_ID = "payment_id";
    private static final String COLUMN_PAYMENT_NAME = "payment_name";
    private static final String COLUMN_PAYMENT_DEADLINE = "payment_deadline";

    // Expense table
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_EXPENSE_ID = "id";
    private static final String COLUMN_EXPENSE_AMOUNT = "amount";
    private static final String COLUMN_EXPENSE_CATEGORY = "category";
    private static final String COLUMN_EXPENSE_DATE = "date";

    // Budget table
    private static final String TABLE_BUDGETS = "budgets";
    private static final String COLUMN_BUDGET_ID = "id";
    private static final String COLUMN_BUDGET_CATEGORY = "category";
    private static final String COLUMN_BUDGET_AMOUNT = "amount";

    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private LinearLayout llBudgets;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUserQuery = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_EMAIL + " TEXT)";
        db.execSQL(createTableUserQuery);

        // Create expenses table
        String createExpensesTableQuery = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXPENSE_AMOUNT + " REAL, " +
                COLUMN_EXPENSE_CATEGORY + " TEXT, " +
                COLUMN_EXPENSE_DATE + " TEXT)";
        db.execSQL(createExpensesTableQuery);

        // Create budgets table
        String createBudgetsTableQuery = "CREATE TABLE " + TABLE_BUDGETS + " (" +
                COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BUDGET_CATEGORY + " TEXT, " +
                COLUMN_BUDGET_AMOUNT + " REAL)";
        db.execSQL(createBudgetsTableQuery);

        String CREATE_TABLE_PAYMENTS = "CREATE TABLE " + TABLE_PAYMENTS + "("
                + COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PAYMENT_NAME + " TEXT,"
                + COLUMN_PAYMENT_DEADLINE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_PAYMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create new tables
        onCreate(sqLiteDatabase);
    }

    public long insertPayment(String paymentName, String paymentDeadline) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_NAME, paymentName);
        values.put(COLUMN_PAYMENT_DEADLINE, paymentDeadline);

        // Insert row
        long id = db.insert(TABLE_PAYMENTS, null, values);

        // Close the database connection
        db.close();

        return id;
    }

    // Retrieve all payments from the database
    @SuppressLint("Range")
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PAYMENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment();
                payment.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_PAYMENT_ID)));
                payment.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PAYMENT_NAME)));
                payment.setDeadline(cursor.getString(cursor.getColumnIndex(COLUMN_PAYMENT_DEADLINE)));
                payments.add(payment);
            } while (cursor.moveToNext());
        }
        // Close the cursor and database
        cursor.close();
        db.close();

        return payments;
    }

    public boolean saveExpense(String category, double amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_AMOUNT, amount);
        values.put(COLUMN_EXPENSE_CATEGORY, category);
        values.put(COLUMN_EXPENSE_DATE, date);
        long result = db.insert(TABLE_EXPENSES, null, values);
        return result != -1;
    }
    public void saveBudgets(Context context, LinearLayout llBudgets) { // Modify method signature
        this.llBudgets = llBudgets; // Assign llBudgets received from the activity

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if (llBudgets != null) {
                int childCount = llBudgets.getChildCount();
                Log.d("Monitoring", "Child count of llBudgets: " + childCount);
                // Your code to iterate through llBudgets and perform operations
            } else {
                Log.e("Monitoring", "llBudgets is null");
                return; // Exit method if llBudgets is null
            }

            // Iterate through each budget category
            for (int i = 0; i < llBudgets.getChildCount(); i++) {
                View child = llBudgets.getChildAt(i);
                if (child instanceof LinearLayout) {
                    LinearLayout categoryLayout = (LinearLayout) child;
                    EditText editTextBudget = (EditText) categoryLayout.findViewWithTag("budget");
                    String category = ((TextView) categoryLayout.getChildAt(0)).getText().toString();
                    String budgetAmountStr = editTextBudget.getText().toString().trim();

                    // Check if budget amount is empty or not
                    if (!budgetAmountStr.isEmpty()) {
                        double budgetAmount = Double.parseDouble(budgetAmountStr);

                        // Check if the budget for the category already exists
                        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BUDGETS +
                                " WHERE " + COLUMN_BUDGET_CATEGORY + " = ?", new String[]{category});

                        ContentValues values = new ContentValues();
                        values.put(COLUMN_BUDGET_CATEGORY, category);
                        values.put(COLUMN_BUDGET_AMOUNT, budgetAmount);

                        if (cursor.getCount() > 0) {
                            // Update the existing budget
                            db.update(TABLE_BUDGETS, values,
                                    COLUMN_BUDGET_CATEGORY + " = ?", new String[]{category});
                        } else {
                            // Insert the new budget
                            db.insert(TABLE_BUDGETS, null, values);
                        }

                        cursor.close();
                    } else {
                        // Show a toast indicating that budget amount is empty
                        Toast.makeText(context, "Please enter a budget amount for " + category, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            // Close the database connection
            db.close();

            // Show a toast indicating successful save
            Toast.makeText(context, "Budgets saved successfully", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            // Handle number format exception
            e.printStackTrace();
            Toast.makeText(context, "Please enter valid budget amounts", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            Toast.makeText(context, "Error saving budgets", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to retrieve all expenses from the database
    @SuppressLint("Range")
    public List<Expense> getAllExpenses() {
        List<Expense> expensesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES, null);
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_ID)));
                expense.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY)));
                expense.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT)));
                expense.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_DATE)));

                expensesList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expensesList;
    }

    // Method to retrieve all budgets from the database
    @SuppressLint("Range")
    public List<Budget> getAllBudgets() {
        List<Budget> budgetsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BUDGETS, null);
        if (cursor.moveToFirst()) {
            do {
                Budget budget = new Budget();
                budget.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_BUDGET_ID)));
                budget.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_BUDGET_CATEGORY)));
                budget.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_BUDGET_AMOUNT)));

                budgetsList.add(budget);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return budgetsList;
    }
    // Method to update a budget in the database
    public boolean updateBudget(int id, String category, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUDGET_CATEGORY, category);
        values.put(COLUMN_BUDGET_AMOUNT, amount);
        int rowsAffected = db.update(TABLE_BUDGETS, values, COLUMN_BUDGET_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    // Method to delete an expense from the database
    public boolean deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_EXPENSES, COLUMN_EXPENSE_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    // Method to delete a budget from the database
    public boolean deleteBudget(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_BUDGETS, COLUMN_BUDGET_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    // Method to get total expenses for a specific category
    public double getTotalExpensesForCategory(String category) {
        double totalExpenses = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_EXPENSE_AMOUNT + ") FROM " + TABLE_EXPENSES +
                " WHERE " + COLUMN_EXPENSE_CATEGORY + " = ?", new String[]{category});
        if (cursor.moveToFirst()) {
            totalExpenses = cursor.getDouble(0);
        }
        cursor.close();
        return totalExpenses;
    }

    // Method to get total budget for a specific category
    public double getTotalBudgetForCategory(String category) {
        double totalBudget = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_BUDGET_AMOUNT + " FROM " + TABLE_BUDGETS +
                " WHERE " + COLUMN_BUDGET_CATEGORY + " = ?", new String[]{category});
        if (cursor.moveToFirst()) {
            totalBudget = cursor.getDouble(0);
        }
        cursor.close();
        return totalBudget;
    }

    @SuppressLint("Range")
    public int getBudgetIdForCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGETS, new String[]{COLUMN_BUDGET_ID},
                COLUMN_BUDGET_CATEGORY + " = ?", new String[]{category},
                null, null, null);

        int budgetId = -1; // Default value if budget not found

        if (cursor.moveToFirst()) {
            budgetId = cursor.getInt(cursor.getColumnIndex(COLUMN_BUDGET_ID));
        }

        cursor.close();
        return budgetId;
    }

    // Method to insert user into the database
    public long insertUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        long id = db.insert(TABLE_USER, null, values);
        db.close();
        return id;
    }

    // Method to verify login credentials
    public boolean isValidLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,
                new String[]{COLUMN_USERNAME, COLUMN_PASSWORD},
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password},
                null, null, null);
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
}
