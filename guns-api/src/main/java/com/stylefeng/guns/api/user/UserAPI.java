package com.stylefeng.guns.api.user;

import com.stylefeng.guns.api.user.model.UserModel;
import com.stylefeng.guns.api.user.vo.UserInfoVo;

public interface UserAPI {

    int login(String username, String password);

    boolean registry(UserModel userModel);

    boolean checkUsername(String username);

    UserInfoVo getUserInfo(String userId);

    UserInfoVo updateUserInfo(UserInfoVo userInfoVo);


}
