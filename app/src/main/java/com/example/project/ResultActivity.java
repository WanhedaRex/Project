package com.example.project;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class ResultActivity extends AppCompatActivity {
    private static final double ANNUAL_INTEREST_RATE = 0.12; // 12% annual interest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView tvResult = findViewById(R.id.tvResult);
        TextView tvDetails = findViewById(R.id.tvDetails);
        RecyclerView rvAmortization = findViewById(R.id.rvAmortization);
        MaterialButton btnBack = findViewById(R.id.btnBack);

        boolean isEligible = getIntent().getBooleanExtra("isEligible", false);
        int creditScore = getIntent().getIntExtra("creditScore", 0);
        double loanAmount = getIntent().getDoubleExtra("loanAmount", 0);
        double income = getIntent().getDoubleExtra("income", 0);
        int loanTerm = getIntent().getIntExtra("loanTerm", 0);
        User user = (User) getIntent().getSerializableExtra("user");

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
        String resultText = isEligible ? getString(R.string.result_eligible) : getString(R.string.result_not_eligible);
        String detailsText = String.format(
                "Monto: %s\nPlazo: %d meses\nPuntaje de crédito: %d\nIngreso mensual: %s",
                currencyFormat.format(loanAmount),
                loanTerm,
                creditScore,
                currencyFormat.format(income)
        );

        tvResult.setText(resultText);
        tvDetails.setText(detailsText);

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        saveSimulation(loanAmount, loanTerm, isEligible, creditScore, date);

        List<AmortizationEntry> amortizationSchedule = calculateAmortization(loanAmount, loanTerm, ANNUAL_INTEREST_RATE);
        AmortizationAdapter adapter = new AmortizationAdapter(amortizationSchedule);
        rvAmortization.setLayoutManager(new LinearLayoutManager(this));
        rvAmortization.setAdapter(adapter);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("email", getIntent().getStringExtra("email"));
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });
    }

    private void saveSimulation(double amount, int term, boolean eligible, int score, String date) {
        SharedPreferences prefs = getSharedPreferences("LoanSimulations", MODE_PRIVATE);
        String existingSimulations = prefs.getString("simulations", "");
        String simulationData = String.format("%s|%d|%b|%d|%s", amount, term, eligible, score, date);
        String updatedSimulations = existingSimulations.isEmpty() ? simulationData : existingSimulations + ";" + simulationData;

        String lastSimulation = String.format(
                getString(R.string.simulation_details),
                NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(amount),
                term,
                eligible ? "Sí" : "No",
                score,
                date
        );

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("simulations", updatedSimulations);
        editor.putString("last_simulation", lastSimulation);
        editor.apply();
    }

    private List<AmortizationEntry> calculateAmortization(double principal, int termMonths, double annualRate) {
        List<AmortizationEntry> schedule = new ArrayList<>();
        double monthlyRate = annualRate / 12;
        double monthlyPayment = principal * (monthlyRate / (1 - Math.pow(1 + monthlyRate, -termMonths)));
        double balance = principal;

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

        for (int month = 1; month <= termMonths; month++) {
            double interest = balance * monthlyRate;
            double principalPayment = monthlyPayment - interest;
            balance -= principalPayment;

            AmortizationEntry entry = new AmortizationEntry(
                    month,
                    currencyFormat.format(monthlyPayment),
                    currencyFormat.format(principalPayment),
                    currencyFormat.format(interest),
                    currencyFormat.format(Math.max(balance, 0))
            );
            schedule.add(entry);
        }
        return schedule;
    }
}