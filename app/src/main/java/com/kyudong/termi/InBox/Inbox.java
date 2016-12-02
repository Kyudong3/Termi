package com.kyudong.termi.InBox;


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
import com.kyudong.termi.ReadMessage;
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

public class Inbox extends Fragment {

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private InBoxRvAdapter adapter;
    private ArrayList<InBoxRvItem> inBoxRvItemArrayList = new ArrayList<>();

    private String token;

    private int position;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.inbox,container,false);

        token = getActivity().getIntent().getStringExtra("authorization");

        //inBoxRvItemArrayList = new ArrayList<>();
        rv = (RecyclerView) v.findViewById(R.id.inRecyclerView);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new InBoxRvAdapter(v.getContext(), inBoxRvItemArrayList, rv, token);

        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        rv.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        Log.e("zzz", token);

        GetInBox getInBox = new GetInBox();
        try {
            getInBox.doGetRequest("http://52.78.240.168/api/messageList/receive", token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //inBoxRvItemArrayList.remove(position);
        //adapter.notifyItemRemoved(position);
        return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int pos = data.getExtras().getInt("pos");
        Log.e("CALLED", "inbox : "  + " size : " + inBoxRvItemArrayList.size() + " position : " + pos );

////////////////////////////////////////////방법 1//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        inBoxRvItemArrayList.remove(pos);
//        adapter.notifyItemRemoved(pos);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////방법 2//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        inBoxRvItemArrayList.clear();
        GetInBox getInBox = new GetInBox();
        try {
            getInBox.doGetRequest("http://52.78.240.168/api/messageList/receive", token);
        } catch (IOException e) {
            e.printStackTrace();
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

        String doGetRequest(String url, String auth) throws IOException {

            Request request = new Request.Builder()
                    .addHeader("authorization", auth)
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
                                //inBoxRvItemArrayList.clear();

                                JSONObject parentJObject = new JSONObject(res);
                                JSONArray parentJArray = parentJObject.getJSONArray("messageData");

                                for(int i = 0; i <parentJArray.length(); i++) {
                                    InBoxRvItem item = new InBoxRvItem();

                                    JSONObject child = parentJArray.getJSONObject(i);

                                    item.text = child.getString("txContent");
                                    item.time = child.getString("dtRegTime");
                                    item.isRead = child.getString("enIsRead");
                                    item.seqNo = child.getInt("nSeqNo");
                                    item.role = child.getString("role");
                                    if(child.getString("enMessageType").equals("G")) {
                                        item.msgType = child.getString("enMessageType");
                                        item.image = R.drawable.aurireceive;
                                    } else if(child.getString("enMessageType").equals("B")) {
                                        item.msgType = child.getString("enMessageType");
                                        item.image = R.drawable.agmareceive;
                                    }
                                    if(item.isRead.equals("N")) {
                                        item.circleImage = R.drawable.light_circle;
                                    } else if(item.isRead.equals("Y")) {
                                        item.circleImage = R.drawable.rv_circle;
                                    }
                                    //item.msgType = child.getString("enMessageType");
                                    item.position = i;

                                    inBoxRvItemArrayList.add(item);
                                }
                                Log.e("CALLED", "ArrayListSize is : " + inBoxRvItemArrayList.size());
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
