package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.databinding.ActivitySuccesfullBinding;

/**
 * SuccesfullActivity es la actividad principal que administra la navegación entre diferentes fragmentos
 * dentro de la aplicación. Utiliza View Binding para inflar el layout y manejar el BottomNavigationView
 * para la navegación entre fragmentos.
 */
public class SuccesfullActivity extends AppCompatActivity {

    // Declaración del binding para la actividad
    private ActivitySuccesfullBinding binding;

    /**
     * Método llamado cuando la actividad es creada. Inicializa las vistas y configura los listeners.
     *
     * @param savedInstanceState Estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflar el layout utilizando View Binding
        binding = ActivitySuccesfullBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Reemplazar el fragmento inicial con UserProfileFragment
        replaceFragment(new UserProfileFragment());

        // Establecer el fondo del BottomNavigationView como transparente
        binding.bottomNavigationView.setBackground(null);

        // Configurar el listener para los elementos del BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            // Reemplazar el fragmento según el elemento seleccionado
            if (item.getItemId() == R.id.userprofile) {
                replaceFragment(new UserProfileFragment());
            } else if (item.getItemId() == R.id.teamprofile) {
                replaceFragment(new TeamProfileFragment());
            } else if (item.getItemId() == R.id.listofmatches){
                replaceFragment(new ListOfMatchesFragment());
            } else if (item.getItemId() == R.id.searchmatches) {
                replaceFragment(new SearchMatchesFragment());
            }
            return true;
        });

        // Configurar el listener para el botón de crear partido
        binding.creatematch.setOnClickListener(v -> {
            replaceFragment(new CreateMatchFragment());
        });
    }

    /**
     * Reemplaza el fragmento actual con uno nuevo.
     *
     * @param fragment El nuevo fragmento que se mostrará.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
