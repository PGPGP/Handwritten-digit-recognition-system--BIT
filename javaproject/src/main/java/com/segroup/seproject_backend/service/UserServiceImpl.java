package com.segroup.seproject_backend.service;

import com.segroup.seproject_backend.data_item.UserDBItem;
import com.segroup.seproject_backend.repository.UserDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Resource
    private UserDao userDao;

    @Override
    public UserDBItem loginService(String uname, String password) {
        // 如果账号密码都对则返回登录的用户对象，若有一个错误则返回null
        UserDBItem user = userDao.check(uname, password);
        // 重要信息置空
        if(user != null){
            user.setUser_password("");
        }
        return user;

    }

    @Override
//    public UserDBItem registService(UserDBItem user) {
//        //当新用户的用户名已存在时
//        if(userDao.unit(user.getUser_name())!=null){
//            // 无法注册
//            return null;
//        }
//        else{
//            //返回创建好的用户对象(带uid)
//            UserDBItem newUser = userDao.addUser(user.getUser_name(), user.getUser_password());
//            if(newUser != null){
//                newUser.setUser_password("");
//            }
//            return newUser;
//        }
//
//    }
    public UserDBItem registService(String name,String password) {
        //当新用户的用户名已存在时
        if(userDao.unit(name)!=null){
            // 无法注册
            return null;
        }
        else{
            //返回创建好的用户对象(带uid)
            UserDBItem newUser = userDao.addUser(name, password);
            if(newUser != null){
                newUser.setUser_password("");
            }
            return newUser;
        }

    }
}
