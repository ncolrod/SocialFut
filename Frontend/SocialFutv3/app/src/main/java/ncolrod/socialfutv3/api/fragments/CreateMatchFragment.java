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
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.requests.CreateMatchRequest;
import ncolrod.socialfutv3.api.responses.CreateMatchResponse;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.tasks.LoadUserDataTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMatchFragment extends Fragment {
    private EditText locationEditText;
    private TimePicker timePicker;
    private EditText priceEditText;
    private Button createMatchButton;
    private Button datePickerButton;
    private SharedViewModel sharedViewModel;
    private Timestamp selectedDate;

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

    private void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Seleccionar Fecha");
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

        if (selectedDate == null) {
            Toast.makeText(getContext(), "Por favor, selecciona una fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDate.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

        Team homeTeam = sharedViewModel.getTeamLiveData().getValue();
        User creatorUser = sharedViewModel.getUserLiveData().getValue();

        if (homeTeam == null || creatorUser == null) {
            Toast.makeText(getContext(), "Error: User or team not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateMatchRequest createMatchRequest = new CreateMatchRequest(homeTeam, location, creatorUser, timestamp, price);
        Call<CreateMatchResponse> call = BackendComunication.getRetrofitRepository().createMatch(createMatchRequest);

        call.enqueue(new Callback<CreateMatchResponse>() {
            @Override
            public void onResponse(Call<CreateMatchResponse> call, Response<CreateMatchResponse> response) {
                if (response.isSuccessful()) {
                    MyMatchFragment myMatchFragment = MyMatchFragment.newInstance(response.body().getMatchId());
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.mymatch, myMatchFragment)
                            .addToBackStack(null)
                            .commit();
                    Log.i("CreateMatchFragment", "Match created successfully: " + response.body().toString());
                } else {
                    Log.e("CreateMatchFragment", "Failed to create match: " + response.code());
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
