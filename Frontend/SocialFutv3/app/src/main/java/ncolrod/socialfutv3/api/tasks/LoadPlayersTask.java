package ncolrod.socialfutv3.api.tasks;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

import ncolrod.socialfutv3.api.fragments.SharedViewModel;
import ncolrod.socialfutv3.api.models.Team;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import retrofit2.Call;

public class LoadPlayersTask extends AsyncTask<Void, Void, List<User>> {
    private RetrofitRepository retrofitRepository;
    private final SharedViewModel sharedViewModel;

    public LoadPlayersTask(SharedViewModel sharedViewModel, RetrofitRepository retrofitRepository) {
        this.sharedViewModel = sharedViewModel;
        this.retrofitRepository = retrofitRepository;
    }

    @Override
    protected List<User> doInBackground(Void... voids) {
        try {
            Log.i(":::LoadPlayersTask:::", "Loading players");
            Call<List<User>> call = BackendComunication.getRetrofitRepository().getPlayers();
            retrofit2.Response<List<User>> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                Log.e(":::LoadPlayersTask:::", "Error loading players: " + response.code());
                return null;
            }
        } catch (IOException e) {
            Log.e(":::LoadPlayersTask:::", "Error loading players", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<User> players) {
        super.onPostExecute(players);
        sharedViewModel.setPlayers(players);
    }
}
