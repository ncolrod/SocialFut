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

        sharedViewModel.getMatchesLiveData().observe(getViewLifecycleOwner(), matches -> {
            if (matches != null) {
                matchesList = matches;
                matchesAdapter = new MatchesAdapter(getContext(), matchesList);
                matchesRecyclerView.setAdapter(matchesAdapter);
                joinTeam = sharedViewModel.getTeamLiveData().getValue();

                matchesAdapter.setOnItemClickListener(new MatchesAdapter.OnItemClickListener() {
                    @Override
                    public void onCardClick(int position) {
                        // Handle card click
                    }

                    @Override
                    public void onJoinButtonClick(int position) {
                        // Desactivar otros partidos
                        for (int i = 0; i < matchesList.size(); i++) {
                            if (i != position) {
                                matchesAdapter.notifyItemChanged(i);
                            }
                        }

                        new AlertDialog.Builder(getContext())
                                .setTitle("Seguro que quieres unirte?")
                                .setView(new EditText(getContext()))
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Realizar la petición para unirse al partido en una tarea asíncrona
                                        new JoinMatchTask(position, joinTeam).execute();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }

                    @Override
                    public void onFinishButtonClick(int position) {
                        // Handle finish button click
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
            // Realizar la petición para unirse al partido en el hilo de fondo
            Match match = matchesList.get(position);
            boolean isCreated = true;
            JoinMatchRequest request = new JoinMatchRequest(match.getId(), joinTeam, isCreated);

            try {
                Call<JoinMatchResponse> joinMatchResponseCall = retrofitRepository.joinMatch(request);
                Response<JoinMatchResponse> joinResponse = joinMatchResponseCall.execute();
                if (joinResponse.isSuccessful()) {
                    Log.e(":::JoinMatchTask:::", "Successful loading join match");
                    return true;
                } else {
                    Log.e(":::JoinMatchTask:::", "Error loading join match: " + joinResponse.code() + " " + joinResponse.message());
                    return false;
                }
            } catch (IOException e) {
                Log.e(":::JoinMatchTask:::", "Error loading join match: " + e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                // Si la petición fue exitosa, cargar el equipo del usuario
                new LoadTeamDataTask(sharedViewModel, retrofitRepository).execute();
            } else {
                // Manejar el caso de que la petición no sea exitosa
                Log.e(":::JoinMatchTask:::", "Error loading join match");
            }
        }
    }
}
