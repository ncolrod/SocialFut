package ncolrod.socialfutv3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import ncolrod.socialfutv3.api.fragments.SuccesfullActivity;
import ncolrod.socialfutv3.api.requests.AuthenticationRequest;
import ncolrod.socialfutv3.api.responses.AuthenticationResponse;
import ncolrod.socialfutv3.api.retrofit.BackendComunication;
import ncolrod.socialfutv3.api.retrofit.TokenHolder;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Actividad para manejar el inicio de sesión del usuario.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword;
    private Button btnRegister;

    /**
     * Método que se llama cuando se crea la actividad.
     *
     * @param savedInstanceState Si la actividad se vuelve a crear, este Bundle contiene los datos más recientes suministrados en onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Vinculación de los elementos con el layout
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        // Crear y vincular los botones de login y registro
        Button btnLogin = findViewById(R.id.buttonLogin);
        btnRegister = findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(this);

        // Listener del botón de Iniciar Sesión
        btnLogin.setOnClickListener(v -> {
            if (editTextEmail.getText() != null && editTextPassword.getText() != null) {
                // Recoger las credenciales de sus respectivos EditText
                String userEmail = editTextEmail.getText().toString();
                String userPassword = editTextPassword.getText().toString();

                // Mediante una tarea asíncrona comprobar si el usuario existe y sus credenciales son correctas y cargar la siguiente pantalla
                new VerifyUserTask(this, userEmail, userPassword).execute();
            } else {
                // Mostrar un mensaje de error en caso de que los campos estén vacíos
                Toast.makeText(this, "Error: Debes rellenar los campos", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnRegister.getId()) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Clase AsyncTask para verificar las credenciales del usuario en segundo plano.
     */
    public class VerifyUserTask extends AsyncTask<Void, Void, AuthenticationResponse> {

        private final String userEmail;
        private final String userPassword;
        private final Context appContext;

        /**
         * Constructor para inicializar el contexto de la aplicación y las credenciales del usuario.
         *
         * @param appContext  el contexto de la aplicación.
         * @param userEmail   el correo electrónico del usuario.
         * @param userPassword la contraseña del usuario.
         */
        public VerifyUserTask(Context appContext, String userEmail, String userPassword) {
            super();
            this.appContext = appContext;
            this.userEmail = userEmail;
            this.userPassword = userPassword;
        }

        /**
         * Realiza la tarea en segundo plano para verificar las credenciales del usuario.
         *
         * @param voids sin parámetros.
         * @return un objeto AuthenticationResponse o null en caso de error.
         */
        @Override
        protected AuthenticationResponse doInBackground(Void... voids) {
            // Inicializar el BackendComunication que se usará para verificar el usuario
            Call<AuthenticationResponse> authenticationCall = BackendComunication.getRetrofitRepository().login(new AuthenticationRequest(userEmail, userPassword));
            try {
                // Guardar la respuesta a nuestra petición
                Response<AuthenticationResponse> authenticationResponse = authenticationCall.execute();
                // Control de errores
                if (authenticationResponse.isSuccessful()) {
                    // Recoger la respuesta
                    Log.e("LoginTask", "Éxito en la ejecución de authenticationResponse, el código de respuesta es: " + authenticationResponse.code());
                    return authenticationResponse.body();
                } else {
                    Log.e("LoginTask", "Error en la ejecución de authenticationResponse, el código de respuesta es: " + authenticationResponse.code());
                    return null;
                }
            } catch (IOException e) {
                Log.e("LoginTask", "Error en la ejecución de authenticationCall", e);
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
                Intent intent = new Intent(getApplicationContext(), SuccesfullActivity.class);
                intent.putExtras(infoBundle);
                startActivity(intent);

            } else {
                // Mostrar un mensaje de error en caso de que las credenciales sean incorrectas
                Toast.makeText(this.appContext, "Error: Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
