package bigbrother.child_monitoring_system;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via id/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogin;
    private Button buttonRegister;
    private static final String TAG = "EmailPassword";
    private ProgressDialog progressDialog;

    // database variable
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference fdb;

    // current user logging in
    private static String userJSON;

    /**
     * Returns the current user
     * @return curUser
     */
    public static String getUserJSON(){
        return  userJSON;
    }

    /**
     * Sets the current user
     * @param user the user
     */
    public static void setUser(String user){
        userJSON = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        fdb = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        buttonRegister = (Button) findViewById(R.id.registerButton);
        buttonLogin = (Button) findViewById(R.id.loginButton);

        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {
            buttonOnLogin(view);
        } else if (view == buttonRegister) {
            buttonOnRegister(view);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }


    //@Override
    public void onResume() {
        super.onResume();
    }

    public void buttonOnLogin(View v) {
        String email = ((EditText) findViewById(R.id.username)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();

        if (email.equals("")) {
            Toast.makeText(LoginActivity.this, "Enter email.", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.equals("")) {
            Toast.makeText(LoginActivity.this, "Enter password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // connect to database if valid login, then send to home screen
        Log.d(TAG, "signIn:" + email);

        // [START sign_in_with_email]
        progressDialog.setMessage("Logging In...");
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        progressDialog.hide();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Auth Failed",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        final DatabaseReference login = fdb.child("users").child(auth.getCurrentUser().getUid());
                        login.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User loginUser = dataSnapshot.getValue(User.class);
                                if (loginUser.getBanned()) {
                                    Log.w(TAG, "signInWithEmail:Banned User");
                                    Toast.makeText(LoginActivity.this, "Account is Banned. Contact System Admin for more information.",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    loginUser.setToken(FirebaseInstanceId.getInstance().getToken());
                                    login.setValue(loginUser);
                                    Intent homeScreen = new Intent("bigbrother.child_monitoring_system.HomeScreen");
                                    homeScreen.putExtra("uid", auth.getCurrentUser().getUid());
                                    startActivity(homeScreen);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });


                    }
                });
    }

    public void buttonOnRegister(View v) {
        Intent register = new Intent("bigbrother.child_monitoring_system.Registration");
        startActivity(register);
    }
}
