package bigbrother.child_monitoring_system;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TeacherClassCardAdapter extends RecyclerView.Adapter<TeacherClassCardAdapter
        .DataObjectHolder> {

    private ArrayList<ChildDataObject> childDataset;
    private static TeacherClassCardAdapter.MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView name;
        TextView macAddress;
        ImageButton buttonDownArrow, buttonEdit;

        public DataObjectHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cardChildName);

            name.setTextSize(20);
            name.setTextColor(Color.parseColor("#F99F20"));
            macAddress = (TextView) itemView.findViewById(R.id.cardChildMAC);
            macAddress.setVisibility(View.INVISIBLE);

            //create button click listener for the edit and delete buttons
            buttonDownArrow = (ImageButton) itemView.findViewById(R.id.card_delete_button);
            buttonEdit = (ImageButton) itemView.findViewById(R.id.card_edit_button);
            buttonEdit.setVisibility(View.INVISIBLE);

            buttonDownArrow.setImageResource(R.drawable.ic_arrow_downward_black_24dp);

            buttonDownArrow.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == buttonDownArrow.getId()) {
                myClickListener.onButtonClick(getAdapterPosition(), v);
            }

            myClickListener.onItemClick(getAdapterPosition(), v);


        }
    }

    public void setOnItemClickListener(TeacherClassCardAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public TeacherClassCardAdapter(ArrayList<ChildDataObject> myData) {
        childDataset = myData;
    }

    @Override
    public TeacherClassCardAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        TeacherClassCardAdapter.DataObjectHolder dataObjectHolder = new TeacherClassCardAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(TeacherClassCardAdapter.DataObjectHolder holder, final int position) {
        holder.name.setText(childDataset.get(position).getName());
        holder.macAddress.setText(childDataset.get(position).getMacAddress());

    }

    public void addItem(ChildDataObject dataObj, int index) {
        childDataset.add(0,dataObj);
        notifyItemInserted(index);

    }

    /* DELETES THE ITEM FROM THE CLASS SECTION AND THE DATABASE */
    public void deleteItemFromClass(int index, String uid, String schoolName){
        // remove the child from the Class Roster
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("daycare")
                .child(schoolName);

        dRef.child("classrooms").child(uid).child(childDataset.get(index).getMacAddress())
                .removeValue();

        notifyDataSetChanged();


    }

    public void deleteItem(int index) {
        childDataset.remove(index);
        notifyItemRemoved(index);
    }

    public ArrayList<ChildDataObject> getChildDataset(){
        if(childDataset == null) {
            childDataset = new ArrayList<ChildDataObject>();
        }
        return childDataset;
    }

    @Override
    public int getItemCount() {

        if(childDataset == null) {
            return 0;
        }

        return childDataset.size();
    }

    public ChildDataObject getChildDataAt(int pos) {
        return childDataset.get(pos);
    }

    public void replaceAt(int pos, ChildDataObject child) {
        childDataset.set(pos, child);
        notifyItemChanged(pos);
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
        public void onButtonClick(int position, View v);
    }
}
