package ncolrod.socialfutv3;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SuccesfullActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succesfull);

        Bundle extras = getIntent().getExtras();
        String userEmail = extras.getString("userEmail");

        textView = findViewById(R.id.textView);
        textView.setText("Welcome "+userEmail);


    }
}