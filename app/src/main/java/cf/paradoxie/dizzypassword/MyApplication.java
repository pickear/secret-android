package cf.paradoxie.dizzypassword;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;


public class MyApplication extends Application {
    public static MyApplication mInstance;
    public static Context mContext;
    public static boolean isShow = true;
    private static Toast mToast;

    @Override
    public void onCreate() {

        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
    }

    //返回
    public static Context getContext() {
        return mContext;
    }

    public static MyApplication getInstance() {
        return mInstance;
    }




    public static void showToast(CharSequence message) {

        if (isShow && message != null && !MyApplication.isStrNull(message + ""))
            if (mToast == null) {
                mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
            }
        mToast.show();
    }

    /**
     * 判断为空,可以empty替换??
     *
     * @param str
     * @return
     */
    public static boolean isStrNull(String str) {
        if (null == str) {
            return true;
        } else if ("".equals(str.trim())) {
            return true;
        } else if ("null".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }




}
