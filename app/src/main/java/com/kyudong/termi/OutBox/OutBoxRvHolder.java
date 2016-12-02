package com.kyudong.termi.OutBox;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyudong.termi.R;

/**
 * Created by Kyudong on 16. 11. 20..
 */
public class OutBoxRvHolder extends RecyclerView.ViewHolder {

    TextView contentTxv;
    ImageView imageView;
    TextView TimeTxv;
    TextView IsReadTxv;
    TextView SeqNoTxv;

    public OutBoxRvHolder(View itemView) {
        super(itemView);

        this.contentTxv = (TextView) itemView.findViewById(R.id.textView62);
        this.imageView = (ImageView) itemView.findViewById(R.id.imageView32);
        this.TimeTxv = (TextView) itemView.findViewById(R.id.Time2);
        this.IsReadTxv = (TextView) itemView.findViewById(R.id.IsRead2);
        this.SeqNoTxv = (TextView) itemView.findViewById(R.id.SeqNo2);
    }
}
