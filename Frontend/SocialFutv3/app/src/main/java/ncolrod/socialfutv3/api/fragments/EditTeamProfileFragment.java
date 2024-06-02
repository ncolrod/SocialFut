package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import ncolrod.socialfutv3.api.tasks.UpdateTeamTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTeamProfileFragment extends Fragment {
    private EditText nameEditText, locationEditText, stadiumEditText, descriptionEditText, teamColorEditText;
    private Button saveButton;
    private SharedViewModel sharedViewModel;
    private RetrofitRepository retrofitRepository;

    public static EditTeamProfileFragment newInstance(Team team) {
        EditTeamProfileFragment fragment = new EditTeamProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("team", team);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitRepository = BackendComunication.getRetrofitRepository();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_team_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        nameEditText = view.findViewById(R.id.editTextTeamName);
        locationEditText = view.findViewById(R.id.editTextLocation);
        stadiumEditText = view.findViewById(R.id.editTextStadium);
        descriptionEditText = view.findViewById(R.id.editTextDescription);
        teamColorEditText = view.findViewById(R.id.editTextTeamColor);
        saveButton = view.findViewById(R.id.buttonSave);

        if (getArguments() != null) {
            Team team = (Team) getArguments().getSerializable("team");
            if (team != null) {
                nameEditText.setText(team.getName());
                locationEditText.setText(team.getLocation());
                stadiumEditText.setText(team.getStadium());
                descriptionEditText.setText(team.getDescription());
                teamColorEditText.setText(team.getTeam_color());
            }
        }

        saveButton.setOnClickListener(v -> {
            Team team = new Team();
            team.setName(nameEditText.getText().toString());
            team.setLocation(locationEditText.getText().toString());
            team.setStadium(stadiumEditText.getText().toString());
            team.setDescription(descriptionEditText.getText().toString());
            team.setTeam_color(teamColorEditText.getText().toString());

            updateTeamProfile(team);
        });
    }

    private void updateTeamProfile(Team updatedTeam) {
        Call<Team> call = retrofitRepository.updateTeam(updatedTeam);
        call.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Equipo actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error al actualizar equipo: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
