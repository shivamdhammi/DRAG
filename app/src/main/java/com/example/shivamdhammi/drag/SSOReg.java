package com.example.shivamdhammi.drag;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SSOReg extends AppCompatActivity {

    private EditText Password,RePassword,SSOName,ISOnumber,Email,Address,Contact,AccountNo,State;
    private Button Register;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth,firebaseAuth;
    private FirebaseAuth mAuthListener;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssoreg);

        Password = (EditText)findViewById(R.id.id_password);
        RePassword = (EditText)findViewById(R.id.id_repassword);
        SSOName = (EditText)findViewById(R.id.id_ssoname1);
        ISOnumber = (EditText)findViewById(R.id.id_isonumber);
        Email = (EditText)findViewById(R.id.id_email);
        Address = (EditText)findViewById(R.id.id_address);
        Contact = (EditText)findViewById(R.id.id_contact1);
        Register = (Button)findViewById(R.id.id_register);
        AccountNo = (EditText)findViewById(R.id.id_account);
        State = (EditText)findViewById(R.id.id_state);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("SSO");

        mAuth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });


    }

    /*@Override
    public void onResume(){
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }*/



    private void addUser(){

        String password = Password.getText().toString().trim();
        String repassword = RePassword.getText().toString().trim();
        String ssoname = SSOName.getText().toString().trim();
        String isonumber = ISOnumber.getText().toString().trim();
        final String email = Email.getText().toString().trim();
        String address = Address.getText().toString().trim();
        String contact = Contact.getText().toString().trim();
        String accountno = AccountNo.getText().toString().trim();
        final String state = State.getText().toString().trim();

        if(!accountno.isEmpty()) {

            if (password.equals(repassword)) {

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    if (!contact.isEmpty() && (contact.length() == 10)) {

                        if (!ssoname.isEmpty()) {

                            if (!isonumber.isEmpty()) {

                                final SSOInfo info = new SSOInfo(ssoname, isonumber, email, address, contact,accountno,state);


                                //Log.d("shivam","Dhammi");

                                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        Log.d("dikkat100", mAuth.getCurrentUser().getUid());

                                        if (task.isSuccessful()) {

                                            myRef.child(state).child(mAuth.getCurrentUser().getUid()).setValue(info);


                                            /*firebaseAuth = FirebaseAuth.getInstance();
                                            mAuthListener = new FirebaseAuth.AuthStateListener() {
                                                @Override
                                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                                    if (user != null) {
                                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                .setDisplayName(state).build();
                                                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d("Display name: ", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            };*/

                                            /*FirebaseUser user = mAuth.getCurrentUser();

                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(state)
                                                    .build();

                                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.d("display name ", "updation successfull");
                                                    }
                                                }
                                            });*/

                                            Toast.makeText(getApplicationContext(), "Registered Successfully..", Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(getApplicationContext(), SProfileEdit.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }

                                        else {

                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                Toast.makeText(getApplicationContext(), "Email is already registered", Toast.LENGTH_LONG).show();
                                            } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                                Toast.makeText(getApplicationContext(), "Password is too weak", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    }
                                });

                            } else {
                                ISOnumber.setError("Please enter ISO number");
                                ISOnumber.requestFocus();
                                return;
                            }
                        } else {
                            SSOName.setError("Please Enter the SSO name.");
                            SSOName.requestFocus();
                            return;
                        }
                    } else {
                        Contact.setError("Please enter the mobile number");
                        Contact.requestFocus();
                        return;
                    }
                } else {
                    Email.setError("Enter a valid Email");
                    Email.requestFocus();
                    return;
                }
            } else {
                RePassword.setError("Password didn't match. Try again.");
                RePassword.requestFocus();
                return;
            }
        }else {
            AccountNo.setError("Please Enter the Account Number");
            AccountNo.requestFocus();
            return;
        }

        }


    }


