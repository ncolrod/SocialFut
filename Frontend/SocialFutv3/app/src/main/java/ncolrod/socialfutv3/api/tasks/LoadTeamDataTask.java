package ncolrod.socialfutv3.api.tasks;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.io.IOException;

import ncolrod.socialfutv3.api.fragments.SharedViewModel;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import retrofit2.Call;

public class LoadTeamDataTask extends AsyncTask<Void, Void, Team> {

    private final SharedViewModel sharedViewModel;
    private final RetrofitRepository retrofitRepository;

    public LoadTeamDataTask(SharedViewModel sharedViewModel, RetrofitRepository retrofitRepository) {
        this.sharedViewModel = sharedViewModel;
        this.retrofitRepository = retrofitRepository;
    }


    @Override
    protected Team doInBackground(Void... voids) {
        try {
            Log.i(":::LoadTeamTask:::", "Loading team");
            Call<Team> call = BackendComunication.getRetrofitRepository().getTeam();
            retrofit2.Response<Team> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                Log.e(":::LoadTeamTask:::", "Error loading team: " + response.code());
                return null;
            }
        } catch (IOException e) {
            Log.e(":::LoadTeamTask:::", "Error loading team", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Team team) {
        super.onPostExecute(team);
        sharedViewModel.setTeam(team);
        //Log.i(":::LoadTeamTask:::", "Loaded team " + team.getName() + " | " + team.getLocation() + " | "+ team.getStadium());
    }
}
