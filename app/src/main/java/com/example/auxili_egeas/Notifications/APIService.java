package com.example.auxili_egeas.Notifications;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.Call;

public interface APIService {

    @Headers (
        {
                "Content-Type:application/json",
                "Authorization:key=AAAAiIHZ5EY:APA91bHeKhl876FZjaW08g2jnCF9ptaN9FcWT--XQz1FthsQ1Rn5jNjcpdHYaL8RrS0-jkAqlzGZRSF-KHCzjIGsvoAfMJ8D9QdkMcCSLu28nlWTNgVn7KmTzkRw3nVew4jRUhsqJfgx"
        }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
