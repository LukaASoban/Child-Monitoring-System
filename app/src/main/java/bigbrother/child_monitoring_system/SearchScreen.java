package bigbrother.child_monitoring_system;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchScreen extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSearch;
    private EditText searchName;
    private String uid;
    private ListView usersList;
    private DatabaseReference dRef;
    private HashMap<Integer, String> uidListView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_search_screen);

        buttonSearch = (Button) findViewById(R.id.searchButton);
        buttonSearch.setOnClickListener(this);
        searchName = (EditText) findViewById(R.id.searchName);
        uidListView = new HashMap<>();

        usersList = (ListView)findViewById(R.id.usersList);
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                final Intent userOptionsScreen = new Intent(SearchScreen.this, UserOptionsScreen.class);
                userOptionsScreen.putExtra("uid", uidListView.get(position));
                startActivity(userOptionsScreen);
            }
        });
        dRef = FirebaseDatabase.getInstance().getReference().child("users");
        uid = getIntent().getStringExtra("uid");
        populateList();

    }


    @Override
    public void onClick(View v) {
        if (v == buttonSearch) {
            final String nameEntered = searchName.getText().toString().trim();
            if (TextUtils.isEmpty(nameEntered)) {
                searchName.setError("Please provide the name of the user to search for");
                return;
            }

            dRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<String> list = new ArrayList<String>();
                    String[] firstLastName = nameEntered.split("\\s+");
                    String firstName = firstLastName[0];
                    String lastName = "";
                    if (firstLastName.length > 1) {
                        lastName = firstLastName[1];
                    }
                    int index = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (firstLastName.length > 1 && user.getFirstName().equals(firstName)
                                && user.getLastName().equals(lastName)) {
                            list.add(user.getFirstName() + " " + user.getLastName());
                            uidListView.put(index++, snapshot.getKey());
                        } else if (firstLastName.length == 1
                                && (user.getFirstName().equals(firstName)
                                || user.getLastName().equals(firstName))) {
                            list.add(user.getFirstName() + " " + user.getLastName());
                            uidListView.put(index++, snapshot.getKey());
                        }

                    }
                    if (list.isEmpty()) {
                        list.add("No results to display");
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(SearchScreen.this, R.layout.menu_item,
                            list);
                    usersList.setAdapter(arrayAdapter);
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {

                }
            });
        }
    }

    public void populateList() {
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<String>();
                int index = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    list.add(user.getFirstName() + " " + user.getLastName());
                    uidListView.put(index++, snapshot.getKey());
                }
                if (list.isEmpty()) {
                    list.add("No results to display");
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(SearchScreen.this, R.layout.menu_item, list);
                usersList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }
}
