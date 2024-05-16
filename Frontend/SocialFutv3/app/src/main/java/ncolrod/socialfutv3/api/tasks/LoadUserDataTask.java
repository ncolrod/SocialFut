package ncolrod.socialfutv3.api.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import ncolrod.socialfutv3.api.fragments.SharedViewModel;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import retrofit2.Call;

public class LoadUserDataTask extends AsyncTask<Void, Void, User> {

    private final SharedViewModel sharedViewModel;
    private final RetrofitRepository retrofitRepository;

    public LoadUserDataTask(SharedViewModel mViewModel, RetrofitRepository retrofitRepository) {
        this.sharedViewModel = mViewModel;
        this.retrofitRepository = retrofitRepository;
    }

    @Override
    protected User doInBackground(Void... voids) {
        try {
            Log.i(":::LoadUserTask:::", "Loading user");
            Call<User> call = BackendComunication.getRetrofitRepository().getUser();
            retrofit2.Response<User> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                Log.e(":::LoadUserTask:::", "Error loading user: " + response.code());
                return null;
            }
        } catch (IOException e) {
            Log.e(":::LoadUserTask:::", "Error loading user", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        sharedViewModel.setUser(user);
        Log.i(":::LoadUserTask:::", "Loaded user " + user.getFirstname() + " " +user.getLastname() +
                " | " + user.getEmail() + " | "+ user.getTelephone()+ " | "+ user.getLocation()+ " | "+user.getPosition());

    }
}
