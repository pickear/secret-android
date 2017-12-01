package cf.paradoxie.dizzypassword.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import cf.paradoxie.dizzypassword.api.AllApi;

public class HeartbeatService extends Service implements Runnable {
    private Thread mThread;
    public int count = 0;
    private boolean isTip = true;
    private static String mRestMsg;
    private static String KEY_REST_MSG = "KEY_REST_MSG";
    boolean heartbeat=true;
    @Override
    public void run() {
        while (heartbeat==true) {
            try {
                if (count > 1) {
                    Log.i("@qi", "offline");
                    count = 1;
                    if (isTip) {
                        //判断应用是否在运行
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(3);
                        for (ActivityManager.RunningTaskInfo info : list) {
                            if (info.topActivity.getPackageName().equals("cf.paradoxie.dizzypassword")) {
                                //通知应用，显示提示“连接不到服务器”
                                Intent intent = new Intent("cf.paradoxie.dizzypassword");
                                intent.putExtra("msg", true);
                                sendBroadcast(intent);
                                break;
                            }
                        }

                        isTip = false;
                    }
                }
                if (mRestMsg != "" && mRestMsg != null) {
                    //向服务器发送心跳包
                    sendHeartbeatPackage();
                    count += 1;
                }

                Thread.sleep(1000 * 60*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendHeartbeatPackage() {
        OkGo.<String>get(AllApi.beat).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo","心跳包启动"+response.body());
                if(response.code()==200){
                    count = 0;
                    isTip = true;
                }

            }
        });


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        Log.e("backinfo", "onDestroy");
        heartbeat=false;
        super.onDestroy();
    }


    public void onStart(Intent intent, int startId) {
        Log.i("backinfo", "service onStart");
        //从本地读取服务器的URL，如果没有就用传进来的URL
        heartbeat=true;
        mRestMsg = getRestMsg();
        if (mRestMsg == null || mRestMsg == "") {
            mRestMsg = intent.getExtras().getString("url");
        }
        setRestMsg(mRestMsg);

        mThread = new Thread(this);
        mThread.start();
        count = 0;

        super.onStart(intent, startId);
    }

    public String getRestMsg() {
        SharedPreferences prefer = getSharedPreferences("settings.data", Context.MODE_PRIVATE);
        Log.i("@qi", "getRestMsg() " + prefer.getString(KEY_REST_MSG, ""));
        return prefer.getString(KEY_REST_MSG, "");
    }

    public void setRestMsg(String restMsg) {
        SharedPreferences prefer = getSharedPreferences("settings.data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefer.edit();
        editor.putString(KEY_REST_MSG, restMsg);
        editor.commit();
    }

}