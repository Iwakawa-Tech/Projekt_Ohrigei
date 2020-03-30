package edu.dlufl.ohrigei.service.userService.Impl

import edu.dlufl.ohrigei.dao.UserDao
import edu.dlufl.ohrigei.model.Admin
import edu.dlufl.ohrigei.model.Delegate
import edu.dlufl.ohrigei.model.Interview
import edu.dlufl.ohrigei.model.User
import edu.dlufl.ohrigei.service.userService.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.ui.Model

import javax.servlet.http.HttpSession

@Service("UserService")
class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    UserDao userDao
    @Override
    String userSignUp(User user, Model model) throws UsernameNotFoundException{
        String emailCheck = userDao.userEmailCheck(user.getEmail())
        if (emailCheck != null) {
            throw new UsernameNotFoundException("电子邮箱重复")
        } else {
            String password=new BCryptPasswordEncoder().encode(user.getPassword())
            String upCaseFirstName = user.getFirst_name().toUpperCase()
            String upCaseLastName = user.getLast_name().toUpperCase()
            user.setFirst_name(upCaseFirstName)
            user.setLast_name(upCaseLastName)
            user.setPassword(password)
            userDao.userSignUp(user)
            userDao.insertDelegate(user)
            return "/login"
        }
    }
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.login(email)
        if (user == null)
            throw new UsernameNotFoundException("用户不存在")
        else if (!user.getEnable_Login()){
            throw new LockedException("账户不可用")
        }
        return user
    }

    @Override
    String userProfile(Model model, String id) {
        Delegate delegate=userDao.getDelegateInfo(Integer.parseInt(id))
        model.addAttribute("delegate",delegate)
        return "user/UserPersonalProfile"
    }

    @Override
    String userInterviewDetail(Model model, String id,HttpSession session) {
        User user=session.getAttribute("USER_INFO") as User
        if (user.getId()!=Integer.parseInt(id)){
            String noInterview="看别人的面试信息不是好孩子哦"
            model.addAttribute("noInterview",noInterview)
        }else {
            try {
                Interview interview=userDao.getInterviewById(Integer.parseInt(id))
                Admin admin=userDao.getInterviewAdmin(interview.getAdminID())
                model.addAttribute("interview",interview)
                model.addAttribute("admin",admin)
            }
            catch (NullPointerException ignored){
                String noInterview="尚未分配面试，请联系管理员。"
                model.addAttribute("noInterview",noInterview)
            }
        }
        return "user/UserInterviewDetail"
    }
}
