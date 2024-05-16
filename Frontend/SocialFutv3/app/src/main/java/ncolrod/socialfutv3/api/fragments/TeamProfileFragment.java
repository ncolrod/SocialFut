package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.adapter.PlayerAdapter;
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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        teamNameTextView = view.findViewById(R.id.teamName);
        teamLocationTextView = view.findViewById(R.id.teamLocation);
        teamStadiumTextView = view.findViewById(R.id.teamStadium);
        teamCaptainTextView = view.findViewById(R.id.teamCaptain);
        recyclerViewPlayers = view.findViewById(R.id.teamPlayersRecyclerView);

        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        playerAdapter = new PlayerAdapter(new ArrayList<>());
        recyclerViewPlayers.setAdapter(playerAdapter);

        mViewModel.getTeamLiveData().observe(getViewLifecycleOwner(), team -> {
            if (team != null) {
                teamNameTextView.setText(team.getName());
                teamLocationTextView.setText(team.getLocation());
                teamStadiumTextView.setText(team.getStadium());
                //teamCaptainTextView.setText(team.getCaptain().getFirstname() + " " + team.getCaptain().getLastname());

                new LoadPlayersTask(mViewModel, retrofitRepository).execute();
            }
        });

        mViewModel.getPlayersLiveData().observe(getViewLifecycleOwner(), players -> {
            if (players != null) {
                playerAdapter.updatePlayers(players);
            }
        });

        new LoadTeamDataTask(mViewModel, retrofitRepository).execute();
    }
}
