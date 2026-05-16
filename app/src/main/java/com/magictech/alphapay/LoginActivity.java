package com.magictech.alphapay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginEmail, etLoginPassword;
    private Button btnLogin;
    private ApiService apiService;

    // SharedPreferences key identifiers for storing string tokens
    private static final String PREF_NAME = "AlphaPayPrefs";
    private static final String KEY_TOKEN = "jwt_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etLoginEmail.getText().toString().trim();
                String password = etLoginPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                } else {
                    executeLogin(email, password);
                }
            }
        });
    }

    private void executeLogin(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<String> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String jwtToken = response.body();

                    // ===> SAVING JWT TOKEN TO SHAREDPREFERENCES <===
                    SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_TOKEN, jwtToken);
                    editor.apply(); // Writes token strings asynchronously to disk storage

                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Log.d("ALPHA_PAY_JWT", "Saved Token Successfully: " + jwtToken);

                    // Token successfully saved, now launch the dashboard application node
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish(); // Destroys LoginActivity layout memory so back key won't loop back to login
                    // Iske baad hum intent chalakar seedha Dashboard Activity par bhej sakte hain
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Connection Mismatch: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}