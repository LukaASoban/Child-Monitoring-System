package bigbrother.child_monitoring_system;

import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by kstra on 2/13/2017.
 */

public class UserOptionsScreen extends AppCompatActivity implements View.OnClickListener {

    private String uid;
    private DatabaseReference fdbUsers;
    private FirebaseAuth firebaseAuth;
    private User currUser;

    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView childFirstName;
    private TextView childLastName;
    private TextView schoolName;

    private Button removeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_options);
        firstName = (TextView) findViewById(R.id.firstNameData);
        lastName = (TextView) findViewById(R.id.lastNameData);
        email = (TextView) findViewById(R.id.emailData);
        childFirstName = (TextView) findViewById(R.id.childFirstNameData);
        childLastName = (TextView) findViewById(R.id.childLastNameData);
        schoolName = (TextView) findViewById(R.id.schoolNameData);

        fdbUsers = FirebaseDatabase.getInstance().getReference().child("users");
        uid = getIntent().getStringExtra("uid");
        firebaseAuth = FirebaseAuth.getInstance();

        removeButton = (Button) findViewById(R.id.removeButton);

        fdbUsers.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);
                firstName.setText(currUser.getFirstName());
                firstName.setFocusable(false);
                lastName.setText(currUser.getLastName());
                lastName.setFocusable(false);
                email.setText(currUser.getEmail());
                //password.setText(currUser.getPassword());
                childFirstName.setText(currUser.getChildFirstName());
                childFirstName.setFocusable(false);
                childLastName.setText(currUser.getChildLastName());
                childLastName.setFocusable(false);
                schoolName.setText(currUser.getSchoolName());
                schoolName.setFocusable(false);
            }
            @Override
            public void onCancelled(DatabaseError error) { }
        });
    }

    @Override
    public void onClick(View v) {
    }

    public void removeUser(View v){
        // remove user
        Toast.makeText(UserOptionsScreen.this, uid, Toast.LENGTH_LONG).show();
//        firebaseAuth.getCurrentUser().delete();
//        firebaseAuth.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Log.d(TAG,"OK! Works fine!");
//                    startActivity(new Intent(Main3WelcomeActivity.this, Main3Activity.class));
//                    finish();
//                } else {
//                    Log.w(TAG,"Something is wrong!");
//                }
//            }
//        });
    }
}
