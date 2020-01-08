package edu.dlufl.ohrigei.controller.adminController

import edu.dlufl.ohrigei.dao.UserDao
import edu.dlufl.ohrigei.model.Admin
import edu.dlufl.ohrigei.model.User
import edu.dlufl.ohrigei.service.adminService.service.AdminAddService
import edu.dlufl.ohrigei.service.adminService.service.AdminDetailService
import edu.dlufl.ohrigei.service.adminService.service.AdminQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import sun.awt.ModalExclude

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
    UserDao userDao

    @RequestMapping("/DashBoard")
    String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName()
        User admin = userDao.getUserByEmail(email)
        model.addAttribute("admin", admin)
        return "admin/DashBoard";
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
}
