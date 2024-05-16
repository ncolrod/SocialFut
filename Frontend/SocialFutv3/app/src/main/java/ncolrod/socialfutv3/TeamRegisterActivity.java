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

public class TeamRegisterActivity extends AppCompatActivity {

    private EditText editTextTeamName, editTextTeamLocation, editTextTeamStadium, editTextJoinCode;
    private Button btnCreateTeam, btnJoinToATeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_register);

        //Vinculacion de los elementos con el layout
        editTextTeamName = findViewById(R.id.editTextTeamName);
        editTextTeamLocation = findViewById(R.id.editTextLocation);
        editTextTeamStadium = findViewById(R.id.editTextStadium);
        editTextJoinCode = findViewById(R.id.editTextJoinCode);
        btnCreateTeam = findViewById(R.id.btnCreateTeamRegister);
        btnJoinToATeam = findViewById(R.id.btnJoinToATeam);

        //Listener del boton para crear el equipo
        btnCreateTeam.setOnClickListener(v -> {
            if (editTextTeamName.getText()!=null && editTextTeamLocation.getText()!=null && editTextTeamStadium.getText()!=null && editTextJoinCode!=null){
                //Recogemos los campos para registrar los equipos
                String teamName = editTextTeamName.getText().toString();
                String teamLocation = editTextTeamLocation.getText().toString();
                String teamStadium = editTextTeamStadium.getText().toString();
                String teamJoinCode = editTextJoinCode.getText().toString();

                //Mediante una tarea asincrona registramos el equipo y realizamos el control de errores
                new RegisterTeamTask(this, teamName, teamLocation,teamStadium,teamJoinCode).execute();

            }else{
                //Mostramos un mensaje de error en caso de que los campos esten vacios
                Toast.makeText(this, "Error: Debes de rellenar los campos", Toast.LENGTH_SHORT).show();
            }
        });




    }


    public class RegisterTeamTask extends AsyncTask<Void, Void, TeamRegisterResponse>{

        private final Context appContext;
        private final String teamName;
        private final String teamLocation;
        private final String teamStadium;
        private final String teamJoinCode;


        public RegisterTeamTask(Context appContext, String teamName, String teamLocation, String teamStadium, String teamJoinCode) {
            this.appContext = appContext;
            this.teamName = teamName;
            this.teamLocation = teamLocation;
            this.teamStadium = teamStadium;
            this.teamJoinCode = teamJoinCode;
        }

        @Override
        protected TeamRegisterResponse doInBackground(Void... voids) {
            //Inicializar el BackendComunication que se usuara para registrar y verificar el usuario
            Call<TeamRegisterResponse> registerTeamCall = BackendComunication.getRetrofitRepository().teamRegister(new TeamRegisterRequest(teamName, teamLocation,teamLocation, teamJoinCode));
            try {
                //Cuardamos la respuesta a nuestra peticion
                Response<TeamRegisterResponse> registerTeamResponse= registerTeamCall.execute();
                //Control de errores
                if (registerTeamResponse.isSuccessful()){
                    //Recojo la respuesta
                    Log.e("RegisterTeamTask", "Exito en la ejecucion de registerTeamResponse el codigo de respuesta es: "+registerTeamResponse.code());
                    return registerTeamResponse.body();
                } else {
                    Log.e("RegisterTask", "Error en la ejecucion de registerTeamResponse el codigo de respuesta es: "+registerTeamResponse.code());
                    return null;
                }
            } catch (IOException e) {
                Log.e("RegisterTask", "Error en la ejecucion de authenticationCall", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(TeamRegisterResponse teamRegisterResponse){
            //Comprobamos la respuesta no sea nula, control de errores
            if (teamRegisterResponse != null){
                //Creamos un bundle con la informacion del usuario
                Bundle infoBundle = new Bundle();
                infoBundle.putBoolean("team", teamRegisterResponse.isSuccesfull()); //Guardamos el token del user en el bundle

                //Iniciamos la app
                Intent intent =  new Intent(getApplicationContext(), SuccesfullActivity.class);
                intent.putExtras(infoBundle);
                startActivity(intent);

            }else{
                // Mostrar un mensaje de error en caso de que las credenciales sean incorrectas
                Toast.makeText(this.appContext, "Error: Ya existe el equipo", Toast.LENGTH_SHORT).show();
            }
        }

    }

}