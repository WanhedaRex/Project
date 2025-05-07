package com.example.project;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
public class HistoryActivity extends AppCompatActivity {
    private RecyclerView rvSimulations;
    private TextView tvEmptyState;
    private HistoryAdapter adapter;
    private List<Simulation> simulations;
    private User user;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        rvSimulations = findViewById(R.id.rvSimulations);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        user = (User) getIntent().getSerializableExtra("user");
        email = getIntent().getStringExtra("email");

        simulations = loadSimulations();
        adapter = new HistoryAdapter(simulations);
        rvSimulations.setLayoutManager(new LinearLayoutManager(this));
        rvSimulations.setAdapter(adapter);

        if (simulations.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvSimulations.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvSimulations.setVisibility(View.VISIBLE);
        }

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
        bottomNavigationView.setSelectedItemId(R.id.nav_history);
    }

    private List<Simulation> loadSimulations() {
        List<Simulation> simulationList = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("LoanSimulations", MODE_PRIVATE);
        String simulationsData = prefs.getString("simulations", "");
        if (!simulationsData.isEmpty()) {
            String[] simulationArray = simulationsData.split(";");
            for (String sim : simulationArray) {
                String[] parts = sim.split("\\|");
                if (parts.length == 5) {
                    double amount = Double.parseDouble(parts[0]);
                    int term = Integer.parseInt(parts[1]);
                    boolean eligible = Boolean.parseBoolean(parts[2]);
                    int score = Integer.parseInt(parts[3]);
                    String date = parts[4];
                    simulationList.add(new Simulation(amount, term, eligible, score, date));
                }
            }
        }
        return simulationList;
    }
}