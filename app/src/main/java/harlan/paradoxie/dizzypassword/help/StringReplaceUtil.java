package harlan.paradoxie.dizzypassword.help;

import android.util.Log;

/**
 * Created by a1 on 2018/1/3.
 */
public class StringReplaceUtil {
    /**
     * 根据用户名的不同长度，来进行替换 ，达到保密效果
     *
     * @param userName 用户名
     * @return 替换后的用户名
     */
    public static String userNameReplaceWithStar(String userName) {
        String userNameAfterReplaced = "";

        if (userName == null){
            userName = "";
        }

        int nameLength = userName.length();

        if (nameLength <= 1) {
            userNameAfterReplaced = "*";
        } else if (nameLength == 2) {
            userNameAfterReplaced =userName.substring(0,1)+ replaceSubString(userName, 1, nameLength);
        }else if(nameLength == 3){
            userNameAfterReplaced =userName.substring(0,1)+ replaceSubString(userName, 1, 2)+userName.substring(2, nameLength);
        }else if(nameLength == 4){
            userNameAfterReplaced =userName.substring(0,1)+ replaceSubString(userName, 1, 3)+userName.substring(3, nameLength);
        } else if (nameLength >= 5 && nameLength <= 8) {
            userNameAfterReplaced =userName.substring(0,2)+ replaceSubString(userName, 2, 4)+userName.substring(4, nameLength);
        } else if (nameLength >8) {
            userNameAfterReplaced = userName.substring(0,3)+replaceSubString(userName,3,nameLength-3)+userName.substring(nameLength-3,nameLength);
        }

        return userNameAfterReplaced;

    }

    /**
     * 把字符串的后n位用“*”号代替
     * @param str 要代替的字符串
     * @param end 代替的位数
     * @return
     */

    public static String replaceSubString(String str,int start,int end){

        String sub="";
        try {
            sub = str.substring(start, end);
            Log.e("backinfo","sub:"+sub);
            StringBuffer sb=new StringBuffer();
            for(int i=0;i<sub.length();i++){
                sb=sb.append("*");
            }
            sub=sb.toString();
           // sub+=sb.toString();
            Log.e("backinfo","改变后sub:"+sub);
        } catch (Exception e) {
            Log.e("backinfo","替换是吧"+e.getLocalizedMessage());
            e.printStackTrace();
        }
        return sub;
    }
}
