package ncolrod.socialfutv3.api.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.User;

/**
 * Adaptador para mostrar las estadísticas de los jugadores en un RecyclerView.
 */
public class ShowPlayerStatsAdapter extends RecyclerView.Adapter<ShowPlayerStatsAdapter.PlayerViewHolder> {

    private List<User> players;

    public ShowPlayerStatsAdapter(List<User> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_player_stats, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        User player = players.get(position);

        // Configurar los valores de los campos del jugador
        holder.playerNameTextView.setText(player.getFirstname() + " " + player.getLastname());
        holder.positionTextView.setText("Position: " + player.getPosition());
        holder.goalsTextView.setText("Goals: " + player.getGoals());
        holder.assistsTextView.setText("Assists: " + player.getAssists());
        holder.matchesTextView.setText("Matches: " + player.getMatchesPlayed());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    /**
     * ViewHolder para los elementos de la lista de estadísticas de jugadores.
     */
    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameTextView, positionTextView, goalsTextView, assistsTextView, matchesTextView;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerName);
            positionTextView = itemView.findViewById(R.id.playerPosition);
            goalsTextView = itemView.findViewById(R.id.playerGoals);
            assistsTextView = itemView.findViewById(R.id.playerAssists);
            matchesTextView = itemView.findViewById(R.id.playerMatches);
        }
    }
}
