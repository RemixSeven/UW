package com.jiadi.uw;

import com.jiadi.uw.domain.User;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 北 on 2017/7/23.
 */

public interface IUserListener {
    void onMsgSendSuccess();
    void onMsgSendFailure();
    void onLoginSuccess();
    void onLoginFailure();
    void onSignUpSuccess(User user);
    void onSignUpFailure(BmobException e);
}
