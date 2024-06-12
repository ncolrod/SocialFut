package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import ncolrod.socialfutv3.api.tasks.LoadMatchesDataTask;
import ncolrod.socialfutv3.api.tasks.LoadMatchesPlayedTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento que muestra los detalles de un partido.
 */
public class MatchDetailFragment extends Fragment {

    private static final String ARG_MATCH_ID = "match_id";
    private int matchId;
    private SharedViewModel sharedViewModel;
    private TextView matchDetailsTextView;
    private TextView countdownStartTextView;
    private TextView countdownEndTextView;
    private EditText homeTeamScoreEditText;
    private EditText awayTeamScoreEditText;
    private Button submitScoreButton;
    private User currentUser;
    private boolean isCreator = false;
    private RetrofitRepository retrofitRepository;

    /**
     * Crea una nueva instancia de MatchDetailFragment con el ID del partido.
     *
     * @param matchId El ID del partido.
     * @return Una nueva instancia de MatchDetailFragment.
     */
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
        countdownStartTextView = view.findViewById(R.id.countdownStartTextView);
        countdownEndTextView = view.findViewById(R.id.countdownEndTextView);
        homeTeamScoreEditText = view.findViewById(R.id.homeTeamScoreEditText);
        awayTeamScoreEditText = view.findViewById(R.id.awayTeamScoreEditText);
        submitScoreButton = view.findViewById(R.id.submitScoreButton);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        currentUser = sharedViewModel.getUserLiveData().getValue();

        if (getArguments() != null) {
            matchId = getArguments().getInt(ARG_MATCH_ID);
            loadMatchDetails(matchId);
        }

        submitScoreButton.setOnClickListener(v -> submitScore());

        return view;
    }

    /**
     * Carga los detalles del partido.
     *
     * @param matchId El ID del partido.
     */
    private void loadMatchDetails(int matchId) {
        sharedViewModel.getMatchesLiveData().observe(getViewLifecycleOwner(), matches -> {
            for (Match match : matches) {
                if (match.getId() == matchId) {
                    matchDetailsTextView.setText(
                            match.getHomeTeam().getName() + " vs " + match.getAwayTeam().getName() + "\n" +
                                    "Location: " + match.getLocation() + "\n" +
                                    "Date: " + match.getDate()
                    );

                    isCreator = match.getCreatorUser().getId() == currentUser.getId();
                    if (isCreator) {
                        enableScoreSubmission();
                    }
                    startCountdown(match);
                    break;
                }
            }
        });
    }

    /**
     * Inicia el contador regresivo para el inicio del partido.
     *
     * @param match El partido.
     */
    private void startCountdown(Match match) {
        long currentTime = System.currentTimeMillis();
        long startTime = match.getDate().getTime();
        long endTime = startTime + TimeUnit.HOURS.toMillis(1);

        if (currentTime < startTime) {
            new CountDownTimer(startTime - currentTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    countdownStartTextView.setText("Time to start: " + formatMillis(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    countdownStartTextView.setText("Match started!");
                    startEndCountdown(endTime - System.currentTimeMillis());
                }
            }.start();
        } else if (currentTime < endTime) {
            countdownStartTextView.setText("Match started!");
            startEndCountdown(endTime - currentTime);
        } else {
            countdownStartTextView.setText("Match finished.");
            countdownEndTextView.setText("");
            if (isCreator) {
                enableScoreSubmission();
            }
        }
    }

    /**
     * Inicia el contador regresivo para el final del partido.
     *
     * @param duration La duración del partido en milisegundos.
     */
    private void startEndCountdown(long duration) {
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownEndTextView.setText("Time to finish: " + formatMillis(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                countdownEndTextView.setText("Match finished.");
                if (isCreator) {
                    enableScoreSubmission();
                }
            }
        }.start();
    }

    /**
     * Habilita la edición y envío del resultado del partido.
     */
    private void enableScoreSubmission() {
        homeTeamScoreEditText.setEnabled(true);
        awayTeamScoreEditText.setEnabled(true);
        submitScoreButton.setEnabled(true);
    }

    /**
     * Formatea un valor en milisegundos a un string en formato HH:mm:ss.
     *
     * @param millis El tiempo en milisegundos.
     * @return El tiempo formateado como string.
     */
    private String formatMillis(long millis) {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    /**
     * Envía el resultado del partido al backend.
     */
    private void submitScore() {
        String homeTeamScore = homeTeamScoreEditText.getText().toString().trim();
        String awayTeamScore = awayTeamScoreEditText.getText().toString().trim();
        String score = homeTeamScore + "-" + awayTeamScore;

        if (homeTeamScore.isEmpty() || awayTeamScore.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a result for both teams", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> call = BackendComunication.getRetrofitRepository().updateMatchResult(matchId, score);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i(":::MatchDetailFragment:::", "Resultado enviado exitosamente");
                    Toast.makeText(getContext(), "Result sent successfully", Toast.LENGTH_SHORT).show();
                    new LoadMatchesDataTask(sharedViewModel, retrofitRepository).execute();
                    new LoadMatchesPlayedTask(sharedViewModel, retrofitRepository).execute();
                    navigateToPlayerStatsFragment();
                } else {
                    Log.e(":::MatchDetailFragment:::", "Error al enviar resultado: " + response.code());
                    Toast.makeText(getContext(), "Error sending result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(":::MatchDetailFragment:::", "Error al enviar resultado", t);
                Toast.makeText(getContext(), "Error sending result", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Navega al fragmento para insertar estadísticas de jugadores.
     */
    private void navigateToPlayerStatsFragment() {
        InsertPlayerStatsFragment fragment = InsertPlayerStatsFragment.newInstance(matchId);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
