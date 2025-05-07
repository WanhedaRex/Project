package com.example.project;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.regex.Pattern;
public class ProfileActivity extends AppCompatActivity {
    private TextInputLayout tilName, tilRFC, tilIncome;
    private TextInputEditText etName, etRFC, etIncome;
    private User user;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tilName = findViewById(R.id.tilName);
        tilRFC = findViewById(R.id.tilRFC);
        tilIncome = findViewById(R.id.tilIncome);
        etName = findViewById(R.id.etName);
        etRFC = findViewById(R.id.etRFC);
        etIncome = findViewById(R.id.etIncome);
        MaterialButton btnSaveProfile = findViewById(R.id.btnSaveProfile);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        user = (User) getIntent().getSerializableExtra("user");
        email = getIntent().getStringExtra("email");

        etName.setText(user.getName());
        etRFC.setText(user.getRFC());
        etIncome.setText(String.valueOf(user.getIncome()));

        btnSaveProfile.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String rfc = etRFC.getText().toString().trim();
            String incomeStr = etIncome.getText().toString().trim();

            tilName.setError(null);
            tilRFC.setError(null);
            tilIncome.setError(null);

            if (name.isEmpty() || rfc.isEmpty() || incomeStr.isEmpty()) {
                if (name.isEmpty()) tilName.setError(getString(R.string.error_empty_fields));
                if (rfc.isEmpty()) tilRFC.setError(getString(R.string.error_empty_fields));
                if (incomeStr.isEmpty()) tilIncome.setError(getString(R.string.error_empty_fields));
                return;
            }

            if (!rfc.matches("[A-Z0-9]{13}")) {
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
                tilIncome.setError("Ingresa un ingreso válido");
                return;
            }

            user.setName(name);
            user.setRFC(rfc);
            user.setIncome(income);

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(email + "_name", name);
            editor.putString(email + "_rfc", rfc);
            editor.putFloat(email + "_income", (float) income);
            editor.apply();

            Toast.makeText(this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("email", email);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (itemId == R.id.nav_loan) {
                Intent intent = new Intent(this, LoanActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.nav_history) {
                Intent intent = new Intent(this, HistoryActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("email", email);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }
}