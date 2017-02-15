package bigbrother.child_monitoring_system;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    private TextView schoolName;

    private Button editButton;

    //menu test//
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBar actionBar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle = "Home";
    ////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
        firstName = (TextView) findViewById(R.id.firstNameData);
        lastName = (TextView) findViewById(R.id.lastNameData);
        email = (TextView) findViewById(R.id.emailData);
        //password = (TextView) findViewById(R.id.passwordData);
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
                schoolName.setText(currUser.getSchoolName());
                schoolName.setFocusable(false);
            }
            @Override
            public void onCancelled(DatabaseError error) { }
        });

        editButton.setOnClickListener(this);

        //menu test//
        mDrawerList = (ListView)findViewById(R.id.navList);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
//        mActivityTitle = getTitle().toString();
        addDrawerItems();
        setupDrawer();
        ////////////
    }




    public void onClick(View v) {
        if (v == editButton && editButton.getText().equals("Edit")) {
            firstName.setFocusableInTouchMode(true);
            lastName.setFocusableInTouchMode(true);
            schoolName.setFocusableInTouchMode(true);
            firstName.setBackground(getResources().getDrawable(R.drawable.textbox));
            lastName.setBackground(getResources().getDrawable(R.drawable.textbox));
            schoolName.setBackground(getResources().getDrawable(R.drawable.textbox));
            editButton.setText("Save");
            //Toast.makeText(Profile.this, "Should be editable", Toast.LENGTH_SHORT).show();
        } else if (v == editButton && editButton.getText().equals("Save")) {
             if (!checkForm()) {
                 return;
             }
            updateFirebase();
            firstName.setFocusable(false);
            lastName.setFocusable(false);
            schoolName.setFocusable(false);
            editButton.setText("Edit");
            firstName.setBackground(getResources().getDrawable(R.drawable.empty));
            lastName.setBackground(getResources().getDrawable(R.drawable.empty));
            schoolName.setBackground(getResources().getDrawable(R.drawable.empty));
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
        currUser.setValues(firstName.getText().toString(), lastName.getText().toString(), schoolName.getText().toString(), email.getText().toString());
        fdbUsers.child(uid).setValue(currUser);
    }

    //menu test//
    private void addDrawerItems() {
        String[] menuArr = getResources().getStringArray(R.array.parent_menu);
        mAdapter = new ArrayAdapter<String>(this, R.layout.menu_item, menuArr);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    final Intent homeScreenIntent = new Intent(Profile.this, HomeScreen.class);
                    homeScreenIntent.putExtra("uid", uid);
                    startActivity(homeScreenIntent);
                } else if (position == 2){
                    final Intent profileScreenIntent = new Intent(Profile.this, Profile.class);
                    profileScreenIntent.putExtra("uid", uid);
                    startActivity(profileScreenIntent);
                } else {
                    Toast.makeText(Profile.this, "Not setup yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    ////////////
}
