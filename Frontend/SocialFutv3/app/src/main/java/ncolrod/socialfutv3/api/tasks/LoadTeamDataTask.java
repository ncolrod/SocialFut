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

/**
 * Clase AsyncTask para cargar los datos del equipo en segundo plano y actualizar el SharedViewModel.
 */
public class LoadTeamDataTask extends AsyncTask<Void, Void, Team> {

    private final SharedViewModel sharedViewModel;
    private final RetrofitRepository retrofitRepository;

    /**
     * Constructor para inicializar el ViewModel compartido y el repositorio de Retrofit.
     *
     * @param sharedViewModel el ViewModel compartido para actualizar los datos.
     * @param retrofitRepository el repositorio de Retrofit para realizar las peticiones.
     */
    public LoadTeamDataTask(SharedViewModel sharedViewModel, RetrofitRepository retrofitRepository) {
        this.sharedViewModel = sharedViewModel;
        this.retrofitRepository = retrofitRepository;
    }

    /**
     * Realiza la tarea en segundo plano para cargar los datos del equipo.
     *
     * @param voids sin parámetros.
     * @return un objeto Team o null en caso de error.
     */
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

    /**
     * Se ejecuta después de que la tarea en segundo plano haya terminado.
     *
     * @param team el objeto Team cargado.
     */
    @Override
    protected void onPostExecute(Team team) {
        super.onPostExecute(team);
        if (team != null) {
            sharedViewModel.setTeam(team);
            Log.i(":::LoadTeamTask:::", "Loaded team " + team.getName() + " | " + team.getLocation() + " | " + team.getStadium());
        } else {
            Log.e(":::LoadTeamTask:::", "Failed to load team");
        }
    }
}
