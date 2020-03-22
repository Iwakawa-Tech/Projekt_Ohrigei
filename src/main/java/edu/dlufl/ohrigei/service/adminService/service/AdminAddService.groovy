package edu.dlufl.ohrigei.service.adminService.service

import com.alibaba.fastjson.JSONObject
import edu.dlufl.ohrigei.model.Admin
import org.springframework.stereotype.Service
import org.springframework.ui.Model

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Service
interface AdminAddService {
    JSONObject addAdmin(HttpServletRequest request)

    JSONObject addGroup(int schoolID, Integer headDelegateID, int size)

    JSONObject addSchool(HttpServletRequest request)

    JSONObject addCommittee(HttpServletRequest request)

    JSONObject addSeat(HttpServletRequest request)
}