package ncolrod.socialfutv3.api.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.NonNull;
import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Match;
import android.os.CountDownTimer;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {
    private List<Match> matches;
    private OnItemClickListener onItemClickListener;
    private int joinedMatchPosition = -1;

    public interface OnItemClickListener {
        void onCardClick(int position);
        void onJoinButtonClick(int position);
        void onFinishButtonClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setJoinedMatchPosition(int position) {
        this.joinedMatchPosition = position;
        notifyDataSetChanged();
    }

    public MatchesAdapter(Context context, List<Match> matches) {
        this.matches = matches;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matches.get(position);
        long currentTime = System.currentTimeMillis();

        holder.matchTitleTextView.setText(String.format("TEAM: %s", match.getHomeTeam().getName()));
        holder.locationTextView.setText(match.getLocation());
        holder.priceTextView.setText(String.format("Price per player: %s", match.getPricePerPerson()));
        holder.dateTextView.setText(match.getDate().toString());

        if (position == joinedMatchPosition) {
            holder.countdownTextView.setVisibility(View.VISIBLE);
        } else {
            holder.countdownTextView.setVisibility(View.GONE);
            holder.finishButton.setVisibility(View.GONE);
            holder.joinButton.setVisibility(View.VISIBLE);
            holder.joinButton.setEnabled(true);
        }

        holder.joinButton.setOnClickListener(v -> {
            if (onItemClickListener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                onItemClickListener.onJoinButtonClick(holder.getAdapterPosition());
                holder.joinButton.setEnabled(false);  // Desactiva el botón una vez que se ha unido
            }
        });

        holder.finishButton.setOnClickListener(v -> {
            if (onItemClickListener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                onItemClickListener.onFinishButtonClick(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                onItemClickListener.onCardClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }


    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView matchTitleTextView;
        TextView locationTextView;
        TextView priceTextView;
        TextView dateTextView;
        TextView countdownTextView;
        Button joinButton;
        Button finishButton;

        public MatchViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            matchTitleTextView = itemView.findViewById(R.id.teamNameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            priceTextView = itemView.findViewById(R.id.pricePerPersonTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            countdownTextView = itemView.findViewById(R.id.countdownTextView);
            joinButton = itemView.findViewById(R.id.joinButton);
            finishButton = itemView.findViewById(R.id.finishButton);
        }
    }
}
