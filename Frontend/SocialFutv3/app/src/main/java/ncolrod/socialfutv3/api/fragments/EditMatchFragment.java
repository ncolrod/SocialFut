package ncolrod.socialfutv3.api.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.requests.CreateMatchRequest;
import ncolrod.socialfutv3.api.responses.GenericResponse;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Fragmento para editar los detalles de un partido.
 */
public class EditMatchFragment extends Fragment {

    private TextView homeTeamTextView;
    private EditText dateEditText, timeEditText, priceEditText, locationEditText;
    private Button saveButton;
    private SharedViewModel sharedViewModel;
    private RetrofitRepository retrofitRepository;
    private Match match;

    /**
     * Crea una nueva instancia del fragmento con los detalles del partido.
     *
     * @param match El partido a editar.
     * @return Una nueva instancia de EditMatchFragment.
     */
    public static EditMatchFragment newInstance(Match match) {
        EditMatchFragment fragment = new EditMatchFragment();
        Bundle args = new Bundle();
        args.putSerializable("match", match);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            match = (Match) getArguments().getSerializable("match");
        }
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        retrofitRepository = BackendComunication.getRetrofitRepository();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_match, container, false);

        homeTeamTextView = view.findViewById(R.id.textViewHomeTeam);
        dateEditText = view.findViewById(R.id.editTextDate);
        timeEditText = view.findViewById(R.id.editTextTime);
        priceEditText = view.findViewById(R.id.editTextPrice);
        locationEditText = view.findViewById(R.id.editTextLocation);
        saveButton = view.findViewById(R.id.buttonSave);

        if (match != null) {
            homeTeamTextView.setText(match.getHomeTeam() != null ? match.getHomeTeam().getName() : "Unspecified team");
            dateEditText.setText(new SimpleDateFormat("yyyy-MM-dd").format(match.getDate()));
            timeEditText.setText(new SimpleDateFormat("HH:mm").format(match.getDate()));
            priceEditText.setText(String.valueOf(match.getPricePerPerson()));
            locationEditText.setText(match.getLocation());
        }

        saveButton.setOnClickListener(v -> saveMatchDetails());

        return view;
    }

    /**
     * Guarda los detalles del partido editado.
     */
    private void saveMatchDetails() {
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(price) || TextUtils.isEmpty(location)) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String dateTime = date + " " + time;
            Timestamp timestamp = Timestamp.valueOf(dateTime + ":00"); // Formato: "yyyy-MM-dd HH:mm:ss"
            match.setDate(timestamp);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), "Incorrect date/time format", Toast.LENGTH_SHORT).show();
            return;
        }

        match.setPricePerPerson(Double.parseDouble(price));
        match.setLocation(location);

        new UpdateMatchTask(match).execute();
    }

    /**
     * Tarea en segundo plano para actualizar los detalles del partido.
     */
    private class UpdateMatchTask extends AsyncTask<Void, Void, Boolean> {
        private Match match;

        public UpdateMatchTask(Match match) {
            this.match = match;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            CreateMatchRequest request = new CreateMatchRequest();
            request.setDate(match.getDate());
            request.setLocation(match.getLocation());
            request.setPricePerPerson(match.getPricePerPerson());
            request.setHomeTeam(match.getHomeTeam());
            request.setCreatorUser(match.getCreatorUser());

            try {
                Call<GenericResponse> call = retrofitRepository.updateMatch(match.getId(), request);
                Response<GenericResponse> response = call.execute();
                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(getContext(), "Match updated successfully", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Error updating match", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
