package ncolrod.socialfutv3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import ncolrod.socialfutv3.api.fragments.SuccesfullActivity;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.requests.TeamJoinRequest;
import ncolrod.socialfutv3.api.responses.TeamJoinResponse;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Actividad para manejar la unión de un usuario a un equipo.
 */
public class TeamLoginActivity extends AppCompatActivity {

    private EditText editTextSafeCode;
    private Button btnJoin, btnCreateTeam;

    /**
     * Método que se llama cuando se crea la actividad.
     *
     * @param savedInstanceState Si la actividad se vuelve a crear, este Bundle contiene los datos más recientes suministrados en onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_login);

        // Vinculamos los elementos del layout
        editTextSafeCode = findViewById(R.id.editTextSafeCode);
        btnJoin = findViewById(R.id.buttonJoin);
        btnCreateTeam = findViewById(R.id.buttonCreateTeam);

        // Configuramos los listeners de los botones
        btnJoin.setOnClickListener(v -> {
            if (editTextSafeCode.getText() != null) {
                // Recogemos credenciales para unirse al equipo
                String safeCode = editTextSafeCode.getText().toString();

                // Comprobamos con una tarea asincrona si el equipo y sus respectivas credenciales son correctas
                new VerifyTeamTask(safeCode, this).execute();
            } else {
                // Mostramos un mensaje de error en caso de que los campos esten vacios
                Toast.makeText(this, "Error: Debes de rellenar los campos", Toast.LENGTH_SHORT).show();
            }
        });

        btnCreateTeam.setOnClickListener(v -> {
            Intent intent = new Intent(TeamLoginActivity.this, TeamRegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Clase AsyncTask para verificar la unión a un equipo en segundo plano.
     */
    public class VerifyTeamTask extends AsyncTask<Void, Void, TeamJoinResponse> {
        private final String safeCode;
        private final Context appContext;

        /**
         * Constructor para inicializar el contexto de la aplicación y el código seguro.
         *
         * @param safeCode  el código seguro del equipo.
         * @param appContext el contexto de la aplicación.
         */
        public VerifyTeamTask(String safeCode, Context appContext) {
            this.safeCode = safeCode;
            this.appContext = appContext;
        }

        /**
         * Realiza la tarea en segundo plano para verificar la unión al equipo.
         *
         * @param voids sin parámetros.
         * @return un objeto TeamJoinResponse o null en caso de error.
         */
        @Override
        protected TeamJoinResponse doInBackground(Void... voids) {
            // Inicializar el BackendComunication que se usará para verificar el usuario
            Call<TeamJoinResponse> teamJoinResponseCall = BackendComunication.getRetrofitRepository().join(new TeamJoinRequest(safeCode));
            try {
                // Guardar la respuesta a nuestra petición
                Response<TeamJoinResponse> teamJoinResponse = teamJoinResponseCall.execute();
                // Control de errores
                if (teamJoinResponse.isSuccessful()) {
                    // Recoger la respuesta
                    Log.e("TeamJoinTask", "Éxito en la ejecución de teamJoinResponseCall, el código de respuesta es: " + teamJoinResponse.code());
                    return teamJoinResponse.body();
                } else {
                    Log.e("TeamJoinTask", "Error en la ejecución de teamJoinResponseCall, el código de respuesta es: " + teamJoinResponse.code());
                    return null;
                }
            } catch (IOException e) {
                Log.e("TeamJoinTask", "Error en la ejecución de teamJoinResponseCall", e);
                return null;
            }
        }

        /**
         * Se ejecuta después de que la tarea en segundo plano haya terminado.
         *
         * @param teamJoinResponse la respuesta de unión al equipo obtenida.
         */
        @Override
        protected void onPostExecute(TeamJoinResponse teamJoinResponse) {
            // Comprobamos la respuesta no sea nula, control de errores
            if (teamJoinResponse != null) {
                // Creamos un bundle para ver si es correcto
                Bundle infoBundle = new Bundle();
                infoBundle.putBoolean("team", teamJoinResponse.isSuccessful());

                // Iniciamos la app
                Intent intent = new Intent(getApplicationContext(), SuccesfullActivity.class);
                intent.putExtras(infoBundle);
                startActivity(intent);

            } else {
                // Mostrar un mensaje de error en caso de que las credenciales sean incorrectas
                Toast.makeText(this.appContext, "Error: Equipo no encontrado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
