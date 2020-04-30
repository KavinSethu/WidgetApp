package kavin.learn.widgetapp.FireBaseGet;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kavin.learn.widgetapp.FireBaseGet.Adapter.MyAdapter;
import kavin.learn.widgetapp.FireBaseGet.Pojo.Uploads;
import kavin.learn.widgetapp.R;

public class FirebaseGetListActivity extends AppCompatActivity {

    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapter;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Uploads> uploads;

    EditText et_name,et_Age,et_image;
    Button save;

    String folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_firebase_get_list);

        et_name=findViewById(R.id.et_name);
        et_Age=findViewById(R.id.et_age);
        et_image=findViewById(R.id.et_image);
        save=findViewById(R.id.btn_Save);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        folder=getIntent().getStringExtra("folder");

        progressDialog = new ProgressDialog(this);

        uploads = new ArrayList<>();

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference(folder);
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("uploads");

        //adding an event listener to fetch values
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Uploads upload = postSnapshot.getValue(Uploads.class);
                    uploads.add(upload);
                }

                if (uploads!=null)
                    Log.d("Upload_file",""+uploads.size());
                else
                    Log.d("Upload_file","failed");

                //creating adapter
                adapter = new MyAdapter(getApplicationContext(), uploads);

                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(FirebaseGetListActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = mDatabase.push().getKey();
                Uploads user = new Uploads(
                        et_Age.getText().toString(),
                        et_name.getText().toString(),
                        et_image.getText().toString());
                mDatabase.child(userId).setValue(user);


            }
        });



    }
}
