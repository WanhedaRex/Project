package com.example.project;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Random;
public class LoanActivity extends AppCompatActivity {
    private TextInputLayout tilAmount, tilTerm;
    private TextInputEditText etAmount, etTerm;
    private LottieAnimationView lottieAnimation;
    private MaterialButton btnSimulate;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        tilAmount = findViewById(R.id.tilAmount);
        tilTerm = findViewById(R.id.tilTerm);
        etAmount = findViewById(R.id.etAmount);
        etTerm = findViewById(R.id.etTerm);
        btnSimulate = findViewById(R.id.btnSimulate);
        lottieAnimation = findViewById(R.id.lottieAnimation);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        user = (User) getIntent().getSerializableExtra("user");
        String email = getIntent().getStringExtra("email");

        lottieAnimation.setVisibility(View.GONE);

        btnSimulate.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString().trim();
            String termStr = etTerm.getText().toString().trim();

            tilAmount.setError(null);
            tilTerm.setError(null);

            if (amountStr.isEmpty() || termStr.isEmpty()) {
                if (amountStr.isEmpty()) tilAmount.setError(getString(R.string.error_empty_fields));
                if (termStr.isEmpty()) tilTerm.setError(getString(R.string.error_empty_fields));
                return;
            }

            double amount;
            int term;
            try {
                amount = Double.parseDouble(amountStr);
                term = Integer.parseInt(termStr);
                if (amount <= 0) {
                    tilAmount.setError("El monto debe ser mayor a 0");
                    return;
                }
                if (term <= 0) {
                    tilTerm.setError("El plazo debe ser mayor a 0");
                    return;
                }
            } catch (NumberFormatException e) {
                tilAmount.setError("Ingresa valores numéricos válidos");
                return;
            }

            btnSimulate.setEnabled(false);
            lottieAnimation.setAnimation(R.raw.loading_animation);
            lottieAnimation.setVisibility(View.VISIBLE);
            lottieAnimation.playAnimation();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Random random = new Random();
                int creditScore = random.nextInt(401) + 400; // 400-800
                boolean isEligible = creditScore >= 600 && amount <= user.getIncome() * 3;

                lottieAnimation.setAnimation(isEligible ? R.raw.success_animation : R.raw.failure_animation);
                lottieAnimation.playAnimation();

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    lottieAnimation.setVisibility(View.GONE);
                    btnSimulate.setEnabled(true);

                    Intent intent = new Intent(this, ResultActivity.class);
                    intent.putExtra("isEligible", isEligible);
                    intent.putExtra("creditScore", creditScore);
                    intent.putExtra("loanAmount", amount);
                    intent.putExtra("income", user.getIncome());
                    intent.putExtra("loanTerm", term);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }, 1000);
            }, 2000);
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
        bottomNavigationView.setSelectedItemId(R.id.nav_loan);
    }
}