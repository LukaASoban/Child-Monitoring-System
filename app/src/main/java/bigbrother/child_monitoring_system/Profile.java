package bigbrother.child_monitoring_system;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private String uid;
    private DatabaseReference fdbUsers;
    private User currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile);
        fdbUsers = FirebaseDatabase.getInstance().getReference().child("users");
        uid = getIntent().getStringExtra("uid");


        fdbUsers.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(DatabaseError error) { }
        });

    }



    private void onSaveClick() {
        if (currUser != null && uid != null) {
            fdbUsers.child(uid).child(uid).setValue(currUser);
        }
    }

}
