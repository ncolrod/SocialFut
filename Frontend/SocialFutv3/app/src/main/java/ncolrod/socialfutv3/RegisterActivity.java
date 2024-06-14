package ncolrod.socialfutv3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import ncolrod.socialfutv3.api.responses.AuthenticationResponse;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.requests.RegisterRequest;
import ncolrod.socialfutv3.api.retrofit.TokenHolder;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Actividad para manejar el registro de nuevos usuarios.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextLastName, editTextEmail, editTextPassword, editTextTelephone, editTextLocation;
    private Button btnLogin;
    private Spinner userPositionSpinner;

    /**
     * Método que se llama cuando se crea la actividad.
     *
     * @param savedInstanceState Si la actividad se vuelve a crear, este Bundle contiene los datos más recientes suministrados en onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Vinculación de los elementos con el layout
        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        editTextTelephone = findViewById(R.id.editTextTelephone);
        editTextLocation = findViewById(R.id.editTextLocation);
        userPositionSpinner = findViewById(R.id.userPositionSpinner);
        btnLogin = findViewById(R.id.btnLogin);

        // Crear y vincular el botón de registro
        Button btnSingUp = findViewById(R.id.buttonSignUpRegister);

        // Listener del botón de registrarse
        btnSingUp.setOnClickListener(v -> {
            if (editTextName.getText() != null && editTextLastName.getText() != null && editTextTelephone != null && editTextLocation != null && editTextEmail.getText() != null && editTextPassword != null) {
                // Recoger los campos de registro
                String userName = editTextName.getText().toString();
                String userLastName = editTextLastName.getText().toString();
                String telephone = editTextTelephone.getText().toString();
                String location = editTextLocation.getText().toString();
                String userEmail = editTextEmail.getText().toString();
                String userPassword = editTextPassword.getText().toString();
                String userPosition = userPositionSpinner.getSelectedItem().toString(); // Obtener la opción seleccionada del Spinner

                // Mediante una tarea asíncrona registrar al usuario controlando errores
                new RegisterUserTask(this, userName, userLastName, telephone, location, userPosition, userEmail, userPassword).execute();
            } else {
                // Mostrar un mensaje de error en caso de que los campos estén vacíos
                Toast.makeText(this, "Error: You must fill out the fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener del botón de login
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

    }

    /**
     * Clase AsyncTask para registrar un nuevo usuario en segundo plano.
     */
    public class RegisterUserTask extends AsyncTask<Void, Void, AuthenticationResponse> {

        private final String userName;
        private final String userLastName;
        private final String telephone;
        private final String location;
        private final String userPosition;
        private final String userEmail;
        private final String userPassword;
        private final Context appContext;

        /**
         * Constructor para inicializar el contexto de la aplicación y los datos del usuario.
         *
         * @param appContext    el contexto de la aplicación.
         * @param userName      el nombre del usuario.
         * @param userLastName  el apellido del usuario.
         * @param telephone     el teléfono del usuario.
         * @param location      la ubicación del usuario.
         * @param userPosition  la posición del usuario.
         * @param userEmail     el correo electrónico del usuario.
         * @param userPassword  la contraseña del usuario.
         */
        public RegisterUserTask(Context appContext, String userName, String userLastName, String telephone, String location, String userPosition, String userEmail, String userPassword) {
            super();
            this.appContext = appContext;
            this.userName = userName;
            this.userLastName = userLastName;
            this.telephone = telephone;
            this.location = location;
            this.userPosition = userPosition;
            this.userEmail = userEmail;
            this.userPassword = userPassword;
        }

        /**
         * Realiza la tarea en segundo plano para registrar al usuario.
         *
         * @param voids sin parámetros.
         * @return un objeto AuthenticationResponse o null en caso de error.
         */
        @Override
        protected AuthenticationResponse doInBackground(Void... voids) {
            // Inicializar el BackendComunication que se usará para registrar y verificar el usuario
            Call<AuthenticationResponse> authenticationCall = BackendComunication.getRetrofitRepository().register(new RegisterRequest(userName, userLastName, telephone, location, userPosition, userEmail, userPassword));
            try {
                // Guardar la respuesta a nuestra petición
                Response<AuthenticationResponse> authenticationResponse = authenticationCall.execute();
                // Control de errores
                if (authenticationResponse.isSuccessful()) {
                    // Recoger la respuesta
                    Log.e("RegisterTask", "Éxito en la ejecución de authenticationResponse, el código de respuesta es: " + authenticationResponse.code());
                    return authenticationResponse.body();
                } else {
                    Log.e("RegisterTask", "Error en la ejecución de authenticationResponse, el código de respuesta es: " + authenticationResponse.code());
                    return null;
                }
            } catch (IOException e) {
                Log.e("RegisterTask", "Error en la ejecución de authenticationCall", e);
                return null;
            }
        }

        /**
         * Se ejecuta después de que la tarea en segundo plano haya terminado.
         *
         * @param authenticationResponse la respuesta de autenticación obtenida.
         */
        @Override
        protected void onPostExecute(AuthenticationResponse authenticationResponse) {
            // Comprobar que la respuesta no sea nula, control de errores
            if (authenticationResponse != null) {
                // Crear un bundle con la información del usuario
                Bundle infoBundle = new Bundle();
                infoBundle.putString("userToken", authenticationResponse.getToken()); // Guardar el token del usuario en el bundle

                TokenHolder.getInstance().setToken(authenticationResponse.getToken());

                // Iniciar la aplicación
                Intent intent = new Intent(getApplicationContext(), TeamLoginActivity.class);
                intent.putExtras(infoBundle);
                startActivity(intent);
            } else {
                // Mostrar un mensaje de error en caso de que las credenciales sean incorrectas
                Toast.makeText(this.appContext, "Error: Incorrect credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
