package com.kyudong.termi;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Kyudong on 16. 11. 21..
 */

///////////////////// 메시지를 생성하고 서버에 보내는 서비스 /////////////////////////
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebasIIDService";

    //어플이 처음 실행될 때 자동적으로 실행되는 메소드, 토큰을 등록하는데 사용한다.
    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e(TAG, "Refreshed token: " + refreshedToken);

        getRegistrationToMain(refreshedToken);
        //sendRegistrationToServer(refreshedToken);
    }

    //Server에 생성된 토큰을 등록하기 위해 보낼 때 사용하는 메소드
    private void sendRegistrationToServer(String token) {
        Log.e(TAG, "new token: " + token);
        //통신하세요

    }

    private String getRegistrationToMain(String token1) {
        return token1;
    }

}
