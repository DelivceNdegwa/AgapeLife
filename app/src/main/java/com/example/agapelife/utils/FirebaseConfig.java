package com.example.agapelife.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.agapelife.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseConfig {
    Context context;
    FirebaseAuth mAuth;

    public FirebaseConfig(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean userIsLoggedIn(){
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
            return true;
        }
        return false;
    }

    public void createNewUser(String email, String password, String progressMessage) {
        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage(progressMessage);
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        progress.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(":::task.isSuccessful:::", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(context, "Sign up was successful, welcome",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, UserMainActivity.class);
                            intent.putExtra("USER_EMAIL", email);
                            intent.putExtra("USER_PASSWORD", password);
                            context.startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(":::task.unSuccessful:::", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Sign up was successful",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, UserMainActivity.class);
                            context.startActivity(intent);

                        }
                    }
                });
    }

}
