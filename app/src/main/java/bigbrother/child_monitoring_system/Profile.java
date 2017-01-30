package bigbrother.child_monitoring_system;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private String uid;
    private DatabaseReference fdbUsers;
    private User currUser;

    private TextView firstName;
    private TextView lastName;
    private TextView email;
    //private TextView password;
    private TextView childFirstName;
    private TextView childLastName;
    private TextView schoolName;

    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile);
        firstName = (TextView) findViewById(R.id.firstNameData);
        lastName = (TextView) findViewById(R.id.lastNameData);
        email = (TextView) findViewById(R.id.emailData);
        //password = (TextView) findViewById(R.id.passwordData);
        childFirstName = (TextView) findViewById(R.id.childFirstNameData);
        childLastName = (TextView) findViewById(R.id.childLastNameData);
        schoolName = (TextView) findViewById(R.id.schoolNameData);

        editButton = (Button) findViewById(R.id.editButton);

        fdbUsers = FirebaseDatabase.getInstance().getReference().child("users");
        uid = getIntent().getStringExtra("uid");


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

        editButton.setOnClickListener(this);
    }




    public void onClick(View v) {
        if (v == editButton && editButton.getText().equals("Edit")) {
            firstName.setFocusableInTouchMode(true);
            lastName.setFocusableInTouchMode(true);
            childFirstName.setFocusableInTouchMode(true);
            childLastName.setFocusableInTouchMode(true);
            schoolName.setFocusableInTouchMode(true);
            editButton.setText("Save");
            //Toast.makeText(Profile.this, "Should be editable", Toast.LENGTH_SHORT).show();
        } else if (v == editButton && editButton.getText().equals("Save")) {
             if (!checkForm()) {
                 return;
             }
            updateFirebase();
            firstName.setFocusable(false);
            lastName.setFocusable(false);
            childFirstName.setFocusable(false);
            childLastName.setFocusable(false);
            schoolName.setFocusable(false);
            editButton.setText("Edit");
            //Toast.makeText(Profile.this, "Should not be editable. Done updating", Toast.LENGTH_SHORT).show();
        } else {

            if (currUser != null && uid != null) {
                fdbUsers.child(uid).child(uid).setValue(currUser);
            }
        }
    }

    private boolean checkForm() {
        boolean good = true;
        if (TextUtils.isEmpty(firstName.getText())) {
            firstName.setError("First Name is required");
            good = false;
        }
        if (TextUtils.isEmpty(lastName.getText())) {
            lastName.setError("Last Name is required");
            good = false;
        }
        if (TextUtils.isEmpty(childFirstName.getText())) {
            childFirstName.setError("Child First Name is required");
            good = false;
        }
        if (TextUtils.isEmpty(childLastName.getText())) {
            childLastName.setError("Child Last Name is required");
            good = false;
        }
        if (TextUtils.isEmpty(schoolName.getText())) {
            schoolName.setError("school name is required");
            good = false;
        }
        if (TextUtils.isEmpty(email.getText())) {
            email.setError( "email is required" );
            good = false;
        }
        return good;
    }

    private void updateFirebase() {
        currUser.setValues(firstName.getText().toString(), lastName.getText().toString(), childFirstName.getText().toString(),
                childLastName.getText().toString(), schoolName.getText().toString(), email.getText().toString());
        fdbUsers.child(uid).setValue(currUser);
    }
}
