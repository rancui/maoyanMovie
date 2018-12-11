package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.model.UserModel;
import com.stylefeng.guns.api.user.vo.UserInfoVo;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUserT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = UserAPI.class)
public class UserServiceImpl implements UserAPI {

    @Autowired
    private MoocUserTMapper moocUserTMapper;


    @Override
    public boolean registry(UserModel userModel) {

        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(userModel.getUsername());
        moocUserT.setEmail(userModel.getEmail());
        moocUserT.setAddress(userModel.getAddress());
        moocUserT.setUserPhone(userModel.getPhone());
        String md5Password = MD5Util.encrypt(userModel.getPassword());
        moocUserT.setUserPwd(md5Password);

        int rowCount = moocUserTMapper.insert(moocUserT);
        if(rowCount>0){
            return true;
        }else {
            return false;
        }


    }

    @Override
    public int login(String username, String password) {

        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(username);

        MoocUserT result = moocUserTMapper.selectOne(moocUserT);
        if(result!=null&&result.getUuid()>0){
            if(result.getUserPwd().equals(MD5Util.encrypt(password))){
                return result.getUuid();
            }
        }
        return 0;
    }


    @Override
    public boolean checkUsername(String username) {

        EntityWrapper<MoocUserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name",username);

        Integer rowCount = moocUserTMapper.selectCount(entityWrapper);
        if(rowCount!=null&&rowCount>0){
            return false;//说明该用户名已存在
        }else {
            return true;//说明该用户名不存在
        }
    }

    private UserInfoVo assmbleUserInfoVo(MoocUserT moocUserT){
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setHeadAddress(moocUserT.getHeadUrl());
        userInfoVo.setAddress(moocUserT.getAddress());
        userInfoVo.setBiography(moocUserT.getBiography());
        userInfoVo.setBirthday(moocUserT.getBirthday());
        userInfoVo.setUuid(moocUserT.getUuid());
        userInfoVo.setUsername(moocUserT.getUserName());
        userInfoVo.setSex(moocUserT.getUserSex());
        userInfoVo.setPhone(moocUserT.getUserPhone());
        userInfoVo.setNickname(moocUserT.getNickName());
        userInfoVo.setLifeState(moocUserT.getLifeState());
        userInfoVo.setEmail(moocUserT.getEmail());
        userInfoVo.setCreateTime(moocUserT.getCreateTime());
        userInfoVo.setUpdateTime(moocUserT.getUpdateTime());
        return userInfoVo;

    }

    @Override
    public UserInfoVo getUserInfo(String userId) {
        MoocUserT moocUserT = moocUserTMapper.selectById(userId);
        if(moocUserT!=null){
            UserInfoVo userInfoVo = this.assmbleUserInfoVo(moocUserT);
            return userInfoVo;
        }else {
            return null;
        }
    }

    @Override
    public UserInfoVo updateUserInfo(UserInfoVo userInfoVo) {
        return null;
    }
}
