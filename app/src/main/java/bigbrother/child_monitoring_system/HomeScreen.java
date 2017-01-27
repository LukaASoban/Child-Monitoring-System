package bigbrother.child_monitoring_system;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private Button buttonProfile;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home_screen);
        uid = getIntent().getStringExtra("uid");

        buttonProfile = (Button) findViewById(R.id.profile);

        buttonProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Intent welcomeScreenIntent = new Intent(this, Profile.class);
        welcomeScreenIntent.putExtra("uid", uid);
        startActivity(welcomeScreenIntent);
    }
}
