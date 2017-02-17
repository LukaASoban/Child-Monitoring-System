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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
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
        setContentView(R.layout.activity_search_screen);

        getSupportActionBar().setTitle("Search Users");

        buttonSearch = (Button) findViewById(R.id.searchButton);
        buttonSearch.setOnClickListener(this);
        searchName = (EditText) findViewById(R.id.searchName);
        uidListView = new HashMap<>();

        usersList = (ListView)findViewById(R.id.usersList);

        dRef = FirebaseDatabase.getInstance().getReference().child("users");
        uid = getIntent().getStringExtra("uid");

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
                    ArrayList<HashMap<String,String>> list =  new ArrayList<HashMap<String, String>>();
                    String[] firstLastName = nameEntered.split("\\s+");
                    String firstName = firstLastName[0];
                    String lastName = "";
                    if (firstLastName.length > 1) {
                        lastName = firstLastName[1];
                    }
                    int index = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        if (firstLastName.length > 1 && user.getFirstName().equals(firstName)
                                && user.getLastName().equals(lastName)) {
                            hashMap.put("User",user.getFirstName() + " " + user.getLastName());
                            hashMap.put("UserType", user.getType().toString());
                            hashMap.put("Access", "Access");
                            list.add(hashMap);
                            uidListView.put(index++, snapshot.getKey());
                        } else if (firstLastName.length == 1
                                && (user.getFirstName().equals(firstName)
                                || user.getLastName().equals(firstName))) {
                            hashMap.put("User",user.getFirstName() + " " + user.getLastName());
                            hashMap.put("UserType", user.getType().toString());
                            hashMap.put("Access", "Access");
                            list.add(hashMap);
                            uidListView.put(index++, snapshot.getKey());
                        }

                    }
                    if (list.isEmpty()) {
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        hashMap.put("User", "No Results");
                        list.add(hashMap);
                    }

                    CustomSearchAdapter adapter = new CustomSearchAdapter(SearchScreen.this, list, R.layout.search_list_item, new String[] {"User", "UserType", "Access"}, new int[] {R.id.textTop, R.id.textBottom, R.id.switchAccess}, uidListView);
                    usersList.setAdapter(adapter);
                    usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            final Intent profileScreen = new Intent(SearchScreen.this, Profile.class);
                            profileScreen.putExtra("uid", uidListView.get(position));
                            startActivity(profileScreen);
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {

                }
            });

//            // create imagebutton to open new dialog fragment for child info
//            ListAdapter adapter = usersList.getAdapter();
//
//            //make an array of imagebuttons
//            ImageButton[] childBtn = new ImageButton[adapter.getCount()];
//
//            ImageButton child = new ImageButton(this);
//            child.setImageResource(R.drawable.ic_select_child);
//
//
//
//            RelativeLayout rl = (RelativeLayout) findViewById(R.id.content_search_screen);
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            rl.addView(, lp);




        }
    }


    /* Private class for ListAdapter */

}
