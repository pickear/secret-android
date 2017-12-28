package harlan.paradoxie.dizzypassword;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import harlan.paradoxie.dizzypassword.appupdate.UpdateConfig;
import harlan.paradoxie.dizzypassword.db.help.DBName;
import harlan.paradoxie.dizzypassword.db.help.MySqlLiteOpenHelper;
import harlan.paradoxie.dizzypassword.db.help.dbutlis.DaoManager;
import harlan.paradoxie.dizzypassword.gen.DaoMaster;
import harlan.paradoxie.dizzypassword.gen.DaoSession;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    public static MyApplication mInstance;
    public static Context mContext;
    public static boolean isShow = true;
    private static Toast mToast;
    //创建数据库的名字
    private static final String DB_NAME = DBName.DBNAME;

    //   private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static MySqlLiteOpenHelper mHelper;
    @Override
    public void onCreate() {

        super.onCreate();
        mInstance = this;
        UpdateConfig.initNoUrl(this);
        mContext = getApplicationContext();
        initOkGo();
        setDatabase();
        DaoManager.getInstance().init(this);
        initsophix();

    }

    public static MyApplication getInstances(){
        return mInstance;
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new MySqlLiteOpenHelper(this, DB_NAME, null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }
    public void closeHelper()
    {
        if(mHelper != null)
        {
            mHelper.close();
            mHelper = null;
        }
    }
    public static String appId;
   private void initsophix(){

       String appVersion;
       try {
           appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
       } catch (Exception e) {
           appVersion = "1.0";
       }
       SophixManager.getInstance().setContext(this)
               .setAppVersion(appVersion)
               .setAesKey(null)
               .setSecretMetaData("24745591-1", "22545c830f99e8092519a93d1514b7bf", "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDJAN3YxzyrKpevGFEXepQ7OlwXlY+Ii8ClC4ygNBEi3ih2NRv/BBhuM2ptJQYIvOi1Eko1iKqcMuEM8CVZRDNWE3S6nZtIYLbj9HeQlsCnlYcJm6Rtx/S1KFE8bhF593HJnltMBZsP/oVQu4/XcRnx9YqFmu0yk+qgmoIQeh1BerFc0qfSTCbmLFeaQimwYdM4XKFBkcSgY8u7WNcN4C781xKovOOkZCsFGuVj7Lhr9UqAVyshsNo8b3U4+2TGGg6gCi/VfjsWAWqb7eayctWJqPb77DiJ2okJPpjJF6Rcxmidg1mtxqKbIYW9bM1fg/FX1IuqJh4P8dgN3z9YnmCRAgMBAAECggEAKXzTTYY6IH5HFq3nNvhnFh6CmZ+WI7HszmQm334/tzmxkkx9/dIDlONp6SNGLpGHWrBuvsP5qwnZZ8k9fEJWckLLyiTzwymrNjvkXeSv2SdE2xfUBZDLEoVC1z1EwC7xvpK/914E1nVLYRGFrAs9NrLep5sFsHXVbNbXV71MwpHJl1Nk41EKv+cyhCTyPXuVhc98Jj+AHAkEFXTPL1iwKwGRp9VqSHOQgF1qBcpwQfCU4hpwRw9nLbJAyI9xT/iIax+571pvbA9nssffVXWiBjxDBfEqZ7nNuGtzMYj2z8b0pD5+QhUMxSS1FqU5q5vUeK7u835/sr9VA2wslWnnrQKBgQD0nDlet3kZ0yQPosvLaKT3x52TXBLH5EtIUZMlmxtMQFZUDBEvfIG54G/iUOIlMgzJArNRWuhBYU74aSk1X/75xYdFlYwJ2Z+OZP+LNoIeT/kgwvxDz//2EN72I3qo17sCZONTdJyQPslZUCYJI4dD3SgDanrkVv8++CGxn8f2qwKBgQDSXNhDqOpdlTNFYukQ7Nf+5gtEN3LK7sGDKIBlbFZCOCVJMWbe8rBd9FqSrJwgkhowBi27Wror8KfP29WOZSS+rNLL/K3gffEvRfwTbhGeCduRXnZWLGdPzP52UxjD3agD/v7eEn6YFPgCumjE3KuyEXsDGjn5VaWiUzSmMzK1swKBgCWp5F+EKp5iV1wc+fow+623S7kD5VRn/3t8LAcPUe20vlYkoYTJTAQ93ZxgTeHiSfutccTmFXrzq8AuGQ1B4bW9x21ccHqXyqyXOo3J79ERCVAVFdivLz9JK7uEjP9wcDgXJrWT8AN513DsGV8w3EQDyoR0IcYE6zpb5HxAGP8PAoGADDDhP5qManv5Cq5ev2JaaiU+xedIucX4ZPd16WrL3O6QCpvYUFdULT25+gIS0jhlWB8ji1YIr/80WnFtAOGPrZUqajPsh7QExC6UGzQnxTbhCJ2m0fukyRUiMg1CxCcWU5T1hD0iJQIiFVZkN/Rp5tnofReKAI8cGDomNPOGns0CgYEAuN5uzO5MTDLTVle+HT3OpLwOVVuv+6SQPUmLc2Pj1zwPx+nvygHGeHEP0Pz5Ds2Rflhi32SMzsnMdXkFRNKMwu1BPuBwxt9wwgbNaCNSZHwsraN45kig6ytqTldr2u9PQUsO17A+Q39kWMTJAEM9bgkMnD3oeIbZPVhTIqZ5QeM=")
               .setEnableDebug(true)
               .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                   @Override
                   public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                       // 补丁加载回调通知
                       if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                           // 表明补丁加载成功
                       } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                           restartApp();
                           // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                           // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                       } else {
                           // 其它错误信息, 查看PatchStatus类说明
                       }
                   }
               }).initialize();
// queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
       SophixManager.getInstance().queryAndLoadNewPatch();
   }
    public void restartApp(){
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void initOkGo() {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
       // HttpHeaders headers = new HttpHeaders();

      //  HttpParams params = new HttpParams();

        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        builder.hostnameVerifier(new SafeHostnameVerifier());

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)

                             //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传

                .setRetryCount(3)  ;                             //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
              /*  .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);         */              //全局公共参数
    }
    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private class SafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                for (X509Certificate certificate : chain) {
                    certificate.checkValidity(); //检查证书是否过期，签名是否通过等
                }
            } catch (Exception e) {
                throw new CertificateException(e);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //验证主机名是否匹配
            //return hostname.equals("server.jeasonlzy.com");
            return true;
        }
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
