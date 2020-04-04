package edu.dlufl.ohrigei.controller.userController

import edu.dlufl.ohrigei.dao.UserDao
import edu.dlufl.ohrigei.model.Delegate
import edu.dlufl.ohrigei.model.User
import edu.dlufl.ohrigei.service.userService.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.HttpSession
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
//    @Autowired
//    ReloadableResourceBundleMessageSource messageSource
//    @RequestMapping("/changeLocale")
//    String changeLocale(String locale,HttpSession session){
//        session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,locale)
//        return "redirect:/user/index"
//    }
    @RequestMapping("/index")
    String toIndex(Model model, Authentication authentication, HttpSession session) {
        User user = userDao.getUserByEmail(authentication.getName());
        Delegate userInfo = userDao.getDelegateInfo(user.getId());
        String description = userDao.getDescription(userInfo.getApplicationStatusID())
        model.addAttribute("description", description)
        model.addAttribute("delegateInfo", userInfo);
        session.setAttribute("USER_INFO", user);
        return "/user/UserIndex";
    }

    @RequestMapping("/test")
    String toTest() {
        return "user/UserAcademicTest";
    }

    @RequestMapping("/userProfile")
    String userProfile(Model model, String id) {
        return userService.userProfile(model, id)
    }

    @RequestMapping("/userInterviewDetail")
    String userInterviewDetail(Model model, String id, HttpSession session) {
        return userService.userInterviewDetail(model, id, session)
    }

    @RequestMapping("/userBillDetail")
    String userBillDetail(Model model, String id, HttpSession session) {
        return userService.userBillDetail(model, id, session)
    }

    @RequestMapping("/userSeatDetail")
    String userSeatDetail(Model model, String id, HttpSession session) {
        return userService.userSeatDetail(model, id, session)
    }

    @RequestMapping("/userGroupDetail")
    String userGroupDetail(Model model, String id, HttpSession session) {
        return userService.userGroupDetail(model,id,session)
    }
}