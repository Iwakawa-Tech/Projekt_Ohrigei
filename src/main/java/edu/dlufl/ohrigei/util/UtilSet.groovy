package edu.dlufl.ohrigei.util

import java.text.SimpleDateFormat


class UtilSet {
    static String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime=simpleDateFormat.format(new Date())
        return currentTime
    }
}
