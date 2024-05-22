package ncolrod.socialfutv3.api.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.adapter.MatchesAdapter;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.tasks.LoadMatchesDataTask;

public class MatchesFragment extends Fragment {
    private RecyclerView matchesRecyclerView;
    private MatchesAdapter matchesAdapter;
    private SharedViewModel sharedViewModel;
    private List<Match> matchesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        matchesRecyclerView = view.findViewById(R.id.matchesRecyclerView);
        matchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        new LoadMatchesDataTask(sharedViewModel, BackendComunication.getRetrofitRepository()).execute();

        sharedViewModel.getMatchesLiveData().observe(getViewLifecycleOwner(), matches -> {
            if (matches != null) {
                matchesList = matches;
                matchesAdapter = new MatchesAdapter(getContext(), matchesList);
                matchesRecyclerView.setAdapter(matchesAdapter);

                matchesAdapter.setOnItemClickListener(new MatchesAdapter.OnItemClickListener() {
                    @Override
                    public void onCardClick(int position) {
                        // Handle card click
                    }

                    @Override
                    public void onJoinButtonClick(int position) {
                        long matchEndTime = calculateMatchEndTime(matchesList.get(position).getDate());
                        matchesAdapter.setMatchEndTime(matchEndTime);
                        matchesAdapter.setJoinedMatchPosition(position);
                        // Desactivar otros partidos
                        for (int i = 0; i < matchesList.size(); i++) {
                            if (i != position) {
                                matchesAdapter.notifyItemChanged(i);
                            }
                        }
                    }

                    @Override
                    public void onFinishButtonClick(int position) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Finalize Match")
                                .setMessage("Please enter the final score")
                                .setView(new EditText(getContext()))
                                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Handle the submission of the score
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }

                });
            }
        });

        return view;
    }

    private long calculateMatchEndTime(Timestamp matchDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(matchDate);
        calendar.add(Calendar.HOUR, 1);
        calendar.add(Calendar.MINUTE, 10);
        return calendar.getTimeInMillis();
    }
}
