package com.labawsrh.aws.trashtec.Interfaces;

import com.labawsrh.aws.trashtec.Models.Data;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiGraphFacebookServices {
    @GET("permissions")
    Call<Data> GetPermisos(@Query("access_token") String access_token);

    @DELETE("permissions/email")
    Call<Boolean> RevocarAccesoFacebook(@Query("access_token") String access_token);
}
