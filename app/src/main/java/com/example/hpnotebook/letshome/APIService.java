package com.example.hpnotebook.letshome;

import com.example.hpnotebook.letshome.Notifications.MyResponse;
import com.example.hpnotebook.letshome.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA64vb9uU:APA91bF0h7rdY-aVqioT_RC1557vRP2oC3rX-mWLkB8nPDNRmnwsBJnvE-VYkhIx5lt2k3nIiQe5wW7qy8dvvistgv_FbK_R8KODhGWde6Ag4hLTc4eQRKyJRTRu-2PVjcmyZR9Z36pj"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
