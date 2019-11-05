package com.kyudong.termi;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Kyudong on 16. 11. 21..
 */

///////////////////// 메시지를 생성하고 서버에 보내는 서비스 /////////////////////////
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebasIIDService";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private String json;
    private String refreshedToken;

    //어플이 처음 실행될 때 자동적으로 실행되는 메소드, 토큰을 등록하는데 사용한다.
    @Override
    public void onTokenRefresh() {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
    }

    //Server에 생성된 토큰을 등록하기 위해 보낼 때 사용하는 메소드
    public void sendRegistrationToServer(String fcm_token, String auth) {
        JSONObject Jobj = new JSONObject();
        try {
            Jobj.put("token", fcm_token);
            json = Jobj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FCMPost fcmPost = new FCMPost();
        try {
            fcmPost.doPostRequest("http://52.78.240.168/api/push/refreshToken", json, auth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRegistrationToMain(String token1) {
        return token1;
    }

    private class FCMPost {

        String doPostRequest(String url, String json, String auth_token) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .header("Content-Type", "application/json")
                    .addHeader("authorization", auth_token)
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }
                @Override
                public void onResponse(Response response) throws IOException {

                }
            });
            return null;
        }
    }

}
