package com.kyudong.termi.InBox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kyudong.termi.Home.HomeActivity;
import com.kyudong.termi.R;
import com.kyudong.termi.ReadMessage;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kyudong on 16. 11. 20..
 */
public class InBoxRvAdapter extends RecyclerView.Adapter<InBoxRvHolder> {

    private static final int REQUEST_CODE = 1003;
    private ArrayList<InBoxRvItem> inBoxRvItemArrayList;
    private Context context;
    private RecyclerView rv;
    private String token;

    public InBoxRvAdapter(Context context, ArrayList<InBoxRvItem> inBoxRvItemArrayList, RecyclerView rv, String token) {
        this.context = context;
        this.inBoxRvItemArrayList = inBoxRvItemArrayList;
        this.rv = rv;
        this.token = token;

    }

    @Override
    public int getItemCount() {
        return inBoxRvItemArrayList.size();
    }

    public void removeItem(int position) {
        inBoxRvItemArrayList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public InBoxRvHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.inbox_rv, parent, false);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = rv.getChildAdapterPosition(v);
                int seqNo = inBoxRvItemArrayList.get(pos).seqNo;
                //int position = inBoxRvItemArrayList.get(pos).position;
                String content = inBoxRvItemArrayList.get(pos).text;
                String time = inBoxRvItemArrayList.get(pos).time;
                String isRead = inBoxRvItemArrayList.get(pos).isRead;
                String role = inBoxRvItemArrayList.get(pos).role;
                String msgType = inBoxRvItemArrayList.get(pos).msgType;
                String canReply = inBoxRvItemArrayList.get(pos).canReply;
                String isXmas = inBoxRvItemArrayList.get(pos).isXmas;

                Intent intent = new Intent(v.getContext(), ReadMessage.class);
                intent.putExtra("seqNo", seqNo);
                intent.putExtra("content", content);
                intent.putExtra("time", time);
                intent.putExtra("isRead", isRead);
                intent.putExtra("role", role);
                intent.putExtra("msgType", msgType);
                intent.putExtra("authorization", token);
                intent.putExtra("pos", pos);
                intent.putExtra("canReply", canReply);
                intent.putExtra("isXmas", isXmas);
                intent.putExtra("int", "inboxRead");

                ((Activity) context).startActivityForResult(intent, REQUEST_CODE);

            }
        });

        return new InBoxRvHolder(layout);
    }

    @Override
    public void onBindViewHolder(InBoxRvHolder holder, int position) {

        InBoxRvItem item = inBoxRvItemArrayList.get(position);

        holder.contentTxv.setText(item.text);
        holder.imageView.setImageResource(item.image);
        holder.circle.setImageResource(item.circleImage);

    }
}
