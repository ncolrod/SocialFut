package ncolrod.socialfutv3.api.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ncolrod.socialfutv3.LoginActivity;
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

/**
 * Fragmento para mostrar el perfil del usuario. Incluye información personal,
 * estadísticas y detalles del equipo del usuario. Permite modificar y eliminar
 * el perfil del usuario.
 */
public class UserProfileFragment extends Fragment {

    private SharedViewModel mViewModel;
    private TextView userNameTextView;
    private TextView userLocationTextView;
    private TextView teamNameTextView;
    private TextView userPosition;
    private TextView statsGoalsTextView;
    private TextView statsAssistsTextView;
    private TextView statsMatchesTextView;
    private RetrofitRepository retrofitRepository;
    private Button modifyProfileButton;
    private Button deleteProfileButton;
    private User currentUser;

    /**
     * Se llama cuando se crea el fragmento.
     *
     * @param savedInstanceState Si el fragmento se recrea a partir de un estado guardado previamente,
     *                           este es el estado.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitRepository = BackendComunication.getRetrofitRepository();
    }

    /**
     * Infla el layout para este fragmento.
     *
     * @param inflater El LayoutInflater.
     * @param container El ViewGroup.
     * @param savedInstanceState El estado guardado.
     * @return La vista inflada.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    /**
     * Configura la vista del fragmento una vez creada.
     *
     * @param view La vista creada.
     * @param savedInstanceState El estado guardado.
     */
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
        modifyProfileButton = view.findViewById(R.id.modifyProfileButton);
        deleteProfileButton = view.findViewById(R.id.deleteProfileButton);

        // Observar los cambios en los datos del usuario
        mViewModel.getUserLiveData().observe(getViewLifecycleOwner(), this::updateUserProfile);
        mViewModel.getTeamLiveData().observe(getViewLifecycleOwner(), this::updateUserTeamProfile);

        // Cargar los datos del usuario y del equipo
        new LoadUserDataTask(mViewModel, retrofitRepository).execute();
        new LoadTeamDataTask(mViewModel, retrofitRepository).execute();

        // Configurar el botón de modificar perfil
        modifyProfileButton.setOnClickListener(v -> {
            EditUserProfileFragment editUserProfileFragment = EditUserProfileFragment.newInstance(currentUser);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, editUserProfileFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Configurar el botón de borrar perfil
        deleteProfileButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    /**
     * Actualiza la información del perfil del usuario en la vista.
     *
     * @param user El usuario actual.
     */
    private void updateUserProfile(User user) {
        if (user != null) {
            userNameTextView.setText(user.getFirstname() + " " + user.getLastname());
            userLocationTextView.setText(user.getLocation());
            userPosition.setText(user.getPosition());
            statsAssistsTextView.setText("Assists: " + user.getAssists());
            statsGoalsTextView.setText("Goals: " + user.getGoals());
            statsMatchesTextView.setText("Matches Played: " + user.getMatchesPlayed());
            currentUser = user; // Actualizar currentUser con el usuario actualizado
        }
    }

    /**
     * Actualiza la información del equipo del usuario en la vista.
     *
     * @param team El equipo actual.
     */
    private void updateUserTeamProfile(Team team) {
        if (team != null) {
            teamNameTextView.setText(team.getName());
        }
    }

    /**
     * Muestra un cuadro de diálogo para confirmar la eliminación del perfil del usuario.
     */
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete your profile? This action can not be undone.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Pedir la contraseña del usuario
                    showPasswordDialog();
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Muestra un cuadro de diálogo para confirmar la contraseña del usuario antes de eliminar el perfil.
     */
    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Password");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String password = input.getText().toString();
            deleteUserProfile(password);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Elimina el perfil del usuario usando la contraseña proporcionada.
     *
     * @param password La contraseña del usuario.
     */
    private void deleteUserProfile(String password) {
        retrofitRepository.deleteUserProfile(password).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Profile deleted successfully", Toast.LENGTH_SHORT).show();
                    // Redirigir al usuario a la pantalla de inicio de sesión
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Error deleting profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error deleting profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
