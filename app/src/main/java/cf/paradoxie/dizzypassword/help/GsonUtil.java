package cf.paradoxie.dizzypassword.help;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by a1 on 2017/11/16.
 */
public class GsonUtil {
    private static class GsonHolder{
        private static final Gson INSTANCE = new GsonBuilder()
                .setLenient()// json宽松
                .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                .serializeNulls() //智能null
                .setPrettyPrinting()// 调教格式
                .disableHtmlEscaping() //默认是GSON把HTML 转义的
                .create();
    }

    /**
     * 获取Gson实例，由于Gson是线程安全的，这里共同使用同一个Gson实例
     */
    public static Gson getGsonInstance()
    {
        return GsonHolder.INSTANCE;
    }
}
