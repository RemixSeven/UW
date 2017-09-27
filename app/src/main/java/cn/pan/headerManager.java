package cn.pan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 沉浸式状态栏操作类
 * <p>
 * 注意：要使用此类，必须在对应的布局中引入public_header
 * 引入方法：<include layout="@layout/public_header" />
 * 将activity中，将类的实例声明为类的成员变量
 * 如：
 * private headerManager manager;
 * 在onCreate方法中实例化成员变量，如
 * manager = new headerManager(this, R.layout.activity_main);
 * 调用getLayout方法，设置activity布局
 * setContentView(manager.getLayout());
 * 注意：要使用其他方法，你必须首先完成布局的设置且至少调用过一次getLayout方法
 * //设置头部标题
 * manager.setHeaderTitle("我是头部标题");
 * //设置返回信息(左侧)
 * manager.setHeaderTips("消息");
 * //设置标题栏颜色(要使用RGB颜色，你需要调用Color.rgb方法)
 * manager.setHeaderColor(Color.rgb(251, 144, 140));
 * //设置右侧消息图标
 * manager.setHeaderIMG(R.drawable.ic_collect_selected);
 * //设置点击事件
 * manager.setClickListener(manager.ID_RETURN, new View.OnClickListener() {
 * <p>
 * /@Override public void onClick(View v) {
 * //响应点击事件
 * }
 * });
 * 更多方法请参考具体注释
 */
public class headerManager {
    public final int ID_RETURN = 0;
    public final int ID_TITLE = 1;
    public final int ID_MSG = 2;
    public final int ID_MSG_TEXT = 3;
    private int sysBarHeight = 0;
    private int headerHeight = 0;
    private Context context = null;
    private boolean isHigh = false;
    private int layoutID = -1;
    private View layout = null;
    private RelativeLayout layout_header = null;

    /**
     * 唯一构造函数
     *
     * @param context  Context 上下文
     * @param layoutID int Activity对应布局ID
     */
    public headerManager(Context context, int layoutID) {
        isHigh = Build.VERSION.SDK_INT >= 19;
        this.context = context;
        this.layoutID = layoutID;
        if (isHigh) {
            //当前安卓版本>=4.4时，初始化系统状态栏高度
            int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            sysBarHeight = context.getResources().getDimensionPixelSize(resId);
        }
    }

    /**
     * 当使用沉浸式头部时，使用该方法取得view对象
     * view对象传入setContentView方法，即可完成界面显示初始化
     * 界面必须已经引入了沉浸式头部
     *
     * @return View Activity界面View
     */
    public View getLayout() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //取得布局
        layout = layoutInflater.inflate(layoutID, null);
        if (layout == null) throw new IllegalArgumentException("layoutID illegal.");
        layout_header = (RelativeLayout) layout.findViewById(R.id.layout_header);
        if (layout_header == null)
            throw new IllegalStateException("you must include public_header in your layout files!");
        ViewGroup.LayoutParams params = layout_header.getLayoutParams();
        headerHeight = params.height;
        if (isHigh) {
            //5.0以上版本系统
            params.height += sysBarHeight;
            layout_header.setLayoutParams(params);
        }
        return layout;
    }


    /**
     * 重设Manager信息
     *
     * @param context  Context 上下文
     * @param layoutID int Activity对应布局ID
     */
    public void reset(Context context, int layoutID) {
        this.context = context;
        this.layoutID = layoutID;
        if (isHigh) {
            //当使用沉浸式头部且当前安卓版本>5.0时，初始化系统状态栏高度
            int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            sysBarHeight = context.getResources().getDimensionPixelSize(resId);
        } else {
            sysBarHeight = 0;
        }
    }

    /**
     * 设置沉浸式标题栏头部标题
     *
     * @param title String 要设置的标题
     */
    public void setHeaderTitle(String title) {
        if (layout == null || layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        TextView tv_title = (TextView) layout_header.findViewById(R.id.tv_header_title);
        tv_title.setText(title);
    }

    /**
     * 设置沉浸式标题栏头部左侧返回提示信息
     *
     * @param tips String 要设置的标题
     */
    public void setHeaderTips(String tips) {
        if (layout == null || layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        TextView tv_title = (TextView) layout_header.findViewById(R.id.tv_header_return_tips);
        tv_title.setText(tips);
    }

    /**
     * 设置沉浸式标题栏头部右侧图标
     *
     * @param resID int 要设置的图标资源ID
     */
    public void setHeaderIMG(int resID) {
        if (layout == null || layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        ImageView iv_msg = (ImageView) layout_header.findViewById(R.id.iv_header_msg);
        iv_msg.setImageResource(resID);
    }

    /**
     * 设置沉浸式标题栏头部右侧图标
     *
     * @param bitmap Bitmap 要设置的图标Bitmap
     */
    public void setHeaderIMG(Bitmap bitmap) {
        if (layout == null || layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        ImageView iv_msg = (ImageView) layout_header.findViewById(R.id.iv_header_msg);
        iv_msg.setImageBitmap(bitmap);
    }

    /**
     * 设置标题栏颜色
     *
     * @param color int 要设置的颜色
     */
    public void setHeaderColor(int color) {
        if (layout == null || layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        layout_header.setBackgroundColor(color);
    }

    /**
     * 隐藏头部标题栏的返回信息
     */
    public void hideTips() {
        if (layout == null || layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        layout_header.findViewById(R.id.tv_header_return_tips).setVisibility(View.INVISIBLE);
    }

    /**
     * 隐藏头部标题栏的右侧图标
     */
    public void hideSMG() {
        if (layout == null || layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        layout_header.findViewById(R.id.iv_header_msg).setVisibility(View.INVISIBLE);
        layout_header.findViewById(R.id.iv_header_msg_text).setVisibility(View.GONE);
    }

    /**
     * 显示头部标题栏的返回信息
     */
    public void showTips() {
        if (layout == null || layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        layout_header.findViewById(R.id.tv_header_return_tips).setVisibility(View.VISIBLE);
    }

    /**
     * 显示头部标题栏的右侧图标
     */
    public void showSMG() {
        if (layout == null || layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        layout_header.findViewById(R.id.iv_header_msg).setVisibility(View.VISIBLE);
        layout_header.findViewById(R.id.iv_header_msg_text).setVisibility(View.GONE);
    }

    /**
     * 为标题上的项目设置点击事件
     *
     * @param ID       int 需要为哪个项目设置点击事件
     * @param listener OnClickListener 点击事件Listener
     */
    public void setClickListener(int ID, View.OnClickListener listener) {
        if (layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        switch (ID) {
            case ID_RETURN:
                layout_header.findViewById(R.id.tv_header_return_tips).setOnClickListener(listener);
                break;
            case ID_TITLE:
                layout_header.findViewById(R.id.tv_header_title).setOnClickListener(listener);
                break;
            case ID_MSG:
                layout_header.findViewById(R.id.iv_header_msg).setOnClickListener(listener);
                break;
            case ID_MSG_TEXT:
                layout_header.findViewById(R.id.iv_header_msg_text).setOnClickListener(listener);
                break;
        }
    }

    /**
     * 将头部右侧图标替换成文字按钮
     *
     * @param text 按钮文字
     */
    public void setIMG2TXT(String text) {
        if (layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        //隐藏原有图标
        layout_header.findViewById(R.id.iv_header_msg).setVisibility(View.GONE);
        //设置新的文本
        TextView tv = (TextView) layout_header.findViewById(R.id.iv_header_msg_text);
        tv.setText(text);
        tv.setVisibility(View.VISIBLE);
    }

    /**
     * 将头部右侧图标替换成文字按钮
     */
    public void setTXT2IMG() {
        if (layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        //隐藏原有文本
        layout_header.findViewById(R.id.iv_header_msg_text).setVisibility(View.GONE);
        //显示图标
        layout_header.findViewById(R.id.iv_header_msg).setVisibility(View.VISIBLE);
    }

    /**
     * 修改头部文本颜色
     *
     * @param ID    int 要修改颜色的控件
     * @param color int 需要设置的颜色
     */
    public void setTextColor(int ID, int color) {
        if (layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        TextView tv = null;
        switch (ID) {
            case ID_RETURN:
                tv = (TextView) layout_header.findViewById(R.id.tv_header_return_tips);
                break;
            case ID_TITLE:
                tv = (TextView) layout_header.findViewById(R.id.tv_header_title);
                break;
            case ID_MSG:
                throw new UnsupportedOperationException("This method only works on TextView.");
            case ID_MSG_TEXT:
                tv = (TextView) layout_header.findViewById(R.id.iv_header_msg_text);
                break;
        }
        assert tv != null;
        tv.setTextColor(color);
    }

    /**
     * 隐藏整个头部
     */
    public void hideHeader() {
        if (layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        ViewGroup.LayoutParams params = layout_header.getLayoutParams();
        if (isHigh) {
            params.height = sysBarHeight;
        } else {
            params.height = 0;
        }
        layout_header.setLayoutParams(params);
    }

    /**
     * 显示整个头部
     */
    public void showHeader() {
        if (layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        ViewGroup.LayoutParams params = layout_header.getLayoutParams();
        if (isHigh) {
            params.height = sysBarHeight + headerHeight;
        } else {
            params.height = headerHeight;
        }
        layout_header.setLayoutParams(params);
    }

    /**
     * 设置标题左侧返回操作
     * 这是一个快捷设置，要设置其他类型的操作，请使用setClickListener方法
     */
    public void setReturn() {
        if (layout_header == null)
            throw new UnsupportedOperationException("you need call getLayout first.");
        TextView tv = (TextView) layout_header.findViewById(R.id.tv_header_return_tips);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
    }
}