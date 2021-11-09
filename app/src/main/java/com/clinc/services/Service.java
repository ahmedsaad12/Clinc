package com.clinc.services;


import com.clinc.models.InstructionsModel;
import com.clinc.models.MessageModel;
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
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    
    @POST("/visits/chat.asmx/get_chat")
    Call<List<MessageModel>> getMessge(
            @Query("patient_id") String patient_id
    );
    
    @POST("visits/chat.asmx/add_msg")
    Call<ResponseBody> sendmessagetext
            (
                    @Query("patient_id") String patient_id,
                    @Query("msg") String msg,
                    @Query("key") String key

//
            );
    
    @POST("/settings/clinic_system_service.asmx/get_clinic_system_content")
    Call<List<SystemModel>> getSystem
            (


//
            );
    
    @POST("/assistant/check_user.asmx/get_users")
    Call<List<UserModel>> login(
            @Query("user_name") String user_name,
            @Query("pass") String pass

    );
    
    @POST("/assistant/get_reservation.asmx/get_times")
    Call<List<TimeModel>> getTimes(
            @Query("date") String date


    );
    @POST("assistant/add_reservation.asmx/create_reservation")
    Call<ResponseBody> book
            (
                    @Query("file_id") String file_id,
                    @Query("user_name") String user_name,
                    @Query("pass") String pass,
                    @Query("time") String time,
                    @Query("date") String date

//
            );
    @POST("/assistant/get_notifications.asmx")
    Call<List<SystemModel>> getNotif
            (


//
            );
    @POST("/settings/articles_service.asmx/get_one_article")
    Call<List<InstructionsModel>> getinstruction
            (


//
            );
}