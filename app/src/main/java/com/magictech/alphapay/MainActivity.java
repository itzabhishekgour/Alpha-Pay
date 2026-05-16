package com.magictech.alphapay;

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

public class MainActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword;
    private Button btnSignup;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Initializations
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);

        // Network API Client Initialization
        apiService = RetrofitClient.getClient().create(ApiService.class);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    executeSignup(name, email, password);
                }
            }
        });
    }

    private void executeSignup(String name, String email, String password) {
        UserRequest request = new UserRequest(name, email, password);
        Call<UserResponse> call = apiService.registerUser(request);

        // Async execution so the mobile main UI thread doesn't freeze
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    Toast.makeText(MainActivity.this, "Registration Success! User ID: " + userResponse.getId(), Toast.LENGTH_LONG).show();
                    Log.d("ALPHA_PAY_SUCCESS", "User saved with email: " + userResponse.getEmail());
                } else {
                    Toast.makeText(MainActivity.this, "Registration Failed! Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("ALPHA_PAY_ERROR", "Error body code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ALPHA_PAY_FAILURE", "Failure log trace: ", t);
            }
        });
    }
}