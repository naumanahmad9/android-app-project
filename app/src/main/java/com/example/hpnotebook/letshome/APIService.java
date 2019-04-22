package com.example.hpnotebook.letshome;

import com.example.hpnotebook.letshome.Notifications.MyResponse;
import com.example.hpnotebook.letshome.Notifications.Sender;
import com.koddev.chatapp.Notifications.MyResponse;
import com.koddev.chatapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AIzaSyAsOjRU_ti8QLPwLBFuBxJyBgSnH_roj8s"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
