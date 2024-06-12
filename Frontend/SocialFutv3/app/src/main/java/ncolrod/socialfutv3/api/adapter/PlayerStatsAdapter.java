package ncolrod.socialfutv3.api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.User;

/**
 * Adaptador para mostrar y actualizar las estadísticas de los jugadores en un RecyclerView.
 */
public class PlayerStatsAdapter extends RecyclerView.Adapter<PlayerStatsAdapter.PlayerStatsViewHolder> {

    private final Context context;
    private final List<User> players;

    public PlayerStatsAdapter(Context context, List<User> players) {
        this.context = context;
        this.players = players;
    }

    @NonNull
    @Override
    public PlayerStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_player_stats, parent, false);
        return new PlayerStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerStatsViewHolder holder, int position) {
        User player = players.get(position);

        // Configurar los valores de los campos del jugador
        holder.playerName.setText(player.getFirstname() + " " + player.getLastname());
        holder.goalsCountTextView.setText(String.valueOf(player.getGoals()));
        holder.assistsCountTextView.setText(String.valueOf(player.getAssists()));

        // Configurar los listeners para los botones de incrementar y decrementar goles
        holder.incrementGoalsButton.setOnClickListener(v -> {
            int goals = player.getGoals() + 1;
            player.setGoals(goals);
            holder.goalsCountTextView.setText(String.valueOf(goals));
        });

        holder.decrementGoalsButton.setOnClickListener(v -> {
            int goals = player.getGoals() - 1;
            if (goals < 0) goals = 0;
            player.setGoals(goals);
            holder.goalsCountTextView.setText(String.valueOf(goals));
        });

        // Configurar los listeners para los botones de incrementar y decrementar asistencias
        holder.incrementAssistsButton.setOnClickListener(v -> {
            int assists = player.getAssists() + 1;
            player.setAssists(assists);
            holder.assistsCountTextView.setText(String.valueOf(assists));
        });

        holder.decrementAssistsButton.setOnClickListener(v -> {
            int assists = player.getAssists() - 1;
            if (assists < 0) assists = 0;
            player.setAssists(assists);
            holder.assistsCountTextView.setText(String.valueOf(assists));
        });

        // Configurar el CheckBox de participación del jugador
        holder.playerParticipationCheckBox.setChecked(player.isParticipated());
        holder.playerParticipationCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            player.setParticipated(isChecked);
            if (isChecked) {
                player.setMatchesPlayed(player.getMatchesPlayed() + 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    /**
     * Obtiene la lista de jugadores.
     *
     * @return la lista de jugadores
     */
    public List<User> getPlayers() {
        return players;
    }

    /**
     * ViewHolder para los elementos de la lista de estadísticas de jugadores.
     */
    public static class PlayerStatsViewHolder extends RecyclerView.ViewHolder {
        TextView playerName;
        CheckBox playerParticipationCheckBox;
        Button decrementGoalsButton;
        Button incrementGoalsButton;
        TextView goalsCountTextView;
        Button decrementAssistsButton;
        Button incrementAssistsButton;
        TextView assistsCountTextView;

        public PlayerStatsViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.playerName);
            playerParticipationCheckBox = itemView.findViewById(R.id.playerParticipationCheckBox);
            decrementGoalsButton = itemView.findViewById(R.id.decrementGoalsButton);
            incrementGoalsButton = itemView.findViewById(R.id.incrementGoalsButton);
            goalsCountTextView = itemView.findViewById(R.id.goalsCountTextView);
            decrementAssistsButton = itemView.findViewById(R.id.decrementAssistsButton);
            incrementAssistsButton = itemView.findViewById(R.id.incrementAssistsButton);
            assistsCountTextView = itemView.findViewById(R.id.assistsCountTextView);
        }
    }
}
