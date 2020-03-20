package edu.dlufl.ohrigei.service.adminService.impl

import com.alibaba.fastjson.JSONObject
import edu.dlufl.ohrigei.dao.AdminDao
import edu.dlufl.ohrigei.service.adminService.service.AdminDeleteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("AdminDeleteService")
class AdminDeleteServiceImpl implements AdminDeleteService{
    @Autowired
    AdminDao adminDao
    @Override
    JSONObject deleteGroup(int groupID) {
        JSONObject jsonObject = new JSONObject()
        try {
            adminDao.deleteGroup(groupID)
            jsonObject.put("status", "SUCCESS")
        } catch (Exception ignored){
            jsonObject.put("status", "ERROR")
        }
        return jsonObject
    }
}
