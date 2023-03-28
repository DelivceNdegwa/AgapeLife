package com.example.agapelife.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.agapelife.R;
import com.example.agapelife.UserMainActivity;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.AnimationsConfig;
import com.example.agapelife.utils.PreferenceStorage;
import com.google.android.material.textfield.TextInputEditText;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginSignUpActivity extends AppCompatActivity {
    ConstraintLayout loginLayout;
    AnimationsConfig layoutGradientConfig;
    Button loginButton, btnSignUp;

    ProgressDialog progress;
    TextInputEditText inputFname, inputPassword;

    PreferenceStorage preferenceStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        inputFname = findViewById(R.id.input_uname);
        inputPassword = findViewById(R.id.input_password);
        loginLayout = findViewById(R.id.login_layout);
//        layoutGradientConfig = new AnimationsConfig();
//        layoutGradientConfig.createGradientAnimation(loginLayout, GlobalVariables.ENTER_FADE_DURATION, GlobalVariables.EXIT_FADE_DURATION);

        loginButton = findViewById(R.id.login_button);
        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSignUpActivity.this, SignUpChoice.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLoginInput(inputFname.getText().toString(), inputPassword.getText().toString());
            }
        });

    }

    private void validateLoginInput(String username, String password) {
        if(username.trim().isEmpty()){
            inputFname.setError("Please insert your username");
        }
//        else if(!email.trim().contains("@")){
//            inputFname.setError("Invalid email");
//        }
        else if(password.trim().isEmpty()){
            inputPassword.setError("Please insert your password");
        }
        else{
            authenticateUser(username, password);
        }
    }

    private void authenticateUser(String username, String password) {
//        AgapeUserResponse loginRequestObject = new AgapeUserResponse(username, password);
        AgapeUserResponse request = new AgapeUserResponse(username, password);
        Call<AgapeUserResponse> call = ServiceGenerator.getInstance().getApiConnector().loginForm(request);

        call.enqueue(new Callback<AgapeUserResponse>() {
            @Override
            public void onResponse(Call<AgapeUserResponse> call, Response<AgapeUserResponse> response) {

                if(response.code() == 200){
                    Toast.makeText(LoginSignUpActivity.this, "Sign in was successful", Toast.LENGTH_SHORT).show();

                    preferenceStorage.saveLoginData(
                            username,
                            password
                    );

                    preferenceStorage.setLoggedInStatus(true);

                    Intent intent = new Intent(LoginSignUpActivity.this, UserMainActivity.class);
                    intent.putExtra("FIRST_NAME", username);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginSignUpActivity.this, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AgapeUserResponse> call, Throwable t) {
                Log.d("LOGIN_LOG:::::::", t.getMessage());
                Toast.makeText(LoginSignUpActivity.this, "Check your connection and try again", Toast.LENGTH_LONG).show();
            }
        });

    }



//    private void authenticateUser(String email, String password) {
//
//        showProgressBar("Signing you in...");
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        progress.dismiss();
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(":::task.isSuccessful:::", "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Intent intent = new Intent(LoginSignUpActivity.this, UserMainActivity.class);
//                            intent.putExtra("USER_EMAIL", email);
//                            intent.putExtra("USER_PASSWORD", password);
//                            startActivity(intent);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("task.unSuccessful", "signInWithEmail:failure", task.getException());
////                            Toast.makeText(LoginSignUpActivity.this, "We could not log you in, please try again",
////                                    Toast.LENGTH_SHORT).show();
//
//                            Toast.makeText(LoginSignUpActivity.this, "Sign up was successful",
//                                    Toast.LENGTH_SHORT).show();
//
//                            Intent intent = new Intent(LoginSignUpActivity.this, UserMainActivity.class);
//                            startActivity(intent);
//                        }
//                    }
//                });
//
//    }

    private void showProgressBar(String message) {
        progress = new ProgressDialog(this);
        progress.setMessage(message);
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        progress.show();
    }


}