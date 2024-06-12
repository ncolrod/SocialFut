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
import ncolrod.socialfutv3.api.requests.TeamRegisterRequest;
import ncolrod.socialfutv3.api.responses.TeamRegisterResponse;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Actividad para manejar el registro de un nuevo equipo.
 */
public class TeamRegisterActivity extends AppCompatActivity {

    private EditText editTextTeamName, editTextTeamLocation, editTextTeamStadium, editTextJoinCode;
    private Button btnCreateTeam, btnJoinToATeam;

    /**
     * Se llama cuando la actividad es creada por primera vez.
     *
     * @param savedInstanceState Si la actividad está siendo re-inicializada después de haber sido previamente cerrada, este Bundle contiene los datos que más recientemente se suministraron en onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_register);

        // Vinculación de los elementos del layout
        editTextTeamName = findViewById(R.id.editTextTeamName);
        editTextTeamLocation = findViewById(R.id.editTextLocation);
        editTextTeamStadium = findViewById(R.id.editTextStadium);
        editTextJoinCode = findViewById(R.id.editTextJoinCode);
        btnCreateTeam = findViewById(R.id.btnCreateTeamRegister);
        btnJoinToATeam = findViewById(R.id.btnJoinToATeam);

        // Configurar el listener para el botón de crear equipo
        btnCreateTeam.setOnClickListener(v -> {
            if (editTextTeamName.getText() != null && editTextTeamLocation.getText() != null && editTextTeamStadium.getText() != null && editTextJoinCode != null) {
                // Recogemos los campos para registrar el equipo
                String teamName = editTextTeamName.getText().toString();
                String teamLocation = editTextTeamLocation.getText().toString();
                String teamStadium = editTextTeamStadium.getText().toString();
                String teamJoinCode = editTextJoinCode.getText().toString();

                // Registrar el equipo de manera asíncrona y manejar errores
                new RegisterTeamTask(this, teamName, teamLocation, teamStadium, teamJoinCode).execute();
            } else {
                // Mostrar un mensaje de error si los campos están vacíos
                Toast.makeText(this, "Error: Debes de rellenar los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * AsyncTask para manejar el proceso de registro de equipo.
     */
    public class RegisterTeamTask extends AsyncTask<Void, Void, TeamRegisterResponse> {

        private final Context appContext;
        private final String teamName;
        private final String teamLocation;
        private final String teamStadium;
        private final String teamJoinCode;

        /**
         * Constructor para inicializar el contexto y la información del equipo.
         *
         * @param appContext   el contexto de la aplicación.
         * @param teamName     el nombre del equipo.
         * @param teamLocation la ubicación del equipo.
         * @param teamStadium  el estadio del equipo.
         * @param teamJoinCode el código de unión del equipo.
         */
        public RegisterTeamTask(Context appContext, String teamName, String teamLocation, String teamStadium, String teamJoinCode) {
            this.appContext = appContext;
            this.teamName = teamName;
            this.teamLocation = teamLocation;
            this.teamStadium = teamStadium;
            this.teamJoinCode = teamJoinCode;
        }

        /**
         * Realiza el registro del equipo en segundo plano.
         *
         * @param voids no hay parámetros.
         * @return un objeto TeamRegisterResponse o null en caso de error.
         */
        @Override
        protected TeamRegisterResponse doInBackground(Void... voids) {
            // Inicializar BackendComunication para registrar y verificar el equipo
            Call<TeamRegisterResponse> registerTeamCall = BackendComunication.getRetrofitRepository().teamRegister(new TeamRegisterRequest(teamName, teamLocation, teamStadium, teamJoinCode));
            try {
                // Guardamos la respuesta a nuestra petición
                Response<TeamRegisterResponse> registerTeamResponse = registerTeamCall.execute();
                // Control de errores
                if (registerTeamResponse.isSuccessful()) {
                    // Recojo la respuesta
                    Log.e("RegisterTeamTask", "Éxito en la ejecución de registerTeamResponse el código de respuesta es: " + registerTeamResponse.code());
                    return registerTeamResponse.body();
                } else {
                    Log.e("RegisterTask", "Error en la ejecución de registerTeamResponse el código de respuesta es: " + registerTeamResponse.code());
                    return null;
                }
            } catch (IOException e) {
                Log.e("RegisterTask", "Error en la ejecución de authenticationCall", e);
                return null;
            }
        }

        /**
         * Se ejecuta después de completar la tarea en segundo plano.
         *
         * @param teamRegisterResponse la respuesta del registro del equipo.
         */
        @Override
        protected void onPostExecute(TeamRegisterResponse teamRegisterResponse) {
            // Comprobamos que la respuesta no sea nula, control de errores
            if (teamRegisterResponse != null) {
                // Creamos un bundle con la información del equipo
                Bundle infoBundle = new Bundle();
                infoBundle.putBoolean("team", teamRegisterResponse.isSuccessful()); // Guardamos el token del equipo en el bundle

                // Iniciamos la app
                Intent intent = new Intent(getApplicationContext(), SuccesfullActivity.class);
                intent.putExtras(infoBundle);
                startActivity(intent);

            } else {
                // Mostrar un mensaje de error en caso de que las credenciales sean incorrectas
                Toast.makeText(this.appContext, "Error: Ya existe el equipo", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
