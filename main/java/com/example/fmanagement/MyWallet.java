package com.example.fmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class MyWallet extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText editTextAmount;
    private DatePicker datePicker;
    private Button buttonSave;
    private Button buttonCancel;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_wallet);

        // Initialize DataBaseHelper with appropriate constructor parameters
        dataBaseHelper = new DataBaseHelper(this);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize views
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextAmount = findViewById(R.id.editTextAmount);
        datePicker = findViewById(R.id.datePicker);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });

        // Set onClickListener for Cancel button
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear fields
                clearFields();
            }
        });
    }

    // Save expense method
    // Save expense method
    private void saveExpense() {
        String category = spinnerCategory.getSelectedItem().toString();
        String amount = editTextAmount.getText().toString();
        String date = getDateFromDatePicker(datePicker);

        // Validate inputs
        if (category.isEmpty() || amount.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call saveExpense method from DatabaseHelper
        boolean expenseSaved = dataBaseHelper.saveExpense(category, Double.parseDouble(amount), date);

        if (expenseSaved) {
            // Expense saved successfully
            Toast.makeText(MyWallet.this, "Expense saved successfully", Toast.LENGTH_SHORT).show();
            // Clear input fields
            clearFields();
        } else {
            // Failed to save expense
            Toast.makeText(MyWallet.this, "Failed to save expense", Toast.LENGTH_SHORT).show();
        }
    }

    // Clear input fields method
    private void clearFields() {
        spinnerCategory.setSelection(0);
        editTextAmount.setText("");
        // Set current date in DatePicker
        datePicker.updateDate(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    // Get date from DatePicker method
    private String getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        return day + "/" + month + "/" + year;
    }

    // Handle Up button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
