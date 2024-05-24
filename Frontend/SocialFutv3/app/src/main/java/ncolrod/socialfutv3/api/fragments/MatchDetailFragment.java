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

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Match;


public class MatchDetailFragment extends Fragment {

    private static final String ARG_MATCH_ID = "match_id";
    private int matchId;
    private SharedViewModel sharedViewModel;
    private TextView matchDetailsTextView;

    public static MatchDetailFragment newInstance(int matchId) {
        MatchDetailFragment fragment = new MatchDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_detail, container, false);
        matchDetailsTextView = view.findViewById(R.id.matchDetailsTextView);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        if (getArguments() != null) {
            matchId = getArguments().getInt(ARG_MATCH_ID);
            loadMatchDetails(matchId);
        }

        return view;
    }

    private void loadMatchDetails(int matchId) {
        sharedViewModel.getMatchesLiveData().observe(getViewLifecycleOwner(), matches -> {
            for (Match match : matches) {
                if (match.getId() == matchId) {
                    matchDetailsTextView.setText("Match ID: " + match.getId() +
                            "\nHome Team: " + match.getHomeTeam().getName() +
                            "\nAway Team: " + match.getAwayTeam().getName() +
                            "\nCreated: " + match.isCreated());
                    break;
                }
            }
        });
    }
}
