package bigbrother.child_monitoring_system;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by Luka on 2/14/2017.
 */

public class CustomSearchAdapter extends SimpleAdapter {
    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String, String>> arrayList;
    HashMap<Integer, String> uidListView;
    String uid;

    public CustomSearchAdapter(Context context, ArrayList<HashMap<String, String>> data, int resource, String[] from, int[] to, HashMap<Integer, String> uidListView) {
        super(context, data, resource, from, to);
        this.context = context;
        this.arrayList = data;
        this.uidListView = uidListView;
        inflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);


        // if there are no results don't display buttons
        if(this.arrayList.get(0).containsValue("No Results")) {
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageButtonChild);
            imageButton.setVisibility(View.GONE);
            Switch sw = (Switch) view.findViewById(R.id.switchAccess);
            sw.setVisibility(View.GONE);
            return view;
        }

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageButtonChild);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent theIntent = new Intent(context, CardContentFragment.class);
                theIntent.putExtra("uid", uidListView.get(position));
                context.startActivity(theIntent);
            }
        });

        return view;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0) {
        return true;
    }
}
