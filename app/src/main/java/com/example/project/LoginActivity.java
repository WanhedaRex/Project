package com.example.project;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.security.MessageDigest;
public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etEmail, etPassword;
    private TextInputLayout tilEmail, tilPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            tilEmail.setError(null);
            tilPassword.setError(null);

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) tilEmail.setError(getString(R.string.error_empty_fields));
                if (password.isEmpty()) tilPassword.setError(getString(R.string.error_empty_fields));
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String userData = prefs.getString("user_" + email, null);
            if (userData == null) {
                tilEmail.setError(getString(R.string.error_login_failed));
                return;
            }

            String hashedPassword = hashPassword(password);
            String[] userParts = userData.split("\\|");
            if (!userParts[0].equals(hashedPassword)) {
                tilPassword.setError(getString(R.string.error_login_failed));
                return;
            }

            String name = userParts[1];
            String rfc = userParts[2];
            double income = Double.parseDouble(userParts[3]);

            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("user", new User(name, rfc, income));
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
