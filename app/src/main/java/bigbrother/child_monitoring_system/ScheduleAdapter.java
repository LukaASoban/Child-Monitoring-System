package bigbrother.child_monitoring_system;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;


public class ScheduleAdapter extends RecyclerView
        .Adapter<ScheduleAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ScheduleAdapter";
    private ArrayList<Schedule> scheduleDataset;
    private static ScheduleAdapter.MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView name;
        TextView macAddress;
        ImageButton buttonDelete, buttonEdit;

        public DataObjectHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cardScheduleName);
            macAddress = (TextView) itemView.findViewById(R.id.cardScheduleMAC);

            //create button click listener for the edit and delete buttons
            buttonDelete = (ImageButton) itemView.findViewById(R.id.card_delete_button);
            buttonEdit = (ImageButton) itemView.findViewById(R.id.card_edit_button);

            buttonDelete.setOnClickListener(this);
            buttonEdit.setOnClickListener(this);


            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == buttonDelete.getId()) {
                myClickListener.onButtonClick(getAdapterPosition(), v);
            } else if(v.getId() == buttonEdit.getId()) {
                myClickListener.onEditButtonClick(getAdapterPosition(), v);
            }

            myClickListener.onItemClick(getAdapterPosition(), v);


        }
    }

    public void setOnItemClickListener(ScheduleAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ScheduleAdapter(ArrayList<Schedule> myData) {
        scheduleDataset = myData;
    }

    @Override
    public ScheduleAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_card, parent, false);

        ScheduleAdapter.DataObjectHolder dataObjectHolder = new ScheduleAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(ScheduleAdapter.DataObjectHolder holder, final int position) {
        holder.name.setText(scheduleDataset.get(position).getName());
        holder.macAddress.setText(scheduleDataset.get(position).getMacAddress());

    }

    public void addItem(Schedule dataObj, int index) {
        scheduleDataset.add(0,dataObj);
        notifyItemInserted(index);

    }

    public void deleteItem(int index) {
        scheduleDataset.remove(index);
        notifyItemRemoved(index);
    }

    public ArrayList<Schedule> getScheduleDataset(){
        if(scheduleDataset == null) {
            scheduleDataset = new ArrayList<Schedule>();
        }
        return scheduleDataset;
    }

    @Override
    public int getItemCount() {

        if(scheduleDataset == null) {
            return 0;
        }

        return scheduleDataset.size();
    }

    public Schedule getScheduleDataAt(int pos) {
        return scheduleDataset.get(pos);
    }

    public void replaceAt(int pos, Schedule schedule) {
        scheduleDataset.set(pos, schedule);
        notifyItemChanged(pos);
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
        public void onButtonClick(int position, View v);
        public void onEditButtonClick(int position, View v);
    }
}