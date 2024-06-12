package ncolrod.socialfutv3.api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.models.Role;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;

/**
 * Adaptador para mostrar la lista de partidos en un RecyclerView.
 */
public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {
    private Context context;
    private List<Match> matchesList;
    private Team currentTeam;
    private User currentUser;
    private OnItemClickListener listener;

    /**
     * Interfaz para manejar los eventos de clic en los elementos de la lista.
     */
    public interface OnItemClickListener {
        void onCardClick(int position);
        void onJoinButtonClick(int position);
        void onCancelButtonClick(int position);
        void onInfoButtonClick(int position);
        void onModifyButtonClick(int position);
        void onDeleteButtonClick(int position);
        void onProfileButtonClick(int position);
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

        // Configurar los valores de los campos del partido
        if (match.getHomeTeam() != null) {
            holder.teamNameTextView.setText(match.getHomeTeam().getName());
        } else {
            holder.teamNameTextView.setText("Unknown Home Team");
        }

        if (match.getDate() != null) {
            holder.dateTextView.setText(formatDate(match.getDate().toString()));
        } else {
            holder.dateTextView.setText("Unknown Date");
        }

        if (match.getLocation() != null) {
            holder.locationTextView.setText(match.getLocation());
        } else {
            holder.locationTextView.setText("Unknown Location");
        }

        if (match.getPricePerPerson() != null) {
            holder.pricePerPersonTextView.setText(String.format("Price per person: %s", match.getPricePerPerson()));
        } else {
            holder.pricePerPersonTextView.setText("Unknown Price");
        }

        // Configurar la visibilidad de los botones según el estado del partido y el usuario
        configureButtonsVisibility(holder, match);

        // Configurar los listeners para los botones y la tarjeta del partido
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCardClick(position);
            }
        });
        holder.joinButton.setOnClickListener(v -> {
            if (listener != null) listener.onJoinButtonClick(position);
        });
        holder.cancelButton.setOnClickListener(v -> {
            if (listener != null) listener.onCancelButtonClick(position);
        });
        holder.infoButton.setOnClickListener(v -> {
            if (listener != null) listener.onInfoButtonClick(position);
        });
        holder.modifyButton.setOnClickListener(v -> {
            if (listener != null) listener.onModifyButtonClick(position);
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteButtonClick(position);
        });
        holder.profileButton.setOnClickListener(v -> {
            if (listener != null) listener.onProfileButtonClick(position);  // Nuevo método
        });
    }

    /**
     * Formatea la fecha en un formato legible.
     *
     * @param date la fecha en formato de cadena
     * @return la fecha formateada
     */
    private String formatDate(String date) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        try {
            return targetFormat.format(originalFormat.parse(date));
        } catch (Exception e) {
            return date;
        }
    }

    /**
     * Configura la visibilidad de los botones según el estado del partido y el usuario.
     *
     * @param holder el ViewHolder del partido
     * @param match  el partido
     */
    private void configureButtonsVisibility(MatchViewHolder holder, Match match) {
        boolean isCreator = currentUser.getId() == match.getCreatorUser().getId();
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean hasJoined = match.isCreated();

        holder.joinButton.setVisibility(View.GONE);
        holder.cancelButton.setVisibility(View.GONE);
        holder.modifyButton.setVisibility(View.GONE);
        holder.deleteButton.setVisibility(View.GONE);

        if (isCreator) {
            holder.modifyButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else if (isAdmin) {
            holder.joinButton.setVisibility(View.VISIBLE);
            holder.cancelButton.setVisibility(hasJoined ? View.VISIBLE : View.GONE);
        }

        holder.infoButton.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }

    /**
     * ViewHolder para los elementos de la lista de partidos.
     */
    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView teamNameTextView, dateTextView, locationTextView, pricePerPersonTextView;
        Button joinButton, cancelButton, infoButton, modifyButton, deleteButton, profileButton;

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
            profileButton = itemView.findViewById(R.id.profileButton);  // Nuevo botón
        }
    }
}
