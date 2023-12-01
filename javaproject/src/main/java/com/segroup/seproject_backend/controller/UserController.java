package com.segroup.seproject_backend.controller;



import com.segroup.seproject_backend.data_item.UserDBItem;
import com.segroup.seproject_backend.repository.UserDao;
import com.segroup.seproject_backend.service.Result;
import com.segroup.seproject_backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
@RestController
//@RequestMapping("/user")
//@PostMapping("/user")

public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/user/login")
    //@GetMapping("/login")
    public Result<UserDBItem> loginController(@RequestParam("user_name") String uname, @RequestParam("user_password") String password) {
        UserDBItem user = userService.loginService(uname, password);
        if (user != null) {
            return Result.success(user, "登录成功！");
        } else {
            return Result.error("123", "账号或密码错误！");
        }
    }

    @PostMapping("/user/register")
    public Result<UserDBItem> registController(@RequestParam("user_name") String uname, @RequestParam("user_password") String password) {
        UserDBItem user = userService.registService(uname, password);
        if (user != null) {
            return Result.success(user, "注册成功！");
        } else {
            return Result.error("456", "用户名已存在！");
        }
    }
}
//    @PostMapping("/user/register")
//    @ResponseBody
//    public Result<UserDBItem> registController( UserDBItem newUser){
//        UserDBItem user = userService.registService(newUser);
//        if(user!=null){
//            return Result.success(user,"注册成功！");
//        }else{
//            return Result.error("456","用户名已存在！");
//        }
//    }
//}

