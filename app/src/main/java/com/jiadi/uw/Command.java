package com.jiadi.uw;

public class Command {
    //小车自检指令
    public static final byte[] CHECK = {(byte) 0xEB, (byte) 0x90, (byte) 0x46, (byte) 0x00, (byte) 0x00};
    //查询GPS
    public static final byte[] GPS = {(byte) 0xEB, (byte) 0x90, (byte) 0x02, (byte) 0x00, (byte) 0x00};
    //查询水上Y91
    public static final byte[] Y91up = {(byte) 0xEB, (byte) 0x90, (byte) 0x03, (byte) 0x00, (byte) 0x00};
    //查询水下Y91
    public static final byte[] Y91down = {(byte) 0xEB, (byte) 0x90, (byte) 0x04, (byte) 0x00, (byte) 0x00};
    //查询编码器
    public static final byte[] bianma = {(byte) 0xEB, (byte) 0x90, (byte) 0x05, (byte) 0x00, (byte) 0x00};
    //查询潜航深度
    public static final byte[] deepth = {(byte) 0xEB, (byte) 0x90, (byte) 0x06, (byte) 0x00, (byte) 0x00};
    //打开LED
    public static final byte[] LEDON = {(byte) 0xEB, (byte) 0x90, (byte) 0x0F,(byte) 0x01, (byte) 0x55, (byte) 0x55};
    //关闭LED
    public static final byte[] LEDOFF = {(byte) 0xEB, (byte) 0x90, (byte) 0x0F, (byte) 0x01,(byte) 0x00, (byte) 0x00};
    //
    public static final byte[] Y91_left_max = {(byte) 0xEB, (byte) 0x90, (byte) 0x0A, (byte) 0x58,(byte) 0x02, (byte)0x5A};
    //
    public static final byte[] Y91_right_max = {(byte) 0xEB, (byte) 0x90, (byte) 0x0B, (byte) 0x58,(byte) 0x02, (byte)0x5A};
    //
    public static final byte[] Y91_left_mid = {(byte) 0xEB, (byte) 0x90, (byte) 0x0A, (byte) 0xF4,(byte) 0x01, (byte)0xF5};
    //
    public static final byte[] Y91_right_mid = {(byte) 0xEB, (byte) 0x90, (byte) 0x0B, (byte) 0xF4,(byte) 0x01, (byte)0xF5};
    //
    public static final byte[] Y91_left_min = {(byte) 0xEB, (byte) 0x90, (byte) 0x0A, (byte) 0x90,(byte) 0x01, (byte)0x91};
    //
    public static final byte[] Y91_right_min = {(byte) 0xEB, (byte) 0x90, (byte) 0x0B, (byte) 0x90,(byte) 0x01, (byte)0x91};
}
