package bigbrother.child_monitoring_system;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Map extends AppCompatActivity implements MapInputDialog.OnCompleteListener, FloorView.onCircleCreateInterface {

    private ImageView floorPlanIV;
    private Drawable floorplan;
    private StorageReference storage;
    private FloorView floorView;
    private Button uploadImageButton;
    private static final int GALLERY_INTENT = 2;

    public static HashMap<String, RoomData> rooms = new HashMap<>();
    public static ArrayList<ChildDataObject> children = new ArrayList<>();

    private DatabaseReference db;
    private DatabaseReference dbChildren;

    private String currentRoomName, currentPiMAC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create the database reference
        db = FirebaseDatabase.getInstance().getReference().child("daycare").child("Georgia Tech").child("mapdata");
        dbChildren = FirebaseDatabase.getInstance().getReference().child("daycare").child("Georgia Tech").child("children");
        //load the rooms for the FloorView
        onLoad();

        setContentView(R.layout.activity_map);

        floorPlanIV = (ImageView) findViewById(R.id.floorPlanIV);
        uploadImageButton = (Button) findViewById(R.id.uploadImageButton);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        storage = FirebaseStorage.getInstance().getReference();

        //set the interface for listening to circle creation
        floorView = (FloorView) findViewById(R.id.floorView);
        floorView.setViewListener(this);

        storage.child("Daycare/daycare1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(Map.this)
                        .load(uri)
                        .into(floorPlanIV);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("download failed");
            }
        });

    }

    public void onLoad() {

        //for the children at that particular school
        dbChildren.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                children.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    java.util.Map<?,?> child = (java.util.Map<?,?>) snapshot.getValue();
                    ChildDataObject ch = new ChildDataObject(child.get("name").toString(),
                            child.get("macAddress").toString(), child.get("location").toString(),
                            child.get("timestamp").toString());
                    addChildToList(ch);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //for the rooms of the particular school
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rooms.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RoomData room = snapshot.getValue(RoomData.class);
                    Log.d("MAPCLASS","THE ROOM WAS LOADED AS " + room.getMac());
                    addRoomToList(room);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //method to add a child to the static arraylist
    public void addChildToList(ChildDataObject child) {
        this.children.add(child);
    }

    // method to call to inner class for adding rooms to pass to view
    public void addRoomToList(RoomData room) {
        this.rooms.put(room.getMac(), room);
    }

    @Override
    public void onComplete(String name, String mac) {
        floorView.onComplete(name, mac);
    }


    @Override
    public void onCircleCreate() {
        showMapDialog();
    }

    @Override
    public void onCircleCreate(String mac, int radius, int xCor, int yCor) {
        //method for getting this info and putting it inside the database so that
        //the teachers and parents can retrieve this information to make their own map
        Log.d("MAPCLASS", mac);


        // check if there is a child node of name "mapdata" and create if not
        java.util.Map<String, Object> mapdata = new HashMap<>();
        mapdata.put(mac, new RoomData(mac, xCor, yCor, radius));
        db.updateChildren(mapdata);

    }

    @Override
    public void onCircleDelete(String mac) {
        db.child(mac).removeValue();
    }

    private void showMapDialog() {
        FragmentManager fm = getSupportFragmentManager();
        MapInputDialog mapInputDialog = MapInputDialog.newInstance();
        mapInputDialog.show(fm, "child_input_fragment");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode ==RESULT_OK) {
            Uri uri = data.getData();

            StorageReference filepath = storage.child("Daycare").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Map.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
