package bigbrother.child_monitoring_system;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private Button buttonProfile;
    private Button buttonSearch;
    private Button buttonMap;
    private String uid;

    //menu test//
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBar actionBar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle = "Home";
    private DatabaseReference dRef;
    private User currentUser;
    private UserType userType;
    ////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home_screen);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

        buttonProfile = (Button) findViewById(R.id.profile);
        buttonSearch = (Button) findViewById(R.id.search);
        buttonMap = (Button) findViewById(R.id.map);

        buttonProfile.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonMap.setOnClickListener(this);

        dRef = FirebaseDatabase.getInstance().getReference().child("users");
        uid = getIntent().getStringExtra("uid");

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(uid)) {
                        currentUser = snapshot.getValue(User.class);
                        if (!currentUser.getType().equals(UserType.ADMIN)) {
                            buttonSearch.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) { }
        });
//        if (currentUser != null && !currentUser.getType().equals(UserType.ADMIN)) {
//            buttonSearch.setVisibility(View.INVISIBLE);
//        }

        //menu test//
        mDrawerList = (ListView)findViewById(R.id.navList);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        getSupportActionBar().setTitle(mActivityTitle);
        addDrawerItems();
        setupDrawer();
        ////////////
    }

    @Override
    public void onClick(View v) {
        if (v == buttonProfile) {
            final Intent welcomeScreenIntent = new Intent(this, Profile.class);
            welcomeScreenIntent.putExtra("uid", uid);
            startActivity(welcomeScreenIntent);
        } else if (v == buttonSearch) {
            final Intent searchScreenIntent = new Intent(this, SearchScreen.class);
            searchScreenIntent.putExtra("uid", uid);
            startActivity(searchScreenIntent);
        } else if (v == buttonMap) {
            final Intent mapScreenIntent = new Intent(this, Map.class);
            mapScreenIntent.putExtra("uid", uid);
            startActivity(mapScreenIntent);
        }
    }

    //menu test//
    private void addDrawerItems() {
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] menuArr = getResources().getStringArray(R.array.parent_menu);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(uid)) {
                        User curUser = snapshot.getValue(User.class);
                        userType = curUser.getType();
                        if (curUser.getType().equals(UserType.ADMIN)) {
                            menuArr = getResources().getStringArray(R.array.admin_menu);
                        } else if (curUser.getType().equals(UserType.EMPLOYEE)){
                            menuArr = getResources().getStringArray(R.array.employee_menu);
                        }
                    }
                }
                mAdapter = new ArrayAdapter<String>(HomeScreen.this, R.layout.menu_item, menuArr);
                mDrawerList.setAdapter(mAdapter);
                mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (userType.equals(UserType.PARENT)) {
                        if (position == 0) {
                            final Intent homeScreenIntent = new Intent(HomeScreen.this, HomeScreen.class);
                            homeScreenIntent.putExtra("uid", uid);
                            startActivity(homeScreenIntent);
                        } else if (position == 1) {
                            final Intent mapScreenIntent = new Intent(HomeScreen.this, Map.class);
                            mapScreenIntent.putExtra("uid", uid);
                            startActivity(mapScreenIntent);
                        } else if (position == 2) {
                            final Intent profileScreenIntent = new Intent(HomeScreen.this, Profile.class);
                            profileScreenIntent.putExtra("uid", uid);
                            startActivity(profileScreenIntent);
                        } else {
                            Toast.makeText(HomeScreen.this, "Not setup yet!", Toast.LENGTH_SHORT).show();
                        }
                } else if (userType.equals(UserType.ADMIN)) {
                    if (position == 0) {
                        final Intent homeScreenIntent = new Intent(HomeScreen.this, HomeScreen.class);
                        homeScreenIntent.putExtra("uid", uid);
                        startActivity(homeScreenIntent);
                    } else if (position == 1) {
                        final Intent mapScreenIntent = new Intent(HomeScreen.this, Map.class);
                        mapScreenIntent.putExtra("uid", uid);
                        startActivity(mapScreenIntent);
                    } else if (position == 2) {
                        final Intent searchScreenIntent = new Intent(HomeScreen.this, SearchScreen.class);
                        searchScreenIntent.putExtra("uid", uid);
                        startActivity(searchScreenIntent);
                    } else if (position == 3){
                        final Intent profileScreenIntent = new Intent(HomeScreen.this, Profile.class);
                        profileScreenIntent.putExtra("uid", uid);
                        startActivity(profileScreenIntent);
                    } else {
                        Toast.makeText(HomeScreen.this, "Not setup yet!", Toast.LENGTH_SHORT).show();
                    }
                } else if (userType.equals(UserType.EMPLOYEE)) {
                    if (position == 0) {
                        final Intent homeScreenIntent = new Intent(HomeScreen.this, HomeScreen.class);
                        homeScreenIntent.putExtra("uid", uid);
                        startActivity(homeScreenIntent);
                    } else if (position == 1) {
                        final Intent mapScreenIntent = new Intent(HomeScreen.this, Map.class);
                        mapScreenIntent.putExtra("uid", uid);
                        startActivity(mapScreenIntent);
                    } else if (position == 3){
                        final Intent profileScreenIntent = new Intent(HomeScreen.this, Profile.class);
                        profileScreenIntent.putExtra("uid", uid);
                        startActivity(profileScreenIntent);
                    } else {
                        Toast.makeText(HomeScreen.this, "Not setup yet!", Toast.LENGTH_SHORT).show();
                    }
                }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

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

