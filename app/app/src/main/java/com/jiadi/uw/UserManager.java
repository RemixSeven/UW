package com.jiadi.uw;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import com.jiadi.uw.domain.User;
/**
 * Created by 北 on 2017/7/23.
 */

public class UserManager {
    private static UserManager userManager;
    private static IUserListener mListener;
    public static UserManager getInstance(IUserListener listener) {
        mListener = listener;
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager();
                }
            }
        }
        return userManager;
    }
    public void sendMsgCode(String phoneNumber) {
        BmobSMS.requestSMSCode(phoneNumber,"uw", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId,BmobException ex) {
                if(ex==null){//验证码发送成功
                    mListener.onMsgSendSuccess();
                } else {
                    mListener.onMsgSendFailure();
                }
            }
        });
    }
    public void signUp(String phoneNumber,String code,String password){
        User user=new User();
        user.setUsername(phoneNumber);
        user.setMobilePhoneNumber(phoneNumber);
        user.setPassword(password);
        user.signOrLogin(code, new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e==null){
                    mListener.onSignUpSuccess(user);
                }else {
                    mListener.onSignUpFailure(e);
                }
            }
        });
    }
    public  void login(String username,String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e==null){
                    mListener.onLoginSuccess();
                }else {
                    mListener.onLoginFailure();
                }
            }
        });
    }
    /*public  void resetPwd(String phoneNumber,String code,String password){
        User user = new User();
        user.setUsername(phoneNumber);
        user.setMobilePhoneNumber(phoneNumber);
        user.setPassword(password);
        user.resetPasswordBySMSCode(this, code, password, new resetPasswordBySMSCode() {

            public void done(BmobException ex) {
                // TODO Auto-generated method stub
                if(ex==null){

                }else{

                }
            }
        });
    }*/

}
