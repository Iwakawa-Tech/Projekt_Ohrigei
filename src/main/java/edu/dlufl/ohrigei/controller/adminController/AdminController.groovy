package edu.dlufl.ohrigei.controller.adminController

import com.alibaba.fastjson.JSONObject
import edu.dlufl.ohrigei.dao.UserDao
import edu.dlufl.ohrigei.model.Admin
import edu.dlufl.ohrigei.model.User
import edu.dlufl.ohrigei.service.adminService.service.AdminAddService
import edu.dlufl.ohrigei.service.adminService.service.AdminCountService
import edu.dlufl.ohrigei.service.adminService.service.AdminDetailService
import edu.dlufl.ohrigei.service.adminService.service.AdminQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import sun.awt.ModalExclude

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/admin")
class AdminController {
    @Autowired
    AdminQueryService adminQueryService
    @Autowired
    AdminAddService adminAddService
    @Autowired
    AdminDetailService adminDetailService
    @Autowired
    AdminCountService adminCountService
    @Autowired
    UserDao userDao

    @RequestMapping("/DashBoard")
    String dashboard(Authentication authentication, HttpSession httpSession, Model model) {
        String email = authentication.getName()
        User userInfo = userDao.getUserByEmail(email)
        httpSession.setAttribute("USER_INFO", userInfo)
        return adminCountService.countDelegate(model, httpSession);
    }

    @RequestMapping("/profile")
    String profile(HttpSession session) {
        return "admin/AdminPersonalProfile"
    }

    @RequestMapping("/manage")
    String queryMembers(HttpSession session, Model model, String type) {
        switch (type) {
            case "admin":
                return adminQueryService.queryAdmin(session, model)
            case "committee":
                return adminQueryService.queryCommittee(session, model)
            case "group":
                return adminQueryService.queryGroup(session, model)
            case "seat": break
            default:
                try {
                    return adminQueryService.queryMembers(session, model, type)
                }
                catch (Exception e) {
                    System.out.println("An error occur" + e)
                }
        }
        return "admin/DashBoard"
    }

    private int cont = 0

    @RequestMapping("/addAdmin")
    String addAdmin(@ModelAttribute(value = "Admin") Admin admin, Model model, HttpSession httpSession) {
        if (cont == 0) {
            model.addAttribute("Admin", new Admin())
            cont = 1
            return "admin/AddAdmin"
        }
        cont = 0
        return adminAddService.addAdmin(httpSession, model, admin)
    }

    @RequestMapping("/adminDetail")
    String adminDetail(Model model, HttpSession httpSession, String id) {
        return adminDetailService.adminDetail(httpSession, model, id)
    }

    @RequestMapping("/addGroupPage")
    String addGroup(Model model) {
        return adminQueryService.queryAllDelegateAndSchool(model)
    }

    @RequestMapping("/groupDetailPage")
    String groupDetail(Model model){
        return adminQueryService.queryAllGroup(model)
    }

    @RequestMapping("/modifyGroupPage")
    String modifyGroup(Model model,String groupID){
        return adminQueryService.queryGroupById(model,groupID)
    }
    @RequestMapping("/groupMemberDetail")
    String groupMemberDetail(Model model,String groupID){

    }

    @RequestMapping(value = "/addGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    JSONObject addGroup(HttpServletRequest request, HttpServletResponse response) {
        int schoolID = request.getParameter("schoolID") as int
        int headDelegateID = request.getParameter("headDelegateID") as int
        int size = request.getParameter("groupSize") as int
        return adminAddService.addGroup(schoolID, headDelegateID, size)
    }
}
