package com.example.agapelife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.agapelife.networking.pojos.AgapeUserRequest;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.AnimationsConfig;
import com.example.agapelife.utils.FirebaseConfig;
import com.example.agapelife.utils.PreferenceStorage;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    Button btnSignUp, btnLogin;
    ConstraintLayout signUpLayout;
    AnimationsConfig animationsConfig = new AnimationsConfig();

    TextInputEditText inputEmail, inputPassword, inputConfirmPassword, inputFirstName, inputLastName, inputIdNumber, inputPhoneNumber;
    String username, phone_number, id_number, passwordtxt, passwordtxt2, emailtxt, first_name, last_name;

    PreferenceStorage preferenceStorage;

    final static int DOCUMENT_CODE = 177;

    FirebaseConfig firebaseConfig = new FirebaseConfig(SignUpActivity.this);
    ProgressDialog progress;


    @Override
    public void onStart() {
        super.onStart();
//       if(firebaseConfig.userIsLoggedIn()){
//           Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//           startActivity(intent);
//       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        preferenceStorage = new PreferenceStorage(this);

        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_signup);
        signUpLayout = findViewById(R.id.signup_layout);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.input_password);
        inputConfirmPassword = findViewById(R.id.input_confirm_password);
        inputFirstName = findViewById(R.id.first_name);
        inputLastName = findViewById(R.id.l_name);
        inputIdNumber = findViewById(R.id.id_number);
        inputPhoneNumber = findViewById(R.id.phone_number);

        progress = new ProgressDialog(this);


        //animationsConfig.createGradientAnimation(signUpLayout, GlobalVariables.ENTER_FADE_DURATION, GlobalVariables.EXIT_FADE_DURATION);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginSignUpActivity.class);
                startActivity(intent);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs(inputEmail, inputPassword, inputConfirmPassword, inputFirstName, inputLastName, inputIdNumber, inputPhoneNumber);

            }
        });


    }


    private void validateInputs(TextInputEditText inputEmail, TextInputEditText inputPassword, TextInputEditText confirmPassword, TextInputEditText firstName, TextInputEditText lastName, TextInputEditText idNumber, TextInputEditText phoneNumber) {
        username = firstName.getText().toString().trim();
        phone_number = phoneNumber.getText().toString().trim();
        id_number = idNumber.getText().toString().trim();
        passwordtxt = inputPassword.getText().toString().trim();
        passwordtxt2 = confirmPassword.getText().toString().trim();
        emailtxt = inputEmail.getText().toString().trim();
        first_name = firstName.getText().toString().trim();
        last_name = lastName.getText().toString().trim();

        boolean result = true;

        if(emailtxt.isEmpty()){
            inputEmail.setError("Please enter your email");
            result=false;
        }
        else if(passwordtxt.isEmpty()){
            inputEmail.setError("Please enter a password");
            result = false;
        }
        else if(!passwordtxt.contentEquals(passwordtxt2.trim())){
            confirmPassword.setError("Password did not match");
            result = false;
        }

        else if(last_name.isEmpty()){
            lastName.setError("Please input your last name");
        }
        else if(id_number.isEmpty()){
            idNumber.setError("Please input your ID Number");
        }
        else if(phone_number.isEmpty()){
            phoneNumber.setError("Please input your phone number");
        }

        else{
            //firebaseConfig.createNewUser(inputEmail.getText().toString(), inputPassword.getText().toString(), "Signing you up...");
            Toast.makeText(this, id_number, Toast.LENGTH_SHORT).show();
            signUpForm(username, phone_number, id_number, passwordtxt, passwordtxt2, emailtxt, first_name, last_name);
        }

    }

    private void signUpForm(String username, String phone_number, String id_number, String passwordtxt, String passwordtxt2, String emailtxt, String first_name, String last_name) {
        progress.setMessage("Signing you up");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        Call<AgapeUserRequest>  call = ServiceGenerator.getInstance().getApiConnector().signUpForm(
                username,
                phone_number,
                id_number,
                passwordtxt,
                passwordtxt2,
                emailtxt,
                first_name,
                last_name
        );

        signUpUser(id_number);

        call.enqueue(new Callback<AgapeUserRequest>() {
            @Override
            public void onResponse(Call<AgapeUserRequest> call, Response<AgapeUserRequest> response) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                Fragment homeFragment = new HomeFragment();
                String message = "Something went wrong, please try again";
                progress.hide();
                Log.d("SIGNUP_RESPONSE", String.valueOf(response.code()));

                if(response.code() == 201){
                    message = "Sign Up was successful";
                    Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();

//                    Bundle bundle = new Bundle();
//                    bundle.putString("ID_NUMBER", id_number);
//                    HomeFragment homeFragment = new HomeFragment();
//                    homeFragment.setArguments(bundle);

                    preferenceStorage.saveUserId(id_number);
                    preferenceStorage.setLoggedInStatus(true);
                    preferenceStorage.saveUserFirstName(first_name);
                    preferenceStorage.saveLoginData(first_name, passwordtxt);

                    Intent intent = new Intent(SignUpActivity.this, UserMainActivity.class);
                    intent.putExtra("FIRST_NAME", first_name);
                    startActivity(intent);
                    finish();

                }
            }
            // not permitted by security policy
            @Override
            public void onFailure(Call<AgapeUserRequest> call, Throwable t) {
                progress.hide();
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void signUpUser(String id_number) {
    }


}