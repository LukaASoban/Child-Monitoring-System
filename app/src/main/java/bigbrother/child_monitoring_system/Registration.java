package bigbrother.child_monitoring_system;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity implements View.OnClickListener{


    private Button buttonRegister;
    private Button buttonCancel;
    private EditText firstNameET;
    private EditText lastNameET;
    private EditText childFirstNameET;
    private EditText childLastNameET;
    private Spinner schoolNameSpinner;
    private EditText emailET;
    private EditText passwordET;
    private EditText conf_passwordET;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dRef;


    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String childFirstName;
    private String childLastName;
    private String schoolName;
    private String confPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.register);
        buttonCancel = (Button) findViewById(R.id.cancel_registration);
        firstNameET = (EditText) findViewById(R.id.firstName);
        lastNameET = (EditText) findViewById(R.id.lastName);
        childFirstNameET = (EditText) findViewById(R.id.child_first_name);
        childLastNameET = (EditText) findViewById(R.id.child_last_name);
        schoolNameSpinner = (Spinner) findViewById(R.id.school_name);
        emailET = (EditText) findViewById(R.id.email);
        passwordET = (EditText) findViewById(R.id.password);
        conf_passwordET  =(EditText) findViewById(R.id.conf_password);

        dRef = FirebaseDatabase.getInstance().getReference();

        populateSchoolSpinner();
        schoolNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                schoolName = parent.getItemAtPosition(pos).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        buttonRegister.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (buttonRegister == view) {
            registerUser();
        }

        if (view == buttonCancel) {
            Intent loginScreen = new Intent(this, LoginActivity.class);
            startActivity(loginScreen);
        }
    }

    private void registerUser() {
        email = emailET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        confPassword = conf_passwordET.getText().toString().trim();
        firstName = firstNameET.getText().toString().trim();
        lastName = lastNameET.getText().toString().trim();
        childFirstName = childFirstNameET.getText().toString().trim();
        childLastName = childLastNameET.getText().toString().trim();

        boolean errors = false;
        if (TextUtils.isEmpty(firstName)) {
            firstNameET.setError("First Name is required");
            errors = true;
        }
        if (TextUtils.isEmpty(lastName)) {
            lastNameET.setError("Last Name is required");
            errors = true;
        }
        if (TextUtils.isEmpty(childFirstName)) {
            childFirstNameET.setError("Child First Name is required");
            errors = true;
        }
        if (TextUtils.isEmpty(childLastName)) {
            childLastNameET.setError("Child Last Name is required");
            errors = true;
        }
        if (TextUtils.isEmpty(email)) {
            emailET.setError( "email is required" );
            errors = true;
        }
        if (TextUtils.isEmpty(password)) {
            passwordET.setError("password is required" );
            errors = true;
        } else if (password.length() < 6) {
            passwordET.setError("password must be at least 6 characters");
            errors = true;
        }
        if (TextUtils.isEmpty(confPassword)) {
            conf_passwordET.setError("confirm password is required" );
            errors = true;
        }
        if (!confPassword.equals(password)) {
            conf_passwordET.setError("Password and Confirm Password must be the same" );
            errors = true;
        }
        if (errors) { //if there were registration mistakes dont send to firebase, wait for the user to fix them
            return;
        }


        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.hide();
                        if (task.isSuccessful()) {
                            Toast.makeText(Registration.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            addUser(firebaseAuth.getCurrentUser().getUid());
                            Intent homeScreen = new Intent("bigbrother.child_monitoring_system.HomeScreen");
                            homeScreen.putExtra("uid", firebaseAuth.getCurrentUser().getUid());
                            startActivity(homeScreen);
                        } else {
                            Toast.makeText(Registration.this, "Registered Un-Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        addUser(firebaseAuth.getCurrentUser().getUid());

    }

    private void addUser(String uid) {
        final User currentUser = new User();
        currentUser.setValues(firstName, lastName, childFirstName, childLastName, schoolName, email);
        currentUser.setPassword(password);
        currentUser.setType(UserType.PARENT);
        dRef.child("users").child(uid).setValue(currentUser);
    }

    private void populateSchoolSpinner() {
        dRef.child("daycare").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue(SchoolName.class).toString());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Registration.this, android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                schoolNameSpinner.setAdapter(dataAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
