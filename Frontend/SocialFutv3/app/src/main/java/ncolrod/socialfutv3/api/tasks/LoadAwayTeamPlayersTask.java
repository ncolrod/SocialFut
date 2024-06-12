package ncolrod.socialfutv3.api.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

import ncolrod.socialfutv3.api.models.User;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.RetrofitRepository;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Task para cargar los jugadores del equipo visitante en segundo plano.
 */
public class LoadAwayTeamPlayersTask extends AsyncTask<Integer, Void, List<User>> {

    private Context context;
    private MutableLiveData<List<User>> liveData;

    /**
     * Constructor para inicializar el contexto y el LiveData.
     *
     * @param context el contexto de la aplicación.
     * @param liveData el LiveData para actualizar con los jugadores cargados.
     */
    public LoadAwayTeamPlayersTask(Context context, MutableLiveData<List<User>> liveData) {
        this.context = context;
        this.liveData = liveData;
    }

    /**
     * Realiza la tarea en segundo plano para cargar los jugadores del equipo visitante.
     *
     * @param params los parámetros, el primer parámetro debe ser el ID del partido.
     * @return una lista de usuarios que representan a los jugadores del equipo visitante.
     */
    @Override
    protected List<User> doInBackground(Integer... params) {
        int matchId = params[0];
        RetrofitRepository service = BackendComunication.getRetrofitRepository();
        Call<List<User>> call = service.getAwayTeamPlayers(matchId);
        try {
            Response<List<User>> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Se ejecuta después de que la tarea en segundo plano haya terminado.
     *
     * @param users la lista de jugadores del equipo visitante cargados.
     */
    @Override
    protected void onPostExecute(List<User> users) {
        if (users != null) {
            liveData.setValue(users);
        } else {
            Toast.makeText(context, "Failed to load away team players", Toast.LENGTH_SHORT).show();
        }
    }
}
