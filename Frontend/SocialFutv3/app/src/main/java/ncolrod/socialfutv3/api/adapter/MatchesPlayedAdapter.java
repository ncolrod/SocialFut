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
import ncolrod.socialfutv3.api.fragments.SharedViewModel;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;

/**
 * Adaptador para mostrar la lista de partidos jugados en un RecyclerView.
 */
public class MatchesPlayedAdapter extends RecyclerView.Adapter<MatchesPlayedAdapter.MatchPlayedViewHolder> {

    private Context context;
    private List<Match> matchesPlayedList;
    private SharedViewModel sharedViewModel;
    private Team currentTeam;
    private User currentUser;
    private OnItemClickListener listener;

    /**
     * Interfaz para manejar los eventos de clic en los elementos de la lista.
     */
    public interface OnItemClickListener {
        void onProfileButtonClick(int position);
    }

    public MatchesPlayedAdapter(Context context, List<Match> matchesPlayedList, SharedViewModel sharedViewModel, Team currentTeam, User currentUser) {
        this.context = context;
        this.matchesPlayedList = matchesPlayedList;
        this.sharedViewModel = sharedViewModel;
        this.currentTeam = currentTeam;
        this.currentUser = currentUser;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MatchPlayedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_match_played, parent, false);
        return new MatchPlayedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchPlayedViewHolder holder, int position) {
        Match match = matchesPlayedList.get(position);

        // Configurar los valores de los campos del partido
        String localTeamName = match.getHomeTeam() != null ? match.getHomeTeam().getName() : "Unknown Team";
        String awayTeamName = match.getAwayTeam() != null ? match.getAwayTeam().getName() : "Unknown Team";
        holder.matchTextView.setText(localTeamName + " vs " + awayTeamName);

        currentTeam = sharedViewModel.getTeamLiveData().getValue();
        boolean homeTeam = false;
        boolean awayTeam = false;

        // Determinar si el equipo actual es el equipo local o visitante
        if (currentTeam.getId() == match.getHomeTeam().getId()) {
            homeTeam = true;
        } else if (currentTeam.getId() == match.getAwayTeam().getId()) {
            awayTeam = true;
        }

        // Separar el resultado en dos partes (goles del equipo local y goles del equipo visitante)
        String[] resultParts = match.getResult().split("-");
        int localGoals = Integer.parseInt(resultParts[0]);
        int visitorGoals = Integer.parseInt(resultParts[1]);

        // Mostrar el resultado numérico del partido
        holder.numericResultTextView.setText(String.format("%d - %d", localGoals, visitorGoals));

        // Determinar si el equipo del usuario es local o visitante y mostrar el resultado correspondiente
        if (homeTeam) {
            if (localGoals > visitorGoals) {
                holder.resultTextView.setText("WIN");
            } else if (localGoals < visitorGoals) {
                holder.resultTextView.setText("LOST");
            } else {
                holder.resultTextView.setText("TIED");
            }
        } else if (awayTeam) {
            if (localGoals < visitorGoals) {
                holder.resultTextView.setText("WIN");
            } else if (localGoals > visitorGoals) {
                holder.resultTextView.setText("LOST");
            } else {
                holder.resultTextView.setText("TIED");
            }
        } else {
            holder.resultTextView.setText("N/A");
        }

        // Configurar los valores de los campos de la fecha, ubicación y resultado
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

        if (match.getResult() != null) {
            holder.numericResultTextView.setText(match.getResult());
        } else {
            holder.numericResultTextView.setText("Unknown Result");
        }

        holder.profileButton.setVisibility(View.VISIBLE);

        holder.profileButton.setOnClickListener(v -> {
            if (listener != null) listener.onProfileButtonClick(position);
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

    @Override
    public int getItemCount() {
        return matchesPlayedList.size();
    }

    /**
     * ViewHolder para los elementos de la lista de partidos jugados.
     */
    public static class MatchPlayedViewHolder extends RecyclerView.ViewHolder {
        TextView matchTextView, dateTextView, locationTextView, numericResultTextView, resultTextView;
        Button profileButton;

        public MatchPlayedViewHolder(@NonNull View itemView) {
            super(itemView);
            matchTextView = itemView.findViewById(R.id.textViewMatch);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            numericResultTextView = itemView.findViewById(R.id.numericResultTextView);
            resultTextView = itemView.findViewById(R.id.textViewResult);
            profileButton = itemView.findViewById(R.id.profileButton);
        }
    }
}
