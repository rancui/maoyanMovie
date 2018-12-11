package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.model.UserModel;
import com.stylefeng.guns.rest.common.ServerResponse;
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



}
