package com.example.appanimals.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appanimals.Admin.Adapter.DetailProfileAdapter;
import com.example.appanimals.Admin.Adapter.OrderAdapter;
import com.example.appanimals.Admin.Adapter.RecyclerViewitemtouchhelperOrder;
import com.example.appanimals.Admin.Adapter.RecyclerViewitemtouchhelperProfile;
import com.example.appanimals.Model.Account;
import com.example.appanimals.Model.InfoCustomer;
import com.example.appanimals.Model.ItemTouchHelperListener;
import com.example.appanimals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class DetailProfileActivity extends AppCompatActivity implements ItemTouchHelperListener, DetailProfileAdapter.IClickShowDataProfile {
    private MaterialToolbar toolbar;
    private RecyclerView rcvProfile;
    private FloatingActionButton fabAddProfile;
    private ArrayList<Account> arrayList;
    private DatabaseReference reference;
    private DetailProfileAdapter adapter;
    private AppBarLayout appBarLayout;
    private FirebaseAuth mAuth;
    private NestedScrollView rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        ActionBar();
        ActionView();
        GetData();
        isExpand();
    }

    private void GetData() {
        String strName = getIntent().getStringExtra("name");
        toolbar.setTitle(strName);
        reference = FirebaseDatabase.getInstance().getReference("Account");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (arrayList != null)
                {
                    arrayList.clear();
                }
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Account account = postSnapshot.getValue(Account.class);
                    arrayList.add(account);
                }
                adapter = new DetailProfileAdapter(arrayList, DetailProfileActivity.this,DetailProfileActivity.this);
                rcvProfile.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isExpand() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)>150)
                {
                    fabAddProfile.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.uptodown);
                    fabAddProfile.setAnimation(animation);
                }
                else {
                    fabAddProfile.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void ActionView() {
        fabAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        reference = FirebaseDatabase.getInstance().getReference("Account");
        final Dialog dialog = new Dialog(DetailProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_profile_edit);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        EditText editTextEmail = dialog.findViewById(R.id.email_admin_edit);
        EditText editTextName = dialog.findViewById(R.id.name_admin_edit);
        EditText editTextPassword = dialog.findViewById(R.id.password_admin_edit);
        EditText editTextPhone = dialog.findViewById(R.id.phone_admin_edit);
        AppCompatButton btnSignIn = dialog.findViewById(R.id.btn_sign_in_admin_edit);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRole(editTextEmail, editTextName,editTextPassword,editTextPhone,dialog);
            }
        });
        dialog.show();

    }

    private void showDialogRole(EditText editTextEmail, EditText editTextName, EditText editTextPassword, EditText editTextPhone, Dialog dialog2) {
        reference = FirebaseDatabase.getInstance().getReference("Account");
        String dateCurrent = new SimpleDateFormat("MMMM dd,yyyy",Locale.getDefault()).format(new Date());
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailProfileActivity.this);
        builder.setTitle("You choose role for this account!");
        final String [] ages = {"user","admin"};
        final int countAge = 2;
        final Set<String> selectedItems = new HashSet<String>();
        builder.setCancelable(false);
        builder.setSingleChoiceItems(ages, countAge, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedItems.clear();
                selectedItems.add(ages[i]);
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedItems.isEmpty())
                {
                    Toast.makeText(DetailProfileActivity.this, "Please chooses age of pet", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    dialog2.show();
                }
                dialog.dismiss();
                dialog2.dismiss();
                String checkedRole = selectedItems.iterator().next();
                String status = "On";
                Account account = new Account(editTextName.getText().toString(),editTextPhone.getText().toString(),editTextEmail.getText().toString(),editTextPassword.getText().toString(),dateCurrent,checkedRole,status);
                mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(),editTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            reference.child(editTextName.getText().toString()).setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(DetailProfileActivity.this, "Make an account successful.", Toast.LENGTH_SHORT).show();
                                        rcvProfile.scrollToPosition(arrayList.size());
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DetailProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog2.show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        rcvProfile = findViewById(R.id.rcv_profile_admin);
        toolbar = findViewById(R.id.toolbar_profile_admin);
        fabAddProfile = findViewById(R.id.fab_add_profile_admin);
        arrayList = new ArrayList<>();
        rcvProfile.setHasFixedSize(true);
        rcvProfile.setLayoutManager(new LinearLayoutManager(this));
        appBarLayout = findViewById(R.id.app_bar_detail_account);
        mAuth = FirebaseAuth.getInstance();
        rootView = findViewById(R.id.layout_showView_profile);
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewitemtouchhelperProfile(0,ItemTouchHelper.LEFT,DetailProfileActivity.this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvProfile);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof DetailProfileAdapter.DetailProfileViewHolder){
            String strUsername = arrayList.get(viewHolder.getLayoutPosition()).getFullName();
            Account infoCustomerDelete = arrayList.get(viewHolder.getLayoutPosition());
            int index = viewHolder.getLayoutPosition();
            adapter.removeItem(index,strUsername);
            Snackbar snackbar = Snackbar.make(rootView,strUsername+"removed",Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.undoItem(infoCustomerDelete,index,strUsername);
                    if (index == 0 || index == arrayList.size() -1){
                        rcvProfile.scrollToPosition(index);
                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

    @Override
    public void onClickShoDataProfile(Account account) {
        final Dialog dialog = new Dialog(DetailProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_profile);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        TextView tvName = dialog.findViewById(R.id.tv_name_detail_profile_admin);
        TextView tvRole = dialog.findViewById(R.id.tv_role_profile_admin_edit);
        TextView tvEmail = dialog.findViewById(R.id.tv_email_detail_profile_admin);
        TextView tvPhone = dialog.findViewById(R.id.tv_phone_detail_profile_admin);
        TextView tvDate = dialog.findViewById(R.id.tv_date_detail_profile_admin);

        tvName.setText(account.getFullName());
        tvDate.setText(account.getDateJoined());
        tvRole.setText(account.getRole());
        tvEmail.setText(account.getEmail());
        tvPhone.setText(account.getPhone());
        dialog.show();
    }

    @Override
    public void onClickResetPassword(Account account) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = account.getEmail();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DetailProfileActivity.this, "Email be sent to ".concat(account.getEmail()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null){
            adapter.release();
        }
    }
}