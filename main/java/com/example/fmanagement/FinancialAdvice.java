package com.example.fmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FinancialAdvice extends AppCompatActivity {

    private TextView textInfo;
    private EditText editTextPaymentName, editTextPaymentDeadline;
    private Button buttonSavePayment;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.financial_advice);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataBaseHelper = new DataBaseHelper(this);

        textInfo = findViewById(R.id.textTheme2);
        editTextPaymentName = findViewById(R.id.editTextPaymentName);
        editTextPaymentDeadline = findViewById(R.id.editTextPaymentDeadline);
        buttonSavePayment = findViewById(R.id.buttonSavePayment);

        buttonSavePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePayment();
            }
        });
    }

    private void savePayment() {
        String paymentName = editTextPaymentName.getText().toString().trim();
        String paymentDeadlineStr = editTextPaymentDeadline.getText().toString().trim();
        if (!paymentName.isEmpty() && !paymentDeadlineStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date paymentDeadline = sdf.parse(paymentDeadlineStr);
                long result = dataBaseHelper.insertPayment(paymentName, sdf.format(paymentDeadline));
                if (result != -1) {
                    Toast.makeText(this, "Payment saved successfully", Toast.LENGTH_SHORT).show();
                    editTextPaymentName.getText().clear();
                    editTextPaymentDeadline.getText().clear();
                } else {
                    Toast.makeText(this, "Failed to save payment", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Invalid date format. Please use YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter payment name and deadline", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
