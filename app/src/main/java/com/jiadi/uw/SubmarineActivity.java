package com.jiadi.uw;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
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

public class SubmarineActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private TextView tv_log;
    private Log log;
    private boolean threadFlag;
    private boolean Flag;
    private boolean Led_Flag;
    private Thread workThread;
    private Thread sendThread;
    private SocketClient socket;
    private String url;
    private int port;
    private int state;

    private byte sync_header1 = (byte) 0xEB;//同步头1
    private byte sync_header2 = (byte) 0x90;//同步头2
    private byte Lenth;//数据长度
    private byte Sum;//校验和
    //设置回传速率
    private byte Rate;//回传速率
    //控制放线舵机
    private byte Dir;//舵机收放线方向（复位值：0x00）
    private byte Rate7;//舵机收放线速度等级，范围：0~250（复位值：0xFA）
    private byte Mode;//工作模式（复位值：0x00）
    //控制水平云台舵机
    private byte Deg1;
    private byte Deg2;//目标角度（°），范围：1～180（复位值：0x0000）
    //控制垂直云台舵机
    private byte DEG1;
    private byte DEG2;//目标角度（°），范围：1～180（复位值：0x0000）
    //控制水上板水平左推进器
    private byte Gas_Left_up_1;
    private byte Gas_Left_up_2;//油门，范围：0～999（复位值：0x0000）
    //控制水上板水平右推进器
    private byte Gas_Right_up_1;
    private byte Gas_Right_up_2;//油门，范围：0～999（复位值：0x0000）
    //控制水下板竖直推进器
    private byte Gas_Vertical_1;
    private byte Gas_Vertical_2;//油门，范围：0～999（复位值：0x0000）
    //控制水上板水平左推进器
    private byte Gas_Left_down_1;
    private byte Gas_Left_down_2;//油门，范围：0～999（复位值：0x0000）
    //控制水上板水平右推进器
    private byte Gas_Right_down_1;
    private byte Gas_Right_down_2;//油门，范围：0～999（复位值：0x0000）
    //控制照明LED灯
    private byte LED;//LED灯亮度，范围0～99

    //自检信息
    private int Power;
    private String STA;

    private static final class STATE {
        public static final int INIT = 0;
        public static final int WIFI_NO_CON = 1000;
        public static final int CONNED = 1001;
    }

    //消息处理
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG.SOCKET_ERROR:
                    log.l("SOCKET异常：" + msg.obj);
                    log.l("重置SOCKET");
                    if (socket != null) {
                        socket.closeSocket();
                        socket = null;
                        state = STATE.INIT;
                    }
                    break;
                case MSG.READ_ERROR:
                    log.l("数据读取异常：" + msg.obj);
                    break;
                case MSG.SEND_ERROR:
                    log.l("数据发送异常：" + msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submarine);
        bind();
        init();
    }

    private void bind() {
        find(this, R.id.bt_left_up).setOnTouchListener(this);
        find(this, R.id.bt_up).setOnTouchListener(this);
        find(this, R.id.bt_right_up).setOnTouchListener(this);
        find(this, R.id.bt_left).setOnTouchListener(this);
        find(this, R.id.bt_o).setOnTouchListener(this);
        find(this, R.id.bt_right).setOnTouchListener(this);
        find(this, R.id.bt_left_down).setOnTouchListener(this);
        find(this, R.id.bt_down).setOnTouchListener(this);
        find(this, R.id.bt_right_down).setOnTouchListener(this);
        find(this, R.id.bt_shallow).setOnTouchListener(this);
        find(this, R.id.bt_deep).setOnTouchListener(this);
        find(this, R.id.bt_led).setOnTouchListener(this);
        find(this, R.id.bt_change).setOnClickListener(this);
        find(this, R.id.bt_start_stop).setOnClickListener(this);
        tv_log = find(this, R.id.tv_log);
    }

    private void init() {
        state = STATE.INIT;
        log = Log.getInstance(this, tv_log);
        threadFlag = false;
        Led_Flag = true;
        log.l("程序启动");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()) {
                    //根据不同的按钮，发送不同的指令
                    /*case R.id.bt_a:
                        sendCommand(Command.);
                        break;
                    case R.id.bt_b:
                        sendCommand(Command.);
                        break;
                    case R.id.bt_c:
                        sendCommand(Command);
                        break;
                    case R.id.bt_d:
                        sendCommand(Command.);
                        break;
                    case R.id.bt_e:
                        sendCommand(Command.);
                        break;
                    case R.id.bt_f:
                        sendCommand(Command.);
                        break;
                    case R.id.bt_left_up:

                        break;*/
                    case R.id.bt_up:
                        sendCommand(Command.Y91_left_max);
                        sendCommand(Command.Y91_right_max);
                        break;
                    /*case R.id.bt_right_up:
                        sendCommand(Command.);
                        break;*/
                    case R.id.bt_left:
                        sendCommand(Command.Y91_right_max);
                        sendCommand(Command.Y91_left_min);
                        break;
                    case R.id.bt_o:
                        sendCommand(Command.Y91_left_mid);
                        sendCommand(Command.Y91_right_mid);
                        break;
                    case R.id.bt_right:
                        sendCommand(Command.Y91_left_max);
                        sendCommand(Command.Y91_right_min);
                        break;
                    /*case R.id.bt_left_down:
                        sendCommand(Command.);
                        break;*/
                    case R.id.bt_down:
                        sendCommand(Command.Y91_right_min);
                        sendCommand(Command.Y91_left_min);
                        break;
                   /* case R.id.bt_right_down:
                        sendCommand(Command.);
                        break;
                    case R.id.bt_shallow:
                        sendCommand(Command.);
                        break;
                    case R.id.bt_deep:
                        sendCommand(Command.);
                        break;*/
                    case R.id.bt_led:
                        if (Led_Flag) {
                            sendCommand(Command.LEDON);
                            Led_Flag = false;
                        } else {
                            sendCommand(Command.LEDOFF);
                            Led_Flag = true;
                        }
                        break;

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
                Intent intent=new Intent(SubmarineActivity.this,Wifisetting.class);
                startActivity(intent);
                break;
        }
    }


    //开始准备工作
    private void start() {
        log.l("开始准备工作");
        //判断wifi状态
        if (!isWifiConnected(this)) {
            log.l("WIFI未连接");
            dialog(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    toast(SubmarineActivity.this, "点击重新载入以重新加载程序", Toast.LENGTH_LONG);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    log.l("程序终止，请打开wifi后再试");
                    state = STATE.WIFI_NO_CON;
                }
            }, "提示", "wifi未连接，是否打开设置连接wifi？", "是", "否", -1);
        } else {
            //初始化主要参数
            url = (String) spGet(this, null, "url", "192.168.0.7");
            port = (int) spGet(this, null, "port", 8233);
            log.l("当前目标设备：" + url);
            log.l("当前目标端口：" + port);
            threadFlag = true;
            //初始化线程
            workThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    log.l("工作线程启动");
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
                            log.l("准备SOCKET缓冲区");
                            dis = new DataInputStream(socket.getInputStream());
                            state = STATE.CONNED;
                            log.l("SOCKET已创建");
                            log.l("与目标设备连接成功");
                            int readCount;
                            while (threadFlag) {
                                //工作线程主循环
                                try {
                                    byte[] buffer = new byte[dis.available()];
                                    readCount = dis.read(buffer);
                                    if (readCount > 0) {
                                        if (readCount < 100) {
                                            log.l("接收到数据：" + binaryToHexString(buffer));
                                            log.l("数据长度：" + readCount);
                                        }
                                    }
                                } catch (Exception e) {
                                    Message message = new Message();
                                    message.what = MSG.READ_ERROR;
                                    message.obj = e.getMessage();
                                    handler.sendMessage(message);
                                }
                                SystemClock.sleep(200);
                            }
                            log.l("工作线程已退出");
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
            log.l("请确保与目标设备连接后再发送指令");
            return;
        }
        Flag = true;
        sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.sendMsg(data);
                    Flag = false;
                } catch (Exception e) {
                    Message message = new Message();
                    message.what = MSG.SEND_ERROR;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        });
        sendThread.start();
        log.l("指令已执行");
        if (!Flag) {
            sendThread.interrupt();
            sendThread = null;
        }
    }

    public String HToB(String a) {
        String b = Integer.toBinaryString(Integer.valueOf(toD(a, 16)));
        return b;
    }

    public String toD(String a, int b) {
        int r = 0;
        for (int i = 0; i < a.length(); i++) {
            r = (int) (r + formatting(a.substring(i, i + 1))
                    * Math.pow(b, a.length() - i - 1));
        }
        return String.valueOf(r);
    }

    public int formatting(String a) {
        int i = 0;
        for (int u = 0; u < 10; u++) {
            if (a.equals(String.valueOf(u))) {
                i = u;
            }
        }
        if (a.equals("A")) {
            i = 10;
        }
        if (a.equals("B")) {
            i = 11;
        }
        if (a.equals("C")) {
            i = 12;
        }
        if (a.equals("D")) {
            i = 13;
        }
        if (a.equals("E")) {
            i = 14;
        }
        if (a.equals("F")) {
            i = 15;
        }
        return i;
    }

    public void Analysis(byte Buffer[], int Length) {
        for (int i = 0; i < Length; i++) {
            if (Buffer[i] == (byte) 0xEA && Buffer[i + 1] == (byte) 0x90) {
                if (Buffer[i + 2] == (byte) 0xFF) {
                    //应答帧
                    switch (Buffer[i + 4]) {
                        case (byte) 0x00:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("设置回传速率:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("设置回传速率:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("设置回传速率:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("设置回传速率:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("设置回传速率:校验和出错");
                                    break;
                            }
                            break;
                        case ((byte) 0x01):
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("查询自检信息:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("查询自检信息:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("查询自检信息:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("查询自检信息:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("查询自检信息:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x02:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("查询GPS:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("查询GPS:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("查询GPS:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("查询GPS:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("查询GPS:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x03:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("查询水上板JY901:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("查询水上板JY901:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("查询水上板JY901:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("查询水上板JY901:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("查询水上板JY901:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x04:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("查询水下板JY901:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("查询水下板JY901:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("查询水下板JY901:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("查询水下板JY901:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("查询水下板JY901:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x05:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("查询编码器:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("查询编码器:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("查询编码器命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("查询编码器:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("查询编码器:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x06:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("查询潜航深度:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("查询潜航深度:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("查询潜航深度:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("查询潜航深度:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("查询潜航深度:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x07:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("控制放线舵机:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("控制放线舵机:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("控制放线舵机:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("控制放线舵机:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("控制放线舵机:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x08:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("控制水平云台舵机:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("控制水平云台舵机:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("控制水平云台舵机:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("控制水平云台舵机:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("控制水平云台舵机:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x09:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("控制垂直云台舵机:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("控制垂直云台舵机:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("控制垂直云台舵机:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("控制垂直云台舵机:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("控制垂直云台舵机:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x0A:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("控制水上板水平左推进器:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("控制水上板水平左推进器:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("控制水上板水平左推进器:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("控制水上板水平左推进器:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("控制水上板水平左推进器:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x0B:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("控制水上板水平右推进器:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("控制水上板水平右推进器:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("控制水上板水平右推进器:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("控制水上板水平右推进器:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("控制水上板水平右推进器:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x0C:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("控制水下板竖直推进器:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("控制水下板竖直推进器:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("控制水下板竖直推进器:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("控制水下板竖直推进器:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("控制水下板竖直推进器:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x0D:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("控制水下板水平左推进器:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("控制水下板水平左推进器:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("控制水下板水平左推进器:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("控制水下板水平左推进器:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("控制水下板水平左推进器:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x0E:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("控制水下板水平右推进器:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("控制水下板水平右推进器:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("控制水下板水平右推进器:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("控制水下板水平右推进器:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("控制水下板水平右推进器:校验和出错");
                                    break;
                            }
                            break;
                        case (byte) 0x0F:
                            switch (Buffer[i + 5]) {
                                case (byte) 0x00:
                                    log.l("控制照明LED灯:指令接收成功");
                                    break;
                                case (byte) 0x01:
                                    log.l("控制照明LED灯:指令同步失败");
                                    break;
                                case (byte) 0x02:
                                    log.l("控制照明LED灯:命令字节无法识别");
                                    break;
                                case (byte) 0x03:
                                    log.l("控制照明LED灯:数据储存溢出");
                                    break;
                                case (byte) 0x04:
                                    log.l("控制照明LED灯:校验和出错");
                                    break;
                            }
                            break;
                    }
                    i = i + 7;
                } else if (Buffer[i + 2] == (byte) 0x01) {
                    //自检
                    String TIME;
                    if (Buffer[i + 4] + Buffer[i + 5] + Buffer[i + 6] + Buffer[i + 7] + Buffer[i + 8] + Buffer[i + 9] == Buffer[i + 10]) {
                        Power = Integer.valueOf(binaryToHexString(Buffer[i + 4]), 16);
                        STA = HToB(binaryToHexString(Buffer[i + 5]));
                        TIME = toD(binaryToHexString(Buffer[i + 6]), 16) + toD(binaryToHexString(Buffer[i + 7]), 16) + toD(binaryToHexString(Buffer[i + 8]), 16) + toD(binaryToHexString(Buffer[i + 9]), 16);
                        log.l(Power + STA + TIME);
                    }
                    i = i + 11;
                } else if (Buffer[i + 2] == (byte) 0x02) {
                    //GPS
                    i = i + 28;
                } else if (Buffer[i + 2] == (byte) 0x03) {
                    //水上板Y91
                    i = i + 23;
                } else if (Buffer[i + 2] == (byte) 0x04) {
                    //水下板Y91
                    i = i + 23;
                } else if (Buffer[i + 2] == (byte) 0x05) {
                    //编码器
                    i = i + 9;
                } else if (Buffer[i + 2] == (byte) 0x06) {
                    //潜航深度
                    String DEPTH = toD(binaryToHexString(Buffer[i + 3]), 16) + toD(binaryToHexString(Buffer[i + 4]), 16);
                    if (Buffer[i + 3] + Buffer[i + 4] == Buffer[i + 5]) {
                        float Voltage = (float) (3.3 * Float.parseFloat(DEPTH) / 4096);
                        float Current = Voltage * 1000 / 120;
                        float Depth = 10000 * Current / 9800;
                        log.l(String.valueOf(Depth));
                    }
                    i = i + 7;
                }
            }
        }
    }
}