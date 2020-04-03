package edu.dlufl.ohrigei.service.userService.Impl

import com.alibaba.fastjson.JSONObject
import edu.dlufl.ohrigei.dao.UserDao
import edu.dlufl.ohrigei.model.*
import edu.dlufl.ohrigei.service.userService.service.UserService
import edu.dlufl.ohrigei.util.UtilSet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.ui.Model

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Service("UserService")
class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    UserDao userDao

    @Override
    String userSignUp(User user, Model model) throws UsernameNotFoundException {
        String emailCheck = userDao.userEmailCheck(user.getEmail())
        if (emailCheck != null) {
            throw new UsernameNotFoundException("电子邮箱重复")
        } else {
            String password = new BCryptPasswordEncoder().encode(user.getPassword())
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
        else if (!user.getEnable_Login()) {
            throw new LockedException("账户不可用")
        }
        return user
    }

    @Override
    String userProfile(Model model, String id) {
        Delegate delegate = userDao.getDelegateInfo(Integer.parseInt(id))
        model.addAttribute("delegate", delegate)
        return "user/UserPersonalProfile"
    }

    @Override
    String userInterviewDetail(Model model, String id, HttpSession session) {
        User user = session.getAttribute("USER_INFO") as User
        if (user.getId() != Integer.parseInt(id)) {
            String noInterview = "看别人的面试信息不是好孩子哦"
            model.addAttribute("noInterview", noInterview)
        } else {
            try {
                Interview interview = userDao.getInterviewById(Integer.parseInt(id))
                Admin admin = userDao.getInterviewAdmin(interview.getAdminID())
                model.addAttribute("interview", interview)
                model.addAttribute("admin", admin)
            }
            catch (NullPointerException ignored) {
                String noInterview = "尚未分配面试，请联系管理员。"
                model.addAttribute("noInterview", noInterview)
            }
        }
        return "user/UserInterviewDetail"
    }

    @Override
    String userBillDetail(Model model, String id, HttpSession session) {
        int delegateID = Integer.parseInt(id)
        User user = session.getAttribute("USER_INFO") as User
        if (delegateID != user.getId()) {
            model.addAttribute("info", "偷看别人的信息不好哦")
        } else {
            Bill bill = userDao.getBillDetail(delegateID)
            Delegate delegate = userDao.getDelegateInfo(delegateID)
            model.addAttribute("bill", bill)
            model.addAttribute("delegate", delegate)
        }
        return "user/UserBillDetail"
    }

    @Override
    String userSeatDetail(Model model, String id, HttpSession session) {
        User user = session.getAttribute("USER_INFO") as User
        User userTmp = new User()
        if (user.getId() != Integer.parseInt(id)) {
            model.addAttribute("info", "偷看别人的信息不好哦")
        } else {
            int userID = Integer.parseInt(id)
            Seat userSeat = userDao.querySeatByID(userID)
            if (userSeat == null) {
                userSeat = new Seat()
                model.addAttribute("userSeatInfo", "尚未选定座位")
                model.addAttribute("committeeInfo", "您尚未选择席位")
            } else {
                List<Seat> seatInSameCommitteeList = userDao.querySeatByCommitteeID(userSeat.getCommitteeID())
                for (int i = 0; i < seatInSameCommitteeList.size(); i++) {
                    userTmp = userDao.getPhoneAndEmailByID(seatInSameCommitteeList[i].getDelegateID())
                    if (userTmp != null) {
                        seatInSameCommitteeList[i].setDelegatePhone(userTmp.getPhone())
                        seatInSameCommitteeList[i].setDelegateEmail(userTmp.getEmail())
                        seatInSameCommitteeList[i].setDelegateName(userTmp.getName())
                    }
                }
                model.addAttribute("seatInSameCommitteeList", seatInSameCommitteeList)
            }
            List<Seat> allSeatList = userDao.queryAllSeat()
            model.addAttribute("allSeatList", allSeatList)
            model.addAttribute("userSeat", userSeat)
        }
        return "user/UserSeatDetail"
    }

    @Override
    JSONObject userSelectSeat(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject()
        int delegateID = request.getParameter("delegateID") as int
        int seatID = request.getParameter("seatID") as int
        int originID = (request.getParameter("originID")) as int
        String date = UtilSet.getCurrentTime()
        if (originID != 0) {
            userDao.deleteOldSeatSelect(originID)
        }
        try {
            userDao.modifyUserSelectSeat(delegateID, seatID, date)
            userDao.updateUserApplicationStatus(delegateID, 12)
            jsonObject.put("status", "SUCCESS")
        }
        catch (Exception ignored) {
            jsonObject.put("status", "ERROR")
        }
        return jsonObject
    }

    @Override
    String userGroupDetail(Model model, String id, HttpSession session) {
        User user = session.getAttribute("USER_INFO") as User
        int delegateID = user.getId()
        Integer groupID = userDao.queryGroupIDByID(delegateID)
        if (groupID != null) {
            Group group = userDao.queryUserGroupDetail(groupID)
            model.addAttribute("group", group)
        } else {
            List<Group> groupList = userDao.queryAllGroup()
            for (int i = 0; i < groupList.size(); i++) {
                int usedSize = userDao.getUsedCapacityByID(groupList[i].getId())
                groupList[i].setUsedCapacity(usedSize)
            }
            model.addAttribute("info", "您尚未加入代表团")
            model.addAttribute("groupList", groupList)
            model.addAttribute("group", new Group())
        }
        return "user/UserGroupDetail"
    }

    @Override
    JSONObject userSelectGroup(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject()
        int delegateID = request.getParameter("delegateID") as int
        int groupID = request.getParameter("groupID") as int
        int usedSize = userDao.getUsedCapacityByID(groupID)
        int size = userDao.queryUserGroupDetail(groupID).getGroupSize()
        if (size - usedSize > 0) {
            try {
                userDao.userSelectGroup(delegateID, groupID)
                jsonObject.put("status", "SUCCESS")
            }
            catch (Exception ignored) {
                jsonObject.put("status", "ERROR")
            }
        } else {
            jsonObject.put("info", "代表团容量已满，请选择其他代表团")
        }
        return jsonObject
    }

    @Override
    JSONObject leaveFromConference(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject()
        int id = request.getParameter("id") as int
        try {
            userDao.leaveFromConference(id)
            jsonObject.put("status", "SUCCESS")
        }
        catch (Exception ignored) {
            jsonObject.put("status", "ERROR")
        }
        return jsonObject
    }

    @Override
    JSONObject leaveGroup(HttpServletRequest request) {
        JSONObject jsonObject=new JSONObject()
        int id=request.getParameter("id") as int
        try {
            userDao.leaveGroup(id)
            jsonObject.put("status", "SUCCESS")
        }
        catch (Exception ignored) {
            jsonObject.put("status", "ERROR")
        }
        return jsonObject
    }
}
