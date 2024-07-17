package com.example.fmanagement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.graphics.Color;
import java.util.Locale;
import java.text.ParseException;
public class FollowWallet extends AppCompatActivity {
    private List<Entry> expensesEntries;
    private LineChart lineChart;
    private CheckBox checkBoxImportantTransactions;
    private CheckBox checkBoxUnusualExpenses;
    private CheckBox checkBoxPaymentDeadlines;
    private Button buttonSaveSettings;

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_wallet);
        expensesEntries = new ArrayList<>();
        // Database initialization
        dataBaseHelper = new DataBaseHelper(this);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize views
        lineChart = findViewById(R.id.lineChart);
        checkBoxImportantTransactions = findViewById(R.id.checkBoxImportantTransactions);
        checkBoxUnusualExpenses = findViewById(R.id.checkBoxUnusualExpenses);
        checkBoxPaymentDeadlines = findViewById(R.id.checkBoxPaymentDeadlines);
        buttonSaveSettings = findViewById(R.id.buttonSaveSettings);
        // Setup LineChart
        setupLineChart();
        // Button click listener
        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    // Setup LineChart with data from database
    private void setupLineChart() {
        // Initialize xLabels if needed
        List<String> xLabels = new ArrayList<>(); // List to store x-axis labels

        // Get daily expenses from database
        List<Expense> expenses = dataBaseHelper.getAllExpenses();

        // Add daily expenses to entries lists
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            expensesEntries.add(new Entry(i, (float) expense.getAmount()));
            xLabels.add(getDayOfMonth(expense.getDate()));
        }

        // Create dataset for expenses
        LineDataSet expensesDataSet = new LineDataSet(expensesEntries, "Expenses");
        expensesDataSet.setColor(Color.BLUE); // Set color for expenses chart
        expensesDataSet.setCircleColor(Color.BLUE); // Set circle color for expenses chart

        // Create a LineData object from the dataset
        LineData lineData = new LineData(expensesDataSet);

        // Set data to the chart
        lineChart.setData(lineData);

        // Customize chart appearance
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);
        lineChart.getLegend().setEnabled(true); // Enable legend to distinguish between expenses and budgets

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Ensure index is within the bounds of xLabels
                int index = (int) value;
                if (index >= 0 && index < xLabels.size()) {
                    return xLabels.get(index);
                } else {
                    return ""; // Return empty string for out-of-bounds indices
                }
            }
        });

        // Add additional charts for food budget, accommodation budget, and transportation budget
        addAdditionalCharts();

        // Refresh chart
        lineChart.invalidate();
    }

    // Method to add additional charts for food budget, accommodation budget, and transportation budget
    private void addAdditionalCharts() {
        if (expensesEntries != null) {
            int size = expensesEntries.size();
            // Use the size as needed
        } else {
            // Handle the case when the list is null
        }
        // Create datasets for additional charts
        List<Entry> foodBudgetEntries = new ArrayList<>();
        List<Entry> accommodationBudgetEntries = new ArrayList<>();
        List<Entry> transportationBudgetEntries = new ArrayList<>();

        // Retrieve budget amounts for food, accommodation, and transportation categories
        double foodBudget = dataBaseHelper.getTotalBudgetForCategory("Food");
        double accommodationBudget = dataBaseHelper.getTotalBudgetForCategory("Accommodation");
        double transportationBudget = dataBaseHelper.getTotalBudgetForCategory("Transportation");

        // Populate entries lists with budget amounts for each day
        for (int i = 0; i < expensesEntries.size(); i++) {
            foodBudgetEntries.add(new Entry(i, (float) foodBudget));
            accommodationBudgetEntries.add(new Entry(i, (float) accommodationBudget));
            transportationBudgetEntries.add(new Entry(i, (float) transportationBudget));
        }

        // Create datasets for each additional chart
        LineDataSet foodBudgetDataSet = new LineDataSet(foodBudgetEntries, "Food Budget");
        LineDataSet accommodationBudgetDataSet = new LineDataSet(accommodationBudgetEntries, "Accommodation Budget");
        LineDataSet transportationBudgetDataSet = new LineDataSet(transportationBudgetEntries, "Transportation Budget");

        // Customize appearance of additional datasets (colors, etc.)
        foodBudgetDataSet.setColor(Color.RED);
        accommodationBudgetDataSet.setColor(Color.GREEN);
        transportationBudgetDataSet.setColor(Color.DKGRAY);

        // Add datasets to LineData object
        LineData lineData = lineChart.getData();
        lineData.addDataSet(foodBudgetDataSet);
        lineData.addDataSet(accommodationBudgetDataSet);
        lineData.addDataSet(transportationBudgetDataSet);
    }

    // Method to get the day of the month from a date string
    private String getDayOfMonth(String date) {
        // Parse date string and extract day of month
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(date));
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            return String.valueOf(dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace(); // This will print the error stack trace
            return ""; // Return empty string in case of parsing error
        }
    }

    // Method to save user settings
    private void saveSettings() {
        try {
            // Get selected options
            boolean importantTransactions = checkBoxImportantTransactions.isChecked();
            boolean unusualExpenses = checkBoxUnusualExpenses.isChecked();
            boolean paymentDeadlines = checkBoxPaymentDeadlines.isChecked();

            // Save settings to preferences or database
            // For demonstration, we'll just show a toast with selected options
            StringBuilder message = new StringBuilder("Settings saved:\n");
            message.append("Notification Options:\n");
            if (importantTransactions) {
                message.append("- Large Transactions\n");
                checkLargeTransactions();
            }
            if (unusualExpenses) {
                message.append("- Unusual Expenses\n");
                checkBudgetLimits();
            }
            if (paymentDeadlines) {
                message.append("- Payment Deadlines\n");
                checkPaymentDeadlines();
            }

            Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving settings", Toast.LENGTH_SHORT).show();
        }
    }
    private void checkBudgetLimits() {
        List<Expense> Expenses = dataBaseHelper.getAllExpenses();
        for (Expense expense : Expenses) {
            double totalExpense = dataBaseHelper.getTotalExpensesForCategory(expense.getCategory());
            double budgetLimit = dataBaseHelper.getTotalBudgetForCategory(expense.getCategory());
            if (totalExpense > budgetLimit) {
                // Dépassement de la limite de dépenses, déclencher une alerte
                String message = "Expense limit for category " + expense.getCategory() + " is passed.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            else{
                String message = "Expense limit is fine";
            }
        }
    }
    private void checkLargeTransactions() {
        List<Expense> Expenses = dataBaseHelper.getAllExpenses();
        for (Expense expense : Expenses) {
            double transactionAmountThreshold = 1000.0; // Define your threshold for "large" transactions
            if (expense.getAmount() > transactionAmountThreshold) {
                // Large transaction found, trigger alert
                String message = "Large transaction detected: " + expense.getAmount();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPaymentDeadlines() {
        // Retrieve payment deadlines from the database
        List<Payment> payments = dataBaseHelper.getAllPayments();

        for (Payment payment : payments) {
            // Compare the current date with the payment deadline
            // For demonstration, let's assume the payment deadline is stored as a string in the format "YYYY-MM-DD"
            String paymentDeadline = payment.getDeadline();
            String currentDate = getCurrentDate(); // Implement a method to get the current date
            // Compare dates
            if (currentDate.compareTo(paymentDeadline) > 0) {
                // Payment deadline passed, trigger alert
                String message = "Payment deadline passed for payment: " + payment.getName();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String getCurrentDate() {
        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        // Format the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

    // Handle Up button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}