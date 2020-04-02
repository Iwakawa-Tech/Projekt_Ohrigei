package edu.dlufl.ohrigei.dao

import edu.dlufl.ohrigei.model.Admin
import edu.dlufl.ohrigei.model.Bill
import edu.dlufl.ohrigei.model.Delegate
import edu.dlufl.ohrigei.model.Interview
import edu.dlufl.ohrigei.model.Seat
import edu.dlufl.ohrigei.model.User
import org.springframework.stereotype.Repository

@Repository
interface UserDao {
    //密码校验
    User loginCheck(User user)
    //区分用户类型
    int authorityControl(String email)

    String userEmailCheck(String email)

    int userSignUp(User user)

    int insertDelegate(User user)

    User login(String email)

    User getUserByEmail(String email)

    Delegate getDelegateInfo(int id)

    String getDescription(int id)

    Interview getInterviewById(int id)

    Admin getInterviewAdmin(int adminID)

    int modifyPayDetail(int id, String serialNumber, String billPayDate)

    Bill getBillDetail(int id)

    Seat querySeatByID(int id)

    List<Seat> querySeatByCommitteeID(int committeeID)

    List<Seat> queryAllSeat()

    User getPhoneAndEmailByID(int id)

    int modifyUserSelectSeat(int delegateID, int seatID, String date)
    int deleteOldSeatSelect(int id)
    int updateUserApplicationStatus(int id,int statusID)
}