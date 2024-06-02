package ncolrod.socialfutv3.api.tasks;

import android.os.AsyncTask;
import android.util.Log;


import ncolrod.socialfutv3.api.fragments.SharedViewModel;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import retrofit2.Call;
import retrofit2.Response;

public class UpdateTeamTask extends AsyncTask<Team, Void, Boolean> {
    private static final String TAG = ":::UpdateTeamTask:::";
    private RetrofitRepository retrofitRepository;
    private SharedViewModel sharedViewModel;
    private String errorMessage = "";

    public UpdateTeamTask(SharedViewModel sharedViewModel) {
        this.retrofitRepository = BackendComunication.getRetrofitRepository();
        this.sharedViewModel = sharedViewModel;
    }

    @Override
    protected Boolean doInBackground(Team... teams) {
        Team updatedTeam = teams[0];
        try {
            Call<Team> call = retrofitRepository.updateTeam(updatedTeam);
            Response<Team> response = call.execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Team updated successfully. Response code: " + response.code());
                sharedViewModel.setTeam(updatedTeam); // Update SharedViewModel
                return true;
            } else {
                errorMessage = "Failed to update team. Response code: " + response.code();
                Log.d(TAG, errorMessage);
                return false;
            }
        } catch (Exception e) {
            errorMessage = "Error: " + e.getMessage();
            Log.e(TAG, errorMessage, e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            Log.d(TAG, "Team updated successfully.");
        } else {
            Log.e(TAG, "Failed to update team. " + errorMessage);
        }
    }
}
