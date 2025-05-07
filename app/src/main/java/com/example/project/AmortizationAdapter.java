package com.example.project;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class AmortizationAdapter extends RecyclerView.Adapter<AmortizationAdapter.ViewHolder> {
    private List<AmortizationEntry> entries;

    public AmortizationAdapter(List<AmortizationEntry> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amortization, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AmortizationEntry entry = entries.get(position);
        holder.tvMonth.setText(String.valueOf(entry.getMonth()));
        holder.tvMonthlyPayment.setText(entry.getMonthlyPayment());
        holder.tvPrincipal.setText(entry.getPrincipal());
        holder.tvInterest.setText(entry.getInterest());
        holder.tvBalance.setText(entry.getBalance());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth, tvMonthlyPayment, tvPrincipal, tvInterest, tvBalance;

        ViewHolder(View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvMonthlyPayment = itemView.findViewById(R.id.tvMonthlyPayment);
            tvPrincipal = itemView.findViewById(R.id.tvPrincipal);
            tvInterest = itemView.findViewById(R.id.tvInterest);
            tvBalance = itemView.findViewById(R.id.tvBalance);
        }
    }
}