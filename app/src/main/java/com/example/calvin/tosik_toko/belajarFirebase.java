package com.example.calvin.tosik_toko;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class belajarFirebase extends AppCompatActivity {
    TextView btnmember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_belajar_firebase);

//         Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("toko");

        String email = "calvin@gmail.com";
        Query query = ref.orderByChild("username").equalTo(email);

          btnmember = (TextView) findViewById(R.id.firebase1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String  username = data.child("username").getValue(String.class);
                        btnmember.setText(username);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }
}
