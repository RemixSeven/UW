package com.jiadi.uw;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;

import static com.jiadi.uw.tools.binaryToHexString;
import static com.jiadi.uw.tools.dialog;
import static com.jiadi.uw.tools.find;
import static com.jiadi.uw.tools.isWifiConnected;
import static com.jiadi.uw.tools.spGet;
import static com.jiadi.uw.tools.toast;

/**
 * Created by 北 on 2017/6/24.
 */

public class SubmarineActivity extends Fragment implements View.OnTouchListener, View.OnClickListener {
    private CheckBox cb_Gravity;
    private Context SContext;
    private Button bt_Back;
    private Button bt_Dive;
    private ImageButton bt_Stop;
    private Button bt_Float;
    private Button bt_LeftMid;
    private Button bt_LeftDown;
    private Button bt_LeftUp;
    private Button bt_RightMid;
    private Button bt_RightDown;
    private Button bt_RightUp;
    private Button bt_TakePic;
    private Button bt_Change;
    private Button bt_Cus1;
    private Button bt_Start;
    private boolean threadFlag;
    private Thread workThread;
    private SocketClient socket;
    private String url;
    private int port;
    private int state;
    private TextView TV_status;

    private static final class STATE {
        public static final int INIT = 0;
        public static final int WIFI_NO_CON = 1000;
        public static final int CONNED = 1001;
    }

    private static final class MSG {
        public static final int CON_SUCCESS = 1000;
        public static final int SOCKET_ERROR = 2000;
        public static final int READ_ERROR = 2001;
        public static final int SEND_ERROR = 2002;
        public static final int CACHE_ERROR = 2002;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_submarine, container, false);
        bindView(view);
        return view;
    }

    private void bindView(View view) {
        bt_Start=tools.find(view,R.id.bt_start_stop);
        bt_Cus1=tools.find(view,R.id.ButtonCus1);
    }

    private void bind() {
        bt_Start.setOnClickListener(this);
        bt_Cus1.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SContext=getActivity();
        bind();
        init();
    }

    //消息处理
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG.SOCKET_ERROR:
                    Log.d("handler", "SOCKET异常：" + String.valueOf(msg.obj));
                    if (socket != null) {
                        socket.closeSocket();
                        socket = null;
                        state = STATE.INIT;
                    }
                    break;
                case MSG.READ_ERROR:
                    Log.d("handler", "数据读取异常：" + String.valueOf(msg.obj));
                    break;
                case MSG.SEND_ERROR:
                    Log.d("handler", "数据发送异常：" + String.valueOf(msg.obj));
                    break;
            }
        }
    };

    private void init() {
        state = STATE.INIT;
        threadFlag = false;
        Log.d("init", "程序启动");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()) {
                    //根据不同的按钮，发送不同的指令
                    case R.id.ButtonCus1:
                        sendCommand(Command.SPEED);
                        break;
                   /* case R.id.bt_change:
                      sendCommand(Command.CHANGE);
                      break;
                    case R.id.btnBack:
                        //sendCommand(Command.BACK);
                        break;
                    case R.id.btnDive:
                        //sendCommand(Command.DIVE);
                        break;
                    case R.id.btnFloat:
                        //sendCommand(Command.FLOAT);
                        break;
                    case R.id.btnForward:
                        //sendCommand(Command.FORWARD);
                        break;
                    case R.id.btnLeft1:
                        //sendCommand(Command.LEFTMID);
                        break;
                    case R.id.btnLeftDown:
                        //sendCommand(Command.LEFTDOWN);
                        break;
                    case R.id.btnLeftUp:
                        //sendCommand(Command.LEFTUP);
                        break;
                    case R.id.btnRight1:
                        //sendCommand(Command.RIGHTMID);
                        break;
                    case R.id.btnRightDown:
                        //sendCommand(Command.RIGHTDOWN);
                        break;
                    case R.id.btnRightUp:
                        //sendCommand(Command.RIGHTUP);
                        break;
                    case R.id.btnStop:
                        //sendCommand(Command.STOP);
                        break;
                    case R.id.ButtonTakePic:
                        //sendCommand(Command.TAKEPIC);
                        break;*/

                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start_stop:
                if (state == STATE.INIT) {
                    ((TextView) v).setText("断开连接");
                    start();
                } else {
                    ((TextView) v).setText("连接设备");
                    stop();
                }
                break;
            case R.id.bt_change:

                break;
        }
    }

    //开始准备工作
    private void start() {
        Log.d("start", "开始准备工作");
        //判断wifi状态
        if (!isWifiConnected(SContext)) {
            Log.d("start", "WIFI未连接");
            dialog(SContext, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    toast(SContext, "点击重新载入以重新加载程序", Toast.LENGTH_LONG);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("start", "程序终止，请打开wifi后再试");
                    state = STATE.WIFI_NO_CON;
                }
            }, "提示", "wifi未连接，是否打开设置连接wifi？", "是", "否", -1);
        } else {
            //初始化主要参数
            url = (String) spGet(SContext, null, "url", "www.baidu.com");
            port = (int) spGet(SContext, null, "port", 80);
            Log.d("start", "当前目标设备：" + url);
            Log.d("start", "当前目标端口：" + port);
            threadFlag = true;
            //初始化线程
            workThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("workThread", "工作线程启动");
                    DataInputStream dis = null;
                    //初始化SOCKET
                    try {
                        if (socket != null) {
                            socket.closeSocket();
                        }
                        //尝试创建SOCKET
                        socket = new SocketClient(url, port);
                    } catch (Exception e) {
                        Message message = new Message();
                        message.what = MSG.SOCKET_ERROR;
                        message.obj = e.getMessage();
                        handler.sendMessage(message);
                    } finally {
                        try {
                            Log.d("workThread", "准备SOCKET缓冲区");
                            dis = new DataInputStream(socket.getInputStream());
                            state = STATE.CONNED;
                            Log.d("workThread", "SOCKET已创建");
                            Log.d("workThread", "与目标设备连接成功");
                            int readCount;
                            while (threadFlag) {
                                //工作线程主循环
                                try {
                                    byte[] buffer = new byte[dis.available()];
                                    readCount = dis.read(buffer);
                                    while (readCount > 0) {
                                        Log.d("workThread", "接收到数据：" + binaryToHexString(buffer));
                                        Log.d("workThread", "数据长度：" + readCount);
                                    }
                                } catch (Exception e) {
                                    Message message = new Message();
                                    message.what = MSG.READ_ERROR;
                                    message.obj = e.getMessage();
                                    handler.sendMessage(message);
                                }
                                SystemClock.sleep(200);
                            }
                            Log.d("workThread", "工作线程已退出");
                        } catch (Exception e) {
                            Message message = new Message();
                            message.what = MSG.CACHE_ERROR;
                            message.obj = e.getMessage();
                            handler.sendMessage(message);
                        }
                    }
                }
            });
            workThread.start();
        }
    }

    public void stop() {
        state = STATE.INIT;
        if (socket != null) {
            socket.closeSocket();
            socket = null;
        }
        threadFlag = false;
    }

    public void sendCommand(final byte[] data) {
        if (state != STATE.CONNED) {
            Log.d("workThread", "请确保与目标设备连接后再发送指令");
            return;
        }

        Log.d("workThread", "尝试发送指令：" + binaryToHexString(data));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.sendMsg(data);
                } catch (Exception e) {
                    Message message = new Message();
                    message.what = MSG.SEND_ERROR;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
}
