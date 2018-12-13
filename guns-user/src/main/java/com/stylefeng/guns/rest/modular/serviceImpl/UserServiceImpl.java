package com.stylefeng.guns.rest.modular.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.model.UserModel;
import com.stylefeng.guns.api.user.vo.UserInfoVo;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Service(interfaceClass = UserAPI.class,loadbalance = "roundrobin")
public class UserServiceImpl implements UserAPI {

    @Autowired
    private MoocUserMapper moocUserMapper;


    @Override
    public boolean registry(UserModel userModel) {

        MoocUser moocUser = new MoocUser();
        moocUser.setUserName(userModel.getUsername());
        moocUser.setEmail(userModel.getEmail());
        moocUser.setAddress(userModel.getAddress());
        moocUser.setUserPhone(userModel.getPhone());
        String md5Password = MD5Util.encrypt(userModel.getPassword());
        moocUser.setUserPwd(md5Password);

        int rowCount = moocUserMapper.insert(moocUser);
        if(rowCount>0){
            return true;
        }else {
            return false;
        }


    }

    @Override
    public int login(String username, String password) {

        MoocUser MoocUser = new MoocUser();
        MoocUser.setUserName(username);

        MoocUser result = moocUserMapper.selectOne(MoocUser);
        if(result!=null&&result.getUuid()>0){
            if(result.getUserPwd().equals(MD5Util.encrypt(password))){
                return result.getUuid();
            }
        }
        return 0;
    }



    @Override
    public boolean checkUsername(String username) {

        EntityWrapper<MoocUser> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name",username);

        Integer rowCount = moocUserMapper.selectCount(entityWrapper);
        if(rowCount!=null&&rowCount>0){
            return false;//说明该用户名已存在
        }else {
            return true;//说明该用户名不存在
        }
    }

    private UserInfoVo assmbleUserInfoVo(MoocUser moocUser){
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setHeadAddress(moocUser.getHeadUrl());
        userInfoVo.setAddress(moocUser.getAddress());
        userInfoVo.setBiography(moocUser.getBiography());
        userInfoVo.setBirthday(moocUser.getBirthday());
        userInfoVo.setUuid(moocUser.getUuid());
        userInfoVo.setUsername(moocUser.getUserName());
        userInfoVo.setSex(moocUser.getUserSex());
        userInfoVo.setPhone(moocUser.getUserPhone());
        userInfoVo.setNickname(moocUser.getNickName());
        userInfoVo.setLifeState(""+moocUser.getLifeState());
        userInfoVo.setEmail(moocUser.getEmail());
        userInfoVo.setCreateTime(moocUser.getCreateTime());
        userInfoVo.setUpdateTime(moocUser.getUpdateTime());
        return userInfoVo;

    }

    @Override
    public UserInfoVo getUserInfo(String userId) {
        MoocUser MoocUser = moocUserMapper.selectById(userId);
        if(MoocUser!=null){
            UserInfoVo userInfoVo = assmbleUserInfoVo(MoocUser);
            return userInfoVo;
        }else {
            return null;
        }
    }

    @Override
    public UserInfoVo updateUserInfo(UserInfoVo userInfoVo) {
        MoocUser MoocUser = new MoocUser();
        MoocUser.setUuid(userInfoVo.getUuid());
        MoocUser.setHeadUrl(userInfoVo.getHeadAddress());
        MoocUser.setAddress(userInfoVo.getAddress());
        MoocUser.setBiography(userInfoVo.getBiography());
        MoocUser.setBirthday(userInfoVo.getBirthday());
        MoocUser.setUuid(userInfoVo.getUuid());
        MoocUser.setUserName(userInfoVo.getUsername());
        MoocUser.setUserSex(userInfoVo.getSex());
        MoocUser.setUserPhone(userInfoVo.getPhone());
        MoocUser.setNickName(userInfoVo.getNickname());
        MoocUser.setLifeState(Integer.parseInt(userInfoVo.getLifeState()));
        MoocUser.setEmail(userInfoVo.getEmail());
        MoocUser.setCreateTime(userInfoVo.getCreateTime());
        MoocUser.setUpdateTime(userInfoVo.getUpdateTime());

        Integer rowCount = moocUserMapper.updateById(MoocUser);
        if(rowCount>0){
           UserInfoVo newUserInfoVo = getUserInfo(String.valueOf(MoocUser.getUuid()));
           return newUserInfoVo;
        }else {
            return userInfoVo;
        }

    }
}
