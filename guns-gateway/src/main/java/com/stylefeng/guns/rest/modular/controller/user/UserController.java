package com.stylefeng.guns.rest.modular.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.model.UserModel;
import com.stylefeng.guns.api.user.vo.UserInfoVo;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.common.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user/")
public class UserController {

    @Reference(interfaceClass = UserAPI.class)
    private UserAPI userAPI;

    //用户注册
    @PostMapping("register")
    public ServerResponse register(UserModel userModel){
        if(userModel.getUsername()==null||userModel.getUsername().trim().length()==0){
            return ServerResponse.createErrorMsg("用户名不能为空");
        }

        if(userModel.getPassword()==null||userModel.getPassword().trim().length()==0){
            return ServerResponse.createErrorMsg("密码不能为空");
        }

        boolean rowCount = userAPI.registry(userModel);
        if(rowCount){
            return ServerResponse.createSuccessMsg("注册成功");
        }else {
            return ServerResponse.createErrorMsg("注册失败");
        }
    }

    //检验用户名是否被注册
    @PostMapping("check_username")
    public ServerResponse checkUsername(String username){

        if(username!=null&&username.trim().length()>0){
            boolean result = userAPI.checkUsername(username);
            if(result){
                return ServerResponse.createSuccessMsg("用户名未被注册，可以使用。");
            }else {
                return ServerResponse.createErrorMsg("用户名已被注册，请更换!");
            }
        }

        return ServerResponse.createErrorMsg("用户名不能为空");


    }

    public ServerResponse logOut(){
         return ServerResponse.createSuccessMsg("登出成功");
    }

    //获取用户信息
    @GetMapping("get_user_info")
    public ServerResponse getUserInfo(){
        String userId = CurrentUser.getCurrentUser();
        if(userId!=null||userId.trim().length()>0){
            Integer uuid = Integer.parseInt(userId);
            UserInfoVo userInfoVo = userAPI.getUserInfo(userId);
            if(userInfoVo!=null){
                return ServerResponse.createSuccessData(userInfoVo);
            }else {
                return ServerResponse.createErrorMsg("查询用户信息失败！");
            }
        }

        return ServerResponse.createErrorMsg("用户未登录");
    }


    //更新个人信息
    @PostMapping("update_user_info")
    public ServerResponse updateUserInfo(UserInfoVo userInfoVo){
        String userId = CurrentUser.getCurrentUser();
        if(userId!=null||userId.trim().length()>0){
            Integer uuid = Integer.parseInt(userId);
            // 判断当前登陆人员的ID与修改的结果ID是否一致
            if(uuid!=userInfoVo.getUuid()){
                return ServerResponse.createErrorMsg("清修改您的个人信息");
            }

            UserInfoVo newUserInfoVo = userAPI.updateUserInfo(userInfoVo);

            if(newUserInfoVo!=null){
                return ServerResponse.createSuccessData(newUserInfoVo);
            }else {
                return ServerResponse.createErrorMsg("更新个人信息失败");
            }
        }

        return ServerResponse.createErrorMsg("用户未登录！");
    }










}
