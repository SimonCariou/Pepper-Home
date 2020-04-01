package com.simoncariou.pepperhome;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LightService {
    @PUT("api/{API_KEY}/groups/{light_id}/action")
    Call<List<ResponseBody>> turnLightsOnOff(@Path("API_KEY") String api_key, @Path("light_id") String id, @Body Action action);
}
