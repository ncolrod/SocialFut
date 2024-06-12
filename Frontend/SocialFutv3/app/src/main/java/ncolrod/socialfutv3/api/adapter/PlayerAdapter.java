package ncolrod.socialfutv3.api.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Role;
import ncolrod.socialfutv3.api.models.User;

/**
 * Adaptador para mostrar la lista de jugadores en un RecyclerView.
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<User> players = new ArrayList<>();

    public PlayerAdapter(List<User> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        User player = players.get(position);
        String fullName = getFullName(player);

        // Añadir "(Captain)" si el jugador es un ADMIN
        if (player.getRole() == Role.ADMIN) {
            fullName += " (Captain)";
        }

        holder.playerNameTextView.setText(fullName);
        holder.playerPositionTextView.setText(player.getPosition() != null ? player.getPosition() : "Position not specified");
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    /**
     * Actualiza la lista de jugadores con una nueva lista.
     *
     * @param newPlayers la nueva lista de jugadores
     */
    public void updatePlayers(List<User> newPlayers) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new PlayerDiffCallback(this.players, newPlayers));
        this.players.clear();
        this.players.addAll(newPlayers);
        diffResult.dispatchUpdatesTo(this);
    }

    /**
     * Obtiene el nombre completo del jugador.
     *
     * @param player el jugador
     * @return el nombre completo del jugador
     */
    private String getFullName(User player) {
        String firstName = player.getFirstname() != null ? player.getFirstname() : "Unknown";
        String lastName = player.getLastname() != null ? player.getLastname() : "Player";
        return firstName + " " + lastName;
    }

    /**
     * ViewHolder para los elementos de la lista de jugadores.
     */
    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameTextView, playerPositionTextView;

        PlayerViewHolder(View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerName);
            playerPositionTextView = itemView.findViewById(R.id.playerPosition);
        }
    }

    /**
     * Callback para calcular las diferencias entre dos listas de jugadores.
     */
    static class PlayerDiffCallback extends DiffUtil.Callback {

        private final List<User> oldList;
        private final List<User> newList;

        PlayerDiffCallback(List<User> oldList, List<User> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            User oldUser = oldList.get(oldItemPosition);
            User newUser = newList.get(newItemPosition);
            return oldUser.equals(newUser);
        }
    }
}
