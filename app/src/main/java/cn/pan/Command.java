package cn.pan;

public class Command {
    //小车自检指令
    public static final byte[] CHECK = {(byte) 0xEB, (byte) 0x90, (byte) 0x46, (byte) 0x00, (byte) 0x00};
    //回传速率指令
    public static final byte[] SPEED = {(byte) 0xEB, (byte) 0x90, (byte) 0xAA, (byte) 0x01, (byte) 0x00, (byte) 0x00};
    public static final byte[] deepth = {(byte) 0xEB, (byte) 0x90, (byte) 0x06, (byte) 0x00, (byte) 0x00};
    //打开LED
    public static final byte[] LEDON = {(byte) 0xEB, (byte) 0x90, (byte) 0x0F,(byte) 0x01, (byte) 0x5A, (byte) 0x5A};
    //关闭LED
    public static final byte[] LEDOFF = {(byte) 0xEB, (byte) 0x90, (byte) 0x0F, (byte) 0x01,(byte) 0x00, (byte) 0x00};
    //水上板左推进器油门最大
    public static final byte[] Y91_left_max = {(byte) 0xEB, (byte) 0x90, (byte) 0x0A,(byte) 0x02, (byte) 0xEE,(byte) 0x02, (byte)0xF0};
    //水上板右推进器油门最大
    public static final byte[] Y91_right_max = {(byte) 0xEB, (byte) 0x90, (byte) 0x0B, (byte) 0x02,(byte) 0xEE,(byte) 0x02, (byte)0xF0};
    //水上板左推进器油门为0
    public static final byte[] Y91_left_mid = {(byte) 0xEB, (byte) 0x90, (byte) 0x0A,(byte) 0x02, (byte) 0xF4,(byte) 0x01, (byte)0xF5};
    //水上板右推进器油门为0
    public static final byte[] Y91_right_mid = {(byte) 0xEB, (byte) 0x90, (byte) 0x0B, (byte) 0x02,(byte) 0xF4,(byte) 0x01, (byte)0xF5};
    //水上板右推进器油门反向最大
    public static final byte[] Y91_left_min = {(byte) 0xEB, (byte) 0x90, (byte) 0x0A, (byte) 0x02,(byte) 0x5E,(byte) 0x01, (byte)0x5F};
    //水上板右推进器油门反向最大
    public static final byte[] Y91_right_min = {(byte) 0xEB, (byte) 0x90, (byte) 0x0B, (byte) 0x02,(byte) 0x5E,(byte) 0x01, (byte)0x5F};
    //放线舵机正向速度最大
    public static final byte[] Put_steering_gear_up= {(byte) 0xEB, (byte) 0x90, (byte) 0x07, (byte) 0x03,(byte) 0x00,(byte) 0x96, (byte) 0x01,(byte)0x97};
    //放线舵机速度为0
    public static final byte[] Put_steering_gear_o= {(byte) 0xEB, (byte) 0x90, (byte) 0x07, (byte) 0x03,(byte) 0x02,(byte) 0x96,(byte) 0x00, (byte)0x98};
    //放线舵机反向速度最大
    public static final byte[] Put_steering_gear_down= {(byte) 0xEB, (byte) 0x90, (byte) 0x07, (byte) 0x03,(byte) 0x01,(byte) 0x96,(byte) 0x01, (byte)0x98};
    //水下板左推进器油门最大
    public static final byte[] Y91_down_left_max = {(byte) 0xEB, (byte) 0x90, (byte) 0x0D,(byte) 0x02, (byte) 0xEE,(byte) 0x02, (byte)0xF0};
    //水下板左推进器油门为0
    public static final byte[] Y91_down_left_mid = {(byte) 0xEB, (byte) 0x90, (byte) 0x0D,(byte) 0x02, (byte) 0xF4,(byte) 0x01, (byte)0xF5};
    //水下板左推进器油门反向最大
    public static final byte[] Y91_down_left_min = {(byte) 0xEB, (byte) 0x90, (byte) 0x0D,(byte) 0x02, (byte) 0x5E,(byte) 0x01, (byte)0x5F};
    //水下板右推进器油门最大
    public static final byte[] Y91_down_right_max = {(byte) 0xEB, (byte) 0x90, (byte) 0x0E,(byte) 0x02, (byte) 0xEE,(byte) 0x02, (byte)0xF0};
    //水下板右推进器油门为0
    public static final byte[] Y91_down_right_mid = {(byte) 0xEB, (byte) 0x90, (byte) 0x0E,(byte) 0x02, (byte) 0xF4,(byte) 0x01, (byte)0xF5};
    //水下板右推进器油门反向最大
    public static final byte[] Y91_down_right_min = {(byte) 0xEB, (byte) 0x90, (byte) 0x0E,(byte) 0x02, (byte) 0x5E,(byte) 0x01, (byte)0x5F};
    //水下板垂直推进器油门最大
    public static final byte[] Y91_down_vertical_max = {(byte) 0xEB, (byte) 0x90, (byte) 0x0C,(byte) 0x02, (byte) 0xEE,(byte) 0x02, (byte)0xF0};
    //水下板垂直推进器油门为0
    public static final byte[] Y91_down_vertical_mid = {(byte) 0xEB, (byte) 0x90, (byte) 0x0C,(byte) 0x02, (byte) 0x2C,(byte) 0x01, (byte)0x2D};
    //水下板垂直推进器油门反向最大
    public static final byte[] Y91_down_vertical_min = {(byte) 0xEB, (byte) 0x90, (byte) 0x0C,(byte) 0x02, (byte) 0xC8,(byte) 0x00, (byte)0xC8};
    //
    public static final byte[] Gear_left= {(byte) 0xEB, (byte) 0x90, (byte) 0x08, (byte) 0x02,(byte) 0x3C,(byte) 0x00, (byte)0x3C};
    //
    public static final byte[] Gear_right= {(byte) 0xEB, (byte) 0x90, (byte) 0x08, (byte) 0x02,(byte) 0x78,(byte) 0x00, (byte)0x78};
    //
    public static final byte[] Gear_o= {(byte) 0xEB, (byte) 0x90, (byte) 0x08, (byte) 0x02,(byte) 0x00,(byte) 0x00, (byte)0x00};
}
