package com.example.project;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.widget.TextView;


public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvLastSimulation = findViewById(R.id.tvLastSimulation);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        User user = (User) getIntent().getSerializableExtra("user");
        String email = getIntent().getStringExtra("email");

        tvWelcome.setText(String.format(getString(R.string.dashboard_welcome), user.getName()));

        SharedPreferences prefs = getSharedPreferences("LoanSimulations", MODE_PRIVATE);
        String lastSimulation = prefs.getString("last_simulation", "No hay simulaciones recientes.");
        tvLastSimulation.setText("Última simulación: " + lastSimulation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
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
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("email", email);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
    }
}