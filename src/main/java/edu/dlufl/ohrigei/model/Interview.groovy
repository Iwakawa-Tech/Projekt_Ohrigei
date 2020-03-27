package edu.dlufl.ohrigei.model

class Interview {
    int id
    int adminID
    String delegateName
    String adminName
    String date
    int interviewPoint
    Boolean interviewStatus
    String comment



    @Override
    public String toString() {
        return "Interview{" +
                "id=" + id +
                ", adminID=" + adminID +
                ", delegateName='" + delegateName + '\'' +
                ", adminName='" + adminName + '\'' +
                ", date=" + date +
                ", interviewPoint=" + interviewPoint +
                ", interviewStatus=" + interviewStatus +
                ", comment=" + comment +
                '}';
    }
}
