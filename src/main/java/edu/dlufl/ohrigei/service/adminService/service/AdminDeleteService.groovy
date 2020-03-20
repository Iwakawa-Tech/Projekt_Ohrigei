package edu.dlufl.ohrigei.service.adminService.service

import com.alibaba.fastjson.JSONObject
import org.springframework.stereotype.Service

@Service
interface AdminDeleteService {
    JSONObject deleteGroup(int GroupID)
}
