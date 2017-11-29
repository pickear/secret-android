package cf.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.weasel.secret.common.domain.Secret;
import com.weasel.secret.common.domain.Subject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.adapter.AddTypeAdapter;
import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.db.help.dbutlis.SecretHelp;
import cf.paradoxie.dizzypassword.db.help.dbutlis.SecretListHelp;
import cf.paradoxie.dizzypassword.dbdomain.SecretList;
import cf.paradoxie.dizzypassword.domian.LoginBean;
import cf.paradoxie.dizzypassword.domian.ServerSecret;
import cf.paradoxie.dizzypassword.domian.UpdataView;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.util.SPUtils;
import cf.paradoxie.dizzypassword.util.StringUtils;
import cf.paradoxie.dizzypassword.widget.CustListView;

public class AddSubject extends Activity {
    AddTypeAdapter adapter;
    List<Secret> secrets = new ArrayList<>();

    View rootView;
    EditText custed;
    Button cancel, sure;
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.secrettitle)
    EditText secrettitle;
    @Bind(R.id.listviewinfo)
    CustListView listviewinfo;
    @Bind(R.id.url)
    EditText url;
    @Bind(R.id.ok)
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        ButterKnife.bind(this);
        rootView = View.inflate(AddSubject.this, R.layout.keyinputview, null);
        Secret secret = new Secret();
        secrets.add(secret);

        adapter = new AddTypeAdapter(AddSubject.this, secrets);
        listviewinfo.setAdapter(adapter);
    }

    Dialog dialogUIUtils;

    @OnClick({R.id.back, R.id.ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ok:
                String eorre = isEmptyEorre();
                if (StringUtils.isEmpty(secrettitle.getText().toString().trim())) {
                    Toast.makeText(AddSubject.this, "请输入标题", Toast.LENGTH_LONG).show();
                } else if (!StringUtils.isEmpty(eorre)) {
                    Toast.makeText(AddSubject.this, eorre, Toast.LENGTH_LONG).show();
                } else {
                    dialogUIUtils = DialogUIUtils.showCustomAlert(AddSubject.this, rootView, Gravity.CENTER, true, false).show();
                    custed = (EditText) rootView.findViewById(R.id.et_1);
                    cancel = (Button) rootView.findViewById(R.id.cancel);
                    sure = (Button) rootView.findViewById(R.id.sure);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUIUtils.dismiss(dialogUIUtils);
                        }
                    });
                    sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!StringUtils.isEmpty(custed.getText().toString().trim()) && custed.getText().toString().trim().length() == 8) {
                                Subject subject = new Subject();
                                subject.setTitle(secrettitle.getText().toString().trim());
                                subject.setUrl(url.getText().toString().trim());
                                subject.setSecrets(adapter.getData());
                                try {
                                    subject.entryptAllSecret(custed.getText().toString().trim());
                                    Log.e("backinfo", GsonUtil.getGsonInstance().toJson(subject));
                                    if(StringUtils.isEmpty(SPUtils.getInstance().getString("username",""))){
                                        cf.paradoxie.dizzypassword.dbdomain.Secret secret=new cf.paradoxie.dizzypassword.dbdomain.Secret();
                                        secret.setTitle(secrettitle.getText().toString().trim());
                                        secret.setUrl(url.getText().toString().trim());
                                        SecretHelp.insert(secret);
                                        Long secretid= SecretHelp.getlastid();
                                       Log.e("backinfo","长度："+adapter.getData().size());
                                        for(int i=0;i<adapter.getData().size();i++){
                                            SecretList secretList=new SecretList();
                                            secretList.setSecretId(secretid);
                                            secretList.setName(adapter.getData().get(i).getName());
                                            secretList.setValuse(adapter.getData().get(i).getValue());
                                            SecretListHelp.insert(secretList);
                                        }

                                        UpdataView updataView=new UpdataView();
                                        updataView.setView("HOME");
                                        EventBus.getDefault().post(updataView);
                                        Toast.makeText(AddSubject.this, "保存本地成功", Toast.LENGTH_LONG).show();
                                        finish();
                                        DialogUIUtils.dismiss(dialogUIUtils);
                                    }else{
                                        Logined(subject);
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(AddSubject.this, "加密出错" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e("backinfo", e.getMessage());
                                    Log.e("backinfo", e.toString());

                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(AddSubject.this, "请输入正确的秘钥", Toast.LENGTH_LONG).show();


                            }

                        }
                    });


                }
                Log.e("backinfo", GsonUtil.getGsonInstance().toJson(adapter.getData()));
                break;
        }
    }

    private String isEmptyEorre() {
        Log.e("backinfo", "数据："+GsonUtil.getGsonInstance().toJson(adapter.getData()));
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (StringUtils.isEmpty(adapter.getData().get(i).getName())) {
                return "请输入或自定义第" + (i + 1) + "项目的密码类型";
            } else if (StringUtils.isEmpty(adapter.getData().get(i).getValue())) {
                return "请输入第" + (i + 1) + "项目的密码";
            }
        }
        return "";
    }

    private void Sava(Subject subject) {
        OkGo.<String>post(AllApi.save).upJson(GsonUtil.getGsonInstance().toJson(subject)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", response.body());

                Log.e("backinfo", response.toString());
                ServerSecret serverSecret = GsonUtil.getGsonInstance().fromJson(response.body(), ServerSecret.class);
                if (response.code() == 200) {
                    Toast.makeText(AddSubject.this, "添加成功", Toast.LENGTH_LONG).show();
                    DialogUIUtils.dismiss(dialogUIUtils);
                } else {
                    Toast.makeText(AddSubject.this, "添加失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void Logined(final Subject subject) {
        OkGo.<String>get(AllApi.logined).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LoginBean loginBean = GsonUtil.getGsonInstance().fromJson(response.body(), LoginBean.class);
                if (loginBean.getCode().equals("0000")) {
                    Sava(subject);
                } else if (loginBean.getCode().equals("0001")) {
                    Toast.makeText(AddSubject.this, "未登录，请先登录", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddSubject.this, Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddSubject.this, loginBean.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
