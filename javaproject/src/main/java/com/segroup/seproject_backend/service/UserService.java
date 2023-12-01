package com.segroup.seproject_backend.service;

import com.segroup.seproject_backend.data_item.UserDBItem;

public interface UserService {
    /**
     * 登录业务逻辑
     * @param uname 账户名
     * @param password 密码
     * @return
     */
    UserDBItem loginService(String uname, String password);

    /**
     * 注册业务逻辑
     //* @param user 要注册的User对象，属性中主键uid要为空，若uid不为空可能会覆盖已存在的user
     * @return
     */
    //UserDBItem registService(UserDBItem user);
    UserDBItem registService(String uname,String password);


}
