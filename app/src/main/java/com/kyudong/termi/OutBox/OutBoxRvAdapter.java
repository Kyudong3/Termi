package com.kyudong.termi.OutBox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyudong.termi.R;
import com.kyudong.termi.ReadMessage;

import java.util.ArrayList;

/**
 * Created by Kyudong on 16. 11. 20..
 */
public class OutBoxRvAdapter extends RecyclerView.Adapter<OutBoxRvHolder> {

    private static final int REQUEST_CODE = 1003;
    private ArrayList<OutBoxRvItem> outBoxRvItemArrayList;
    private Context context;
    private RecyclerView rv;
    private String token;

    public OutBoxRvAdapter(Context context, ArrayList<OutBoxRvItem> outBoxRvItemArrayList, RecyclerView rv, String token) {
        this.context = context;
        this.outBoxRvItemArrayList = outBoxRvItemArrayList;
        this.rv = rv;
        this.token = token;
    }

    @Override
    public int getItemCount() {
        return outBoxRvItemArrayList.size();
    }

    @Override
    public OutBoxRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.outbox_rv, parent, false
        );

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = rv.getChildAdapterPosition(v);
                int seqNo = outBoxRvItemArrayList.get(pos).seqNo;
                String content = outBoxRvItemArrayList.get(pos).text;
                String time = outBoxRvItemArrayList.get(pos).time;
                String isRead = outBoxRvItemArrayList.get(pos).isRead;
                String role = outBoxRvItemArrayList.get(pos).role;
                String msgType = outBoxRvItemArrayList.get(pos).msgType;

                Intent intent = new Intent(v.getContext(), ReadMessage.class);

                intent.putExtra("seqNo", seqNo);
                intent.putExtra("content", content);
                intent.putExtra("time", time);
                intent.putExtra("isRead", isRead);
                intent.putExtra("role", role);
                intent.putExtra("msgType", msgType);
                intent.putExtra("authorization", token);
                intent.putExtra("pos", pos);
                intent.putExtra("int", 1);

                ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
                //v.getContext().startActivity(intent);
            }
        });
        return new OutBoxRvHolder(layout);
    }

    @Override
    public void onBindViewHolder(OutBoxRvHolder holder, int position) {

        OutBoxRvItem item = outBoxRvItemArrayList.get(position);

        holder.contentTxv.setText(item.text);
        //holder.SeqNoTxv.setText(item.seqNo);
        holder.TimeTxv.setText(item.time);
        holder.IsReadTxv.setText(item.isRead);
        holder.imageView.setImageResource(item.image);
    }
}
