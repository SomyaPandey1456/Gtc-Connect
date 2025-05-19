package com.example.gtcconnect;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @POST("/users/register")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/users/login")
    Call<Void> executeLogin(@Body HashMap<String, String> map);

    // Add Division Details
    @Multipart
    @POST("/division/create")
    Call<ResponseBody> createDivision(
            @Part MultipartBody.Part gsImage,
            @Part MultipartBody.Part csImage,
            @Part MultipartBody.Part jsImage,
            @Part MultipartBody.Part ciImage,
            @Part MultipartBody.Part aciImage,
            @Part MultipartBody.Part atciImage,
            @Part MultipartBody.Part tresImage,
            @Part("div_name") RequestBody divName,
            @Part("gs_name") RequestBody gsName,
            @Part("cs_name") RequestBody csName,
            @Part("js_name") RequestBody jsName,
            @Part("ci_name") RequestBody ciName,
            @Part("aci_name") RequestBody aciName,
            @Part("atci_name") RequestBody atciName,
            @Part("tres_name") RequestBody tresName
    );

    @GET("division/getAllDivisions")
    Call<List<String>> getDivisions();

    @Multipart
    @POST("club/create")
    Call<ResponseBody> createClub(
            @Part MultipartBody.Part club_image,
            @Part MultipartBody.Part head1_image,
            @Part MultipartBody.Part head2_image,
            @Part("club_name") RequestBody club_name,
            @Part("div_name") RequestBody div_name,
            @Part("club_desc") RequestBody club_desc,
            @Part("head1_name") RequestBody head1_name,
            @Part("head2_name") RequestBody head2_name
    );

    @GET("division/getClubsByDivision/{divisionName}")
    Call<List<Club>> getClubsByDivision(@Path("divisionName") String divisionName);

    @GET("club/getClubById/{clubId}")
    Call<Club> getClubDetailsById(@Path("clubId") String clubId);

    // Add Event with Poster
    @Multipart
    @POST("events/create")
    Call<ResponseBody> createEvent(
            @Part MultipartBody.Part event_poster,  // Image file for the event poster
            @Part("eventName") RequestBody eventName,
            @Part("eventVenue") RequestBody eventVenue,
            @Part("eventDate") RequestBody eventDate,
            @Part("eventTime") RequestBody eventTime,
            @Part("eventDescription") RequestBody eventDescription,
            @Part("chiefGuest") RequestBody chiefGuest, // Can be empty or null
            @Part("clubId") RequestBody clubId,
            @Part("eventType") RequestBody eventType
    );
}
