package cf.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.weasel.secret.common.helper.EntryptionHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.help.Constant;
import cf.paradoxie.dizzypassword.password.PswInputView;
import cf.paradoxie.dizzypassword.util.ACache;

public class ConfirmPassword extends Activity {

    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.psw_input)
    PswInputView pswInput;
    @BindView(R.id.ok)
    Button ok;
   String pd="";
    ACache cache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            pd=bundle.getString("pd","");
            Log.e("backinfo","pd："+pd);
        }
        cache=ACache.get(ConfirmPassword.this);
    }

    @OnClick({R.id.back, R.id.ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ok:
                if(pswInput.isFinishInput()==true){
                    Log.e("backinfo", "输入的密码：" + pswInput.Inputresult());
                   if(pswInput.Inputresult().equals(pd)){
                       try {
                           String pd=EntryptionHelper.encrypt(pswInput.Inputresult(), Constant.PD);
                           cache.put(Constant.PD,pd);
                           Log.e("backinfo", "加密后的秘钥：" + cache.getAsString(Constant.PD));
                           Toast.makeText(ConfirmPassword.this,"密码设置成功",Toast.LENGTH_LONG).show();
                           finish();
                       }catch (Exception e){
                           Toast.makeText(ConfirmPassword.this,"密码加密出错",Toast.LENGTH_LONG).show();
                       }

                   } else{
                       Toast.makeText(ConfirmPassword.this,"密码不一致",Toast.LENGTH_LONG).show();
                   }
                }else{
                    Toast.makeText(ConfirmPassword.this,"请输入完整的8位密码",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
