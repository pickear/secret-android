package cf.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.weasel.secret.common.domain.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.util.StringUtils;

public class Register extends Activity {


    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.photo_number)
    EditText photoNumber;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.re_password)
    EditText rePassword;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.next)
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
        photoNumber.addTextChangedListener(new TextChangeWatcher());
        password.addTextChangedListener(new TextChangeWatcher());
        rePassword.addTextChangedListener(new TextChangeWatcher());
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
                next.setBackgroundResource(R.drawable.click_bg);
            } else {
                next.setBackgroundResource(R.drawable.no_click_bg);
            }

        }
    }

    private boolean isclick() {
        if (photoNumber.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() >= 6 && rePassword.getText().toString().trim().length() >= 6 && rePassword.getText().toString().trim().equals(password.getText().toString().trim())) {
            return true;
        }
        return false;
    }

    @OnClick({R.id.back, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next:
                if (StringUtils.isEmpty(returnError())) {
                    Toast.makeText(Register.this, returnError(), Toast.LENGTH_LONG).show();
                } else {
                    register();
                }
                break;
        }
    }

    private String returnError() {
        if (StringUtils.isEmpty(photoNumber.getText().toString().trim())) {
            return "请输入用户名";
        } else if (StringUtils.isEmpty(password.getText().toString())) {
            return "请输入密码";
        } else if (StringUtils.isEmpty(rePassword.getText().toString())) {
            return "请输入重复密码";
        } else if (StringUtils.equals(rePassword.getText().toString().trim(), password.getText().toString().trim())) {
            return "请输入密码不一致";
        }
        return "";
    }

    private void register() {
        User user = new User();
        user.setUsername(photoNumber.getText().toString().trim());
        user.setPassword(password.getText().toString().trim());
        user.setEmail(email.getText().toString().trim());
      /*  HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", photoNumber.getText().toString().trim());
        params.put("password", password.getText().toString().trim());
        params.put("email", email.getText().toString().trim());
        JSONObject jsonObject = new JSONObject(params);*/
        OkGo.<String>post(AllApi.register).tag(this)

                .upJson(GsonUtil.getGsonInstance().toJson(user))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("backinfo", "注册返回的消息response.body()：" + response.body());
                        Log.e("backinfo", "注册返回的消息response.message()：" + response.message());
                        Log.e("backinfo", "注册返回的消息response.toString()：" + response.toString());

                        Log.e("backinfo", "注册返回的消息response.code()：" + response.code());
                        if (response.code() == 200) {
                            Toast.makeText(Register.this, "恭喜你注册成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.e("backinfo", "注册返回错误的消息response.code()：" + response.code());
                        Log.e("backinfo", "注册返回错误的消息response.message()：" + response.message());
                        super.onError(response);
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
    }
}
