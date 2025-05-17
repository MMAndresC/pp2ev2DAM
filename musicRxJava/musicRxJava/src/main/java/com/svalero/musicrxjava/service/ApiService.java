package com.svalero.musicrxjava.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svalero.musicrxjava.constants.Constants;
import com.svalero.musicrxjava.utils.ErrorLogger;
import com.svalero.musicrxjava.utils.SessionManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static Retrofit retrofit = null;

    public static Retrofit buildInstance(String ignored) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();

                        // Dynamically read token to include it in request header
                        String token = SessionManager.getInstance().getAuthToken();
                        Request.Builder requestBuilder = original.newBuilder();

                        if (token != null && !token.isEmpty()) {
                            requestBuilder.header("Authorization", token);
                        }

                        Request request = requestBuilder
                                .method(original.method(), original.body())
                                .build();

                        okhttp3.Response response = chain.proceed(request);
                        return response;
                    })
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.LOCAL_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
