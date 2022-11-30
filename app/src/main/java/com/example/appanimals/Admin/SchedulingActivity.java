package com.example.appanimals.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appanimals.Admin.Adapter.ProductAdminAdapter;
import com.example.appanimals.Admin.Adapter.RecyclerViewitemtouchhelperScheduling;
import com.example.appanimals.Admin.Adapter.SchedulingAdapter;
import com.example.appanimals.Model.ItemTouchHelperListener;
import com.example.appanimals.Model.Scheduling;
import com.example.appanimals.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SchedulingActivity extends AppCompatActivity implements ItemTouchHelperListener{
    private NestedScrollView rootView;
    private RecyclerView rcvScheduling;
    private MaterialToolbar toolbarScheduling;
    private SchedulingAdapter schedulingAdapter;
    private ArrayList<Scheduling> arrayList;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scheduling);
        initView();
        ActionBar();
        GetData();
    }

    private void GetData() {
        String strName = getIntent().getStringExtra("name");
        toolbarScheduling.setTitle(strName);
        reference = FirebaseDatabase.getInstance().getReference("Scheduling");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (arrayList != null){
                    arrayList.clear();
                }
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Scheduling scheduling = postSnapshot.getValue(Scheduling.class);
                    arrayList.add(scheduling);
                }
                schedulingAdapter = new SchedulingAdapter(arrayList, SchedulingActivity.this, new SchedulingAdapter.IClickListener() {
                    @Override
                    public void onClickShoData(Scheduling scheduling) {
                        final Dialog dialog = new Dialog(SchedulingActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.layout_dialog_scheduling);
                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.WRAP_CONTENT);
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCancelable(true);
                        TextView tvName = dialog.findViewById(R.id.tv_name_detail_scheduling_admin);
                        TextView tvAnimal = dialog.findViewById(R.id.tv_animal_detail_scheduling_admin);
                        TextView tvSex = dialog.findViewById(R.id.tv_sex_detail_scheduling_admin);
                        TextView tvAge = dialog.findViewById(R.id.tv_age_detail_scheduling_admin);
                        TextView tvDate = dialog.findViewById(R.id.tv_date_detail_scheduling_admin);
                        TextView tvTime = dialog.findViewById(R.id.tv_time_detail_scheduling_admin);
                        TextView tvTypeOfProcedure = dialog.findViewById(R.id.tv_type_of_procedure_detailed_scheduling_admin);
                        TextView tvProcedure = dialog.findViewById(R.id.tv_procedure_detail_scheduling_admin);
                        TextView tvKind = dialog.findViewById(R.id.tv_kind_detail_scheduling_admin);
                        tvName.setText("Customer: "+scheduling.getNameCustomer());
                        tvAnimal.setText("Animal: "+scheduling.getName());
                        tvSex.setText("Sex: "+scheduling.getSex());
                        tvAge.setText("Age: "+scheduling.getAge());
                        tvDate.setText(scheduling.getDate());
                        tvTime.setText(scheduling.getTime());
                        tvTypeOfProcedure.setText("Type of Procedure: "+scheduling.getTypeOfProcedure());
                        tvProcedure.setText("Procedure: "+scheduling.getProcedure());
                        tvKind.setText("Kind: "+scheduling.getKind());
                        dialog.show();
                    }
                });
                rcvScheduling.setAdapter(schedulingAdapter);
                schedulingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void ActionBar() {
        setSupportActionBar(toolbarScheduling);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarScheduling.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        rcvScheduling = findViewById(R.id.rcv_scheduling);
        rootView = findViewById(R.id.layout_scheduling_admin);
        toolbarScheduling = findViewById(R.id.toolbar_scheduling_admin);
        arrayList = new ArrayList<>();
        rcvScheduling.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewitemtouchhelperScheduling(0,ItemTouchHelper.LEFT,SchedulingActivity.this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvScheduling);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (schedulingAdapter != null) {
            schedulingAdapter.release();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof SchedulingAdapter.SchedulingViewHolder){
            String strName = arrayList.get(viewHolder.getLayoutPosition()).getNameCustomer();
            Scheduling schedulingDelete = arrayList.get(viewHolder.getLayoutPosition());
            int index = viewHolder.getLayoutPosition();
            schedulingAdapter.removeItem(index,strName);
            Snackbar snackbar = Snackbar.make(rootView,strName+"removed",Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    schedulingAdapter.undoItem(schedulingDelete,index,strName);
                    if (index == 0 || index == arrayList.size() -1){
                        rcvScheduling.scrollToPosition(index);
                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}