package com.example.fmanagement;

import static com.example.fmanagement.R.id.ll_budgets;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class Monitoring extends AppCompatActivity {

    private LinearLayout llBudgets;
    private Button buttonSaveBudgets;
    private Button buttonReset;
    private CheckBox checkBoxAlertSound;
    private CheckBox checkBoxAlertVibration;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitoring);

        // Initialize DataBaseHelper with appropriate constructor parameters
        dataBaseHelper = new DataBaseHelper(this);

        // Toolbar setup
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize views
        llBudgets = findViewById(R.id.ll_budgets);
        buttonSaveBudgets = findViewById(R.id.buttonSaveBudgets);
        buttonReset = findViewById(R.id.buttonReset);
        checkBoxAlertSound = findViewById(R.id.checkBoxAlertSound);
        checkBoxAlertVibration = findViewById(R.id.checkBoxAlertVibration);
        // Set onClickListener for Save Budgets button
        buttonSaveBudgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.saveBudgets(Monitoring.this, llBudgets); // Pass llBudgets to saveBudgets method
                saveSettings();
            }
        });

        // Set onClickListener for Reset button
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBudgets();
            }
        });
    }

    // Reset budgets method
    private void resetBudgets() {
        // Iterate through each budget category and reset the budget to zero
        for (int i = 0; i < llBudgets.getChildCount(); i++) {
            View child = llBudgets.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout categoryLayout = (LinearLayout) child;
                EditText editTextBudget = categoryLayout.findViewWithTag("budget");
                editTextBudget.setText(""); // Reset budget to zero
            }
        }
        Toast.makeText(this, "Budgets reset successfully", Toast.LENGTH_SHORT).show();
    }
    private void saveSettings() {
        try {
            // Get selected options
            boolean sound = checkBoxAlertSound.isChecked();
            boolean vibration = checkBoxAlertVibration.isChecked();

            // Save settings to preferences or database
            // For demonstration, we'll just show a toast with selected options
            StringBuilder message = new StringBuilder("Settings saved:\n");
            message.append("Alert notifications for monitoring:\n");
            if (sound) {
                message.append("- Sound\n");
            }
            if (vibration) {
                message.append("- Vibration\n");
            }

            Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving settings", Toast.LENGTH_SHORT).show();
        }
    }
    // Handle Up button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
