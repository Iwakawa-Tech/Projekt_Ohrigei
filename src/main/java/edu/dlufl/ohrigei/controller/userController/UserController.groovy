package edu.dlufl.ohrigei.controller.userController;

import edu.dlufl.ohrigei.dao.UserDao;
import edu.dlufl.ohrigei.model.Delegate;
import edu.dlufl.ohrigei.model.User
import edu.dlufl.ohrigei.service.userService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @author yifei.yuan
 */
@Controller
@RequestMapping("/user")
class UserController {
    @Autowired
    UserService userService
    @Autowired
    UserDao userDao;

    @RequestMapping("/index")
    String toIndex(Model model, Authentication authentication, HttpSession session) {
        User user = userDao.getUserByEmail(authentication.getName());
        Delegate userInfo = userDao.getDelegateInfo(user.getId());
        model.addAttribute("delegateInfo", userInfo);
        session.setAttribute("USER_INFO", user);
        return "/user/UserIndex";
    }

    @RequestMapping("/test")
    String toTest() {
        return "user/UserAcademicTest";
    }

    @RequestMapping("/userProfile")
    String userProfile(Model model,String id) {
        return userService.userProfile(model,id)
    }
}