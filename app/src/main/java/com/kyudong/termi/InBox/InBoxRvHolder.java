package com.kyudong.termi.InBox;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyudong.termi.R;

/**
 * Created by Kyudong on 16. 11. 20..
 */
public class InBoxRvHolder extends RecyclerView.ViewHolder {

    TextView contentTxv;
    ImageView imageView;
    TextView TimeTxv;
    TextView IsReadTxv;
    TextView SeqNoTxv;
    ImageView circle;

    public InBoxRvHolder(View itemView) {
        super(itemView);

        this.contentTxv = (TextView) itemView.findViewById(R.id.textView6);
        this.imageView = (ImageView) itemView.findViewById(R.id.imageView3);
        this.TimeTxv = (TextView) itemView.findViewById(R.id.Time);
        this.IsReadTxv = (TextView) itemView.findViewById(R.id.IsRead);
        this.SeqNoTxv = (TextView) itemView.findViewById(R.id.SeqNo);
        this.circle = (ImageView) itemView.findViewById(R.id.circleImageView);
    }
}
