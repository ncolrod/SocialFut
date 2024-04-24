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

import ncolrod.socialfutv3.api.AuthenticationRequest;
import ncolrod.socialfutv3.api.AuthenticationRespose;
import ncolrod.socialfutv3.api.BackendComunication;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //Vinculacion de los elementos con el layout
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        //Creamos y vinculamos los botones de login y sing up
        Button btnLogin = findViewById(R.id.buttonLogin);
        btnRegister = findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(this);

        //Listener del boton de Iniciar Sesion
        btnLogin.setOnClickListener(v -> {
            if (editTextEmail.getText() != null && editTextPassword.getText() != null){
                //Recogemos las credenciales de sus respectivos editText
                String userEmail = editTextEmail.getText().toString();
                String userPassword = editTextPassword.getText().toString();

                //Mediante una tarea asincrona compruebo si el usuario existe y sus credenciales son correctas y cargo la siguiente pantalla
                new VerifyUserTask(this, userEmail, userPassword).execute();
            } else {
                //Mostramos un mensaje de error en caso de que los campos esten vacios
                Toast.makeText(this, "Error: Debes de rellenar los campos", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent =  new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == btnRegister.getId()){
            Intent intent =  new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

    }



    public class VerifyUserTask extends AsyncTask<Void, Void, AuthenticationRespose> {

        private final String userEmail;
        private final String userPassword;
        private final Context appContext;

        public VerifyUserTask(Context appContext, String userEmail, String userPassword) {
            super();
            this.appContext = appContext;
            this.userEmail = userEmail;
            this.userPassword = userPassword;
        }


        @Override
        protected AuthenticationRespose doInBackground(Void... voids) {
            //Inicializar el BackendComunication que se usuara para verificar el usuario
            Call<AuthenticationRespose> authenticationCall = BackendComunication.getRetrofitRepository().login(new AuthenticationRequest(userEmail, userPassword));
            try {
                //Cuardamos la respuesta a nuestra peticion
                Response<AuthenticationRespose> authenticationResponse= authenticationCall.execute();
                //Control de errores
                if (authenticationResponse.isSuccessful()){
                    //Recojo la respuesta
                    Log.e("LoginTask", "Exito en la ejecucion de authenticationResponse el codigo de respuesta es: "+authenticationResponse.code());
                    return authenticationResponse.body();
                } else {
                    Log.e("LoginTask", "Error en la ejecucion de authenticationResponse el codigo de respuesta es: "+authenticationResponse.code());
                    return null;
                }
            } catch (IOException e) {
                Log.e("LoginTask", "Error en la ejecucion de authenticationCall", e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(AuthenticationRespose authenticationRespose){
            //Comprobamos la respuesta no sea nula, control de errores
            if (authenticationRespose != null){
                //Creamos un bundle con la informacion del usuario
                Bundle infoBundle = new Bundle();
                infoBundle.putString("userToken", authenticationRespose.getToken()); //Guardamos el token del user en el bundle

                //Iniciamos la app
                Intent intent =  new Intent(getApplicationContext(), SuccesfullActivity.class);
                intent.putExtras(infoBundle);
                startActivity(intent);

            }else{
                // Mostrar un mensaje de error en caso de que las credenciales sean incorrectas
                Toast.makeText(this.appContext, "Error: Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }
}