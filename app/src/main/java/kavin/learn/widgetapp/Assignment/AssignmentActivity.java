package kavin.learn.widgetapp.Assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import kavin.learn.widgetapp.R;

public class AssignmentActivity extends AppCompatActivity {

    RecyclerView assign_top_recycler,assign_bottom_recycler;
    RecyclerView.LayoutManager mLayoutManager;
    GridLayoutManager gridLayoutManager;
    AssignmentTopAdapter assignmentTopAdapter;
    AssignmentBottomAdapter assignmentBottomAdapter;
    List<AssignBottomPojo> assignBottomPojoList=new ArrayList<>();
    List<AssignTopPojo> assignTopPojoList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        findviews();

        loadData();
    }

    private void loadData() {

        AssignTopPojo a1=new AssignTopPojo();
        a1.setName("Tamil");
        a1.setImage("https://homepages.cae.wisc.edu/~ece533/images/cat.png");

        AssignTopPojo a2=new AssignTopPojo();
        a2.setName("English");
        a2.setImage("https://homepages.cae.wisc.edu/~ece533/images/fruits.png");

        AssignTopPojo a3=new AssignTopPojo();
        a3.setName("Maths");
        a3.setImage("https://homepages.cae.wisc.edu/~ece533/images/frymire.png");


        AssignTopPojo a4=new AssignTopPojo();
        a4.setName("Science");
        a4.setImage("https://homepages.cae.wisc.edu/~ece533/images/cat.png");

        AssignTopPojo a5=new AssignTopPojo();
        a5.setName("Social");
        a5.setImage("https://homepages.cae.wisc.edu/~ece533/images/fruits.png");

        assignTopPojoList.add(a1);
        assignTopPojoList.add(a2);
        assignTopPojoList.add(a3);
        assignTopPojoList.add(a4);
        assignTopPojoList.add(a5);


        AssignBottomPojo b1=new AssignBottomPojo();
        b1.setName("English");

        AssignBottomPojo b2=new AssignBottomPojo();
        b2.setName("Tamil");

        AssignBottomPojo b3=new AssignBottomPojo();
        b3.setName("Grammer");

        AssignBottomPojo b4=new AssignBottomPojo();
        b4.setName("Video");

        AssignBottomPojo b5=new AssignBottomPojo();
        b5.setName("Science");

        assignBottomPojoList.add(b1);
        assignBottomPojoList.add(b2);
        assignBottomPojoList.add(b3);
        assignBottomPojoList.add(b4);
        assignBottomPojoList.add(b5);

        assignmentTopAdapter.notifyDataSetChanged();
        assignmentBottomAdapter.notifyDataSetChanged();
    }

    private void findviews() {
        assign_top_recycler=findViewById(R.id.assign_top_recycler);
        assign_bottom_recycler=findViewById(R.id.assign_bottom_recycler);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false);
        assign_top_recycler.setLayoutManager(mLayoutManager);
        assign_top_recycler.setItemAnimator(new DefaultItemAnimator());
        assignmentTopAdapter=new AssignmentTopAdapter(this,assignTopPojoList);
        assign_top_recycler.setAdapter(assignmentTopAdapter);

        gridLayoutManager = new GridLayoutManager(this,2);
        assign_bottom_recycler.setLayoutManager(gridLayoutManager);
        assign_bottom_recycler.setItemAnimator(new DefaultItemAnimator());
        assignmentBottomAdapter=new AssignmentBottomAdapter(this,assignBottomPojoList);
        assign_bottom_recycler.setAdapter(assignmentBottomAdapter);
    }
}
