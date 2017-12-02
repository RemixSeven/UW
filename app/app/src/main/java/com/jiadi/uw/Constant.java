package com.jiadi.uw;

import android.util.Log;

/**
 * Created by Administrator on 2017/7/5.
 */

public class Constant {

    public static final int COMMAND_LENGTH = 5;
    public static final int COMMAND_RADIOX = 16;

    public static class CommandArray {

        public byte mCmd1 = 0;
        public byte mCmd2 = 0;
        public byte mCmd3 = 0;
        public CommandArray (int cmd1, int cmd2, int cmd3) {
            mCmd1 = (byte)cmd1;
            mCmd2 = (byte)cmd2;
            mCmd3 = (byte)cmd3;
        }

        public CommandArray (String cmdLine) {
            int icmd1 = -1;
            int icmd2 = -1;
            int icmd3 = -1;

            if (cmdLine != null
                    && (cmdLine.startsWith("FF") || cmdLine.startsWith("ff"))
                    && (cmdLine.endsWith("FF") || cmdLine.endsWith("ff"))
                    && cmdLine.length() == COMMAND_LENGTH*2 ) {
                String cmd1 = cmdLine.substring(2, 4);
                String cmd2 = cmdLine.substring(4, 6);
                String cmd3 = cmdLine.substring(6, 8);

                try {
                    icmd1 = Integer.parseInt(cmd1, COMMAND_RADIOX);
                    icmd2 = Integer.parseInt(cmd2, COMMAND_RADIOX);
                    icmd3 = Integer.parseInt(cmd3, COMMAND_RADIOX);
                } catch (Exception e) {
                    icmd1 = icmd2 = icmd3 = -1;
                }

                if (icmd1 >= 0 && icmd2 >= 0 && icmd3 >= 0) {
                    mCmd1 = (byte)icmd1;
                    mCmd2 = (byte)icmd2;
                    mCmd3 = (byte)icmd3;

                } else {
                    Log.i("Constant", "uncorrect command:" + cmdLine
                            + " cmd1=" + icmd1
                            + " cmd2=" + icmd2
                            + " cmd3=" + icmd3);
                }
            } else {
                Log.i("Constant", "error format command:" + cmdLine
                        + " cmd1=" + icmd1
                        + " cmd2=" + icmd2
                        + " cmd3=" + icmd3);
            }
        }

        public boolean isValid() {
            if (mCmd1 != 0 || mCmd2 != 0 || mCmd3 != 0) {
                return true;
            } else {
                return false;
            }
        }
    }
}
