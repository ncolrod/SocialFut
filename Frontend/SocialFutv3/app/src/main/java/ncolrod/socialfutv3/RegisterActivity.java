package ncolrod.socialfutv3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import ncolrod.socialfutv3.api.AuthenticationRequest;
import ncolrod.socialfutv3.api.AuthenticationRespose;
import ncolrod.socialfutv3.api.BackendComunication;
import ncolrod.socialfutv3.api.RegisterRequest;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextLastName, editTextEmail, editTextPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Vinculacion de los elementos con el layout
        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        btnLogin = findViewById(R.id.btnLogin);

        //Creamos y vinculamos el boton de sing up
        Button btnSingUp = findViewById(R.id.buttonSignUpRegister);

        //Listener del boton de Registrarse, a su vez te loguera en la APP ya en mi API lo he realizado asi
        btnSingUp.setOnClickListener(v -> {
            if (editTextName.getText() != null && editTextLastName.getText() != null && editTextEmail.getText() != null && editTextPassword != null){
                //Recogemos los campos de registro
                String userName = editTextName.getText().toString();
                String userLastName = editTextLastName.getText().toString();
                String userEmail = editTextEmail.getText().toString();
                String userPassword = editTextPassword.getText().toString();

                //Mediante una tarea asincrona registro al susuario siempre y cuando no exista y controlando errores
                new RegisterUserTask(this, userName, userLastName, userEmail, userPassword).execute();

            }else{
                //Mostramos un mensaje de error en caso de que los campos esten vacios
                Toast.makeText(this, "Error: Debes de rellenar los campos", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent =  new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

    }

    public class RegisterUserTask extends AsyncTask<Void, Void, AuthenticationRespose> {

        private final String userName;
        private final String userLastName;
        private final String userEmail;
        private final String userPassword;
        private final Context appContext;

        public RegisterUserTask(Context appContext,String userName, String userLastName, String userEmail, String userPassword) {
            super();
            this.appContext = appContext;
            this.userName = userName;
            this.userLastName = userLastName;
            this.userEmail = userEmail;
            this.userPassword = userPassword;
        }


        @Override
        protected AuthenticationRespose doInBackground(Void... voids) {
            //Inicializar el BackendComunication que se usuara para registrar y verificar el usuario
            Call<AuthenticationRespose> authenticationCall = BackendComunication.getRetrofitRepository().register(new RegisterRequest(userName, userLastName,userEmail, userPassword));
            try {
                //Cuardamos la respuesta a nuestra peticion
                Response<AuthenticationRespose> authenticationResponse= authenticationCall.execute();
                //Control de errores
                if (authenticationResponse.isSuccessful()){
                    //Recojo la respuesta
                    Log.e("RegisterTask", "Exito en la ejecucion de authenticationResponse el codigo de respuesta es: "+authenticationResponse.code());
                    return authenticationResponse.body();
                } else {
                    Log.e("RegisterTask", "Error en la ejecucion de authenticationResponse el codigo de respuesta es: "+authenticationResponse.code());
                    return null;
                }
            } catch (IOException e) {
                Log.e("RegisterTask", "Error en la ejecucion de authenticationCall", e);
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