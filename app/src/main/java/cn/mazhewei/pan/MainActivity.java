package cn.mazhewei.pan;

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

import static cn.mazhewei.pan.tools.binaryToHexString;
import static cn.mazhewei.pan.tools.dialog;
import static cn.mazhewei.pan.tools.find;
import static cn.mazhewei.pan.tools.isWifiConnected;
import static cn.mazhewei.pan.tools.spGet;
import static cn.mazhewei.pan.tools.toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private TextView tv_log;
    private Log log;
    private boolean threadFlag;
    private boolean Flag;
    private Thread workThread;
    private Thread sendThread;
    private SocketClient socket;
    private String url;
    private int port;
    private int state;

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
                        try {
                            socket.closeSocket();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
        setContentView(R.layout.activity_main);
        bind();
        init();
    }

    private void bind() {
        find(this, R.id.bt_a).setOnTouchListener(this);
        find(this, R.id.bt_b).setOnTouchListener(this);
        find(this, R.id.bt_c).setOnTouchListener(this);
        find(this, R.id.bt_d).setOnTouchListener(this);
        find(this, R.id.bt_e).setOnTouchListener(this);
        find(this, R.id.bt_f).setOnTouchListener(this);
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
        find(this, R.id.bt_camera).setOnTouchListener(this);
        find(this, R.id.bt_set).setOnClickListener(this);
        find(this, R.id.bt_start_stop).setOnClickListener(this);
        tv_log = find(this, R.id.tv_log);
    }

    private void init() {
        state = STATE.INIT;
        log = Log.getInstance(this, tv_log);
        threadFlag = false;
        log.l("程序启动");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()) {
                    //根据不同的按钮，发送不同的指令
                    case R.id.bt_a:
                        sendCommand(Command.CHECK);
                        break;
                    case R.id.bt_b:
                        sendCommand(Command.SPEED);
                        break;
                    case R.id.bt_c:
                        sendCommand(Command.Y91down);
                        break;
                    case R.id.bt_d:
                        sendCommand(Command.GPS);
                        break;
                    case R.id.bt_e:
                        sendCommand(Command.bianma);
                        break;
                    case R.id.bt_f:
                        sendCommand(Command.deepth);
                        break;
                    case R.id.bt_left_up:
                        sendCommand(Command.UPLEFT);
                        break;
                    case R.id.bt_up:
                        sendCommand(Command.ERECTIVE);
                        break;
                    case R.id.bt_right_up:
                        sendCommand(Command.UPRIGHT);
                        break;
                    case R.id.bt_left:
                        sendCommand(Command.DOWNLEFT);
                        break;
                    case R.id.bt_o:
                        //sendCommand(Command.);
                        break;
                    case R.id.bt_right:
                        sendCommand(Command.DOWNRIIGHT);
                        break;
                    case R.id.bt_left_down:
                        //sendCommand(Command.);
                        break;
                    case R.id.bt_down:
                        sendCommand(Command.LEVEL);
                        break;
                    case R.id.bt_right_down:
                        //sendCommand(Command.);
                        break;
                    case R.id.bt_shallow:
                        sendCommand(Command.SHALLOW);
                        break;
                    case R.id.bt_deep:
                        sendCommand(Command.DEEP);
                        break;
                    case R.id.bt_camera:
                        sendCommand(Command.LED);
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
            case R.id.bt_set:

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
                    toast(MainActivity.this, "点击重新载入以重新加载程序", Toast.LENGTH_LONG);
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
                                        log.l("接收到数据：" + binaryToHexString(buffer));
                                        log.l("数据长度：" + readCount);
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
            try {
                socket.closeSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        log.l("尝试发送指令：" + binaryToHexString(data));
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
        if (!Flag) {
            sendThread.interrupt();
            sendThread = null;
        }
    }

    public void Analysis(byte Buffer[], int Length) {
        for(int i=0;i<Length;i++) {
            if (Buffer[i] == (byte) 0xEB && Buffer[1] == (byte) 0x90) {
                if (Buffer[3] == (byte) 0xFF) {
                    //应答帧
                } else if (Buffer[3] == (byte) 0x01) {
                    //自检
                } else if (Buffer[3] == (byte) 0x02) {
                    //GPS
                } else if (Buffer[3] == (byte) 0x03) {
                    //水上板Y91
                } else if (Buffer[3] == (byte) 0x04) {
                    //水下板Y91
                } else if (Buffer[3] == (byte) 0x05) {
                    //编码器
                } else if (Buffer[3] == (byte) 0x06) {
                    //潜航深度
                }
            }
        }
    }
}
