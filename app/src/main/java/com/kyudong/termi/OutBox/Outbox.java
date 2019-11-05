package com.kyudong.termi.OutBox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyudong.termi.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Outbox extends Fragment {

    private RecyclerView outRv;
    private LinearLayoutManager llm;
    private OutBoxRvAdapter adapter;
    private ArrayList<OutBoxRvItem> outBoxRvItemArrayList = new ArrayList<>();

    private static final int REQUEST_CODE = 1003;
    private static final int REQUEST_CODE2 = 1004;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.outbox,container,false);

        token = getActivity().getIntent().getStringExtra("authorization");

        outRv = (RecyclerView) v.findViewById(R.id.outRecyclerView);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new OutBoxRvAdapter(getContext(), outBoxRvItemArrayList, outRv, token);

        outRv.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        outRv.setLayoutManager(llm);
        outRv.setAdapter(adapter);
        outRv.setHasFixedSize(true);

        GetInBox getInBox = new GetInBox();
        try {
            getInBox.doGetRequest("http://52.78.240.168/api/messageList/send");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(data != null) {
                int pos = data.getExtras().getInt("pos");
                String aa = data.getStringExtra("a");
                int a = data.getExtras().getInt("int");
                if(aa.equals("b")) {
////////////////////////////////////////////방법 1//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    outBoxRvItemArrayList.remove(pos);
                    adapter.notifyItemRemoved(pos);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
               /// 백버튼, 백 , 답장하기 때 ////
                } else if(aa.equals("a")) {
                    int diff = data.getExtras().getInt("diff");
                    if(diff==0) {           // 그냥 백버튼 백일 때
                    } else if(diff==1) {    // 답장하기이면
                        outBoxRvItemArrayList.clear();
                        GetInBox getInBox = new GetInBox();
                        try {
                            getInBox.doGetRequest("http://52.78.240.168/api/messageList/send");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if(requestCode == REQUEST_CODE2) {
                outBoxRvItemArrayList.clear();
                GetInBox getInBox = new GetInBox();
                try {
                    getInBox.doGetRequest("http://52.78.240.168/api/messageList/send");
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } else {
            outBoxRvItemArrayList.clear();
            GetInBox getInBox = new GetInBox();
            try {
                getInBox.doGetRequest("http://52.78.240.168/api/messageList/send");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            //mDivider = ResourcesCompat.getDrawable(context.getResources(), R.drawable.line_divider, context.getTheme());
            mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    public class GetInBox {

        String doGetRequest(String url) throws IOException {

            Request request = new Request.Builder()
                    .addHeader("authorization", token)
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {

                    final String res = response.body().string();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject parentJObject = new JSONObject(res);
                                JSONArray parentJArray = parentJObject.getJSONArray("messageData");

                                for(int i = 0; i <parentJArray.length(); i++) {
                                    OutBoxRvItem item = new OutBoxRvItem();

                                    JSONObject child = parentJArray.getJSONObject(i);

                                    item.text = child.getString("txContent");
                                    item.time = child.getString("dtRegTime");
                                    //item.isRead = child.getString("enIsRead");
                                    item.seqNo = child.getInt("nSeqNo");
                                    item.role = child.getString("role");
                                    item.isXmas = child.getString("enIsXmas");

                                    if(child.getString("enMessageType").equals("G")) {
                                        item.msgType = child.getString("enMessageType");
                                        item.image = R.drawable.aurireceive;
                                    } else if(child.getString("enMessageType").equals("B")) {
                                        item.msgType = child.getString("enMessageType");
                                        item.image = R.drawable.agmareceive;
                                    }
                                    outBoxRvItemArrayList.add(item);
                                }

                                adapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });

            return null;
        }
    }
}
