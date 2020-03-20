package edu.dlufl.ohrigei.controller.adminController

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import edu.dlufl.ohrigei.dao.UserDao
import edu.dlufl.ohrigei.model.Admin
import edu.dlufl.ohrigei.model.User
import edu.dlufl.ohrigei.service.adminService.service.AdminAddService
import edu.dlufl.ohrigei.service.adminService.service.AdminCountService
import edu.dlufl.ohrigei.service.adminService.service.AdminDeleteService
import edu.dlufl.ohrigei.service.adminService.service.AdminDetailService
import edu.dlufl.ohrigei.service.adminService.service.AdminModifyService
import edu.dlufl.ohrigei.service.adminService.service.AdminQueryService
import groovyjarjarantlr.StringUtils
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
    AdminModifyService adminModifyService
    @Autowired
    AdminDeleteService adminDeleteService
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

    @RequestMapping("/addAdminPage")
    String addAdmin(HttpServletRequest request, Model model, HttpSession httpSession) {
        return "admin/AddAdmin"
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
    String groupDetail(Model model) {
        return adminQueryService.queryAllGroup(model)
    }
    @RequestMapping("/addSchoolPage")
    String addSchoolPage(Model model){
        return adminQueryService.queryAllSchoolType(model)
    }
    @RequestMapping("/modifyGroupPage")
    String modifyGroup(Model model, String groupID) {
        return adminQueryService.queryGroupById(model, groupID)
    }

    @RequestMapping("/groupMemberDetail")
    String groupMemberDetail(Model model, String groupID) {
        return null
    }

    @RequestMapping("/memberDetailPage")
    String memberDetailPage(Model model, String id) {
        int ID = Integer.parseInt(id)
        return adminQueryService.queryMemberByID(model, ID)
    }

    @RequestMapping(value = "/addGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    JSONObject addGroup(HttpServletRequest request, HttpServletResponse response) {
        Integer headDelegateID
        if (request.getParameter("headDelegateID") == '' || request.getParameter("headDelegateID") == null) {
            headDelegateID = null
        } else {
            headDelegateID = Integer.parseInt(request.getParameter("headDelegateID"))
        }
        int schoolID = request.getParameter("schoolID") as int
        int size = request.getParameter("groupSize") as int
        return adminAddService.addGroup(schoolID, headDelegateID, size)
    }

    @RequestMapping(value = "/modifyGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    JSONObject modifyGroup(HttpServletRequest request) {
        Integer headDelegateID
        if (request.getParameter("headDelegateID") == '' || request.getParameter("headDelegateID") == null) {
            headDelegateID = null
        } else {
            headDelegateID = Integer.parseInt(request.getParameter("headDelegateID"))
        }
        int schoolID = request.getParameter("schoolID") as int
        int size = request.getParameter("groupSize") as int
        int groupID = request.getParameter("groupID") as int
        return adminModifyService.modifyGroup(schoolID, headDelegateID, size, groupID)
    }

    @RequestMapping(value = "/deleteGroup", method = RequestMethod.POST)
    @ResponseBody
    JSONObject deleteGroup(HttpServletRequest request) {
        int groupID = request.getParameter("groupID") as int
        return adminDeleteService.deleteGroup(groupID)
    }

    @RequestMapping(value = "/addAdmin", method = RequestMethod.POST)
    @ResponseBody
    JSONObject addAdmin(HttpServletRequest request) {
        return adminAddService.addAdmin(request)
    }

    @RequestMapping(value = "/modifyUserLoginStatus", method = RequestMethod.POST)
    @ResponseBody
    JSONObject modifyLoginStatus(HttpServletRequest request) {
        adminModifyService.modifyLoginStatus(request)
    }

    @RequestMapping(value = "/addSchool",method = RequestMethod.POST)
    @ResponseBody
    JSONObject addSchool(HttpServletRequest request){
        adminAddService.addSchool(request)
    }
}
