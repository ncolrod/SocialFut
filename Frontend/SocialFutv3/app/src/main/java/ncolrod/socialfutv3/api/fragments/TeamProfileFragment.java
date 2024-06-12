package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.adapter.PlayerAdapter;
import ncolrod.socialfutv3.api.models.Role;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import ncolrod.socialfutv3.api.tasks.LoadPlayersTask;
import ncolrod.socialfutv3.api.tasks.LoadTeamDataTask;

/**
 * TeamProfileFragment muestra el perfil del equipo, incluyendo su información y los jugadores del equipo.
 * Permite a los administradores editar la información del equipo.
 */
public class TeamProfileFragment extends Fragment {

    // Declaración de variables miembro
    private SharedViewModel mViewModel;
    private TextView teamNameTextView;
    private TextView teamLocationTextView;
    private TextView teamStadiumTextView;
    private RecyclerView recyclerViewPlayers;
    private PlayerAdapter playerAdapter;
    private RetrofitRepository retrofitRepository;
    private TextView tvPartidosGanados, tvPartidosPerdidos, tvPartidosEmpatados;
    private String captains;

    /**
     * Infla el layout para este fragmento.
     *
     * @param inflater El LayoutInflater.
     * @param container El ViewGroup.
     * @param savedInstanceState El estado guardado.
     * @return La vista inflada.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_profile, container, false);
    }

    /**
     * Configura la vista del fragmento una vez creada.
     *
     * @param view La vista creada.
     * @param savedInstanceState El estado guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicialización del ViewModel y las tareas de carga
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        new LoadTeamDataTask(mViewModel, retrofitRepository).execute();
        new LoadPlayersTask(mViewModel, retrofitRepository).execute();

        // Asignación de vistas
        teamNameTextView = view.findViewById(R.id.teamName);
        teamLocationTextView = view.findViewById(R.id.teamLocation);
        teamStadiumTextView = view.findViewById(R.id.teamStadium);
        tvPartidosGanados = view.findViewById(R.id.statsWon);
        tvPartidosPerdidos = view.findViewById(R.id.statsLost);
        tvPartidosEmpatados = view.findViewById(R.id.statsTie);
        recyclerViewPlayers = view.findViewById(R.id.teamPlayersRecyclerView);
        LinearLayout llTeam = view.findViewById(R.id.llteam);

        // Configuración del RecyclerView
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        playerAdapter = new PlayerAdapter(new ArrayList<>());
        recyclerViewPlayers.setAdapter(playerAdapter);

        // Observación de los datos del equipo
        mViewModel.getTeamLiveData().observe(getViewLifecycleOwner(), team -> {
            if (team != null) {
                teamNameTextView.setText(team.getName());
                teamLocationTextView.setText(team.getLocation());
                teamStadiumTextView.setText(team.getStadium());
                tvPartidosGanados.setText("Matches won: " + team.getMatchesWon());
                tvPartidosPerdidos.setText("Lost matches: " + team.getLostMatches());
                tvPartidosEmpatados.setText("Tied matches: " + team.getTiedMatches());
            }
        });

        // Observación de los datos de los jugadores
        mViewModel.getPlayersLiveData().observe(getViewLifecycleOwner(), players -> {
            if (players != null) {
                playerAdapter.updatePlayers(players);
            }
        });

        // Configuración del listener para editar el equipo
        llTeam.setOnClickListener(v -> showEditTeamDialog());
    }

    /**
     * Muestra un diálogo para editar la información del equipo si el usuario tiene permisos.
     */
    private void showEditTeamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Team");

        User currentUser = mViewModel.getUserLiveData().getValue();
        Team currentTeam = mViewModel.getTeamLiveData().getValue();

        if (currentUser != null && currentTeam != null && currentUser.getRole() == Role.ADMIN) {
            builder.setMessage("Do you want to modify the team?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Pasa el equipo actual al fragmento de edición
                        EditTeamProfileFragment editTeamProfileFragment = EditTeamProfileFragment.newInstance(currentTeam);
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, editTeamProfileFragment)
                                .addToBackStack(null)
                                .commit();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        } else {
            builder.setMessage("You do not have permissions to modify the equipment.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        }

        builder.show();
    }
}
