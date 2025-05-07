package com.example.project;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.security.MessageDigest;
import java.util.regex.Pattern;
public class RegisterActivity extends AppCompatActivity{
    private TextInputEditText etEmail, etPassword, etName, etRFC, etIncome;
    private TextInputLayout tilEmail, tilPassword, tilName, tilRFC, tilIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilName = findViewById(R.id.tilName);
        tilRFC = findViewById(R.id.tilRFC);
        tilIncome = findViewById(R.id.tilIncome);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        etRFC = findViewById(R.id.etRFC);
        etIncome = findViewById(R.id.etIncome);
        MaterialButton btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String rfc = etRFC.getText().toString().trim();
            String incomeStr = etIncome.getText().toString().trim();

            // Reset errors
            tilEmail.setError(null);
            tilPassword.setError(null);
            tilName.setError(null);
            tilRFC.setError(null);
            tilIncome.setError(null);

            // Validate inputs
            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || rfc.isEmpty() || incomeStr.isEmpty()) {
                if (email.isEmpty()) tilEmail.setError(getString(R.string.error_empty_fields));
                if (password.isEmpty()) tilPassword.setError(getString(R.string.error_empty_fields));
                if (name.isEmpty()) tilName.setError(getString(R.string.error_empty_fields));
                if (rfc.isEmpty()) tilRFC.setError(getString(R.string.error_empty_fields));
                if (incomeStr.isEmpty()) tilIncome.setError(getString(R.string.error_empty_fields));
                return;
            }

            // Validate email
            Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
            if (!emailPattern.matcher(email).matches()) {
                tilEmail.setError(getString(R.string.error_invalid_email));
                return;
            }

            // Validate password
            if (password.length() < 6) {
                tilPassword.setError(getString(R.string.error_invalid_password));
                return;
            }

            // Validate RFC
            Pattern rfcPattern = Pattern.compile("^[A-Z]{4}\\d{6}[A-Z0-9]{3}$");
            if (!rfcPattern.matcher(rfc).matches()) {
                tilRFC.setError(getString(R.string.error_invalid_rfc));
                return;
            }

            // Validate income
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

            // Check if user exists
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            if (prefs.contains("user_" + email)) {
                tilEmail.setError(getString(R.string.error_user_exists));
                return;
            }

            // Hash password
            String hashedPassword = hashPassword(password);

            // Save user data
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_" + email, hashedPassword + "|" + name + "|" + rfc + "|" + income);
            editor.apply();

            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return password; // Fallback (not secure, for simulation only)
        }
    }
}
