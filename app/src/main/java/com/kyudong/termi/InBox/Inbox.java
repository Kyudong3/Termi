package com.kyudong.termi.InBox;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kyudong.termi.R;
import com.kyudong.termi.ReadMessage;
import com.kyudong.termi.UnRead;
import com.kyudong.termi.UserToken;
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

    private TextView comingMessageTxv;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private InBoxRvAdapter adapter;
    private ArrayList<InBoxRvItem> inBoxRvItemArrayList = new ArrayList<>();

    private static final int REQUEST_CODE = 1003;
    private static final int REQUEST_CODE2 = 1004;

    private String token;

    private ReceivedMailDialog dialog;
    private ReadReceivedMailDialog readRDialog;
    private int position;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private XmasDialog xmasDialog;
    private int dialogInt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.inbox,container,false);

        token = getActivity().getIntent().getStringExtra("authorization");

        comingMessageTxv = (TextView) v.findViewById(R.id.comingMessageTxv);
        rv = (RecyclerView) v.findViewById(R.id.inRecyclerView);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new InBoxRvAdapter(v.getContext(), inBoxRvItemArrayList, rv, token);

        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        rv.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        GetInBox getInBox = new GetInBox();
        try {
            getInBox.doGetRequest("http://52.78.240.168/api/messageList/receive", token);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int pos = data.getExtras().getInt("pos");
        String aa = data.getStringExtra("a");
        int a = data.getExtras().getInt("int");

        ///// 삭제하기 눌렀을 때 /////
        if(aa.equals("b")) {
            inBoxRvItemArrayList.remove(pos);
            adapter.notifyItemRemoved(pos);
        ///// 백버튼, 백, 답장할 때 /////
        } else if(aa.equals("a")) {

            inBoxRvItemArrayList.get(pos).circleImage = R.drawable.rv_circle;
            adapter.notifyItemChanged(pos);
        }


////////////////////////////////////////////방법 2//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        inBoxRvItemArrayList.clear();
//        GetInBox getInBox = new GetInBox();
//        try {
//            getInBox.doGetRequest("http://52.78.240.168/api/messageList/receive", token);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    public void setData(int pos) {
        position = pos;
    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
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
                                JSONObject parentJObject = new JSONObject(res);
                                JSONArray parentJArray = parentJObject.getJSONArray("messageData");

                                if(parentJArray.length()==0) {
                                } else {
                                    for(int i = 0; i <parentJArray.length(); i++) {
                                        InBoxRvItem item = new InBoxRvItem();

                                        JSONObject child = parentJArray.getJSONObject(i);

                                        item.text = child.getString("txContent");
                                        item.time = child.getString("dtReservationTime");
                                        item.isRead = child.getString("enIsRead");
                                        item.seqNo = child.getInt("nSeqNo");
                                        item.role = child.getString("role");
                                        item.canReply = child.getString("enCanReply");
                                        item.isXmas = child.getString("enIsXmas");

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
                                        item.position = i;

                                        inBoxRvItemArrayList.add(item);
                                    }
                                }

                                adapter.notifyDataSetChanged();

                                int sending_count = parentJObject.getInt("sending_count");
                                comingMessageTxv.setText("배달 중인 쪽지가 " + sending_count + "개 있어요!");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            SharedPreferences mPrefs = getActivity().getSharedPreferences("popup", Context.MODE_PRIVATE);
                            int pop_seqNo = mPrefs.getInt("message_id",0);


                            if(inBoxRvItemArrayList.isEmpty()) {
                                dialogInt = 0;
                                xmasDialog = new XmasDialog(getContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                                xmasDialog.setCanceledOnTouchOutside(false);
                                xmasDialog.show();
                            } else if(inBoxRvItemArrayList.get(0).seqNo > pop_seqNo) {
                                dialogInt = 2;
                                xmasDialog = new XmasDialog(getContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                                xmasDialog.setCanceledOnTouchOutside(false);
                                xmasDialog.show();
                                xmasDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface d) {
                                            dialog = new ReceivedMailDialog(getContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                                            dialog.show();
                                    }
                                });
                            } else if(inBoxRvItemArrayList.get(0).seqNo <= pop_seqNo) {
                                dialogInt = 0;
                                xmasDialog = new XmasDialog(getContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                                xmasDialog.setCanceledOnTouchOutside(false);
                                xmasDialog.show();
                            }
                        }
                    });
                }
            });

            return null;
        }
    }

    private class ReceivedMailDialog extends Dialog {

        public ReceivedMailDialog(Context context, int themeResId) {
            super(context, themeResId);

            init();
        }

        private void init() {
            setContentView(R.layout.received_customdialog);

            ImageView dialogIV = (ImageView) findViewById(R.id.dialogIV);
            TextView dialogTxv = (TextView) findViewById(R.id.dialogTxv);
            final Button readDialog = (Button) findViewById(R.id.readDialog);
            Button ignoreBtn = (Button) findViewById(R.id.ignoreBtn);

            if(inBoxRvItemArrayList.get(0).isXmas.equals("Y")) {
                if(inBoxRvItemArrayList.get(0).msgType.equals("G")) {
                    dialogIV.setImageResource(R.drawable.xmasauri);
                    dialogTxv.setText("X-mas 어리가 들고 온 메세지");
                } else if(inBoxRvItemArrayList.get(0).msgType.equals("B")) {
                    dialogIV.setImageResource(R.drawable.xmasagma);
                    dialogTxv.setText("X-mas 아그마가 들고 온 메세지");
                }
            } else if(inBoxRvItemArrayList.get(0).isXmas.equals("N")) {
                if(inBoxRvItemArrayList.get(0).msgType.equals("G")) {
                    dialogIV.setImageResource(R.drawable.receivedauridialog);
                    dialogTxv.setText("어리가 들고 온 메세지");
                } else if(inBoxRvItemArrayList.get(0).msgType.equals("B")) {
                    dialogIV.setImageResource(R.drawable.receivedagmadialog);
                    dialogTxv.setText("아그마가 들고 온 메세지");
                }
            }

            readDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readRDialog = new ReadReceivedMailDialog(getContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                    readRDialog.show();
                    //dialog.dismiss();

                    dialog.dismiss();

                    SharedPreferences mPrefs = getActivity().getSharedPreferences("popup", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putInt("message_id", inBoxRvItemArrayList.get(0).seqNo);
                    editor.commit();
                }
            });

            ignoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    SharedPreferences mPrefs = getActivity().getSharedPreferences("popup", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putInt("message_id", inBoxRvItemArrayList.get(0).seqNo);
                    editor.commit();
                }
            });
        }
    }

    private class ReadReceivedMailDialog extends Dialog {

        public ReadReceivedMailDialog(Context context) {
            super(context);

            init();
        }

        public ReadReceivedMailDialog(Context context, int themeResId) {
            super(context, themeResId);

            init();
        }

        private void init() {
            setContentView(R.layout.activity_un_read);

            ReadMessageApi readMessageApi = new ReadMessageApi();
            try {
                readMessageApi.doGetRequest("http://52.78.240.168/api/readMessage/" + inBoxRvItemArrayList.get(0).seqNo, inBoxRvItemArrayList.get(0).role);
            } catch (IOException e) {
                e.printStackTrace();
            }

            inBoxRvItemArrayList.get(0).circleImage = R.drawable.rv_circle;
            adapter.notifyItemChanged(0);

            RelativeLayout readBGLayout = (RelativeLayout) findViewById(R.id.readbackGroundLL);
            Button dialogCloseBtn = (Button) findViewById(R.id.dialogCloseBtn);
            TextView dialogContentTxv = (TextView) findViewById(R.id.dialogReadContent);

            if(inBoxRvItemArrayList.get(0).msgType.equals("G")) {
                readBGLayout.setBackgroundResource(R.drawable.dialogreadmessageauri);
            } else if(inBoxRvItemArrayList.get(0).msgType.equals("B")) {
                readBGLayout.setBackgroundResource(R.drawable.dialogreadmessageagma);
            }

            dialogContentTxv.setText(inBoxRvItemArrayList.get(0).text);
            dialogContentTxv.setMovementMethod(new ScrollingMovementMethod());
            dialogCloseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readRDialog.dismiss();
                }
            });
        }
    }

    private class XmasDialog extends Dialog {

        public XmasDialog(Context context) {
            super(context);

            init();
        }

        public XmasDialog(Context context, int themeResId) {
            super(context, themeResId);

            init();
        }

        private void init() {
            setContentView(R.layout.xmas_popup_dialog);

            Button popupConfirmBtn = (Button) findViewById(R.id.xmas_popup_btn);

            popupConfirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialogInt == 0) {
                        xmasDialog.dismiss();
                    } else if (dialogInt == 2) {
                        xmasDialog.dismiss();
                        dialog = new ReceivedMailDialog(getContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                        dialog.show();
                    }
                }
            });
        }
    }

    public class ReadMessageApi {

        String doGetRequest(String url, String role) throws IOException {
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
                }
            });
            return null;
        }
    }
}
