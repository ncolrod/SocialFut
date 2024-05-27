package ncolrod.socialfutv3.api.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ncolrod.socialfutv3.R;
import ncolrod.socialfutv3.databinding.ActivitySuccesfullBinding;


public class SuccesfullActivity extends AppCompatActivity {

    ActivitySuccesfullBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuccesfullBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new UserProfileFragment());

        // Establecer el fondo del BottomNavigationView como transparente
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.userprofile) {
                replaceFragment(new UserProfileFragment());
            } else if (item.getItemId() == R.id.teamprofile) {
                replaceFragment(new TeamProfileFragment());
            } else if (item.getItemId() == R.id.mymatch){
                replaceFragment(new MyMatchFragment());
            } else if (item.getItemId() == R.id.matches) {
                replaceFragment(new MatchesFragment());
            }

            return true;
        });

        binding.creatematch.setOnClickListener(v -> {
            replaceFragment(new CreateMatchFragment());
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void createMatch(){

    }

}