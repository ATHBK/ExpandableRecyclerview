package com.athbk.expandrecyclerviewsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    Button btnAdd;
    Button btnRemove;
    Button btnChange;
    Button btnToogle;

    private boolean isExpand = false;

    private ArrayList<ParentModel> listParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnRemove = (Button) findViewById(R.id.btnRemove);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnToogle = (Button) findViewById(R.id.btnToggle);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        final ParentModel parent1 = new ParentModel("Parent 1", Arrays.asList(new ChildModel("Child 1.1"), new ChildModel("Child 1.2")));
        parent1.setExpand(true);
        ParentModel parent2 = new ParentModel("Parent 2", Arrays.asList(new ChildModel("Child 2.1"), new ChildModel("Child 2.2"), new ChildModel("Child 2.3")));
        parent2.setExpand(true);
        ParentModel parent3 = new ParentModel("Parent 3", Arrays.asList(new ChildModel("Child 3.1")));
        parent3.setExpand(true);

        ParentModel parent4 = new ParentModel("Parent 4", Arrays.asList(new ChildModel("Child 4.1")));

        listParent = new ArrayList<>();
        listParent.add(parent1);
        listParent.add(parent2);
        listParent.add(parent3);
        listParent.add(parent4);
//
        adapter = new Adapter(listParent);

        recyclerView.setAdapter(adapter);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *  Add parent
                 */

//                ParentModel parent1 = new ParentModel("Parent Add 1", Arrays.asList(new ChildModel("Child Add 1.1")));
//                ParentModel parent2 = new ParentModel("Parent Add 2", Arrays.asList(new ChildModel("Child Add 2.1"), new ChildModel("Child Add 2.2")));
//                ParentModel parent3 = new ParentModel("Parent Add 3", Arrays.asList(new ChildModel("Child Add 3.1")));

//                listParent.add(2, parent1);
//                listParent.add(3, parent2);
//                listParent.add(3, parent3);

//                adapter.notifyParentRangeInsert(2, 2);

//                adapter.notifyParentInsert(2);


                /**
                 *  Add child
                 */
                ParentModel parent1 = listParent.get(1);
                ChildModel child = new ChildModel("Child Add 1.3");
                ArrayList<ChildModel> listChild = new ArrayList<ChildModel>(parent1.getListChild());
                listChild.add(1, child);
                listChild.add(2, child);
                parent1.setListChilds(listChild);

                listParent.set(1, parent1);

//
                adapter.notifyChildRangeInsert(1, 1, 2);

            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *  Remove Parent
                 */
//                listParent.remove(1);
//                listParent.remove(1);
////                listParent.remove(1);
//                adapter.notifyParentRangeRemove(1, 2);


//                adapter.notifyParentRemove(1);


                /**
                 * Remove Child
                 */
                ParentModel parent1 = listParent.get(1);
//                ChildModel child = new ChildModel("Child Add 1.3");
                ArrayList<ChildModel> listChild = new ArrayList<ChildModel>(parent1.getListChild());
                listChild.remove(1);
                listChild.remove(1);

                parent1.setListChilds(listChild);
                adapter.notifyChildRangeRemove(1, 1, 2);

            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *      Change Parent
                 */
//                    ParentModel parentModel1 = listParent.get(1);
//                    parentModel1.setTitleParent("Parent Change 1");
//
//                    ParentModel parentModel2 = listParent.get(2);
//                    parentModel2.setTitleParent("Parent Change 2");
//
//                    adapter.notifyParentRangeChange(1, 2);

                /**
                 *      Change Child
                 */

                ParentModel parentModel1 = listParent.get(1);
                ArrayList<ChildModel> listChild = new ArrayList<ChildModel>(parentModel1.getListChild());
                listChild.set(0, new ChildModel("Child 2.1 Change"));
//                listChild.set(1, new ChildModel("Child 2.2 Change"));


                parentModel1.setListChilds(listChild);
                adapter.notifyChildRangeChange(1, 0, 1);


            }
        });

        btnToogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpand){
                    isExpand = true;
                    adapter.updateExpandAll();
                }
                else {
                    isExpand = false;
                    adapter.updateCollapseAll();
                }
            }
        });
    }
}
