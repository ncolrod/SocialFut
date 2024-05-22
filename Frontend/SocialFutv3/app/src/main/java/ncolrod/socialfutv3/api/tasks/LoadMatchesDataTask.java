package ncolrod.socialfutv3.api.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import ncolrod.socialfutv3.api.fragments.SharedViewModel;
import ncolrod.socialfutv3.api.models.Match;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import retrofit2.Call;

public class LoadMatchesDataTask extends AsyncTask<Void, Void, List<Match>> {

    private final SharedViewModel sharedViewModel;
    private final RetrofitRepository retrofitRepository;

    public LoadMatchesDataTask(SharedViewModel sharedViewModel, RetrofitRepository retrofitRepository) {
        this.sharedViewModel = sharedViewModel;
        this.retrofitRepository = retrofitRepository;
    }

    @Override
    protected List<Match> doInBackground(Void... voids) {
        try {
            Log.i("LoadMatchesTask", "Loading matches");
            Call<List<Match>> call = BackendComunication.getRetrofitRepository().getJoinMatches();
            retrofit2.Response<List<Match>> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                Log.e("LoadMatchesTask", "Error loading matches: " + response.code());
                return null;
            }
        } catch (IOException e) {
            Log.e("LoadMatchesTask", "Error loading matches", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Match> matches) {
        super.onPostExecute(matches);
        if (matches != null) {
            sharedViewModel.setMatches(matches);
        }
    }
}
