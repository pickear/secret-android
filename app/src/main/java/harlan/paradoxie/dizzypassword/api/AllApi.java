package harlan.paradoxie.dizzypassword.api;

import harlan.paradoxie.dizzypassword.MyApplication;
import harlan.paradoxie.dizzypassword.util.ACache;
import harlan.paradoxie.dizzypassword.util.SPUtils;
import okhttp3.Cache;

/**
 * Created by a1 on 2017/11/15.
 */
public class AllApi {

    public static String Home="http://192.168.3.100:3330";
   // public static String Home="https://secret.kisme.org/";
    public static String register=Home+"/user/register";
    public static String login=Home+"/user/login";
    public static String save=Home+"/subject/save";
 public static String saves=Home+"/subject/saves";
    public static String savelist=Home+"/subject/synchronize";
    public static String list=Home+"/subject/list";
    //public static String logined=Home+"/user/logined";
    public static String beat=Home+"/user/beat";
    public static String query=Home+"/user/query";
    public static String delete=Home+"/subject/delete";
    public static String update=Home+"/update-info";
}
