package cf.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.weasel.secret.common.domain.User;
import com.weasel.secret.common.helper.EntryptionHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.domian.LoginBean;
import cf.paradoxie.dizzypassword.domian.UpdataView;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.service.HeartbeatService;
import cf.paradoxie.dizzypassword.util.SPUtils;
import cf.paradoxie.dizzypassword.util.StringUtils;

public class Login extends Activity {


    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.view)
    RelativeLayout view;
    @Bind(R.id.user_name)
    EditText userName;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.user_info)
    LinearLayout userInfo;
    @Bind(R.id.login)
    Button login;
    @Bind(R.id.ly)
    LinearLayout ly;
    @Bind(R.id.CloudSynchronization)
    CheckBox CloudSynchronization;
    @Bind(R.id.go_register)
    TextView goRegister;
    @Bind(R.id.lys)
    LinearLayout lys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // new ArrayList<String>().stream().forEach(name -> System.out.println(name));
        ButterKnife.bind(this);
        userName.addTextChangedListener(new TextChangeWatcher());
        password.addTextChangedListener(new TextChangeWatcher());
        CloudSynchronization.setChecked(SPUtils.getInstance().getBoolean("cloud", true));
        CloudSynchronization.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.getInstance().put("cloud", isChecked);
                Log.e("backinfo", "cloud:" + SPUtils.getInstance().getBoolean("cloud"));
            }
        });

    }

    @OnClick({R.id.back, R.id.login, R.id.go_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.login:
                if (!StringUtils.isEmpty(LoginError())) {
                    Toast.makeText(Login.this, LoginError(), Toast.LENGTH_LONG).show();
                } else {
                    login();
                }
                break;
            case R.id.go_register:
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);

                break;
        }
    }

    class TextChangeWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isclick() == true) {
                login.setBackgroundResource(R.drawable.click_bg);
            } else {
                login.setBackgroundResource(R.drawable.no_click_bg);
            }

        }
    }

    private boolean isclick() {
        if (userName.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() >= 6) {
            return true;
        }
        return false;
    }

    private String LoginError() {
        if (StringUtils.isEmpty(userName.getText().toString().trim())) {
            return "请输入用户名";
        } else if (StringUtils.isEmpty(password.getText().toString())) {
            return "请输入密码";
        }
        return "";
    }

    User user = new User();
    Dialog dialog;

    private void login() {

        user.setUsername(userName.getText().toString().trim());
        user.setPassword(password.getText().toString().trim());
        // Log.e("backinfo", GsonUtil.getGsonInstance().toJson(user));
     /*   HashMap<String, String> params = new HashMap<>();
        params.put("username", userName.getText().toString().trim());
        params.put("password", password.getText().toString().trim());
*/


        OkGo.<String>post(AllApi.login).tag(this).upJson(GsonUtil.getGsonInstance().toJson(user)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", "登录返回的消息response.body()：" + response.body());
                Log.e("backinfo", "登录返回的消息response.message()：" + response.message());
                Log.e("backinfo", "登录返回的消息response.toString()：" + response.toString());
                LoginBean loginBean = GsonUtil.getGsonInstance().fromJson(response.body(), LoginBean.class);
                if (loginBean.getCode().equals("0000")) {
                    Toast.makeText(Login.this, loginBean.getMessage(), Toast.LENGTH_LONG).show();
                    try {
                        SPUtils.getInstance().put("username", user.getUsername());
                        SPUtils.getInstance().put("password", EntryptionHelper.encrypt("password", user.getPassword()));
                        Intent serviceIntent = new Intent(Login.this, HeartbeatService.class);
                        serviceIntent.putExtra("url", AllApi.beat);
                        startService(serviceIntent);
                        UpdataView updataView=new UpdataView();
                        updataView.setView("HOME");
                        EventBus.getDefault().post(updataView);
                    } catch (Exception e) {
                        Log.e("backinfo", "密码加密出错");
                        e.printStackTrace();
                    }
                    finish();
                } else {
                    Toast.makeText(Login.this, loginBean.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onError(Response<String> response) {
                Toast.makeText(Login.this, "登录失败", Toast.LENGTH_LONG).show();
                super.onError(response);
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                dialog = DialogUIUtils.showLoading(Login.this, "登录中...", true, false, false, true).show();
                super.onStart(request);
            }

            @Override
            public void onFinish() {
                if (dialog != null && dialog.isShowing()) {
                    DialogUIUtils.dismiss(dialog);
                }
                super.onFinish();
            }
        });


    }
}
