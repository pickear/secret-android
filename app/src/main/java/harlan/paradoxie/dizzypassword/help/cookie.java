package harlan.paradoxie.dizzypassword.help;

import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.CookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by a1 on 2017/12/8.
 */
public class cookie {
    public static void Getcookie(String url){
        //一般手动取出cookie的目的只是交给 webview 等等，非必要情况不要自己操作
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        HttpUrl httpUrl = HttpUrl.parse(url);
        List<Cookie> cookies = cookieStore.getCookie(httpUrl);
        Log.e("backinfo", "对应的cookie如下：" + cookies.toString());
    }
}
