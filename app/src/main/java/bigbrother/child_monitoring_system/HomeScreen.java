package bigbrother.child_monitoring_system;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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


public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private Button buttonProfile;
    private Button buttonSearch;
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
    ////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home_screen);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
        uid = getIntent().getStringExtra("uid");

        buttonProfile = (Button) findViewById(R.id.profile);
        buttonSearch = (Button) findViewById(R.id.search);

        buttonProfile.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);


        //menu test//
        mDrawerList = (ListView)findViewById(R.id.navList);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        getSupportActionBar().setTitle("Home");
        addDrawerItems();
        setupDrawer();
        ////////////

        dRef = FirebaseDatabase.getInstance().getReference().child("users");
        uid = getIntent().getStringExtra("uid");

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().toString().equals(uid)) {
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
    }

    @Override
    public void onClick(View v) {
        if (v == buttonProfile) {
            final Intent welcomeScreenIntent = new Intent(this, Profile.class);
            welcomeScreenIntent.putExtra("uid", uid);
            startActivity(welcomeScreenIntent);
        } else if (v == buttonSearch) {
            final Intent searchScreenIntent = new Intent(this, SearchScreen.class);
            startActivity(searchScreenIntent);
        }

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
                    final Intent homeScreenIntent = new Intent(HomeScreen.this, HomeScreen.class);
                    homeScreenIntent.putExtra("uid", uid);
                    startActivity(homeScreenIntent);
                } else if (position == 2) {
                    final Intent profileScreenIntent = new Intent(HomeScreen.this, Profile.class);
                    profileScreenIntent.putExtra("uid", uid);
                    startActivity(profileScreenIntent);
                } else {
                    Toast.makeText(HomeScreen.this, "Not setup yet!", Toast.LENGTH_SHORT).show();
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
