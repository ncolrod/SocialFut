package ncolrod.socialfutv3.api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.NonNull;
import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Match;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {
    private Context context;
    private List<Match> matchesList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCardClick(int position);
        void onJoinButtonClick(int position);
        void onCancelButtonClick(int position);
        void onInfoButtonClick(int position);
    }

    public MatchesAdapter(Context context, List<Match> matchesList) {
        this.context = context;
        this.matchesList = matchesList;
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
        holder.bind(match, listener);
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {
        private Button joinButton;
        private Button cancelButton;
        private Button infoButton;
        private TextView textViewTeamHomeName, textViewLocation, textViewDate, textViewPricePerPerson;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            joinButton = itemView.findViewById(R.id.joinButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            infoButton = itemView.findViewById(R.id.infoButton);
            textViewTeamHomeName = itemView.findViewById(R.id.teamNameTextView);
            textViewLocation = itemView.findViewById(R.id.locationTextView);
            textViewDate = itemView.findViewById(R.id.dateTextView);
            textViewPricePerPerson = itemView.findViewById(R.id.pricePerPersonTextView);
        }

        public void bind(Match match, OnItemClickListener listener) {
            textViewTeamHomeName.setText(match.getHomeTeam().getName());
            textViewLocation.setText(match.getLocation());
            textViewDate.setText(match.getDate().toString());
            textViewPricePerPerson.setText(String.valueOf(match.getPricePerPerson()));

            joinButton.setVisibility(match.isCreated() ? View.GONE : View.VISIBLE);
            cancelButton.setVisibility(match.isCreated() ? View.VISIBLE : View.GONE);
            infoButton.setVisibility(match.isCreated() ? View.VISIBLE : View.GONE);

            joinButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onJoinButtonClick(position);
                    }
                }
            });

            cancelButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCancelButtonClick(position);
                    }
                }
            });

            infoButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onInfoButtonClick(position);
                    }
                }
            });

        }
    }
}
