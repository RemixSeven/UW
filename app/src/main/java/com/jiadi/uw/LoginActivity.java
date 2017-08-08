package com.jiadi.uw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiadi.uw.domain.User;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;



public class LoginActivity extends Activity implements View.OnClickListener{
    private EditText username;
    private EditText password;
    private TextView title_text;
    private ImageView clear_username;
    private ImageView clear_password;
    private CheckBox login_checkbox;
    private TextView forget_password;
    private Button login;
    private Button register;
    private Button repassword;
    private EditText login_code;
    private Button login_getCode;
    private ImageView login_clear_code;
    private RelativeLayout getCode;
    private boolean isUserNumberNull = true;
    private boolean isPasswordNull = true;
    private boolean isMsgCodeNull = true;
    private String Username;
    private String Password;
    private String msgCode;
    private Handler mHandler;
    private static String TAG="LoginActivity";
    private int secCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"110b1f4775a23d1ec258d25cecb2e318");
        setContentView(R.layout.activity_login);
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        clear_username= (ImageView) findViewById(R.id.clear_username);
        clear_password= (ImageView) findViewById(R.id.clear_password);
        login= (Button) findViewById(R.id.login);
        register= (Button) findViewById(R.id.register);
        forget_password= (TextView) findViewById(R.id.forget_password);
        login_checkbox= (CheckBox) findViewById(R.id.login_checkBox);
        //title_text= (TextView) findViewById(R.id.title_text);
        repassword= (Button) findViewById(R.id.reset_password);
        getCode= (RelativeLayout) findViewById(R.id.getCode);
        login_code= (EditText) findViewById(R.id.login_code);
        login_getCode= (Button) findViewById(R.id.login_getCode);
        login_clear_code= (ImageView) findViewById(R.id.login_clear_code);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        repassword.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        clear_password.setOnClickListener(this);
        clear_username.setOnClickListener(this);
        login_getCode.setOnClickListener(this);
        login_clear_code.setOnClickListener(this);
        login_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                password.setSelection(password.length());
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isUserNumberNull= TextUtils.isEmpty(username.getText());
                clear_username.setVisibility(isUserNumberNull?View.GONE:View.VISIBLE);
                clear_username.setEnabled(!isUserNumberNull);
                login.setEnabled((isUserNumberNull||isPasswordNull ? false : true));
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isPasswordNull=TextUtils.isEmpty(password.getText());
                clear_password.setVisibility(isPasswordNull?View.GONE:View.VISIBLE);
                clear_password.setEnabled(!isPasswordNull);
                login.setEnabled((isUserNumberNull||isPasswordNull ? false : true));
            }
        });
        login_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isMsgCodeNull = TextUtils.isEmpty(login_code.getText());
                login_clear_code.setVisibility(isMsgCodeNull ? View.GONE : View.VISIBLE);
                login_clear_code.setEnabled(!isMsgCodeNull);
                repassword.setEnabled((isUserNumberNull||isMsgCodeNull||isPasswordNull
                        ? false : true));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear_username:
                username.setText("");
                clear_username.setVisibility(View.GONE);
                break;
            case R.id.clear_password:
                password.setText("");
                clear_password.setVisibility(View.GONE);
                break;
            case R.id.forget_password:
                title_text.setText(R.string.re_password);
                login.setVisibility(View.GONE);
                repassword.setVisibility(View.VISIBLE);
                getCode.setVisibility(View.VISIBLE);
                break;
            case R.id.login_getCode:
                Username=username.getText().toString();
                if(isUserNumberNull(Username)){
                    UserManager.getInstance(new MsgSendCallback()).sendMsgCode(Username);
                }else {
                    Toast.makeText(LoginActivity.this,R.string.phone_number_incorrect,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.login_clear_code:
                login_code.setText("");
                login_clear_code.setVisibility(View.GONE);
                break;
            case R.id.reset_password:

                Username=username.getText().toString();
                msgCode=login_code.getText().toString();
                Password=password.getText().toString();
                login.setVisibility(View.VISIBLE);
                getCode.setVisibility(View.GONE);
                repassword.setVisibility(View.GONE);
                //import cn.bmob.v3.listener.ResetPasswordByCodeListener一直导不进去，所以没写完。。。
                /*if (isUserNumberNull(Username)&&isCode(msgCode)&&!!TextUtils.isEmpty(Password)){
                    UserManager.getInstance(new resetPwd()).resetPwd(Username,msgCode,Password);
                }else {
                    Toast.makeText(LoginActivity.this,R.string.register_input_incorrect,Toast.LENGTH_LONG).show();
                }*/
                break;
            case R.id.register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                Username=username.getText().toString();
                Password=password.getText().toString();
                if (!TextUtils.isEmpty(Username)&&!TextUtils.isEmpty(Password)){
                    UserManager.getInstance(new LoginCallBack()).login(Username,Password);
                }else {
                    Toast.makeText(LoginActivity.this,R.string.login_empty,Toast.LENGTH_LONG).show();
                }
                break;
            default:break;
        }
    }

    private boolean isCode(String msgCode) {
        if (!msgCode.matches("^[0-9]*$")){
            return false;
        }
        if (TextUtils.isEmpty(msgCode)) {
            return false;
        }
        return true;
    }

    private boolean isUserNumberNull(String username) {
        if (TextUtils.isEmpty(username)) {
            return false;
        }
        if (!username.matches("^[0-9]*$")) {
            Log.i(TAG, "isPhoneNumber: match error--"+username);
            return false;
        }
        if (username.length() != 11) {
            Log.i(TAG, "isPhoneNumber: length error--"+username);
            return false;
        }
        if (username.indexOf(0) == '1') {
            Log.i(TAG, "isPhoneNumber: start error--"+username);
            return false;
        }
        return true;
    }


    private class LoginCallBack implements IUserListener {
        @Override
        public void onMsgSendSuccess() {

        }

        @Override
        public void onMsgSendFailure() {

        }



        @Override
        public void onLoginSuccess() {
            Intent intent=new Intent(LoginActivity.this,UWActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(LoginActivity.this, R.string.login_success,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLoginFailure() {
            Toast.makeText(LoginActivity.this, R.string.login_failure,Toast.LENGTH_LONG).show();
        }



        @Override
        public void onSignUpSuccess(User user) {

        }

        @Override
        public void onSignUpFailure(BmobException e) {

        }
    }

    private class MsgSendCallback implements IUserListener {
        @Override
        public void onMsgSendSuccess() {
            Toast.makeText(LoginActivity.this, R.string.sms_code_send_success,Toast.LENGTH_LONG).show();
            //验证码发送成功，倒计时
            setCodeTimeDown();

        }

        @Override
        public void onMsgSendFailure() {
            Toast.makeText(LoginActivity.this, R.string.sms_code_send_failure,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLoginSuccess() {

        }

        @Override
        public void onLoginFailure() {

        }

        @Override
        public void onSignUpSuccess(User user) {

        }

        @Override
        public void onSignUpFailure(BmobException e) {

        }
    }

    private void setCodeTimeDown() {
        login_getCode.setEnabled(false);
        final Timer timer = new Timer();
        secCount = 60;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        secCount--;
                        login_getCode.setText(secCount+" s");
                        if (secCount<=0) {
                            timer.cancel();
                            login_getCode.setText(R.string.reSend);
                            login_getCode.setEnabled(true);
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask,1000,1000);
    }


}
