package edu.dlufl.ohrigei.controller.userController;

import edu.dlufl.ohrigei.dao.UserDao;
import edu.dlufl.ohrigei.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yifei.yuan
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserDao userDao;
    @RequestMapping("/index")
    public String toIndex(Model model, Authentication authentication) {
        User user=userDao.getUserByEmail(authentication.getName());
        model.addAttribute("user",user);
        return "/user/UserIndex";
    }
    @RequestMapping("/test")
    public String toTest(){
        return "user/UserAcademicTest";
    }
}
