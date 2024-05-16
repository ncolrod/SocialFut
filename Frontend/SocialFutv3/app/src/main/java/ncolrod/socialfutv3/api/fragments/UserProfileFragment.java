package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import ncolrod.socialfutv3.api.tasks.LoadTeamDataTask;
import ncolrod.socialfutv3.api.tasks.LoadUserDataTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {

    private SharedViewModel mViewModel;
    private TextView userNameTextView;
    private TextView userLocationTextView;
    private TextView teamNameTextView;
    private TextView userPosition;
    private TextView statsGoalsTextView;
    private TextView statsAssistsTextView;
    private TextView statsMatchesTextView;
    private Spinner userPositionSpinner;
    private RetrofitRepository retrofitRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitRepository = BackendComunication.getRetrofitRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar el modelo de datos compartido
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Inicializar vistas
        userNameTextView = view.findViewById(R.id.userName);
        userLocationTextView = view.findViewById(R.id.userLocation);
        teamNameTextView = view.findViewById(R.id.teamName);
        userPosition = view.findViewById(R.id.userPosition);
        statsGoalsTextView = view.findViewById(R.id.statsGoals);
        statsAssistsTextView = view.findViewById(R.id.statsAssists);
        statsMatchesTextView = view.findViewById(R.id.statsMatches);

        // Observar los cambios en los datos del usuario
        mViewModel.getUserLiveData().observe(getViewLifecycleOwner(), this::updateUserProfile);
        mViewModel.getTeamLiveData().observe(getViewLifecycleOwner(), this::updateUserTeamProfile);
        //mViewModel.getUserStaticsLiveData().observe(getViewLifecycleOwner(), this::updateUserStatistics);

        // Cargar los datos del usuario y del equipo
        new LoadUserDataTask(mViewModel, retrofitRepository).execute();
        //new LoadTeamDataTask(mViewModel, retrofitRepository).execute();

    }

    private void updateUserProfile(User user) {
        if (user != null) {
            userNameTextView.setText(user.getFirstname() + " " + user.getLastname());
            userLocationTextView.setText(user.getLocation());
            userPosition.setText(user.getPosition());
            //statsTextView.setText("Goles: " + user.getGoals() + " | Asistencias: " + user.getAssists() + " | Partidos Jugados: " + user.getMatchesPlayed());
        }
    }

    private void updateUserTeamProfile(Team team) {
        if (team != null) {
            teamNameTextView.setText(team.getName());
        }
    }


}
