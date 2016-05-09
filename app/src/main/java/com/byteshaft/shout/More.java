package com.byteshaft.shout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class More extends Fragment implements View.OnClickListener {

    private View mBaseView;
    private TextView key;
    private TextView link;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.more, container, false);
        key = (TextView) mBaseView.findViewById(R.id.key);
        link = (TextView) mBaseView.findViewById(R.id.link);
        key.setText(About.text);
        link.setOnClickListener(this);
        return mBaseView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.link:
                MainActivity.getInstance().externalUrlConfirmation("http://8ccc.com.au/schedule", getActivity());
                break;
        }
    }
}
