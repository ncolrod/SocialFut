package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento para editar el perfil del usuario.
 */
public class EditUserProfileFragment extends Fragment {
    private EditText firstnameEditText, lastnameEditText, locationEditText;
    private Spinner positionSpinner;
    private Button saveButton;
    private SharedViewModel sharedViewModel;
    private RetrofitRepository retrofitRepository;

    /**
     * Crea una nueva instancia del fragmento con los detalles del usuario.
     *
     * @param user El usuario a editar.
     * @return Una nueva instancia de EditUserProfileFragment.
     */
    public static EditUserProfileFragment newInstance(User user) {
        EditUserProfileFragment fragment = new EditUserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
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
        return inflater.inflate(R.layout.fragment_edit_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        firstnameEditText = view.findViewById(R.id.editTextFirstname);
        lastnameEditText = view.findViewById(R.id.editTextLastname);
        locationEditText = view.findViewById(R.id.editTextLocation);
        positionSpinner = view.findViewById(R.id.spinnerPosition);
        saveButton = view.findViewById(R.id.buttonSave);

        // Configurar el adaptador para el Spinner de posiciones
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.positions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(adapter);

        // Cargar los datos del usuario si se proporcionan
        if (getArguments() != null) {
            User user = (User) getArguments().getSerializable("user");
            if (user != null) {
                firstnameEditText.setText(user.getFirstname());
                lastnameEditText.setText(user.getLastname());
                locationEditText.setText(user.getLocation());
                positionSpinner.setSelection(adapter.getPosition(user.getPosition()));
            }
        }

        // Configurar el botón de guardar
        saveButton.setOnClickListener(v -> {
            User user = new User();
            user.setFirstname(firstnameEditText.getText().toString());
            user.setLastname(lastnameEditText.getText().toString());
            user.setLocation(locationEditText.getText().toString());
            user.setPosition(positionSpinner.getSelectedItem().toString());

            updateUserProfile(user);
        });
    }

    /**
     * Actualiza el perfil del usuario.
     *
     * @param updatedUser El usuario actualizado.
     */
    private void updateUserProfile(User updatedUser) {
        Call<User> call = retrofitRepository.updateUserProfile(updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error updating profile.", Toast.LENGTH_SHORT).show();
                    Log.i(":::UpdateUserProfile:::", "Error actualizando perfil: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Error updating profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
