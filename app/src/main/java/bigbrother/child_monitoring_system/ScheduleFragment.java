package bigbrother.child_monitoring_system;

/**
 * Created by Michael on 3/10/2017.
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;


public class ScheduleFragment extends AppCompatActivity implements ScheduleInputFragment.OnCompleteListener {


    private DatabaseReference fdbUsers;
    private DatabaseReference fdbSchool;
    private User currentUser;
    private Schedule schedule;
    private ArrayList<ScheduleDataObject> results = new ArrayList<ScheduleDataObject>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardContentFragment";
    private String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule_menu);

        sid = currentUser.getSchoolName();

        fdbSchool = FirebaseDatabase.getInstance().getReference().child("daycare").child(sid);

        fdbSchool.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                schedule = dataSnapshot.getValue(Schedule.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ScheduleAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        // On click listener for the FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showScheduleDialog(null, -1);

            }
        });


        ((ScheduleAdapter) mAdapter).setOnItemClickListener(new ScheduleAdapter
                .MyClickListener() {

            @Override
            public void onEditButtonClick(int pos, View v) {
                showScheduleDialog(((ScheduleAdapter) mAdapter).getScheduleDataAt(pos), pos);
            }

            @Override
            public void onButtonClick(int position, View v) {
                schedule.getSchedule().remove(position);
                ((ScheduleAdapter) mAdapter).deleteItem(position);
                ((ScheduleAdapter) mAdapter).getScheduleObjectDataset();
                notifyDatabaseChange();
            }

            @Override
            public void onItemClick(int position, View v) {
                // add in if needed later
            }
        });

    }


    /**
     * Implementing the OnComplete interface to pass ScheduleObjects back
     */
    public void onComplete(ScheduleDataObject scheduleDataObject) {
        //add the child card to the recyclerview only if it has changed
        ((ScheduleAdapter) mAdapter).addItem(scheduleDataObject, 0);
        schedule.getSchedule().add(scheduleDataObject);

        ((ScheduleAdapter) mAdapter).getScheduleObjectDataset();
    }

    /**
     * Implementing the onComplete interface for editing ScheduleObjects
     */
    public void onComplete(ScheduleDataObject scheduleDataObject, int pos) {

        if (pos == -1) {
            schedule.getSchedule().add(0,scheduleDataObject);
            ((ScheduleAdapter) mAdapter).addItem(scheduleDataObject, 0);
            notifyDatabaseChange();
            return;
        }

        //replace the object with the newly edited one
        schedule.getSchedule().add(pos,scheduleDataObject);
        ((ScheduleAdapter) mAdapter).replaceAt(pos, scheduleDataObject);
        notifyDatabaseChange();
    }


    private ArrayList<ScheduleDataObject> getDataSet() {
        results = new ArrayList<ScheduleDataObject>();
        fdbSchool.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.child("Schedule").getChildren()) {
                    results.add(data.getValue(ScheduleDataObject.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return results;
    }

    public void notifyDatabaseChange() {
        ArrayList<ScheduleDataObject> schedule;
        ArrayList<ChildDataObject> children = ((ScheduleAdapter) mAdapter).getScheduleObjectDataset();
        currentUser.setChildren();
        fdbUsers.child(sid).setValue(currentUser);
        fdbSchool = FirebaseDatabase.getInstance().getReference().child("daycare").child(currentUser.getSchoolName());
        for (ChildDataObject c : currentUser.getChildren()) {
            fdbSchool.child(String.valueOf(c.getMacAddress())).setValue(c);
        }
    }

    private void showScheduleDialog(ScheduleDataObject scheduleDataObject, int pos) {
        FragmentManager fm = getSupportFragmentManager();
        ScheduleInputFragment scheduleInputFragment = ScheduleInputFragment.newInstance("New ScheduleDataObject", scheduleDataObject, pos);
        scheduleInputFragment.show(fm, "schedule_input_fragment");
    }

}