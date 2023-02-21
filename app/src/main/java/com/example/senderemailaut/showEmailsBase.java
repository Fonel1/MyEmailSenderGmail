package com.example.senderemailaut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.senderemailaut.Adapter.ShowEmailsBaseAdapter;
import com.example.senderemailaut.EventBus.EmailsDbLoadEvent;
import com.example.senderemailaut.Model.Receiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class showEmailsBase extends AppCompatActivity {

    @BindView(R.id.emailsDbRecycler)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showemailsbase);

        ButterKnife.bind(this);
        
        initView();
        
        loadEmailBase();
    }

    private void loadEmailBase() {

        CollectionReference emailsBase = FirebaseFirestore.getInstance()
                .collection("emails");

        emailsBase.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        EventBus.getDefault().post(new EmailsDbLoadEvent(false, e.getMessage()));
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<Receiver> receiverList = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot:task.getResult())
                            {
                                Receiver receiver = documentSnapshot.toObject(Receiver.class);
                                receiverList.add(receiver);
                            }
                            EventBus.getDefault().post(new EmailsDbLoadEvent(true, receiverList));
                        }
                    }
                });

    }

    private void initView() {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void display(EmailsDbLoadEvent event)
    {
        if (event.isSuccess())
        {
            ShowEmailsBaseAdapter adapter = new ShowEmailsBaseAdapter(this, event.getReceiverList());
            recyclerView.setAdapter(adapter);
        }
        else
        {
            Toast.makeText(this, event.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}