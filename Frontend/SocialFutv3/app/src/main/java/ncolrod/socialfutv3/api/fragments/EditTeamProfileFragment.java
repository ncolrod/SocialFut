package ncolrod.socialfutv3.api.fragments;

import android.app.AlertDialog;
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

import java.util.List;
import java.util.stream.Collectors;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Role;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import ncolrod.socialfutv3.api.tasks.LoadPlayersTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento para editar el perfil del equipo.
 */
public class EditTeamProfileFragment extends Fragment {

    private EditText nameEditText, locationEditText, stadiumEditText, descriptionEditText, teamColorEditText;
    private Button saveButton, editCaptainsButton;
    private SharedViewModel sharedViewModel;
    private RetrofitRepository retrofitRepository;
    private User selectedCaptain;

    /**
     * Crea una nueva instancia del fragmento con los detalles del equipo.
     *
     * @param team El equipo a editar.
     * @return Una nueva instancia de EditTeamProfileFragment.
     */
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
        editCaptainsButton = view.findViewById(R.id.buttonEditCaptains);

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

        editCaptainsButton.setOnClickListener(v -> showEditCaptainsDialog());
    }

    /**
     * Muestra un cuadro de diálogo para seleccionar el capitán del equipo.
     */
    private void showEditCaptainsDialog() {
        new LoadPlayersTask(sharedViewModel, retrofitRepository).execute();
        sharedViewModel.getPlayersLiveData().observe(getViewLifecycleOwner(), players -> {
            List<String> playerNames = players.stream()
                    .map(player -> player.getFirstname() + " " + player.getLastname() + " (" + player.getRole() + ")")
                    .collect(Collectors.toList());
            CharSequence[] playerNamesArray = playerNames.toArray(new CharSequence[0]);

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Select Captain");
            builder.setSingleChoiceItems(playerNamesArray, -1, (dialogInterface, i) -> {
                // Guarda la selección
                selectedCaptain = players.get(i);
            });
            builder.setPositiveButton("Promote to Captain", (dialog, which) -> {
                if (selectedCaptain != null) {
                    updateRole(selectedCaptain, "ADMIN");
                }
            });
            builder.setNegativeButton("Demote to Player", (dialog, which) -> {
                if (selectedCaptain != null) {
                    updateRole(selectedCaptain, "USER");
                }
            });
            builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    /**
     * Actualiza el rol de un usuario.
     *
     * @param user El usuario a actualizar.
     * @param role El nuevo rol del usuario.
     */
    private void updateRole(User user, String role) {
        Log.i(":::UpdateRole:::", "Función de actualización para ID de usuario:" + user.getId() + " a " + role);
        retrofitRepository.updateRole(user.getId(), role).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Role successfully updated to " + role, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(":::UpdateRole:::", "No se pudo actualizar la función:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(":::UpdateRole:::", "Error de red al actualizar el rol", t);
            }
        });
    }

    /**
     * Actualiza el perfil del equipo.
     *
     * @param updatedTeam El equipo actualizado.
     */
    private void updateTeamProfile(Team updatedTeam) {
        Call<Team> call = retrofitRepository.updateTeam(updatedTeam);
        call.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Team successfully updated", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error updating team: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                Toast.makeText(getContext(), "Error en la red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
