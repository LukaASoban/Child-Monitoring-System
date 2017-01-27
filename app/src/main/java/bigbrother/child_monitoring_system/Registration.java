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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class Registration extends AppCompatActivity implements View.OnClickListener{


    private Button buttonRegister;
    private Button buttonCancel;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText firstNameET;
    private EditText lastNameET;
    private EditText childFirstNameET;
    private EditText childLastNameET;
    private EditText schoolNameET;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        //System.out.println("firebaseAuth is null: " + firebaseAuth == null);
        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.register);
        buttonCancel = (Button) findViewById(R.id.cancel_registration);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        firstNameET = (EditText) findViewById(R.id.firstName);
        lastNameET = (EditText) findViewById(R.id.lastName);
        childFirstNameET = (EditText) findViewById(R.id.child_first_name);
        childLastNameET = (EditText) findViewById(R.id.child_last_name);
        schoolNameET = (EditText) findViewById(R.id.school_name);
        emailET = (EditText) findViewById(R.id.email);
        passwordET = (EditText) findViewById(R.id.password);
        conf_passwordET  =(EditText) findViewById(R.id.conf_password);

        dRef = FirebaseDatabase.getInstance().getReference();

        buttonRegister.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (buttonRegister == view) {
            registerUser();
        }

        if (view == buttonCancel) {
            //cancelRegistration();
        }

    }

    private void registerUser() {
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        firstName = firstNameET.getText().toString().trim();
        lastName = lastNameET.getText().toString().trim();
        childFirstName = childFirstNameET.getText().toString().trim();
        childLastName = childLastNameET.getText().toString().trim();
        schoolName = schoolNameET.getText().toString().trim();
        String confPass = conf_passwordET.getText().toString().trim();

        //TODO verify registration form info

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Registration.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            //System.out.println("Registered Successfully");
                            addUser(firebaseAuth.getCurrentUser().getUid());
                            final Intent homeScreen = new Intent("bigbrother.child_monitoring_system.HomeScreen");
                            homeScreen.putExtra("uid", firebaseAuth.getCurrentUser().getUid());
                            startActivity(homeScreen);

                        } else {
                            Toast.makeText(Registration.this, "Registered Un-Successfully", Toast.LENGTH_SHORT).show();
                            //System.out.println("Registeration unsuccessful");
                        }

                    }
                });

//        Log.w("TAG","***********************************");
//
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String post = dataSnapshot.getValue(String.class);
//                Log.w("TAG","***********************************" + post);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
//            }
//        };
    }

    private void addUser(String uid) {
        User currentUser = new User();
        currentUser.setEmail(email);
        currentUser.setPassword(password);
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setSchoolName(schoolName);
        currentUser.setChildFirstName(childFirstName);
        currentUser.setChildLastName(childLastName);
        currentUser.setType(UserType.PARENT);
        dRef.child("users").child(uid).setValue(currentUser);
    }

}
