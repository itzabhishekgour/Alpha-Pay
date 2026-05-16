package com.magictech.alphapay;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Relative path matching your API gateway routing structure
    @POST("alphapay/user/register")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);

    // Endpoint path mapping for authentication route
    // It accepts raw email/password strings and returns a raw string JWT token text
    @POST("alphapay/user/login")
    Call<String> loginUser(@Body LoginRequest loginRequest);

    @GET("alphapay/wallet/{userId}/balance")
    Call<WalletResponse> getWalletBalance(
            @Header("Authorization") String token,
            @Path("userId") Long userId //
    );
}
