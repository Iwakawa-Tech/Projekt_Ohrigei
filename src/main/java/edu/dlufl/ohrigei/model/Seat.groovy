package edu.dlufl.ohrigei.model

class Seat {
    int id
    String seatName
    int committeeID
    String committeeName
    int seatDifficulty

    int getId() {
        return id
    }

    void setId(int id) {
        this.id = id
    }

    String getSeatName() {
        return seatName
    }

    void setSeatName(String seatName) {
        this.seatName = seatName
    }

    int getCommitteeID() {
        return committeeID
    }

    void setCommitteeID(int committeeID) {
        this.committeeID = committeeID
    }

    String getCommitteeName() {
        return committeeName
    }

    void setCommitteeName(String committeeName) {
        this.committeeName = committeeName
    }

    int getSeatDifficulty() {
        return seatDifficulty
    }

    void setSeatDifficulty(int seatDifficulty) {
        this.seatDifficulty = seatDifficulty
    }

    @Override
    String toString() {
        return "Seat{" +
                "id=" + id +
                ", seatName='" + seatName + '\'' +
                ", committeeID=" + committeeID +
                ", committeeName='" + committeeName + '\'' +
                ", seatDifficulty=" + seatDifficulty +
                '}';
    }
}
