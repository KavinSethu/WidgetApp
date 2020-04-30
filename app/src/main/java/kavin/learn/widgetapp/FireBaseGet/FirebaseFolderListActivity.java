package kavin.learn.widgetapp.FireBaseGet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kavin.learn.widgetapp.FireBaseGet.Adapter.FolderAdapter;
import kavin.learn.widgetapp.FireBaseGet.Pojo.Folder;
import kavin.learn.widgetapp.R;

public class FirebaseFolderListActivity extends AppCompatActivity {

    //database reference
    private DatabaseReference mDatabase;

    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapter;

    //list to hold all the uploaded images
    private List<Folder> folderList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_folder_list);

        recyclerView = (RecyclerView) findViewById(R.id.list_folder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               /* for (DataSnapshot dataSnap:dataSnapshot.getChildren()){
                    Folder folder = dataSnap.getValue(Folder.class);
                    folderList.add(folder);
                    Log.d("Folder_name",""+folder.name);
                }*/

                for (DataSnapshot dataSnap:dataSnapshot.getChildren()){
                    Folder folder = new Folder();
                    folder.setName(dataSnap.getKey());
                    folder.setImage("");
                    folder.setAge("");
                    folderList.add(folder);
                    Log.d("Folder_name",""+folder.name);
                }

                //creating adapter
                adapter = new FolderAdapter(getApplicationContext(), folderList);

                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
