package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.adapter.PlayerStatsAdapter;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.requests.PlayerStatsUpdateRequest;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import ncolrod.socialfutv3.api.tasks.LoadHomeTeamPlayersTask;
import ncolrod.socialfutv3.api.tasks.LoadAwayTeamPlayersTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertPlayerStatsFragment extends Fragment {

    private static final String ARG_MATCH_ID = "matchId";

    private int matchId;
    private SharedViewModel sharedViewModel;
    private RecyclerView homeTeamRecyclerView;
    private RecyclerView awayTeamRecyclerView;
    private PlayerStatsAdapter homeTeamAdapter;
    private PlayerStatsAdapter awayTeamAdapter;
    private Button saveButton;

    public static InsertPlayerStatsFragment newInstance(int matchId) {
        InsertPlayerStatsFragment fragment = new InsertPlayerStatsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_insert_player_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            matchId = getArguments().getInt(ARG_MATCH_ID);
        }

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        homeTeamRecyclerView = view.findViewById(R.id.homeTeamRecyclerView);
        awayTeamRecyclerView = view.findViewById(R.id.awayTeamRecyclerView);
        saveButton = view.findViewById(R.id.saveButton);

        homeTeamRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        awayTeamRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedViewModel.getHomeTeamPlayersLiveData().observe(getViewLifecycleOwner(), homeTeamPlayers -> {
            homeTeamAdapter = new PlayerStatsAdapter(getContext(), homeTeamPlayers);
            homeTeamRecyclerView.setAdapter(homeTeamAdapter);
        });

        sharedViewModel.getAwayTeamPlayersLiveData().observe(getViewLifecycleOwner(), awayTeamPlayers -> {
            awayTeamAdapter = new PlayerStatsAdapter(getContext(), awayTeamPlayers);
            awayTeamRecyclerView.setAdapter(awayTeamAdapter);
        });

        loadHomeTeamPlayers();
        loadAwayTeamPlayers();

        saveButton.setOnClickListener(v -> saveStats());
    }

    private void loadHomeTeamPlayers() {
        new LoadHomeTeamPlayersTask(getContext(), sharedViewModel.getHomeTeamPlayers()).execute(matchId);
    }

    private void loadAwayTeamPlayers() {
        new LoadAwayTeamPlayersTask(getContext(), sharedViewModel.getAwayTeamPlayers()).execute(matchId);
    }

    private void saveStats() {
        List<User> homeTeamPlayers = homeTeamAdapter.getPlayers();
        List<User> awayTeamPlayers = awayTeamAdapter.getPlayers();
        List<PlayerStatsUpdateRequest> playerStats = new ArrayList<>();

        for (User player : homeTeamPlayers) {
            PlayerStatsUpdateRequest stats = new PlayerStatsUpdateRequest();
            stats.setPlayerId(player.getId());
            stats.setGoals(player.getGoals());
            stats.setAssists(player.getAssists());
            stats.setParticipated(player.getMatchesPlayed());
            playerStats.add(stats);
        }

        for (User player : awayTeamPlayers) {
            PlayerStatsUpdateRequest stats = new PlayerStatsUpdateRequest();
            stats.setPlayerId(player.getId());
            stats.setGoals(player.getGoals());
            stats.setAssists(player.getAssists());
            stats.setParticipated(player.getMatchesPlayed());
            playerStats.add(stats);
        }

        RetrofitRepository service = BackendComunication.getRetrofitRepository();
        service.updatePlayerStats(playerStats).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Stats saved successfully", Toast.LENGTH_SHORT).show();
                    updateUserLiveData(homeTeamPlayers, awayTeamPlayers);
                    navigateToUserProfile();

                } else {
                    Toast.makeText(getContext(), "Failed to save stats", Toast.LENGTH_SHORT).show();
                    Log.i(":::UpdatePlayerStats:::", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserLiveData(List<User> homeTeamPlayers, List<User> awayTeamPlayers) {
        User currentUser = sharedViewModel.getUserLiveData().getValue();
        if (currentUser != null) {
            for (User player : homeTeamPlayers) {
                if (player.getId() == currentUser.getId()) {
                    currentUser.setGoals(player.getGoals());
                    currentUser.setAssists(player.getAssists());
                    currentUser.setMatchesPlayed(player.getMatchesPlayed());
                    break;
                }
            }
            for (User player : awayTeamPlayers) {
                if (player.getId() == currentUser.getId()) {
                    currentUser.setGoals(player.getGoals());
                    currentUser.setAssists(player.getAssists());
                    currentUser.setMatchesPlayed(player.getMatchesPlayed());
                    break;
                }
            }
            sharedViewModel.setUser(currentUser);
        }
    }

    private void navigateToUserProfile() {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, userProfileFragment)
                .addToBackStack(null)
                .commit();
    }
}
