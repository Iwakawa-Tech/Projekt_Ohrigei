package edu.dlufl.ohrigei.controller.adminController

import com.alibaba.fastjson.JSONObject
import edu.dlufl.ohrigei.dao.UserDao
import edu.dlufl.ohrigei.service.adminService.service.AdminAddService
import edu.dlufl.ohrigei.service.adminService.service.AdminCountService
import edu.dlufl.ohrigei.service.adminService.service.AdminDeleteService
import edu.dlufl.ohrigei.service.adminService.service.AdminDetailService
import edu.dlufl.ohrigei.service.adminService.service.AdminModifyService
import edu.dlufl.ohrigei.service.adminService.service.AdminQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/admin")
class AdminRestController {
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

    @PostMapping("/addGroup")
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

    @PostMapping("/modifyGroup")
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

    @PostMapping("/deleteGroup")
    JSONObject deleteGroup(HttpServletRequest request) {
        int groupID = request.getParameter("groupID") as int
        return adminDeleteService.deleteGroup(groupID)
    }

    @PostMapping("/addAdmin")
    JSONObject addAdmin(HttpServletRequest request) {
        return adminAddService.addAdmin(request)
    }

    @PostMapping("/modifyUserLoginStatus")
    JSONObject modifyLoginStatus(HttpServletRequest request) {
        adminModifyService.modifyLoginStatus(request)
    }

    @PostMapping("/addSchool")
    JSONObject addSchool(HttpServletRequest request) {
        adminAddService.addSchool(request)
    }

    @PostMapping("/addCommittee")
    JSONObject addCommittee(HttpServletRequest request) {
        return adminAddService.addCommittee(request)
    }

    @PostMapping("/addSeat")
    JSONObject addSeat(HttpServletRequest request) {
        return adminAddService.addSeat(request)
    }

    @GetMapping("/getApplicationStatusList")
    List<JSONObject> getApplyStatusList() {
        return adminQueryService.getApplyStatusList()
    }

    @PostMapping("/modifyApplicationStatus")
    JSONObject modifyApplicationStatus(HttpServletRequest request){
        return adminModifyService.modifyApplicationStatus(request)
    }

    @PostMapping("/addInterview")
    JSONObject addInterview(HttpServletRequest request){
        return adminAddService.addInterview(request)
    }
}