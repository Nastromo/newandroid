package com.facelocation.testretrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FaceAPI {

//    @POST("api/auth/register")
//    Call<ResponseBody> registerUser(
//            @Body RegistrationBody body
//    );

    @POST("{user}")
    Call<ResponseBody> registerUser(
            @HeaderMap Map<String, String> headers,
            @Path("user") String userName,
            @Query("user") String user,
            @Query("passwd") String password,
            @Query("api-type") String type
    );
}

