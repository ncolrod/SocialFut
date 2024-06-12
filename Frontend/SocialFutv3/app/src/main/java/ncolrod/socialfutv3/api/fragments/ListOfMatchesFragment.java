package ncolrod.socialfutv3.api.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.adapter.MatchesPlayedAdapter;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import ncolrod.socialfutv3.api.tasks.LoadMatchesPlayedTask;
import ncolrod.socialfutv3.api.tasks.LoadPlayersTask;
import ncolrod.socialfutv3.api.tasks.LoadTeamDataTask;

/**
 * Fragmento que muestra la lista de partidos jugados.
 */
public class ListOfMatchesFragment extends Fragment {

    private RecyclerView matchesPlayedRecyclerView;
    private MatchesPlayedAdapter matchesPlayedAdapter;
    private SharedViewModel sharedViewModel;
    private List<Match> matchesList;
    private Team currentTeam;
    private User currentUser;
    private RetrofitRepository retrofitRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_list_of_matches, container, false);
        matchesPlayedRecyclerView = view.findViewById(R.id.matchesPlayedRecyclerView);
        matchesPlayedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa el ViewModel compartido
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Obtiene una instancia del repositorio de Retrofit
        retrofitRepository = BackendComunication.getRetrofitRepository();

        // Cargar datos del jugador, partidos jugados y equipo
        new LoadPlayersTask(sharedViewModel, retrofitRepository).execute();
        new LoadMatchesPlayedTask(sharedViewModel, retrofitRepository).execute();
        new LoadTeamDataTask(sharedViewModel, retrofitRepository).execute();

        // Observar los partidos jugados y actualizar el adaptador cuando cambie
        sharedViewModel.getMatchesPlayedLiveData().observe(getViewLifecycleOwner(), matches -> {
            if (matches != null) {
                matchesList = matches;
                currentTeam = sharedViewModel.getTeamLiveData().getValue();
                currentUser = sharedViewModel.getUserLiveData().getValue();
                // Configurar el adaptador con la lista de partidos jugados
                matchesPlayedAdapter = new MatchesPlayedAdapter(getContext(), matchesList, sharedViewModel, currentTeam, currentUser);
                matchesPlayedRecyclerView.setAdapter(matchesPlayedAdapter);

                matchesPlayedAdapter.setOnItemClickListener(new MatchesPlayedAdapter.OnItemClickListener() {
                    @Override
                    public void onProfileButtonClick(int position) {
                        showTeamInfoDialog(matchesList.get(position));
                    }
                });
            }
        });

        return view;
    }

    /**
     * Muestra un diálogo con la información del equipo.
     *
     * @param match El partido seleccionado.
     */
    private void showTeamInfoDialog(Match match) {
        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        // Determina si el equipo actual es el equipo local
        boolean isHomeTeam = false;
        if (currentTeam.getId() == match.getHomeTeam().getId()) {
            isHomeTeam = true;
        }

        // Obtiene el equipo contrario
        Team teamToShow = isHomeTeam ? awayTeam : homeTeam;

        // Muestra la información del equipo en un diálogo
        if (teamToShow != null) {
            String teamInfo = teamToShow.getName() + "\n" +
                    "Matches won: " + teamToShow.getMatchesWon() + "\n" +
                    "Lost matches: " + teamToShow.getLostMatches() + "\n" +
                    "Tied matches: " + teamToShow.getTiedMatches();

            new AlertDialog.Builder(getContext())
                    .setTitle("Team Information")
                    .setMessage(teamInfo)
                    .setNeutralButton("Close", null)
                    .show();
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle("Team Information")
                    .setMessage("No information was found on the opposing team.")
                    .setNeutralButton("Close", null)
                    .show();
        }
    }
}
