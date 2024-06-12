package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.sql.Timestamp;
import java.util.Calendar;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Role;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.requests.CreateMatchRequest;
import ncolrod.socialfutv3.api.responses.CreateMatchResponse;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import ncolrod.socialfutv3.api.tasks.LoadMatchesDataTask;
import ncolrod.socialfutv3.api.tasks.LoadMatchesPlayedTask;
import ncolrod.socialfutv3.api.tasks.LoadUserDataTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento para crear un nuevo partido.
 */
public class CreateMatchFragment extends Fragment {
    private EditText locationEditText;
    private TimePicker timePicker;
    private EditText priceEditText;
    private Button createMatchButton;
    private Button datePickerButton;
    private SharedViewModel sharedViewModel;
    private Timestamp selectedDate;
    private RetrofitRepository retrofitRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_match, container, false);

        locationEditText = view.findViewById(R.id.locationEditText);
        datePickerButton = view.findViewById(R.id.datePickerButton);
        timePicker = view.findViewById(R.id.timePicker);
        priceEditText = view.findViewById(R.id.priceEditText);
        createMatchButton = view.findViewById(R.id.createMatchButton);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        new LoadUserDataTask(sharedViewModel, BackendComunication.getRetrofitRepository()).execute();

        datePickerButton.setOnClickListener(v -> showDatePicker());
        createMatchButton.setOnClickListener(v -> createMatch());

        return view;
    }

    /**
     * Muestra el selector de fecha.
     */
    private void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date");
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        builder.setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker<Long> datePicker = builder.build();
        datePicker.show(getParentFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);
            selectedDate = new Timestamp(calendar.getTimeInMillis());
            datePickerButton.setText(datePicker.getHeaderText());
        });
    }

    /**
     * Crea un nuevo partido con los datos proporcionados.
     */
    private void createMatch() {
        String location = locationEditText.getText().toString();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        double price;

        try {
            price = Double.parseDouble(priceEditText.getText().toString());
        } catch (NumberFormatException e) {
            price = 0.0;
        }

        Team homeTeam = sharedViewModel.getTeamLiveData().getValue();
        User creatorUser = sharedViewModel.getUserLiveData().getValue();

        // Verificamos los permisos del usuario
        if (creatorUser.getRole() != Role.ADMIN) {
            Toast.makeText(getContext(), "You don´t have permissions to create the match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificamos la disponibilidad del equipo
        if (homeTeam.isAvailable()) {
            Toast.makeText(getContext(), "The team cannot create a match because it has already created one", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificamos que los campos no estén vacíos
        if (selectedDate == null || location.isEmpty()) {
            Toast.makeText(getContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDate.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

        // Verificamos que el equipo y el usuario estén cargados
        if (homeTeam == null || creatorUser == null) {
            Toast.makeText(getContext(), "Error: User or team not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creamos la solicitud de creación de partido
        CreateMatchRequest createMatchRequest = new CreateMatchRequest(homeTeam, location, creatorUser, timestamp, price);
        Call<CreateMatchResponse> call = BackendComunication.getRetrofitRepository().createMatch(createMatchRequest);

        // Enviamos la solicitud al backend
        call.enqueue(new Callback<CreateMatchResponse>() {
            @Override
            public void onResponse(Call<CreateMatchResponse> call, Response<CreateMatchResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Successfully created match", Toast.LENGTH_SHORT).show();
                    Log.i("CreateMatchFragment", "Successfully created match:" + response.body().toString());
                    new LoadMatchesDataTask(sharedViewModel, retrofitRepository).execute();
                    new LoadMatchesPlayedTask(sharedViewModel, retrofitRepository).execute();
                    new LoadMatchesDataTask(sharedViewModel, retrofitRepository).execute();
                } else {
                    Toast.makeText(getContext(), "Could not create match", Toast.LENGTH_SHORT).show();
                    Log.e("CreateMatchFragment", "Could not create match: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CreateMatchResponse> call, Throwable t) {
                Log.e("CreateMatchFragment", "Error creating match", t);
                Toast.makeText(getContext(), "Error creating match", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
