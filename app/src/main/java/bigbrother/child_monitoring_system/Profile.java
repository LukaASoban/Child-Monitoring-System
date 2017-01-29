package bigbrother.child_monitoring_system;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private String uid;
    private DatabaseReference fdbUsers;
    private User currUser;

    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView password;
    private TextView childFirstName;
    private TextView childLastName;
    private TextView schoolName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile);
        firstName = (TextView) findViewById(R.id.firstNameData);
        lastName = (TextView) findViewById(R.id.lastNameData);
        email = (TextView) findViewById(R.id.emailData);
        password = (TextView) findViewById(R.id.passwordData);
        childFirstName = (TextView) findViewById(R.id.childFirstNameData);
        childLastName = (TextView) findViewById(R.id.childLastNameData);
        schoolName = (TextView) findViewById(R.id.schoolNameData);

        fdbUsers = FirebaseDatabase.getInstance().getReference().child("users");
        uid = getIntent().getStringExtra("uid");


        fdbUsers.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);
                firstName.setText(currUser.getFirstName());
                lastName.setText(currUser.getLastName());
                email.setText(currUser.getEmail());
                password.setText(currUser.getPassword());
                childFirstName.setText(currUser.getChildFirstName());
                childLastName.setText(currUser.getChildLastName());
                schoolName.setText(currUser.getSchoolName());
            }
            @Override
            public void onCancelled(DatabaseError error) { }
        });

    }



    private void onSaveClick() {
        if (currUser != null && uid != null) {
            fdbUsers.child(uid).child(uid).setValue(currUser);
        }
    }

}
