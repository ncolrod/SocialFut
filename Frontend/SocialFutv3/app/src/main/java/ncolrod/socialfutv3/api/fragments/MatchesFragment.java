package ncolrod.socialfutv3.api.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;
import java.util.List;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.adapter.MatchesAdapter;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.requests.JoinMatchRequest;
import ncolrod.socialfutv3.api.responses.JoinMatchResponse;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import ncolrod.socialfutv3.api.tasks.LoadMatchesDataTask;
import ncolrod.socialfutv3.api.tasks.LoadTeamDataTask;
import retrofit2.Call;
import retrofit2.Response;

public class MatchesFragment extends Fragment {
    private RecyclerView matchesRecyclerView;
    private MatchesAdapter matchesAdapter;
    private SharedViewModel sharedViewModel;
    private List<Match> matchesList;
    private Team joinTeam;

    private RetrofitRepository retrofitRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        matchesRecyclerView = view.findViewById(R.id.matchesRecyclerView);
        matchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        retrofitRepository = BackendComunication.getRetrofitRepository();

        new LoadMatchesDataTask(sharedViewModel, retrofitRepository).execute();
        new LoadTeamDataTask(sharedViewModel,retrofitRepository).execute();

        sharedViewModel.getMatchesLiveData().observe(getViewLifecycleOwner(), matches -> {
            if (matches != null) {
                matchesList = matches;
                matchesAdapter = new MatchesAdapter(getContext(), matchesList);
                matchesRecyclerView.setAdapter(matchesAdapter);
                joinTeam = sharedViewModel.getTeamLiveData().getValue();

                matchesAdapter.setOnItemClickListener(new MatchesAdapter.OnItemClickListener() {
                    @Override
                    public void onCardClick(int position) {
                    }

                    @Override
                    public void onJoinButtonClick(int position) {
                        joinTeam = sharedViewModel.getTeamLiveData().getValue();
                        if (joinTeam.isAvailable()==true) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Error")
                                    .setMessage("Tu equipo no está disponible para unirse a otros partidos.")
                                    .setPositiveButton("OK", null)
                                    .show();
                            return;
                        }

                        for (int i = 0; i < matchesList.size(); i++) {
                            if (i != position) {
                                matchesList.get(i).setCreated(false);
                            }
                        }

                        new AlertDialog.Builder(getContext())
                                .setTitle("Seguro que quieres unirte?")
                                .setView(new EditText(getContext()))
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        new JoinMatchTask(position, joinTeam).execute();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }

                    @Override
                    public void onCancelButtonClick(int position) {
                        new CancelMatchTask(position, matchesList.get(position).getId()).execute();
                    }

                    @Override
                    public void onInfoButtonClick(int position) {
                        // Navegar al fragment de detalles del partido
                        MatchDetailFragment matchDetailsFragment = MatchDetailFragment.newInstance(matchesList.get(position).getId());
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, matchDetailsFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        });

        return view;
    }

    private class JoinMatchTask extends AsyncTask<Void, Void, Boolean> {
        private int position;
        private Team joinTeam;

        public JoinMatchTask(int position, Team joinTeam) {
            this.position = position;
            this.joinTeam = joinTeam;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Match match = matchesList.get(position);
            JoinMatchRequest request = new JoinMatchRequest(match.getId(), joinTeam, true);

            try {
                Call<JoinMatchResponse> call = retrofitRepository.joinMatch(request);
                Response<JoinMatchResponse> response = call.execute();
                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                new LoadTeamDataTask(sharedViewModel, retrofitRepository).execute();
                matchesList.get(position).setCreated(true);
                matchesAdapter.notifyItemChanged(position);
                if (joinTeam != null) {
                    joinTeam.setAvailable(true);
                    sharedViewModel.getTeamLiveData().setValue(joinTeam);
                    Log.i(":::JoinMatchTask:::", "Equipo se ha unido al partido");
                }
            } else {
                Log.e(":::JoinMatchTask:::", "Error loading join match");
                matchesList.get(position).setCreated(false);
                matchesAdapter.notifyItemChanged(position);
            }
        }
    }

    private class CancelMatchTask extends AsyncTask<Void, Void, Boolean> {
        private int position;
        private int matchId;

        public CancelMatchTask(int position, int matchId) {
            this.position = position;
            this.matchId = matchId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Call<Boolean> call = retrofitRepository.cancelMatch(matchId);
                Response<Boolean> response = call.execute();
                if (response.isSuccessful()) {
                    return true;
                } else {
                    Log.i(":::CancelMatchTask:::", response.code() + " " + response.errorBody().string());
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                joinTeam = sharedViewModel.getTeamLiveData().getValue();
                if (joinTeam != null) {
                    joinTeam.setAvailable(false);
                    sharedViewModel.getTeamLiveData().setValue(joinTeam);
                    matchesList.get(position).setCreated(false);
                    matchesAdapter.notifyItemChanged(position);
                    Log.i(":::CancelMatchTask:::", "Equipo se ha salido del partido y está disponible");
                }
            } else {
                Log.e(":::CancelMatchTask:::", "Error canceling join match");
            }
        }
    }

}

