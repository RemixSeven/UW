package cn.mazhewei.pan;

import android.app.Activity;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * 日志操作类
 */
public class Log {
    private Context context;
    private TextView tv_log;
    private static boolean isReady;

    static {
        isReady = false;
    }

    private Log(Context context, TextView tv_log) {
        if (tv_log == null) throw new IllegalArgumentException("TextView is null.");
        this.tv_log = tv_log;
        this.context = context;
        tv_log.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public static Log getInstance(Context context, TextView tv_log) {
        isReady = true;
        return new Log(context, tv_log);
    }

    public void setTv_log(TextView tv_log) {
        this.tv_log = tv_log;
    }

    /**
     * 写入日志
     *
     * @param log String 待写入的日志
     */
    public void l(final String log) {
        if (isReady) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_log.append(tools.getTime());
                    tv_log.append(" ");
                    tv_log.append(log);
                    tv_log.append("\n");
                    int offset = tv_log.getLineCount() * tv_log.getLineHeight();
                    if (offset > tv_log.getHeight()) {
                        tv_log.scrollTo(0, offset - tv_log.getHeight());
                    }
                }
            });
        } else
            throw new IllegalStateException("Log is't ready!");
    }
}
