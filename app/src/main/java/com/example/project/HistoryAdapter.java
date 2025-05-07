package com.example.project;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.SimulationViewHolder> {
    private List<Simulation> simulations;

    public HistoryAdapter(List<Simulation> simulations) {
        this.simulations = simulations;
    }

    @NonNull
    @Override
    public SimulationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simulation, parent, false);
        return new SimulationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimulationViewHolder holder, int position) {
        Simulation simulation = simulations.get(position);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
        String eligibleText = simulation.isEligible() ? "Sí" : "No";
        String details = String.format(
                holder.itemView.getContext().getString(R.string.simulation_details),
                currencyFormat.format(simulation.getAmount()),
                simulation.getTerm(),
                eligibleText,
                simulation.getScore(),
                simulation.getDate()
        );
        holder.tvSimulationDetails.setText(details);
    }

    @Override
    public int getItemCount() {
        return simulations.size();
    }

    static class SimulationViewHolder extends RecyclerView.ViewHolder {
        TextView tvSimulationDetails;

        SimulationViewHolder(View itemView) {
            super(itemView);
            tvSimulationDetails = itemView.findViewById(R.id.tvSimulationDetails);
        }
    }
}