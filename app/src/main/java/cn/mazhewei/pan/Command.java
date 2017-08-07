package cn.mazhewei.pan;

public class Command {
    //小车自检指令
    public static final byte[] CHECK = {(byte) 0xEB, (byte) 0x90, (byte) 0x46, (byte) 0x00, (byte) 0x00};
    //回传速率指令
    public static final byte[] SPEED = {(byte) 0xEB, (byte) 0x90, (byte) 0xAA, (byte) 0x01, (byte) 0x0B, (byte) 0x0B};
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
    //水上板左推进
    public static final byte[]  UPLEFT= {(byte) 0xEB, (byte) 0x90, (byte) 0x0A, (byte) 0x02, (byte) 0x00,(byte) 0x64,(byte) 0x64};
    //水上板右推进
    public static final byte[] UPRIGHT = {(byte) 0xEB, (byte) 0x90, (byte) 0x0B, (byte) 0x02, (byte) 0x00,(byte) 0x64,(byte) 0x64};
    //水下板左推进
    public static final byte[] DOWNLEFT = {(byte) 0xEB, (byte) 0x90, (byte) 0x0C, (byte) 0x02, (byte) 0x00,(byte) 0x64,(byte) 0x64};
    //水下板右推进
    public static final byte[] DOWNRIIGHT = {(byte) 0xEB, (byte) 0x90, (byte) 0x0D, (byte) 0x02,(byte) 0x00,(byte) 0x64, (byte) 0x64};
    //放线舵机上浮
    public static final byte[] SHALLOW = {(byte) 0xEB, (byte) 0x90, (byte) 0x07, (byte) 0x03, (byte) 0x00,(byte) 0x32,(byte) 0x01,(byte) 0x33};
    //放线舵机下潜
    public static final byte[] DEEP= {(byte) 0xEB, (byte) 0x90, (byte) 0x07, (byte) 0x03, (byte) 0x00,(byte) 0x32,(byte) 0x01,(byte) 0x33};
    //水平云台控制
    public static final byte[] LEVEL = {(byte) 0xEB, (byte) 0x90, (byte) 0x08, (byte) 0x02, (byte) 0x00,(byte) 0x32,(byte) 0x32};
    //垂直云台控制
    public static final byte[] ERECTIVE = {(byte) 0xEB, (byte) 0x90, (byte) 0x09, (byte) 0x02, (byte) 0x00,(byte) 0x32,(byte) 0x32};
    //LED照明
    public static final byte[] LED = {(byte) 0xEB, (byte) 0x90, (byte) 0x0F, (byte) 0x01, (byte) 0x32,(byte) 0x32};
}
