package com.haif.haifojuserservice.controller.inner;

import com.haif.haifojmodel.model.entity.User;
import com.haif.haifojserviceclient.service.UserFeignClient;
import com.haif.haifojuserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

    @Override
    @GetMapping("/get/userIds")
    public List<User> listByIds(@RequestParam("userIds") Collection<Long> userIds) {
        return userService.listByIds(userIds);
    }
}
