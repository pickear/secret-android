package harlan.paradoxie.dizzypassword.appupdate;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alibaba.fastjson.JSON;
import com.dou361.update.ParseData;
import com.dou361.update.UpdateHelper;
import com.dou361.update.bean.Update;

import harlan.paradoxie.dizzypassword.appupdate.bean.CheckResultRepository;
import harlan.paradoxie.dizzypassword.appupdate.bean.UpdateBean;

/**
 * Created by a1 on 2017/6/13.
 */
public class UpdateConfig {

    /**分离网络使用的初始化*/
    public static void initNoUrl(Context context) {
        UpdateHelper.init(context);
        UpdateHelper.getInstance()
                /**可填：清除旧的自定义布局设置。之前有设置过自定义布局，建议这里调用下*/
                .setClearCustomLayoutSetting()
                        /**可填：自定义更新弹出的dialog的布局样式，主要案例中的布局样式里面的id为（jjdxm_update_content、jjdxm_update_id_ok、jjdxm_update_id_cancel）的view类型和id不能修改，其他的都可以修改或删除*/
//                .setDialogLayout(R.layout.custom_update_dialog)
                        /**可填：自定义更新状态栏的布局样式，主要案例中的布局样式里面的id为（jjdxm_update_rich_notification_continue、jjdxm_update_rich_notification_cancel、jjdxm_update_title、jjdxm_update_progress_text、jjdxm_update_progress_bar）的view类型和id不能修改，其他的都可以修改或删除*/
//                .setStatusBarLayout(R.layout.custom_download_notification)
                        /**可填：自定义强制更新弹出的下载进度的布局样式，主要案例中的布局样式里面的id为(jjdxm_update_progress_bar、jjdxm_update_progress_text)的view类型和id不能修改，其他的都可以修改或删除*/
//                .setDialogDownloadLayout(R.layout.custom_download_dialog)
                        /**必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理*/
                .setCheckJsonParser(new ParseData() {
                    @Override
                    public Update parse(String response) {
                        /**真实情况下使用的解析  response接口请求返回的数据*/
                        CheckResultRepository checkResultRepository = JSON.parseObject(response, CheckResultRepository.class);
                        /**这里是模拟后台接口返回的json数据解析的bean，需要根据真实情况来写*/
                        UpdateBean updateBean = checkResultRepository.getData();
                        Update update = new Update();
                        /**必填：此apk包的下载地址*/
                        update.setUpdateUrl(updateBean.getDownload_url());
                        /**必填：此apk包的版本号*/
                        update.setVersionCode(updateBean.getV_code());
                        /**可填：此apk包的版本号*/
                        update.setApkSize(updateBean.getV_size());
                        /**必填：此apk包的版本名称*/
                        update.setVersionName(updateBean.getV_name());
                        /**可填：此apk包的更新内容*/
                        update.setUpdateContent(updateBean.getUpdate_content());
                        /**可填：此apk包是否为强制更新*/
                        update.setForce(updateBean.isForce());
                        return update;
                    }
                });
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
}
