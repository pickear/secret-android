package cf.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;

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
                break;
            case R.id.go_register:
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);

                break;
        }
    }
}
