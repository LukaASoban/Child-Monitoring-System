package bigbrother.child_monitoring_system;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


public class ScheduleAdapter extends RecyclerView
        .Adapter<ScheduleAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ScheduleAdapter";
    private ArrayList<ScheduleDataObject> scheduleObjectDataset;
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

    public ScheduleAdapter(ArrayList<ScheduleDataObject> myData) {
        scheduleObjectDataset = myData;
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
        holder.name.setText(scheduleObjectDataset.get(position).getName());
        holder.macAddress.setText(scheduleObjectDataset.get(position).getMacAddress());

    }

    public void addItem(ScheduleDataObject dataObj, int index) {
        scheduleObjectDataset.add(0,dataObj);
        notifyItemInserted(index);

    }

    public void deleteItem(int index) {
        scheduleObjectDataset.remove(index);
        notifyItemRemoved(index);
    }

    public ArrayList<ScheduleDataObject> getScheduleObjectDataset(){
        if(scheduleObjectDataset == null) {
            scheduleObjectDataset = new ArrayList<ScheduleDataObject>();
        }
        return scheduleObjectDataset;
    }

    @Override
    public int getItemCount() {

        if(scheduleObjectDataset == null) {
            return 0;
        }

        return scheduleObjectDataset.size();
    }

    public ScheduleDataObject getScheduleDataAt(int pos) {
        return scheduleObjectDataset.get(pos);
    }

    public void replaceAt(int pos, ScheduleDataObject scheduleDataObject) {
        scheduleObjectDataset.set(pos, scheduleDataObject);
        notifyItemChanged(pos);
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
        public void onButtonClick(int position, View v);
        public void onEditButtonClick(int position, View v);
    }
}