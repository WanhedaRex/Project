package com.example.project;

import android.os.Bundle;


import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText etName, etRFC, etIncome;
    private TextInputLayout tilName, tilRFC, tilIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tilName = findViewById(R.id.tilName);
        tilRFC = findViewById(R.id.tilRFC);
        tilIncome = findViewById(R.id.tilIncome);
        etName = findViewById(R.id.etName);
        etRFC = findViewById(R.id.etRFC);
        etIncome = findViewById(R.id.etIncome);
        MaterialButton btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String rfc = etRFC.getText().toString().trim();
            String incomeStr = etIncome.getText().toString().trim();

            // Reset errors
            tilName.setError(null);
            tilRFC.setError(null);
            tilIncome.setError(null);

            if (name.isEmpty() || rfc.isEmpty() || incomeStr.isEmpty()) {
                if (name.isEmpty()) tilName.setError(getString(R.string.error_empty_fields));
                if (rfc.isEmpty()) tilRFC.setError(getString(R.string.error_empty_fields));
                if (incomeStr.isEmpty()) tilIncome.setError(getString(R.string.error_empty_fields));
                return;
            }

            // Validate RFC (e.g., ABCD123456XYZ)
            Pattern rfcPattern = Pattern.compile("^[A-Z]{4}\\d{6}[A-Z0-9]{3}$");
            if (!rfcPattern.matcher(rfc).matches()) {
                tilRFC.setError(getString(R.string.error_invalid_rfc));
                return;
            }

            double income;
            try {
                income = Double.parseDouble(incomeStr);
                if (income <= 0) {
                    tilIncome.setError(getString(R.string.error_invalid_income));
                    return;
                }
            } catch (NumberFormatException e) {
                tilIncome.setError(getString(R.string.error_invalid_income));
                return;
            }

            User user = new User(name, rfc, income);
            Intent intent = new Intent(this, LoanActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}