package com.carehack.medivault;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by user on 11/25/2017.
 */

public class SharedKeyManager {
    String phone;
    String sharedKey;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedKeyManager(Activity activity) {
        this.firebaseDatabase=FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();
        this.sharedPreferences = activity.getSharedPreferences(Utils.pref, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }


    public void assignNewSharedKey(final String phone)
    {
        databaseReference.child("Shared Key").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tempKey=Utils.randomSequenceGenerator(15);
                while(dataSnapshot.hasChild(phone) && dataSnapshot.child(phone).getValue().toString().equals(tempKey))
                {
                    tempKey=Utils.randomSequenceGenerator(15);
                }
                databaseReference.child("Shared Key").child(phone).setValue(tempKey);
                editor.putString("sharedkey",tempKey);
                editor.commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSharedKey() {
        return sharedKey;
    }

    public void setSharedKey(String sharedKey) {
        this.sharedKey = sharedKey;
    }


}
