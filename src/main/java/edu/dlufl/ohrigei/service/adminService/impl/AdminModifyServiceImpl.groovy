package edu.dlufl.ohrigei.service.adminService.impl

import com.alibaba.fastjson.JSONObject
import edu.dlufl.ohrigei.dao.AdminDao
import edu.dlufl.ohrigei.service.adminService.service.AdminModifyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.servlet.http.HttpServletRequest

@Service("AdminModifyService")
class AdminModifyServiceImpl implements AdminModifyService {
    @Autowired
    AdminDao adminDao

    @Override
    JSONObject modifyGroup(int schoolID, Integer headDelegateID, int size, int groupID) {
        JSONObject jsonObject = new JSONObject()
        try {
            adminDao.modifyGroup(schoolID, headDelegateID, size, groupID)
            jsonObject.put("status", "SUCCESS")
        }
        catch (Exception ignored) {
            jsonObject.put("status", "ERROR")
        }
        return jsonObject
    }

    @Override
    JSONObject modifyLoginStatus(HttpServletRequest request) {
        JSONObject jsonObject=new JSONObject()
        int userID= request.getParameter("userID") as int
        boolean loginStatus=Boolean.parseBoolean(request.getParameter("LoginStatus"))
        if (loginStatus){
            try {
                adminDao.modifyLoginStatusById(userID,false)
                jsonObject.put("status","SUCCESS")
            }
            catch (Exception ignored){
                jsonObject.put("status","ERROR")
            }
        }else if (!loginStatus){
            try {
                adminDao.modifyLoginStatusById(userID,true)
                jsonObject.put("status","SUCCESS")
            }
            catch (Exception ignored){
                jsonObject.put("status","ERROR")
            }
        }
        return jsonObject
    }

    @Override
    JSONObject modifyApplicationStatus(HttpServletRequest request) {
        JSONObject jsonObject=new JSONObject()
        int id=request.getParameter("id") as int
        int applicationID=request.getParameter("applicationStatus") as int
        try {
            adminDao.modifyApplicationStatus(id,applicationID)
            jsonObject.put("status","SUCCESS")
        }
        catch (Exception ignored){
            jsonObject.put("status","ERROR")
        }
        return null
    }
}
