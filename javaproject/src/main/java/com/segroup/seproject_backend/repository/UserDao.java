package com.segroup.seproject_backend.repository;

import com.segroup.seproject_backend.data_item.UserDBItem;

//实现数据库操作
public interface UserDao {
    //检查用户名和密码是否正确
    public UserDBItem check(String name, String password);

    //用于注册增加用户用户
    public UserDBItem addUser(String name,String password);

    //检查用户名是否唯一
    public UserDBItem unit(String name);
}
