package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.adapter.ShowPlayerStatsAdapter;
import ncolrod.socialfutv3.api.tasks.LoadHomeTeamPlayersTask;

/**
 * Fragmento que muestra la lista de jugadores de un equipo en un partido.
 */
public class PlayersFragment extends Fragment {
    private static final String ARG_MATCH_ID = "matchId";
    private int matchId;
    private SharedViewModel sharedViewModel;
    private RecyclerView playersRecyclerView;
    private ShowPlayerStatsAdapter playerAdapter;

    /**
     * Crea una nueva instancia de PlayersFragment con el ID del partido.
     *
     * @param matchId El ID del partido.
     * @return Una nueva instancia de PlayersFragment.
     */
    public static PlayersFragment newInstance(int matchId) {
        PlayersFragment fragment = new PlayersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_players, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            matchId = getArguments().getInt(ARG_MATCH_ID);
        }

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        playersRecyclerView = view.findViewById(R.id.playersRecyclerView);
        playersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Observar los jugadores del equipo local y actualizar el adaptador cuando cambie
        sharedViewModel.getHomeTeamPlayersLiveData().observe(getViewLifecycleOwner(), players -> {
            playerAdapter = new ShowPlayerStatsAdapter(players);
            playersRecyclerView.setAdapter(playerAdapter);
        });

        loadPlayers();
    }

    /**
     * Carga los jugadores del equipo local.
     */
    private void loadPlayers() {
        new LoadHomeTeamPlayersTask(getContext(), sharedViewModel.getHomeTeamPlayers()).execute(matchId);
    }
}
