package ncolrod.socialfutv3.api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.models.Role;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {
    private Context context;
    private List<Match> matchesList;
    private Team currentTeam;
    private User currentUser;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCardClick(int position);
        void onJoinButtonClick(int position);
        void onCancelButtonClick(int position);
        void onInfoButtonClick(int position);
        void onModifyButtonClick(int position);
        void onDeleteButtonClick(int position);
    }

    public MatchesAdapter(Context context, List<Match> matchesList, Team currentTeam, User currentUser) {
        this.context = context;
        this.matchesList = matchesList;
        this.currentTeam = currentTeam;
        this.currentUser = currentUser;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matchesList.get(position);

        holder.teamNameTextView.setText(match.getHomeTeam().getName());
        holder.dateTextView.setText(match.getDate().toString());
        holder.locationTextView.setText(match.getLocation());
        holder.pricePerPersonTextView.setText(String.format("Price per person: %s", match.getPricePerPerson()));

        boolean hasJoined = match.isCreated();

        if (currentUser.getId()==match.getCreatorUser().getId()) {
            holder.joinButton.setVisibility(View.GONE);
            holder.cancelButton.setVisibility(View.GONE);
            holder.modifyButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else if (currentUser.getRole() == Role.ADMIN) {
            holder.joinButton.setVisibility(View.VISIBLE);
            holder.cancelButton.setVisibility(hasJoined ? View.VISIBLE : View.GONE);
            holder.modifyButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            holder.joinButton.setVisibility(View.GONE);
            holder.cancelButton.setVisibility(View.GONE);
            holder.modifyButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.infoButton.setVisibility(View.VISIBLE);

        holder.joinButton.setOnClickListener(v -> listener.onJoinButtonClick(position));
        holder.cancelButton.setOnClickListener(v -> listener.onCancelButtonClick(position)); // Manejar la lógica para cancelar
        holder.infoButton.setOnClickListener(v -> listener.onInfoButtonClick(position));
        holder.modifyButton.setOnClickListener(v -> listener.onModifyButtonClick(position));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteButtonClick(position));
    }


    @Override
    public int getItemCount() {
        return matchesList.size();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView teamNameTextView, dateTextView, locationTextView, pricePerPersonTextView;
        Button joinButton, cancelButton, infoButton, modifyButton, deleteButton;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            teamNameTextView = itemView.findViewById(R.id.teamNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            pricePerPersonTextView = itemView.findViewById(R.id.pricePerPersonTextView);
            joinButton = itemView.findViewById(R.id.joinButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            infoButton = itemView.findViewById(R.id.infoButton);
            modifyButton = itemView.findViewById(R.id.modifyButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
