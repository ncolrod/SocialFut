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

public class TeamProfileFragment extends Fragment {

    private SharedViewModel mViewModel;
    private TextView teamNameTextView;
    private TextView teamLocationTextView;
    private TextView teamStadiumTextView;
    private TextView teamCaptainTextView;
    private RecyclerView recyclerViewPlayers;
    private PlayerAdapter playerAdapter;
    private RetrofitRepository retrofitRepository;
    private TextView tvPartidosGanados, tvPartidosPerdidos, tvPartidosEmpatados;
    private String captains;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        new LoadTeamDataTask(mViewModel, retrofitRepository).execute();
        new LoadPlayersTask(mViewModel, retrofitRepository).execute();

        teamNameTextView = view.findViewById(R.id.teamName);
        teamLocationTextView = view.findViewById(R.id.teamLocation);
        teamStadiumTextView = view.findViewById(R.id.teamStadium);
        teamCaptainTextView = view.findViewById(R.id.teamCaptain);
        tvPartidosGanados = view.findViewById(R.id.statsWon);
        tvPartidosPerdidos = view.findViewById(R.id.statsLost);
        tvPartidosEmpatados = view.findViewById(R.id.statsTie);
        recyclerViewPlayers = view.findViewById(R.id.teamPlayersRecyclerView);
        LinearLayout llTeam = view.findViewById(R.id.llteam);

        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        playerAdapter = new PlayerAdapter(new ArrayList<>());
        recyclerViewPlayers.setAdapter(playerAdapter);

        mViewModel.getTeamLiveData().observe(getViewLifecycleOwner(), team -> {
            if (team != null) {
                teamNameTextView.setText(team.getName());
                teamLocationTextView.setText(team.getLocation());
                teamStadiumTextView.setText(team.getStadium());
                tvPartidosGanados.setText("Partidos ganados: "+team.getMatchesWon());
                tvPartidosPerdidos.setText("Partidos perdidos: "+team.getLostMatches());
                tvPartidosEmpatados.setText("Partidos empatados: "+team.getTiedMatches());
                //teamCaptainTextView.setText(team.getCaptain().getFirstname() + " " + team.getCaptain().getLastname());
            }
        });

        mViewModel.getPlayersLiveData().observe(getViewLifecycleOwner(), players -> {
            if (players != null) {
                playerAdapter.updatePlayers(players);
            }
        });

        new LoadTeamDataTask(mViewModel, retrofitRepository).execute();
        new LoadPlayersTask(mViewModel, retrofitRepository).execute();
        // Set up click listener to show edit dialog
        llTeam.setOnClickListener(v -> showEditTeamDialog());
    }

    private void showEditTeamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Editar Equipo");

        User currentUser = mViewModel.getUserLiveData().getValue();
        Team currentTeam = mViewModel.getTeamLiveData().getValue(); // Asegúrate de que esto no sea nulo

        if (currentUser != null && currentTeam != null && currentUser.getRole() == Role.ADMIN) {
            builder.setMessage("¿Quieres modificar el equipo?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        // Pasa el equipo actual al fragmento de edición
                        EditTeamProfileFragment editTeamProfileFragment = EditTeamProfileFragment.newInstance(currentTeam);
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, editTeamProfileFragment)
                                .addToBackStack(null)
                                .commit();
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        } else {
            builder.setMessage("No tienes permisos para modificar el equipo.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        }

        builder.show();
    }
}
