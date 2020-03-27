package edu.dlufl.ohrigei.util

import com.alibaba.fastjson.JSONObject


class Base64ToFile {
    static JSONObject base64ToFile(String base64, String userName) {
        JSONObject object = new JSONObject()
        try {
            String[] splitBase64 = base64.split(',')
            byte[] decoder = Base64.getDecoder().decode(splitBase64[1])
            String filePath = "C:\\UserFile\\testImg"
            String timeStamp = new Date().getTime()
            String filename = userName + timeStamp
            FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath + "\\" + filename + ".jpg"))
            fileOutputStream.write(decoder)
            fileOutputStream.flush()
            fileOutputStream.close()
            object.put("status", "SUCCESS")
            return object
        }
        catch (Exception ignored) {
            object.put("status", "ERROR")
            return object
        }
    }
}
