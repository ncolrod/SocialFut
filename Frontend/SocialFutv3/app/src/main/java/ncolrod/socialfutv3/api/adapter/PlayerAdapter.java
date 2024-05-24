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

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<User> players;

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
        holder.playerNameTextView.setText(player.getFirstname() + " " + player.getLastname());
        holder.playerPositionTextView.setText(player.getPosition());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void updatePlayers(List<User> newPlayers) {
        players.clear();
        players.addAll(newPlayers);
        notifyDataSetChanged();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameTextView, playerPositionTextView;

        PlayerViewHolder(View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerName);
            playerPositionTextView = itemView.findViewById(R.id.playerPosition);
        }
    }
}
