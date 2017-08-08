package com.jiadi.uw;

public class Command {
    //小车自检指令
    public static final byte[] CHECK = {(byte) 0xEB, (byte) 0x90, (byte) 0x46, (byte) 0x00, (byte) 0x00};
    //回传速率指令
    public static final byte[] SPEED = {(byte) 0xEB, (byte) 0x90, (byte) 0xAA, (byte) 0x01, (byte) 0x00, (byte) 0x00};
}
