package cf.paradoxie.dizzypassword;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.weasel.secret.common.domain.User;
import com.weasel.secret.common.helper.EntryptionHelper;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.db.help.DBName;
import cf.paradoxie.dizzypassword.db.help.MySqlLiteOpenHelper;
import cf.paradoxie.dizzypassword.db.help.dbutlis.DaoManager;
import cf.paradoxie.dizzypassword.domian.LoginBean;
import cf.paradoxie.dizzypassword.gen.DaoMaster;
import cf.paradoxie.dizzypassword.gen.DaoSession;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.util.SPUtils;
import cf.paradoxie.dizzypassword.util.StringUtils;
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
        mContext = getApplicationContext();
        initOkGo();
        setDatabase();
        DaoManager.getInstance().init(this);
        if(!StringUtils.isEmpty(SPUtils.getInstance().getString("username",""))){
            login();
        }
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
        OkGo.getInstance().init(this)                           //必须调用初始化
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

    private void login() {
        User user=new User();
        user.setUsername(SPUtils.getInstance().getString("username"));
        try {
            user.setPassword(EntryptionHelper.decrypt("password",SPUtils.getInstance().getString("password")));
        } catch (Exception e) {
            e.printStackTrace();
        }


        OkGo.<String>post(AllApi.login).tag(this).upJson(GsonUtil.getGsonInstance().toJson(user)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", "登录返回的消息response.body()：" + response.body());
                Log.e("backinfo", "登录返回的消息response.message()：" + response.message());
                Log.e("backinfo", "登录返回的消息response.toString()：" + response.toString());
                LoginBean loginBean = GsonUtil.getGsonInstance().fromJson(response.body(), LoginBean.class);
                if (loginBean.getCode().equals("0000")) {

                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
            }

            @Override
            public void onFinish() {

                super.onFinish();
            }
        });


    }




}
