package bigbrother.child_monitoring_system;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class ScheduleInputFragment extends android.support.v4.app.DialogFragment {

    private Button accept, cancel;
    private EditText scheduleName, scheduleMAC;

    private OnCompleteListener mListener;

    public ScheduleInputFragment() {
        //empty
    }

    public static ScheduleInputFragment newInstance(String title, ScheduleDataObject scheduleDataObject, int pos) {
        ScheduleInputFragment frag = new ScheduleInputFragment();
        Bundle args = new Bundle();
        args.putString("title", title);

        //if the object is null that means the dialog is being open by the FAB and not
        //the edit button
        if(scheduleDataObject != null) {
            args.putString("name", scheduleDataObject.getName());
            args.putString("mac", scheduleDataObject.getMacAddress());
        }
        args.putInt("position", pos);
        frag.setArguments(args);
        return frag;
    }


    //create interface to pass the data back to the previous activity
    public static interface OnCompleteListener {
        public abstract void onComplete(ScheduleDataObject scheduleDataObject);
        public abstract void onComplete(ScheduleDataObject scheduleDataObject, int pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if(context instanceof Activity) {
            a =(Activity) context;
        } else {
            a = null;
        }

        try {
            this.mListener = (OnCompleteListener) a;
        } catch (final ClassCastException e) {
            throw new ClassCastException(a.toString() + " must implement OnCompleteListener");
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_input_fragment, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        scheduleName = (EditText) view.findViewById(R.id.scheduleName);
        scheduleName.setHint("Room");
        scheduleMAC = (EditText) view.findViewById(R.id.scheduleMAC);
        scheduleMAC.setHint("Room Monitor MAC");

        cancel = (Button) view.findViewById(R.id.buttonCancel);
        //make click listeners for the buttons
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
            }
        });

        final OnCompleteListener mListenerTemp = this.mListener;
        accept = (Button) view.findViewById(R.id.buttonOK);
        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(TextUtils.isEmpty(scheduleName.getText().toString().trim())) {
                    scheduleName.setError("Please input a name");
                    return;
                } else if(TextUtils.isEmpty(scheduleMAC.getText().toString().trim())) {
                    scheduleMAC.setError("Please provide the rooms's MAC address");
                    return;
                }

                ScheduleDataObject scheduleDataObject = new ScheduleDataObject(scheduleName.getText().toString(), scheduleMAC.getText().toString());

                Log.d("CHILD INPUT FRAG", "" + getArguments().getInt("position"));

                mListenerTemp.onComplete(scheduleDataObject, getArguments().getInt("position"));
                dismiss();
            }
        });

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        scheduleName.setText(getArguments().getString("name"));
        scheduleMAC.setText(getArguments().getString("mac"));
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        scheduleName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }



}
