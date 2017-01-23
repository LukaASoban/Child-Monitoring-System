package bigbrother.child_monitoring_system;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via id/password.
 */
public class LoginActivity extends AppCompatActivity {

    // database variable

    // current user logging in
    private static User curUser;

    /**
     * Returns the current user
     * @return curUser
     */
    public static User getCurUser(){
        return  curUser;
    }

    /**
     * Sets the current user
     * @param user the user
     */
    public static void setUser(User user){
        curUser = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void buttonOnLogin(View v) {
        final String username = ((EditText) findViewById(R.id.username)).getText().toString();
        final String password = ((EditText) findViewById(R.id.password)).getText().toString();
        // connect to database if valid login, then send to home screen
    }

    public void buttonOnRegister(View v) {
        final Intent registerIntent = new Intent(this, /*register screen class*/);
        startActivity(registerIntent);
    }
}
