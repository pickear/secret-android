package cf.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.util.StringUtils;

public class Login extends Activity {

    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.view)
    RelativeLayout view;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.user_info)
    LinearLayout userInfo;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.ly)
    LinearLayout ly;
    @BindView(R.id.Forget_password)
    TextView ForgetPassword;
    @BindView(R.id.go_register)
    TextView goRegister;
    @BindView(R.id.lys)
    LinearLayout lys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.login, R.id.go_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.login:
                if(!StringUtils.isEmpty(LoginError())){
                    Toast.makeText(Login.this,LoginError(),Toast.LENGTH_LONG).show();
                }else{
                    login();
                }
                break;
            case R.id.go_register:
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);

                break;
        }
    }
    private String LoginError() {
        if (StringUtils.isEmpty(userName.getText().toString().trim())) {
            return "请输入用户名";
        } else if (StringUtils.isEmpty(password.getText().toString())) {
            return "请输入密码";
        }
        return "";
    }
    private void login(){
        HttpParams httpParams=new HttpParams();
        httpParams.put("username",userName.getText().toString().trim());
        httpParams.put("password",password.getText().toString().trim());
        OkGo.<String>post(AllApi.login).tag(this).params(httpParams).execute(new Callback<String>() {
            @Override
            public void onStart(Request<String, ? extends Request> request) {

            }

            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", "注册返回的消息response.body()：" + response.body());
                Log.e("backinfo","注册返回的消息response.message()："+response.message());
                Log.e("backinfo","注册返回的消息response.code()："+response.code());
                if(response.code()==200){
                    Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCacheSuccess(Response<String> response) {

            }

            @Override
            public void onError(Response<String> response) {
                Log.e("backinfo","注册返回错误的消息response.message()："+response.message());
                Log.e("backinfo","注册返回错误的消息response.code()："+response.code());
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void uploadProgress(Progress progress) {

            }

            @Override
            public void downloadProgress(Progress progress) {

            }

            @Override
            public String convertResponse(okhttp3.Response response) throws Throwable {
                return null;
            }
        });

    }
}
