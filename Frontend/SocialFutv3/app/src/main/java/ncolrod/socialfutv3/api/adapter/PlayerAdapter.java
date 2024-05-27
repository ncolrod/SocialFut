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
import ncolrod.socialfutv3.api.models.User;

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
        holder.playerNameTextView.setText(getFullName(player));
        holder.playerPositionTextView.setText(player.getPosition() != null ? player.getPosition() : "Position not specified");
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void updatePlayers(List<User> newPlayers) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new PlayerDiffCallback(this.players, newPlayers));
        this.players.clear();
        this.players.addAll(newPlayers);
        diffResult.dispatchUpdatesTo(this);
    }

    private String getFullName(User player) {
        String firstName = player.getFirstname() != null ? player.getFirstname() : "Unknown";
        String lastName = player.getLastname() != null ? player.getLastname() : "Player";
        return firstName + " " + lastName;
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameTextView, playerPositionTextView;

        PlayerViewHolder(View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerName);
            playerPositionTextView = itemView.findViewById(R.id.playerPosition);
        }
    }

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
