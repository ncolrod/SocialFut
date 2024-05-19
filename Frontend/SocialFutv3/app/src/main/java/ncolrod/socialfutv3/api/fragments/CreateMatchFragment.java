package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Timestamp;
import java.util.Calendar;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.requests.CreateMatchRequest;
import ncolrod.socialfutv3.api.responses.CreateMatchResponse;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMatchFragment extends Fragment {

    private EditText locationEditText;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText priceEditText;
    private Button createMatchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_match, container, false);

        locationEditText = view.findViewById(R.id.locationEditText);
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        priceEditText = view.findViewById(R.id.priceEditText);
        createMatchButton = view.findViewById(R.id.createMatchButton);

        createMatchButton.setOnClickListener(v -> createMatch());

        return view;
    }

    private void createMatch() {
        String location = locationEditText.getText().toString();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        double price;

        try {
            price = Double.parseDouble(priceEditText.getText().toString());
        } catch (NumberFormatException e) {
            price = 0.0; // Default to 0 if parsing fails
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

        Team homeTeam = getUserTeam();
        User creatorUser = getCreatorUser();

        CreateMatchRequest createMatchRequest = new CreateMatchRequest(homeTeam, location, creatorUser, timestamp, price);
        Call<CreateMatchResponse> call = BackendComunication.getRetrofitRepository().createMatch(createMatchRequest);

        call.enqueue(new Callback<CreateMatchResponse>() {
            @Override
            public void onResponse(Call<CreateMatchResponse> call, Response<CreateMatchResponse> response) {
                if (response.isSuccessful()) {
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

    private Team getUserTeam() {
        return new Team(); // Example placeholder
    }

    private User getCreatorUser() {
        return new User(); // Example placeholder
    }
}
