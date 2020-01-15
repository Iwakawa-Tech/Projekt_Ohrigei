package edu.dlufl.ohrigei.dao

import edu.dlufl.ohrigei.model.Admin
import edu.dlufl.ohrigei.model.Committee
import edu.dlufl.ohrigei.model.Delegate
import edu.dlufl.ohrigei.model.Group
import edu.dlufl.ohrigei.model.School

interface AdminDao {
    List<Delegate> queryAllMember(int type)

    List<Group> queryAllGroup()

    List<Admin> queryAllAdmin()

    List<Committee> queryAllCommittee()

    int addAdminStep1(Admin admin)

    int addAdminStep2(Admin admin)

    int getInsertID(String email)

    String emailCheck(String email)

    Admin adminDetail(int id)

    int countDelegate(int roleId)

    List<School> queryAllSchool()

    List<Delegate> queryMemberAbleToUse()

    int addGroup(int schoolID,int headDelegateID,int size)
}