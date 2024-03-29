package com.metacodersbd.noteit;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacodersbd.noteit.models.testModel;
import com.metacodersbd.noteit.signFunction.signInPage;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class TakeNote extends AppCompatActivity {

    FirebaseAuth mauth  ;
    DatabaseReference  mref ;

    EditText titleEdit , descEIDT ;
    Button uploadBTn ;
    int clr = -10603087 ;

    String  uid ,  title , desc , date  , colorID  , POSTID = "-00";

    ProgressBar pbar ;
    SpectrumPalette spectrumPalette ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_note);

        mauth = FirebaseAuth.getInstance();
        uid = mauth.getUid() ;
        mref = FirebaseDatabase.getInstance().getReference("notes").child(uid);

        //init views
        titleEdit = findViewById(R.id.title);
        descEIDT = findViewById(R.id.descEdit);
        uploadBTn = findViewById(R.id.uplaodBtn);
        pbar = findViewById(R.id.pbar);
        spectrumPalette = findViewById(R.id.palette) ;
        pbar.setVisibility(View.GONE);


        spectrumPalette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {

                clr = color ;

             //   Toast.makeText(getApplicationContext() , "Error : " + clr , Toast.LENGTH_LONG).show();

                       // uploadBTn.setBackgroundColor(clr);

            }
        });


        uploadBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                colorID = String.valueOf(clr) ;
                // taking the date from the andorid Clock
                String delegate = "hh:mm aaa";

                String  Time = String.valueOf(DateFormat.format(delegate, Calendar.getInstance().getTime()));


                 String  DATE = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                 date = Time + " "+DATE;


                // getting the data from the views
                title = titleEdit.getText().toString().toUpperCase() ;
                desc = descEIDT.getText().toString();


                //database Ref


                if(TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)){

                    Toast.makeText(getApplicationContext() , "Fill Up The Data Properly" , Toast.LENGTH_SHORT)
                            .show();

                }
                else {


                    pbar.setVisibility(View.VISIBLE);
                    uploadDataToFireBase(title , desc , date , colorID);



                }






            }
        });







    }

    private void uploadDataToFireBase(String Title, String Desc , String Date , String color ) {



        String push_id = mref.push().getKey() ;

        HashMap map = new HashMap();

        map.put("title" , Title) ;
        map.put("desc" , Desc) ;
        map.put("date" ,Date ) ;
        map.put("color_id", color) ;
        map.put("id",push_id );

        mref.child(push_id).setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        pbar.setVisibility(View.GONE);
                        finish();


                    }
                }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                pbar.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Error : "+ e.getMessage() , Toast.LENGTH_LONG)
                        .show();


            }
        })  ;






    }

    public  void testDwld(){

        DatabaseReference mreftest = FirebaseDatabase.getInstance().getReference("counter");


                mreftest.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        testModel model = dataSnapshot.getValue(testModel.class) ;


                        titleEdit.setText(model.getKeepTrack());



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }) ;






    }


}
