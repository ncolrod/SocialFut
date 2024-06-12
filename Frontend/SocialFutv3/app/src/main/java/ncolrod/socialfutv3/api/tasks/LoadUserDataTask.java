package ncolrod.socialfutv3.api.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import ncolrod.socialfutv3.api.fragments.SharedViewModel;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import retrofit2.Call;

/**
 * Clase AsyncTask para cargar los datos del usuario en segundo plano y actualizar el SharedViewModel.
 */
public class LoadUserDataTask extends AsyncTask<Void, Void, User> {

    private final SharedViewModel sharedViewModel;
    private final RetrofitRepository retrofitRepository;

    /**
     * Constructor para inicializar el ViewModel compartido y el repositorio de Retrofit.
     *
     * @param sharedViewModel el ViewModel compartido para actualizar los datos.
     * @param retrofitRepository el repositorio de Retrofit para realizar las peticiones.
     */
    public LoadUserDataTask(SharedViewModel sharedViewModel, RetrofitRepository retrofitRepository) {
        this.sharedViewModel = sharedViewModel;
        this.retrofitRepository = retrofitRepository;
    }

    /**
     * Realiza la tarea en segundo plano para cargar los datos del usuario.
     *
     * @param voids sin parámetros.
     * @return un objeto User o null en caso de error.
     */
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

    /**
     * Se ejecuta después de que la tarea en segundo plano haya terminado.
     *
     * @param user el objeto User cargado.
     */
    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if (user != null) {
            sharedViewModel.setUser(user);
            Log.i(":::LoadUserTask:::", "Loaded user " + user.getFirstname() + " " + user.getLastname() +
                    " | " + user.getEmail() + " | " + user.getTelephone() + " | " + user.getLocation() + " | " + user.getPosition());
        } else {
            Log.e(":::LoadUserTask:::", "Failed to load user");
        }
    }
}
