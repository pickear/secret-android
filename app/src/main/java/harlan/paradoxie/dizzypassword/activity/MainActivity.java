package harlan.paradoxie.dizzypassword.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.update.UpdateHelper;
import com.dou361.update.listener.ForceListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.weasel.secret.common.domain.UpdateInfo;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import harlan.paradoxie.dizzypassword.MyApplication;
import harlan.paradoxie.dizzypassword.R;
import harlan.paradoxie.dizzypassword.api.AllApi;
import harlan.paradoxie.dizzypassword.fragment.Homefragment;
import harlan.paradoxie.dizzypassword.fragment.Myfragment;
import harlan.paradoxie.dizzypassword.help.GsonUtil;
import harlan.paradoxie.dizzypassword.tabhost.Tab;
import harlan.paradoxie.dizzypassword.tabhost.TabFragmentHost;
import harlan.paradoxie.dizzypassword.util.StringUtils;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LayoutInflater layoutInflater;
    private TabFragmentHost mTabHost;
    String TAG="backinfo";

    boolean isExit = false;
    private List<Tab> mTabs = new ArrayList<Tab>(2);
    /*private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;*/
    Homefragment homefragment = new Homefragment();
    Myfragment twofragment = new Myfragment();
    private String mTextviewArray[] = {"密码列表", "我的"};
    private int mImageViewArray[] = {R.drawable.productclassification_bg, R.drawable.my_bg
    };
   // Socket mSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutInflater = LayoutInflater.from(this);


        mTabHost = (TabFragmentHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        initView();
        Toast.makeText(this,"测试热修复",Toast.LENGTH_LONG).show();
        initSocketHttps();
        connectSocket();
        updateapp();

    }
    public void updateapp(){
        OkGo.<String>get(AllApi.update).params("version", getVersionName(MainActivity.this)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", "response:" + response.body());
                harlan.paradoxie.dizzypassword.appupdate.bean.Update update = new harlan.paradoxie.dizzypassword.appupdate.bean.Update();
                UpdateInfo info = GsonUtil.getGsonInstance().fromJson(response.body(), UpdateInfo.class);
                if(info.isHasUpdate()==true&& !StringUtils.isEmpty(info.getUrl())){
                    update.setCode(0);
                    update.getData().setDownload_url(info.getUrl());
                    update.getData().setForce(info.isForce());
                    update.getData().setV_size((int) info.getSize());
                    update.getData().setUpdate_content(info.getDescription());
                    update.getData().setV_code(getVersionCode(MainActivity.this)+1);
                    update.getData().setV_name(getVersionName(MainActivity.this));
                    networkUpdate(GsonUtil.getGsonInstance().toJson(update));
                }
              /*  String requestStri = "{\"code\":0,\"data\":{\"download_url\":\"http://wap.apk.anzhi.com/data3/apk/201512/20/55089e385f6e9f350e6455f995ca3452_26503500.apk\",\"force\":false,\"update_content\":\"测试更新接口\",\"v_code\":\"10\",\"v_name\":\"v1.0.0.16070810\",\"v_sha1\":\"7db76e18ac92bb29ff0ef012abfe178a78477534\",\"v_size\":12365909}}";
                networkAutoUpdate(requestStri);*/


            }
        });
    }

    private void networkAutoUpdate(String requestStri) {
        UpdateHelper.getInstance()
                .setRequestResultData(requestStri)
                .setForceListener(new ForceListener() {
                    @Override
                    public void onUserCancel(boolean force) {
                        if (force) {
                            //退出应用
                            finish();
                        }
                    }
                })
                .checkNoUrl(MainActivity.this);

    }

    /**
     * 分离网络的检测更新
     */
    private void networkUpdate(String data) {

        UpdateHelper.getInstance()
                .setRequestResultData(data)
                .setForceListener(new ForceListener() {
                    @Override
                    public void onUserCancel(boolean force) {
                        if (force) {
                            //退出应用
                            finish();
                        }
                    }
                })
                .checkNoUrl(MainActivity.this);
    }
    private void initView() {
        Tab tab_home = new Tab(homefragment.getClass(), R.string.home, mImageViewArray[0]);
        Tab tws = new Tab(twofragment.getClass(), R.string.My, mImageViewArray[1]);
        mTabs.add(tab_home);
        mTabs.add(tws);
        int i = -1;
        for (Tab tab : mTabs) {
            i = i + 1;
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(i));//需要一个View显示 菜单控件
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }
        // mTabHost.getTabWidget().setDividerDrawable(null);
    }

    private View buildIndicator(int index) {
        View view = null;
        view = layoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);
        return view;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
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
    @Override
    public void onBackPressed() {
        exitBy2Click();
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true;
            // 准备退出
            MyApplication.showToast("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    TrustManager[] trustCerts = new TrustManager[]{new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {

        }
    }};

    private void initSocketHttps() {
       /* SSLContext sc = null;
        TrustManager[] trustCerts = new TrustManager[] { new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted( X509Certificate[] chain, String authType )
                    throws CertificateException {
            }

            @Override
            public void checkClientTrusted( X509Certificate[] chain, String authType )
                    throws CertificateException {

            }
        } };
        try {
            sc = SSLContext.getInstance( "TLS" );
            sc.init( null, trustCerts, null );
            IO.Options opts = new IO.Options();
            opts.sslContext = sc;
            opts.hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify( String s, SSLSession sslSession ) {
                    return true;
                }
            };
            mSocket = IO.socket( "https://35.165.220.128:8012", opts );
        } catch ( NoSuchAlgorithmException e ) {
            e.printStackTrace();
        } catch ( KeyManagementException e ) {
            e.printStackTrace();
        } catch ( URISyntaxException e ) {
            e.printStackTrace();
        }*/
    }

    /**
     * 初始化Socket,Http的连接方式
     */
    private void initSocketHttp() {
      /*  try {
            mSocket = IO.socket( "http://35.165.220.128:8012" ); // 初始化Socket
        } catch ( URISyntaxException e ) {
            e.printStackTrace();
        }*/
    }

    private void connectSocket() {
     /*   try {
            mSocket.connect();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put( "userName", "小王" ); // 这里一般是设置登录名
           // mSocket.emit( "loginName", jsonObject ); // 发送登录人
        } catch ( JSONException e ) {
            e.printStackTrace();
        }

        mSocket.on( Socket.EVENT_CONNECT, onConnect );// 连接成功
        mSocket.on( Socket.EVENT_DISCONNECT, onDisconnect );// 断开连接
        mSocket.on( Socket.EVENT_CONNECT_ERROR, onConnectError );// 连接异常
        mSocket.on( Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeoutError );// 连接超时
        mSocket.on( "newMessage", onConnectMsg );// 监听消息事件回调*/
    }

    private void disConnectSocket() {
     /*   mSocket.disconnect();

        mSocket.off( Socket.EVENT_CONNECT, onConnect );// 连接成功
        mSocket.off( Socket.EVENT_DISCONNECT, onDisconnect );// 断开连接
        mSocket.off( Socket.EVENT_CONNECT_ERROR, onConnectError );// 连接异常
        mSocket.off( Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeoutError );// 连接超时
        mSocket.off( "newMessage", onConnectMsg );// 监听消息事件回调*/
    }

  /*  private Emitter.Listener onConnectMsg = new Emitter.Listener() {
        @Override
        public void call( final Object... args ) {
            // 在这里处理你的消息
            Log.e( TAG, "服务器返回来的消息 : " + args[0] );
        }
    };

    *//**
     * 实现消息回调接口
     *//*
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call( final Object... args ) {
            Log.e( TAG, "连接成功 " + args[0] );


        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call( Object... args ) {


        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call( final Object... args ) {
            Log.e( TAG, "连接 失败" + args[0] );
        }
    };

    private Emitter.Listener onConnectTimeoutError = new Emitter.Listener() {
        @Override
        public void call( final Object... args ) {
            Log.e( TAG, "连接 超时" + args[0] );

        }
    };
*/



}
