package ncolrod.socialfutv3.api.tasks;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

import ncolrod.socialfutv3.api.fragments.SharedViewModel;
import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import retrofit2.Call;

/**
 * Task para cargar la lista de jugadores en segundo plano y actualizar el SharedViewModel.
 */
public class LoadPlayersTask extends AsyncTask<Void, Void, List<User>> {

    private final RetrofitRepository retrofitRepository;
    private final SharedViewModel sharedViewModel;

    /**
     * Constructor para inicializar el ViewModel compartido y el repositorio de Retrofit.
     *
     * @param sharedViewModel el ViewModel compartido para actualizar los datos.
     * @param retrofitRepository el repositorio de Retrofit para realizar las peticiones.
     */
    public LoadPlayersTask(SharedViewModel sharedViewModel, RetrofitRepository retrofitRepository) {
        this.sharedViewModel = sharedViewModel;
        this.retrofitRepository = retrofitRepository;
    }

    /**
     * Realiza la tarea en segundo plano para cargar la lista de jugadores.
     *
     * @param voids sin parámetros.
     * @return una lista de jugadores o null en caso de error.
     */
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

    /**
     * Se ejecuta después de que la tarea en segundo plano haya terminado.
     *
     * @param players la lista de jugadores cargados.
     */
    @Override
    protected void onPostExecute(List<User> players) {
        super.onPostExecute(players);
        if (players != null) {
            sharedViewModel.setPlayers(players);
        }
    }
}
