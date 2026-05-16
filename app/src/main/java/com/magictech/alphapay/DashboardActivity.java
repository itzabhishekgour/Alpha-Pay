package com.magictech.alphapay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWalletBalance, tvWelcomeUser;
    private Button btnRefreshBalance, btnSendMoney;
    private ApiService apiService;

    private static final String PREF_NAME = "AlphaPayPrefs";
    private static final String KEY_TOKEN = "jwt_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Handles full screen rendering parameters for immersive displays
        getWindow().setFlags(
                android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_dashboard);

        // UI Binding
        tvWalletBalance = findViewById(R.id.tvWalletBalance);
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        btnRefreshBalance = findViewById(R.id.btnRefreshBalance);
        btnSendMoney = findViewById(R.id.btnSendMoney);

        // Network Client Initialization
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Initial Fetch
        fetchLiveBalance();

        // Refresh Button functionality
        btnRefreshBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLiveBalance();
            }
        });
    }

    private void fetchLiveBalance() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedToken = prefs.getString(KEY_TOKEN, null);

        if (savedToken == null) {
            Toast.makeText(this, "Session Expired! Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Standard HTTP JWT Format requirement: "Bearer <token_string>"
        String authorizationHeader = "Bearer " + savedToken;

        // Pass dynamic or static testing userId '1L' as query param
        Call<WalletResponse> call = apiService.getWalletBalance(authorizationHeader, 1L);
        call.enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WalletResponse wallet = response.body();

                    // Formatting double digits to currency structure
                    tvWalletBalance.setText(String.format("₹ %.2f", wallet.getBalance()));
                    tvWelcomeUser.setText("Account Verified Live!");
                    Log.d("ALPHA_DASHBOARD", "Balance fetched: " + wallet.getBalance());
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to get balance. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("ALPHA_DASHBOARD", "Error response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ALPHA_DASHBOARD", "Failed network callback trace: ", t);
            }
        });
    }
}