package cn.mazhewei.pan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * 安卓操作封装(所有方法均为静态方法)
 * 类名.方法名调用或者导包后直接使用方法名
 * 导包 import static 包名.tools.*
 * Author:小马
 * 参考资料 http://blog.csdn.net/lmj623565791/article/details/38965311
 * Date:2017/2/9
 */
public class tools {
    /**
     * 私有构造，禁止实例化
     * 静态方法无需实例化
     */
    private tools() {
        throw new UnsupportedOperationException("Can't be instantiated");
    }

    /**
     * 打开新的页面，忽略页面返回
     * 典型用法：
     * tools.page(this, MainActivity.class);
     *
     * @param context Context 上下文
     * @param cls     Class 需要开启的页面class
     */
    public static void page(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * 打开新的页面，忽略页面返回，并向新页面传值
     * 当传递多个数值时，请使用Bundle类型传参
     * 支持的参数类型：
     * Byte\Long\Float\Boolean\Integer\String\Double\Short\Bundle
     * 典型用法：
     * tools.page(this, MainActivity.class, "data", 123);
     *
     * @param context Context 上下文
     * @param cls     Class 需要开启的页面class
     * @param key     String 传递的参数对应的key，当使用Bundle传参时，key值被忽略
     * @param extra   Object 需要传递的额外参数
     * @throws UnsupportedOperationException 不支持的参数类型
     */
    public static void page(Context context, Class<?> cls, String key, Object extra) {
        Intent intent = new Intent(context, cls);
        if (extra instanceof String) {
            intent.putExtra(key, (String) extra);
        } else if (extra instanceof Integer) {
            intent.putExtra(key, (Integer) extra);
        } else if (extra instanceof Boolean) {
            intent.putExtra(key, (Boolean) extra);
        } else if (extra instanceof Float) {
            intent.putExtra(key, (Float) extra);
        } else if (extra instanceof Long) {
            intent.putExtra(key, (Long) extra);
        } else if (extra instanceof Byte) {
            intent.putExtra(key, (Byte) extra);
        } else if (extra instanceof Double) {
            intent.putExtra(key, (Double) extra);
        } else if (extra instanceof Short) {
            intent.putExtra(key, (Short) extra);
        } else if (extra instanceof Bundle) {
            intent.putExtras((Bundle) extra);
        } else {
            throw new UnsupportedOperationException("UnsupportedClass");
        }
        context.startActivity(intent);
    }

    /**
     * 打开新的页面，接收页面返回，并同时支持向新页面传值
     * 当传递多个数值时，请使用Bundle类型传参
     * 支持的参数类型：
     * Byte\Long\Float\Boolean\Integer\String\Double\Short\Bundle
     * 如果不需要传值，将key和extra参数传入null
     * 注意：要接收返回，需要在新开启的Activity调用setResult、finish方法
     * 并在调用本方法的Activity(或其他相关类中)中重写onActivityResult方法接收返回
     * 典型用法：
     * tools.page(this, MainActivity.class, 0, null, null);
     *
     * @param context     Context 上下文
     * @param cls         Class 需要开启的页面class
     * @param requestCode int 请求代码（请求编号）
     * @param key         String 传递的参数对应的key，当使用Bundle传参时，key值被忽略
     * @param extra       Object 需要传递的额外参数
     * @throws UnsupportedOperationException 不支持的参数类型
     */
    public static void page(Context context, Class<?> cls, int requestCode, String key, Object extra) {
        Intent intent = new Intent(context, cls);
        if (key != null) {
            if (extra instanceof String) {
                intent.putExtra(key, (String) extra);
            } else if (extra instanceof Integer) {
                intent.putExtra(key, (Integer) extra);
            } else if (extra instanceof Boolean) {
                intent.putExtra(key, (Boolean) extra);
            } else if (extra instanceof Float) {
                intent.putExtra(key, (Float) extra);
            } else if (extra instanceof Long) {
                intent.putExtra(key, (Long) extra);
            } else if (extra instanceof Byte) {
                intent.putExtra(key, (Byte) extra);
            } else if (extra instanceof Double) {
                intent.putExtra(key, (Double) extra);
            } else if (extra instanceof Short) {
                intent.putExtra(key, (Short) extra);
            } else if (extra instanceof Bundle) {
                intent.putExtras((Bundle) extra);
            } else {
                throw new UnsupportedOperationException("UnsupportedClass");
            }
        }
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 关闭页面
     * 典型用法：
     * tools.closePage(this);
     *
     * @param context Context 上下文
     */
    public static void closePage(Context context) {
        ((Activity) context).finish();
    }

    /**
     * findViewByID操作简化版(通过上下文)
     * 典型用法:
     * EditText et = tools.find(this, R.id.et);
     *
     * @param context Context 上下文
     * @param id      int 需要查找的ID
     * @return View   返回类型转换后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T find(Context context, int id) {
        View v = ((Activity) context).findViewById(id);
        return (T) v;
    }

    /**
     * findViewByID操作简化版(通过view)
     * 典型用法:
     * EditText et = tools.find(view, R.id.et);
     *
     * @param view View 查找来源视图(在什么中查找)
     * @param id   int 需要查找的ID
     * @return View   返回类型转换后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T find(View view, int id) {
        View v = view.findViewById(id);
        return (T) v;
    }

    /**
     * toast简化版(显示时间-短)
     * 典型用法：
     * tools.toast(this, "hello world!");
     *
     * @param context Context 上下文
     * @param tips    String 要显示的文本
     */
    public static void toast(Context context, String tips) {
        Toast.makeText(context, tips.trim(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast简化版(自定义显示长短)
     * 典型用法：
     * tools.toast(this, "hello world!", 1);
     *
     * @param context Context 上下文
     * @param tips    String 要显示的文本
     * @param length  int 0→短,1→长
     */
    public static void toast(Context context, String tips, int length) {
        Toast.makeText(context, tips.trim(), length).show();
    }

    /**
     * 显示安卓对话框
     * 样式：仅有确定(单个)按钮
     * 以下参数传入null，则使用默认设置
     * title→"标题"
     * text→"文本"
     * btOK→"确定"
     * 当iconID被设置为-1时，表示不使用图标
     * 典型用法：
     * tools.dialog(this, null, "提示", "Hello world", null, -1);
     *
     * @param context  Context 上下文
     * @param listener OnClickListener 确定按钮点击事件监听器
     * @param title    String 显示的对话框标题
     * @param text     String 显示的对话框内容
     * @param btOK     String 确定按钮文字
     * @param iconID   int 设置的图表资源ID
     */
    public static void dialog(Context context, DialogInterface.OnClickListener listener, String title, String text, String btOK, int iconID) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        if (title == null) title = "标题";
        if (text == null) text = "文本";
        if (btOK == null) btOK = "确定";
        if (iconID != -1)
            dialog.setIcon(iconID);
        dialog.setTitle(title);
        dialog.setMessage(text);
        dialog.setPositiveButton(btOK, listener);
        dialog.create().show();
    }

    /**
     * 显示安卓对话框
     * 样式：确定、取消(两个)按钮
     * 以下参数传入null，则使用默认设置
     * title→"标题"
     * text→"文本"
     * btOK→"确定"
     * btCancel→"取消"
     * 当iconID被设置为-1时，表示不使用图标
     * 典型用法：
     * tools.dialog(this, okListenner, cancelListenner, "提示", "Hello world", null, null, -1);
     *
     * @param context   Context 上下文
     * @param listener1 OnClickListener 确定按钮点击事件监听器
     * @param listener2 OnClickListener 取消按钮点击事件监听器
     * @param title     String 显示的对话框标题
     * @param text      String 显示的对话框内容
     * @param btOK      String 确定按钮文字
     * @param btCancel  String 取消按钮文字
     * @param iconID    int 设置的图标资源ID
     */
    public static void dialog(Context context, DialogInterface.OnClickListener listener1, DialogInterface.OnClickListener listener2, String title, String text, String btOK, String btCancel, int iconID) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        if (title == null) title = "标题";
        if (text == null) text = "文本";
        if (btOK == null) btOK = "确定";
        if (btCancel == null) btCancel = "取消";
        if (iconID != -1)
            dialog.setIcon(iconID);
        dialog.setTitle(title);
        dialog.setMessage(text);
        dialog.setPositiveButton(btOK, listener1);
        dialog.setNegativeButton(btCancel, listener2);
        dialog.create().show();
    }

    /**
     * 显示安卓对话框
     * 样式：多选菜单
     * 以下参数传入null，则使用默认设置
     * title→"标题"
     * text→"文本"
     * btOK→"确定"
     * btCancel→"取消"
     * 典型用法：
     * tools.dialog(this, "提示", new String[]{"Hello world"}, listener);
     *
     * @param context  Context 上下文
     * @param title    String 显示的对话框标题
     * @param items    String 列表项
     * @param listener OnClickListener 列表项点击事件
     */
    public static void dialog(Context context, String title, String[] items, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        if (title == null) title = "标题";
        dialog.setTitle(title);
        dialog.setItems(items, listener);
        dialog.create().show();
    }

    /**
     * 显示安卓对话框(扩展)
     * 允许将外部View传入对话框以便在对话框中显示
     * 样式：确定、取消(两个)按钮
     * 以下参数传入null，则使用默认设置
     * title→"标题"
     * btOK→"确定"
     * btCancel→"取消"
     * 典型用法：
     * tools.dialogEx(this, okListenner, cancelListenner, "提示", etInput, null, null);
     *
     * @param context   Context 上下文
     * @param listener1 OnClickListener 确定按钮点击事件监听器
     * @param listener2 OnClickListener 取消按钮点击事件监听器
     * @param title     String 显示的对话框标题
     * @param exView    View 外部传入的View
     * @param btOK      String 确定按钮文字
     * @param btCancel  String 取消按钮文字
     */
    public static void dialogEx(Context context, DialogInterface.OnClickListener listener1, DialogInterface.OnClickListener listener2, String title, View exView, String btOK, String btCancel) {
        if (exView == null) throw new InvalidParameterException("InvalidParameter:exView");
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        if (title == null) title = "标题";
        if (btOK == null) btOK = "确定";
        if (btCancel == null) btCancel = "取消";
        dialog.setTitle(title);
        dialog.setView(exView);
        dialog.setPositiveButton(btOK, listener1);
        dialog.setNegativeButton(btCancel, listener2);
        dialog.create().show();
    }

    /**
     * 快速获取EditText中的文本值
     * 典型用法：
     * String et_text = tools.getEtString(et);
     *
     * @param et EditText 要获取数据的EditText对象
     * @return String 返回文本String
     */
    @NonNull
    public static String getEtString(EditText et) {
        return et.getText().toString().trim();
    }

    /**
     * 快速获取EditText中的文本值
     * 典型用法：
     * String et_text = tools.getEtString(this, R.id.textView);
     *
     * @param context Context 上下文
     * @param et_id   EditText 要获取数据的EditText的ID
     * @return String 返回文本String
     */
    @NonNull
    public static String getEtString(Context context, int et_id) {
        return ((EditText) tools.find(context, et_id)).getText().toString().trim();
    }

    /**
     * SharedPreferences操作封装
     * 向SharedPreferences传入值并保存
     * filename传入null，则使用包名作为文件名
     * 典型用法：
     * tools.spPut(this, null, "login_status", true);
     *
     * @param context  Context 上下文
     * @param filename String 保存在磁盘上的文件名
     * @param key      String 保存的键值对的键名
     * @param object   Object 保存的值
     */
    public static void spPut(Context context, String filename, String key, Object object) {
        if (filename == null) filename = context.getPackageName().replace(".", "_");
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.apply();
    }

    /**
     * SharedPreferences操作封装
     * 从SharedPreferences读取值并返回
     * filename传入null，则使用包名作为文件名
     * 典型用法：
     * Boolean loginStatus = (Boolean) tools.spGet(this, null, "login_status", false);
     *
     * @param context       Context 上下文
     * @param filename      String 保存在磁盘上的文件名
     * @param key           String 保存的键值对的键名
     * @param defaultObject Object 默认值(该值不能为null)
     * @return Object 返回读取到的数据，如果读取失败，则返回默认值参数
     */
    public static Object spGet(Context context, String filename, String key, Object defaultObject) {
        if (filename == null) filename = context.getPackageName().replace(".", "_");
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            return defaultObject;
        }
    }

    /**
     * SharedPreferences操作封装
     * 从SharedPreferences删除值
     * filename传入null，则使用包名作为文件名
     * 典型用法：
     * tools.spRemove(this, null, "login_status");
     *
     * @param context  Context 上下文
     * @param filename String 保存在磁盘上的文件名
     * @param key      String 保存的键值对的键名
     */
    public static void spRemove(Context context, String filename, String key) {
        if (filename == null) filename = context.getPackageName().replace(".", "_");
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * SharedPreferences操作封装
     * 清空SharedPreferences值
     * filename传入null，则使用包名作为文件名
     * 典型用法：
     * tools.spClear(this, null);
     *
     * @param context  Context 上下文
     * @param filename String 保存在磁盘上的文件名
     */
    public static void spClear(Context context, String filename) {
        if (filename == null) filename = context.getPackageName().replace(".", "_");
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * SharedPreferences操作封装
     * 查询SharedPreferences键值是否存在
     * filename传入null，则使用包名作为文件名
     * 典型用法：
     * boolean loginStatusExist = tools.spContains(this, null, "login_status");
     *
     * @param context  Context 上下文
     * @param filename String 保存在磁盘上的文件名
     * @param key      String 保存的键值对的键名
     * @return boolean 键值对是否存在
     */
    public static boolean spContains(Context context, String filename, String key) {
        if (filename == null) filename = context.getPackageName().replace(".", "_");
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * SharedPreferences操作封装
     * 查询SharedPreferences所有键值对
     * filename传入null，则使用包名作为文件名
     * 典型用法：
     * Map<String, ?> result = tools.spGetAll(this, null);
     *
     * @param context  Context 上下文
     * @param filename String 保存在磁盘上的文件名
     * @return Map sp中的所有键值对信息
     */
    public static Map<String, ?> spGetAll(Context context, String filename) {
        if (filename == null) filename = context.getPackageName().replace(".", "_");
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * MD5加密封装
     * 典型用法：
     * String pwdCoded = tools.getMD5("123456");
     *
     * @param val String 需要加密的字符串
     * @return String 加密完成后的字符串
     */
    @Nullable
    public static String getMD5(String val) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes("UTF-8"));
            //得到加密数据
            byte[] m = md5.digest();
            StringBuilder sb = new StringBuilder();
            //转换成String串
            for (byte value : m) {
                if (Integer.toHexString(0xff & value).length() == 1) {
                    sb.append("0").append(Integer.toHexString(0xff & value));
                } else {
                    sb.append(Integer.toHexString(0xff & value));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断SDCard是否可用
     * 典型用法：
     * boolean sdExist = tools.isSDCardEnable();
     *
     * @return boolean 返回SD卡是否可用
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     * 典型用法：
     * String sdPath = tools.getSDCardPath();
     *
     * @return String SD卡路径
     */
    @SuppressWarnings("WeakerAccess")
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取系统存储路径
     * 典型用法：
     * String rootPath = tools.getRootDirectoryPath();
     *
     * @return String 系统存储路径
     */
    @NonNull
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 获取APP_Data路径
     * 典型用法：
     * String dataPath = tools.getDataDirectoryPath();
     *
     * @return String APP_Data路径
     */
    @NonNull
    @SuppressWarnings("WeakerAccess")
    public static String getDataDirectoryPath() {
        return Environment.getDataDirectory().getAbsolutePath();
    }

    /**
     * 获取SDCard或者内部存储剩余可用容量字节数，单位byte
     * 典型用法：
     * long bytes = tools.getFreeBytes(true);
     *
     * @param isSD boolean 是否为SD卡
     * @return long 容量字节，SDCard或者内部存储可用空间
     */
    public static long getFreeBytes(boolean isSD) {
        String path;
        if (isSD) {
            path = getSDCardPath();
        } else {
            path = getDataDirectoryPath();
        }
        StatFs stat = new StatFs(path);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获得屏幕宽高度
     * 将返回结果数组，第一个元素为宽度，第二个元素为高度
     * 典型用法：
     * int[] screenSize = tools.getScreenSize(this);
     *
     * @param context Context 上下文
     * @return int[] 屏幕的宽高
     */
    public static int[] getScreenSize(Context context) {
        int[] array = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        array[0] = outMetrics.widthPixels;
        array[1] = outMetrics.heightPixels;
        return array;
    }

    /**
     * 输入框打开软键盘
     * 典型用法：
     * tools.openKeyboard(this, et);
     *
     * @param context Context 上下文
     * @param et      EditText 输入框
     */
    public static void openKeyboard(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 输入框关闭软键盘
     * 典型用法：
     * tools.closeKeyboard(this, et);
     *
     * @param context Context 上下文
     * @param et      EditText 输入框
     */
    public static void closeKeyboard(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * 判断网络是否连接
     * 注意：需要ACCESS_NETWORK_STATE权限
     * 典型用法：
     * Boolean isNetworkConnected = tools.isNetworkConnected(this);
     *
     * @param context Context 上下文
     * @return boolean 网络是否连接
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否为wifi连接
     * 注意：需要ACCESS_NETWORK_STATE权限
     * 典型用法：
     * Boolean isWifiConnected = tools.isWifiConnected(this);
     *
     * @param context Context 上下文
     * @return boolean 是否为wifi连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
        return true;
    }

    /**
     * 单位转换封装
     * dp转px
     *
     * @param context Context 上下文
     * @param dpVal   float 待转换的dp值
     * @return int 转换后的px值
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 单位转换封装
     * sp转px
     *
     * @param context Context 上下文
     * @param spVal   float 待转换的sp值
     * @return int 转换后的px值
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 单位转换封装
     * px转dp
     *
     * @param context Context 上下文
     * @param pxVal   float 待转换的px值
     * @return float 转换后的dp值
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * 单位转换封装
     * px转sp
     *
     * @param context Context 上下文
     * @param pxVal   float 待转换的px值
     * @return float 转换后的sp值
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 根据字符串获得APP内部资源
     * location支持的参数类型：
     * layout\string\drawable\animation\color
     * location传入null则使用默认drawable
     * 典型用法：
     * 获取字符串：
     * int id = tools.getRes(this, "app_name", "string", true);//等效于R.string.app_name
     * 获取APP内部图片：
     * Drawable drawable = tools.getRes(this, "ic_logo", null, false);
     *
     * @param context  Context 上下文
     * @param resName  String 资源名
     * @param location String 资源类型(资源所在位置)
     * @param isID     boolean 是否仅返回资源ID
     * @return 泛型 返回资源(或资源ID)
     * 返回的类型为:（所有的返回均为对象）
     * layout → XmlResourceParser
     * string → String
     * drawable → Drawable
     * animation → XmlResourceParser
     * color → Integer
     * ID → Integer
     * @throws UnsupportedOperationException 不支持的参数location
     */
    @SuppressWarnings("unchecked")
    public static <T> T getRes(Context context, String resName, String location, boolean isID) {
        if (location == null) location = "drawable";
        int resId = context.getResources().getIdentifier(resName, location, context.getPackageName());
        if (isID) return (T) Integer.valueOf(resId);
        switch (location) {
            case "layout":
                return (T) context.getResources().getLayout(resId);
            case "string":
                return (T) context.getResources().getString(resId);
            case "drawable":
                return (T) context.getResources().getDrawable(resId);
            case "animation":
                return (T) context.getResources().getAnimation(resId);
            case "color":
                return (T) Integer.valueOf(context.getResources().getColor(resId));
            default:
                throw new UnsupportedOperationException("UnsupportedType");
        }
    }

    /**
     * 将drawable转换为bitmap
     *
     * @param drawable Drawable 可绘制资源
     * @return Bitmap 返回bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 将bitmap转换为drawable
     *
     * @param context Context 上下文
     * @param bitmap  Bitmap 待转换的bitmap
     * @return Drawable 转换后的Drawable
     */
    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * 动态申请权限(在安卓6.0版本以上调用危险操作时必须调用本方法)
     * 要使用回调，在Activity中重写onRequestPermissionsResult方法
     * 这是一个异步方法，在执行相应功能前，你必须确保权限已经申请
     * (在合适的时机比如onCreate的最后申请)
     * 要实现更高级的权限控制和管理，推荐使用easyPermissions开源库。
     * 本方法仅能满足最基本的需求。
     * 典型用法：
     * 以申请照相机权限为例：
     * tools.applyPermission(this, new String[]{Manifest.permission.CAMERA}, 0);
     *
     * @param context     Context 上下文
     * @param permissions String数组 需要申请的权限内容
     *                    传入Manifest.permission.XXX
     * @param requestCode int 请求码
     */
    public static void applyPermission(Context context, String[] permissions, int requestCode) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                //如果当前未获得该权限则申请权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
            }
        }
    }

    /**
     * 获取当前系统时间字符串
     *
     * @return 返回时间字符串(不含日期)
     */
    public static String getTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        return df.format(new Date());
    }

    /**
     * 将字节数组转换为16进制字符串
     *
     * @param bytes 带转换的字节数组
     * @return 16进制表示的字符串
     */
    public static String binaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex;
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result += hex + " ";
        }
        return result;
    }
}
