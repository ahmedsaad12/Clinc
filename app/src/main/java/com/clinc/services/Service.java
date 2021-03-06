package com.clinc.services;


import com.clinc.models.InstructionsModel;
import com.clinc.models.MessageModel;
import com.clinc.models.ProfileModel;
import com.clinc.models.ReservisionModel;
import com.clinc.models.SystemModel;
import com.clinc.models.TimeModel;
import com.clinc.models.UserModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Query;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    
    @GET("/visits/chat.asmx/get_chat")
    Call<List<MessageModel>> getMessge(
            @Query("patient_id") String patient_id
    );
    
    @GET("visits/chat.asmx/add_msg")
    Call<ResponseBody> sendmessagetext
            (
                    @Query("patient_id") String patient_id,
                    @Query("msg") String msg,
                    @Query("key") String key

//
            );
    
    @GET("/settings/clinic_system_service.asmx/get_clinic_system_content")
    Call<List<SystemModel>> getSystem
            (


//
            );
    
    @GET("/assistant/check_user.asmx/get_users")
    Call<List<UserModel>> login(
            @Query("user_name") String user_name,
            @Query("pass") String pass

    );
    
    @GET("/assistant/get_reservation.asmx/get_times")
    Call<List<TimeModel>> getTimes(
            @Query("date") String date


    );
    @GET("assistant/add_reservation.asmx/create_reservation")
    Call<ResponseBody> book
            (
                    @Query("file_id") String file_id,
                    @Query("user_name") String user_name,
                    @Query("pass") String pass,
                    @Query("time") String time,
                    @Query("date") String date

//
            );
    @GET("/assistant/get_notifications.asmx/get_notification")
    Call<List<SystemModel>> getNotif
            (


//
            );
    @GET("/settings/articles_service.asmx/get_all_articles")
    Call<List<InstructionsModel>> getinstruction
            (


//
            );
    @GET("/assistant/get_profile.asmx/get_info")
    Call<List<ProfileModel>> getProfile
            (
                    @Query("file_id") String file_id

//
            );
    @GET("/assistant/get_reservation.asmx/get_patient_reservation")
    Call<List<ReservisionModel>> getReserv(
            @Query("user_name") String user_name,
            @Query("pass") String pass

    );
}