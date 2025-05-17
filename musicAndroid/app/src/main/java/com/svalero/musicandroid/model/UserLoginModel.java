package com.svalero.musicandroid.model;

import com.svalero.musicandroid.api.ArtistsApi;
import com.svalero.musicandroid.api.ArtistsApiInterface;
import com.svalero.musicandroid.contract.UserLoginContract;
import com.svalero.musicandroid.domain.TokenResponse;
import com.svalero.musicandroid.domain.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginModel implements UserLoginContract.Model {

    @Override
    public void loginUser(User user, OnLoginUserListener listener) {
        ArtistsApiInterface artistsApi = ArtistsApi.buildInstance();
        Call<TokenResponse> getLoginCall = artistsApi.loginUser(user);
        getLoginCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                int statusCode = response.code();
                if (statusCode >= 200 && statusCode < 300) {
                    listener.onLoginUserSuccess(response.body());
                } else if (statusCode >= 400 && statusCode < 500) {
                    listener.onLoginUserError("Client error: " + statusCode);
                } else if (statusCode >= 500 && statusCode < 600) {
                    listener.onLoginUserError("Server error: " + statusCode + " The API is not available.  Please try again");
                } else {
                    listener.onLoginUserError("Unexpected response: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                listener.onLoginUserError("Request failed: " + t.getMessage());
            }
        });
    }
}

