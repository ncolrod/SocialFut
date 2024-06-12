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

/**
 * Task para cargar los partidos en segundo plano y actualizar el SharedViewModel.
 */
public class LoadMatchesDataTask extends AsyncTask<Void, Void, List<Match>> {

    private final SharedViewModel sharedViewModel;
    private final RetrofitRepository retrofitRepository;

    /**
     * Constructor para inicializar el ViewModel compartido y el repositorio de Retrofit.
     *
     * @param sharedViewModel el ViewModel compartido para actualizar los datos.
     * @param retrofitRepository el repositorio de Retrofit para realizar las peticiones.
     */
    public LoadMatchesDataTask(SharedViewModel sharedViewModel, RetrofitRepository retrofitRepository) {
        this.sharedViewModel = sharedViewModel;
        this.retrofitRepository = retrofitRepository;
    }

    /**
     * Realiza la tarea en segundo plano para cargar los partidos.
     *
     * @param voids sin parámetros.
     * @return una lista de partidos o null en caso de error.
     */
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

    /**
     * Se ejecuta después de que la tarea en segundo plano haya terminado.
     *
     * @param matches la lista de partidos cargados.
     */
    @Override
    protected void onPostExecute(List<Match> matches) {
        super.onPostExecute(matches);
        if (matches != null) {
            sharedViewModel.setMatches(matches);
        }
    }
}
