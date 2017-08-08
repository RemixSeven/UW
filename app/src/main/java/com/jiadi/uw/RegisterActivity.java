package com.jiadi.uw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;


import com.jiadi.uw.domain.User;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 北 on 2017/7/23.
 */

public class RegisterActivity extends Activity implements View.OnClickListener{
    private ImageView titleBarBack;
    private EditText etPhoneNumber;
    private ImageView clearPhoneNumber;
    private EditText code;
    private Button getCode;
    private ImageView clearCode;
    private EditText etPassword;
    private CheckBox passwordCheckBox;
    private ImageView clearPassword;
    private EditText repassword;
    private ImageView clearRepassword;
    private Button register;

    private boolean isPhoneNumberNull = true;
    private boolean isMsgCodeNull = true;
    private boolean isPasswordNull = true;
    private boolean isRepasswordNull = true;
    private String phoneNumber;
    private String msgCode;
    private String password;
    private String RePassword;
    private static String TAG="RegisterActivity";
    public static final String INTENT_USER = "user";
    private int secCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        titleBarBack= (ImageView) findViewById(R.id.register_back);
        etPhoneNumber= (EditText) findViewById(R.id.register_phoneNumber);
        clearPhoneNumber= (ImageView) findViewById(R.id.register_clear_phoneNumber);
        code= (EditText) findViewById(R.id.register_code);
        getCode= (Button) findViewById(R.id.register_getCode);
        clearCode= (ImageView) findViewById(R.id.register_clear_code);
        etPassword= (EditText) findViewById(R.id.register_password);
        passwordCheckBox= (CheckBox) findViewById(R.id.register_password_checkBox);
        clearPassword= (ImageView) findViewById(R.id.register_clear_password);
        repassword= (EditText) findViewById(R.id.register_repassword);
        clearRepassword= (ImageView) findViewById(R.id.register_clear_repassword);
        register= (Button) findViewById(R.id.register);
        titleBarBack.setOnClickListener(this);
        clearPhoneNumber.setOnClickListener(this);
        getCode.setOnClickListener(this);
        register.setOnClickListener(this);
        clearPassword.setOnClickListener(this);
        clearCode.setOnClickListener(this);
        clearRepassword.setOnClickListener(this);
        passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    repassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    repassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPassword.setSelection(etPassword.length());
                repassword.setSelection(repassword.length());
            }
        });
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isPhoneNumberNull = TextUtils.isEmpty(etPhoneNumber.getText());
                clearPhoneNumber.setVisibility(isPhoneNumberNull ? View.GONE : View.VISIBLE);
                clearPhoneNumber.setEnabled(!isPhoneNumberNull);
                register.setEnabled((isPhoneNumberNull||isMsgCodeNull||isPasswordNull||isRepasswordNull
                        ? false : true));
            }
        });
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isMsgCodeNull = TextUtils.isEmpty(code.getText());
                clearCode.setVisibility(isMsgCodeNull ? View.GONE : View.VISIBLE);
                clearCode.setEnabled(!isMsgCodeNull);
                register.setEnabled((isPhoneNumberNull||isMsgCodeNull||isPasswordNull||isRepasswordNull
                        ? false : true));
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isPasswordNull = TextUtils.isEmpty(etPassword.getText());
                clearPassword.setVisibility(isPasswordNull ? View.GONE : View.VISIBLE);
                clearPassword.setEnabled(!isPasswordNull);
                register.setEnabled((isPhoneNumberNull||isMsgCodeNull||isPasswordNull||isRepasswordNull
                        ? false : true));
            }
        });
        repassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isRepasswordNull=TextUtils.isEmpty(repassword.getText());
                clearRepassword.setVisibility(isRepasswordNull?View.GONE : View.VISIBLE);
                clearRepassword.setEnabled(!isRepasswordNull);
                register.setEnabled((isPhoneNumberNull||isMsgCodeNull||isPasswordNull||isRepasswordNull
                        ? false : true));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_back:
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.register_clear_phoneNumber:
                etPhoneNumber.setText("");
                clearPhoneNumber.setVisibility(View.GONE);
                break;
            case R.id.register_clear_code:
                code.setText("");
                clearCode.setVisibility(View.GONE);
                break;
            case R.id.register_clear_password:
                etPassword.setText("");
                clearPassword.setVisibility(View.GONE);
                break;
            case R.id.register_clear_repassword:
                repassword.setText("");
                clearRepassword.setVisibility(View.GONE);
                break;
            case R.id.register_getCode:
                phoneNumber=etPhoneNumber.getText().toString();
                if(isPhoneNumber(phoneNumber)){
                    UserManager.getInstance(new MsgSendCallback()).sendMsgCode(phoneNumber);
                }else {
                    Toast.makeText(RegisterActivity.this,R.string.phone_number_incorrect,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.register:
                phoneNumber=etPhoneNumber.getText().toString();
                msgCode=code.getText().toString();
                password=etPassword.getText().toString();
                RePassword=repassword.getText().toString();
                if (isPhoneNumber(phoneNumber)&&isCode(msgCode)&&TextUtils.equals(password,RePassword)){
                    UserManager.getInstance(new SignUpCallback()).signUp(phoneNumber,msgCode,password);
                }else {
                    Toast.makeText(RegisterActivity.this,R.string.register_input_incorrect,Toast.LENGTH_LONG).show();
                }
                break;
            default:
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

    private boolean isPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }
        if (!phoneNumber.matches("^[0-9]*$")) {
            Log.i(TAG, "isPhoneNumber: match error--"+phoneNumber);
            return false;
        }
        if (phoneNumber.length() != 11) {
            Log.i(TAG, "isPhoneNumber: length error--"+phoneNumber);
            return false;
        }
        if (phoneNumber.indexOf(0) == '1') {
            Log.i(TAG, "isPhoneNumber: start error--"+phoneNumber);
            return false;
        }
        return true;
    }


    private class MsgSendCallback implements IUserListener {

        @Override
        public void onMsgSendSuccess() {
            Toast.makeText(RegisterActivity.this, R.string.sms_code_send_success,Toast.LENGTH_LONG).show();
            //验证码发送成功，倒计时
            setCodeTimeDown();
        }

        @Override
        public void onMsgSendFailure() {
            Toast.makeText(RegisterActivity.this, R.string.sms_code_send_failure,Toast.LENGTH_LONG).show();
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
        getCode.setEnabled(false);
        final Timer timer = new Timer();
        secCount = 60;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        secCount--;
                        getCode.setText(secCount+" s");
                        if (secCount<=0) {
                            timer.cancel();
                            getCode.setText(R.string.reSend);
                            getCode.setEnabled(true);
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask,1000,1000);
    }

    private class SignUpCallback implements IUserListener {


        @Override
        public void onMsgSendSuccess() {

        }

        @Override
        public void onMsgSendFailure() {

        }

        @Override
        public void onLoginSuccess() {

        }

        @Override
        public void onLoginFailure() {

        }

        @Override
        public void onSignUpSuccess(User user) {
            Intent intent= new Intent(RegisterActivity.this,UWActivity.class);
            intent.putExtra(INTENT_USER,user.getUsername());
            startActivity(intent);
            Toast.makeText(RegisterActivity.this, R.string.register_success,Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        public void onSignUpFailure(BmobException e) {
            Toast.makeText(RegisterActivity.this, R.string.register_failure,Toast.LENGTH_LONG).show();
        }
    }
}
