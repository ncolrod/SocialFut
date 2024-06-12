package ncolrod.socialfutv3.api.fragments;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
import ncolrod.socialfutv3.api.models.Role;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.requests.JoinMatchRequest;
import ncolrod.socialfutv3.api.responses.GenericResponse;
import ncolrod.socialfutv3.api.responses.JoinMatchResponse;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import ncolrod.socialfutv3.api.tasks.LoadMatchesDataTask;
import ncolrod.socialfutv3.api.tasks.LoadPlayersTask;
import ncolrod.socialfutv3.api.tasks.LoadTeamDataTask;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Fragmento para buscar y unirse a partidos.
 */
public class SearchMatchesFragment extends Fragment {
    private RecyclerView matchesRecyclerView;
    private MatchesAdapter matchesAdapter;
    private SharedViewModel sharedViewModel;
    private List<Match> matchesList;
    private Team currentTeam;
    private User currentUser;
    private RetrofitRepository retrofitRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        matchesRecyclerView = view.findViewById(R.id.matchesRecyclerView);
        matchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        retrofitRepository = BackendComunication.getRetrofitRepository();

        // Cargar jugadores, datos de partidos y equipo
        new LoadPlayersTask(sharedViewModel, retrofitRepository).execute();
        new LoadMatchesDataTask(sharedViewModel, retrofitRepository).execute();
        new LoadTeamDataTask(sharedViewModel, retrofitRepository).execute();

        // Observar cambios en la lista de partidos y actualizar el adaptador
        sharedViewModel.getMatchesLiveData().observe(getViewLifecycleOwner(), matches -> {
            if (matches != null) {
                matchesList = matches;
                currentTeam = sharedViewModel.getTeamLiveData().getValue();
                currentUser = sharedViewModel.getUserLiveData().getValue();
                matchesAdapter = new MatchesAdapter(getContext(), matchesList, currentTeam, currentUser);
                matchesRecyclerView.setAdapter(matchesAdapter);

                // Configurar el listener de clics para los elementos del adaptador
                matchesAdapter.setOnItemClickListener(new MatchesAdapter.OnItemClickListener() {
                    @Override
                    public void onCardClick(int position) {
                        showTeamInfoDialog(matchesList.get(position));
                    }

                    @Override
                    public void onJoinButtonClick(int position) {
                        if (currentUser.getRole() != Role.ADMIN) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Error")
                                    .setMessage("Only administrators can join to matches.")
                                    .setPositiveButton("OK", null)
                                    .show();
                        } else if (currentTeam.isAvailable()) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Error")
                                    .setMessage("Your team is already in a match.")
                                    .setPositiveButton("OK", null)
                                    .show();
                        } else {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Are you sure you want to join?")
                                    .setView(new EditText(getContext()))
                                    .setPositiveButton("Si", (dialog, whichButton) -> new JoinMatchTask(position, currentTeam).execute())
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelButtonClick(int position) {
                        if (currentUser.getRole() == Role.ADMIN) {
                            new CancelMatchTask(position, matchesList.get(position).getId()).execute();
                        } else {
                            Toast.makeText(getContext(), "You do not have permission to cancel a match. Only the captains.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onInfoButtonClick(int position) {
                        Match match = matchesList.get(position);
                        Team awayTeam = match.getAwayTeam();
                        if (awayTeam == null) {
                            Toast.makeText(getContext(), "You don't have permissions, join first.", Toast.LENGTH_SHORT).show();
                        } else {
                            MatchDetailFragment matchDetailsFragment = MatchDetailFragment.newInstance(matchesList.get(position).getId());
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.frame_layout, matchDetailsFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }

                    @Override
                    public void onModifyButtonClick(int position) {
                        if (currentUser.getId() == matchesList.get(position).getCreatorUser().getId()) {
                            sharedViewModel.getMatchesLiveData().observe(getViewLifecycleOwner(), matches -> {
                                if (matches != null) {
                                    for (Match match : matches) {
                                        // Verificar que el equipo no sea nulo antes de intentar acceder a él
                                        if (match.getHomeTeam() != null && match.getHomeTeam().getName() != null) {
                                            Log.d(":::MyMatchFragment:::", "Equipo local " + match.getHomeTeam().getName());
                                        } else {
                                            Log.d(":::MyMatchFragment:::", "El equipo local es nulo");
                                        }
                                    }
                                }
                            });

                            Match match = matchesList.get(position);
                            EditMatchFragment editMatchFragment = EditMatchFragment.newInstance(match);
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.frame_layout, editMatchFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("You do not have permissions to edit the match.")
                                    .setNegativeButton("Ok", null)
                                    .show();
                        }
                    }

                    @Override
                    public void onDeleteButtonClick(int position) {
                        if (currentUser.getId() == matchesList.get(position).getCreatorUser().getId()) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Delete Match")
                                    .setMessage("Are you sure you want to delete this match?")
                                    .setPositiveButton("Yes", (dialog, whichButton) -> new DeleteMatchTask(position, matchesList.get(position).getId()).execute())
                                    .setNegativeButton("No", null)
                                    .show();
                        } else {
                            Toast.makeText(getContext(), "You do not have permissions to delete a match. Only the creator can delete it.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onProfileButtonClick(int position) {
                        showTeamInfoDialog(matchesList.get(position));
                    }
                });
            }
        });

        return view;
    }

    private void showTeamInfoDialog(Match match) {
        Team homeTeam = match.getHomeTeam();
        String teamInfo = (homeTeam != null ? homeTeam.getName() : "Unknown") + "\n" +
                "Matches won: " + (homeTeam != null ? homeTeam.getMatchesWon() : "Unknown") + "\n" +
                "Lost matches: " + (homeTeam != null ? homeTeam.getLostMatches() : "Unknown") + "\n" +
                "Tied matches: " + (homeTeam != null ? homeTeam.getTiedMatches() : "Unknown");

        new AlertDialog.Builder(getContext())
                .setTitle("Team Information")
                .setMessage(teamInfo)
                .setPositiveButton("See Players", (dialog, which) -> {
                    PlayersFragment playersFragment = PlayersFragment.newInstance(match.getId());
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, playersFragment)
                            .addToBackStack(null)
                            .commit();
                })
                .setNeutralButton("Close", null)
                .show();
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
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Success")
                        .setMessage("You have successfully joined the match.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("Error")
                        .setMessage("He couldn't join the party. Try again.")
                        .setPositiveButton("OK", null)
                        .show();
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
                Team joinTeam = sharedViewModel.getTeamLiveData().getValue();
                if (joinTeam != null) {
                    joinTeam.setAvailable(false);
                    sharedViewModel.setTeam(joinTeam);
                    matchesList.get(position).setCreated(false);
                    matchesAdapter.notifyItemChanged(position);
                    Log.i(":::CancelMatchTask:::", "Equipo se ha salido del partido y está disponible");
                }
            } else {
                Log.e(":::CancelMatchTask:::", "Error canceling join match");
            }
        }
    }

    private class DeleteMatchTask extends AsyncTask<Void, Void, Boolean> {
        private int position;
        private int matchId;

        public DeleteMatchTask(int position, int matchId) {
            this.position = position;
            this.matchId = matchId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Call<GenericResponse> call = retrofitRepository.deleteMatch(matchId);
                Response<GenericResponse> response = call.execute();
                if (response.isSuccessful()) {
                    Log.i(":::TaskDeleteMatch:::", "Borrado con éxito");
                    return true;
                } else {
                    Log.i(":::TaskDeleteMatch:::", "Error: " + response.code());
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Log.i(":::TaskDeleteMatch:::", "Borrado con éxito");
                matchesList.get(position).getHomeTeam().setAvailable(false);
                if (matchesList.get(position).getAwayTeam() != null) {
                    matchesList.get(position).getAwayTeam().setAvailable(false);
                }
                matchesList.remove(position);
                matchesAdapter.notifyItemRemoved(position);
                new AlertDialog.Builder(getContext())
                        .setTitle("Success")
                        .setMessage("Match successfully eliminated.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                Log.i(":::TaskDeleteMatch:::", "Error al borrar partido: ");
                new AlertDialog.Builder(getContext())
                        .setTitle("Error")
                        .setMessage("The match could not be eliminated. Try again.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }
}
