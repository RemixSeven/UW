package com.jiadi.uw;
/**
 * Created by 北 on 2017/6/24.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class VesselActivity extends Fragment {
    private ImageButton bt_Back;
    private ImageButton bt_Stop;
    private ImageButton bt_RightUp;
    private ImageButton bt_Right;
    private ImageButton bt_RightDown;
    private ImageButton bt_LeftUp;
    private ImageButton bt_Left;
    private ImageButton bt_LeftDown;
    private ImageButton bt_Indicator;
    private ImageButton bt_update;
    private Context VContext;
    private TextView Date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_vessel, container, false);
        bindView(view);
        return view;
    }

    private void bindView(View view) {

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
