package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ncolrod.socialfutv3.api.tasks.LoadMatchesDataTask;
import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.adapter.MatchesAdapter;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.requests.JoinMatchRequest;
import ncolrod.socialfutv3.api.responses.JoinMatchResponse;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchesFragment extends Fragment {

    private RecyclerView matchesRecyclerView;
    private MatchesAdapter matchesAdapter;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        matchesRecyclerView = view.findViewById(R.id.matchesRecyclerView);
        matchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Iniciar la tarea asincrónica para cargar los partidos
        new LoadMatchesDataTask(sharedViewModel, BackendComunication.getRetrofitRepository()).execute();

        // Observa la lista de partidos en el ViewModel y actualiza el adaptador
        sharedViewModel.getMatchesLiveData().observe(getViewLifecycleOwner(), matches -> {
            if (matches != null) {
                // Crear el adaptador una vez que se hayan cargado los datos
                matchesAdapter = new MatchesAdapter(getContext(), matches);
                matchesRecyclerView.setAdapter(matchesAdapter);

                matchesAdapter.setOnItemClickListener(new MatchesAdapter.OnItemClickListener() {
                    @Override
                    public void onCardClick(int position) {
                        // Acción al hacer clic en la tarjeta
                        Toast.makeText(getContext(), "Cargando datos del equipo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onJoinButtonClick(int position) {
                        joinMatch(matches.get(position));
                    }
                });
            }
        });

        return view;
    }

    private void joinMatch(Match match) {
        Team awayTeam = sharedViewModel.getTeamLiveData().getValue();

        if (awayTeam == null) {
            Toast.makeText(getContext(), "Error: Team not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        JoinMatchRequest joinMatchRequest = new JoinMatchRequest(match.getId(), awayTeam);
        Call<JoinMatchResponse> call = BackendComunication.getRetrofitRepository().joinMatch(joinMatchRequest);

        call.enqueue(new Callback<JoinMatchResponse>() {
            @Override
            public void onResponse(Call<JoinMatchResponse> call, Response<JoinMatchResponse> response) {
                if (response.isSuccessful()) {
                    JoinMatchResponse joinMatchResponse = response.body();
                    if (joinMatchResponse.isSuccess()) {
                        Toast.makeText(getContext(), "Successfully joined the match", Toast.LENGTH_SHORT).show();

                        // Desactivar todos los botones de unirse
                        long currentTime = System.currentTimeMillis();
                        long disableUntil = currentTime + 4500000; // 1 hora y 15 minutos en milisegundos
                        matchesAdapter.setDisableUntil(disableUntil);

                        // Configurar el temporizador para reactivar los botones después de una hora y cuarto
                        new Handler().postDelayed(() -> {
                            matchesAdapter.setDisableUntil(0);
                        }, 4500000); // 1 hora y 15 minutos

                    } else {
                        Toast.makeText(getContext(), "Failed to join the match: " + joinMatchResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("MatchesFragment", "Failed to join match: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JoinMatchResponse> call, Throwable t) {
                Log.e("MatchesFragment", "Error joining match", t);
                Toast.makeText(getContext(), "Error joining match", Toast.LENGTH_SHORT).show();
            }
        });
    }
}