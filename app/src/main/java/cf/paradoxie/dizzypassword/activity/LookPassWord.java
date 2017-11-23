package cf.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.password.PswInputView;

public class LookPassWord extends Activity {

    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.psw_input)
    PswInputView pswInput;
    @BindView(R.id.next)
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_pass_word);
        ButterKnife.bind(this);
        pswInput.setInputCallBack(new PswInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
               // Toast.makeText(LookPassWord.this, result, Toast.LENGTH_SHORT).show();

            }
        });
    }
    @OnClick({R.id.back, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next:
                if(pswInput.isFinishInput()==true){
                    Intent intent=new Intent(LookPassWord.this,ConfirmPassword.class);
                    intent.putExtra("pd",pswInput.Inputresult());
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LookPassWord.this, "请输入完整的8位密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
