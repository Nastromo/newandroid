package com.face_location.facelocation.model;

import com.face_location.facelocation.model.DeviceToken.DeviceTokenBody;
import com.face_location.facelocation.model.Events.MyEventResponse;
import com.face_location.facelocation.model.GetEvent.EventResponse;
import com.face_location.facelocation.model.GetMainChat.MainChatResponse;
import com.face_location.facelocation.model.GetNearestEvents.NearestEventResponse;
import com.face_location.facelocation.model.Issue.IssueBody;
import com.face_location.facelocation.model.Location.LocationBody;
import com.face_location.facelocation.model.Location.LocationGetResponse;
import com.face_location.facelocation.model.Location.LocationPutResponse;
import com.face_location.facelocation.model.Location.LocationResponse;
import com.face_location.facelocation.model.MyProfile.ProfileBody;
import com.face_location.facelocation.model.MyProfile.ProfileResponse;
import com.face_location.facelocation.model.PostChat.ChatBody;
import com.face_location.facelocation.model.PostChat.ChatResponse;
import com.face_location.facelocation.model.PostEvent.EventBody;
import com.face_location.facelocation.model.PostLocalization.LocalizationBody;
import com.face_location.facelocation.model.PostLocalization.LocalizationResponse;
import com.face_location.facelocation.model.Registration.RegistrationBody;
import com.face_location.facelocation.model.Registration.RegistrationResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 26.11.17.
 */

public interface FacelocationAPI {

    //Registration
    @POST("api/auth/register")
    Call<RegistrationResponse> registerUser(
            @HeaderMap Map<String, String> headers,
            @Body RegistrationBody body
    );

    //Login
    @POST("api/auth/login")
    Call<RegistrationResponse> loginUser(
            @HeaderMap Map<String, String> headers,
            @Body RegistrationBody body
    );


    //New LocationForAdapter
    @POST("api/locations")
    Call<LocationResponse> addLocation(
            @HeaderMap Map<String, String> headers,
            @Body LocationBody body
    );

    //Search LocationForAdapter
    @GET("api/locations")
    Call <List<LocationGetResponse>> searchLocation(
            @HeaderMap Map<String, String> headers
    );

    //Upload User Avatar
    @Multipart
    @POST("api/profile/avatar")
    Call <ResponseBody> uploadAvatar(
            @HeaderMap Map<String, String> header,
            @Part MultipartBody.Part file
            );

    //Update My Profile
    @POST("api/profile")
    Call<ProfileResponse> updateMyProfile(
            @HeaderMap Map<String, String> headers,
            @Body ProfileBody body
    );

    //My Profile
    @GET("api/profile")
    Call <ProfileResponse> getMyProfile(
            @HeaderMap Map<String, String> headers
    );

    //My Events
    @GET("api/events")
    Call <List<MyEventResponse>> getMyEvents(
            @HeaderMap Map<String, String> headers,
            @Query("user") String userID
    );

    //My Visited Events
    @GET("api/events")
    Call <List<MyEventResponse>> getMyVisitedEvents(
            @HeaderMap Map<String, String> headers,
            @Query("subscriber") String userID
    );

    //My Locations
    @GET("api/locations")
    Call <List<LocationGetResponse>> getMyLocations(
            @HeaderMap Map<String, String> headers,
            @Query("user") String userID
    );

    //Update My Location
    @PUT("api/locations/{locationID}")
    Call <LocationPutResponse> updateLocation(
            @HeaderMap Map<String, String> headers,
            @Path("locationID") String locationID,
            @Body LocationBody body
    );

    //Get My Event
    @GET("api/events/{eventID}")
    Call<EventResponse> getMyEvent(
            @HeaderMap Map<String, String> headers,
            @Path("eventID") String eventID
    );

    //Get Similar Events
    @GET("api/events/")
    Call <List<EventResponse>> getSimilarEvent(
            @HeaderMap Map<String, String> headers,
            @Query("limit") int limit,
            @Query("exclude") String eventID,
            @Query("start") String date,
            @Query("type") String type
    );

    //Create new Event
    @POST("api/events")
    Call<com.face_location.facelocation.model.PostEvent.EventResponse> createEvent(
            @HeaderMap Map<String, String> headers,
            @Body EventBody body
    );

    //Upload Event Cover
    @Multipart
    @POST("api/events/{eventID}/upload-cover")
    Call <ResponseBody> uploadEventCover(
            @HeaderMap Map<String, String> header,
            @Path("eventID") String eventID,
            @Part MultipartBody.Part file
    );

    //Send Issue Report
    @POST("api/chat")
    Call<ResponseBody> sendIssue(
            @HeaderMap Map<String, String> headers,
            @Body IssueBody body
    );

    //Get events coordinates
    @GET("api/events/")
    Call <List<NearestEventResponse>> getNearestEvents(
            @HeaderMap Map<String, String> headers,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("published") boolean published
    );

    //Localize User on Event
    @POST("api/events/{eventID}/activate")
    Call <List<LocalizationResponse>> localizUser(
            @HeaderMap Map<String, String> headers,
            @Path("eventID") String eventID,
            @Body LocalizationBody body
    );

    //Ban user on event
    @GET("api/events/{eventID}/ban/{userID}")
    Call <ResponseBody> userBan(
            @HeaderMap Map<String, String> headers,
            @Path("eventID") String eventID,
            @Path("userID") String userID
    );

    //Get Main Event Chat
    @GET("api/chat")
    Call <List<MainChatResponse>> getChat(
            @HeaderMap Map<String, String> headers,
            @Query("event") String eventID
    );

    //Create chat
    @POST("api/chat")
    Call<ChatResponse> createChat(
            @HeaderMap Map<String, String> headers,
            @Body ChatBody body
    );

    //Get Main Event Chat
    @GET("api/chat/")
    Call <List<MainChatResponse>> getChatByID(
            @HeaderMap Map<String, String> headers,
            @Query("chatID") String chatID
    );

    //Send Device Token
    @POST("api/profile/device-token")
    Call<ResponseBody> sendDeviceToken(
            @HeaderMap Map<String, String> headers,
            @Body DeviceTokenBody body
    );


}
