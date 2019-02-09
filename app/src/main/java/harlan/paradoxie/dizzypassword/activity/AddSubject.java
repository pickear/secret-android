package harlan.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import harlan.paradoxie.dizzypassword.R;
import harlan.paradoxie.dizzypassword.adapter.AddTypeAdapter;
import harlan.paradoxie.dizzypassword.api.AllApi;
import harlan.paradoxie.dizzypassword.db.help.dbutlis.SecretHelp;
import harlan.paradoxie.dizzypassword.db.help.dbutlis.SecretListHelp;
import harlan.paradoxie.dizzypassword.dbdomain.SecretList;
import harlan.paradoxie.dizzypassword.dbdomain.ServerDatail;
import harlan.paradoxie.dizzypassword.domian.ServerSecret;
import harlan.paradoxie.dizzypassword.domian.UpdataView;
import harlan.paradoxie.dizzypassword.help.Date_U;
import harlan.paradoxie.dizzypassword.help.GsonUtil;
import harlan.paradoxie.dizzypassword.util.SPUtils;
import harlan.paradoxie.dizzypassword.util.StringUtils;
import harlan.paradoxie.dizzypassword.widget.CustListView;

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
    MaterialDialog dialog;
    @Bind(R.id.account)
    EditText account;

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
                }else if(StringUtils.isEmpty(account.getText().toString().trim())){
                    Toast.makeText(AddSubject.this, "请输入账号", Toast.LENGTH_LONG).show();
                } else if (!StringUtils.isEmpty(eorre)) {
                    Toast.makeText(AddSubject.this, eorre, Toast.LENGTH_LONG).show();
                } else {
                    dialog = new MaterialDialog.Builder(AddSubject.this)
                            .customView(rootView, false)
                            .backgroundColor(Color.parseColor("#ffffff"))
                            .build();
                    dialog.show();
                    custed = (EditText) dialog.findViewById(R.id.et_1);
                    cancel = (Button) dialog.findViewById(R.id.cancel);
                    sure = (Button) dialog.findViewById(R.id.sure);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!StringUtils.isEmpty(custed.getText().toString().trim()) && custed.getText().toString().trim().length() == 8) {
                                Subject subject = new Subject();
                                subject.setCreateTime(Date_U.getNowDate());
                                subject.setTitle(secrettitle.getText().toString().trim());
                                subject.setUrl(url.getText().toString().trim());
                                subject.setAccount(account.getText().toString().trim());
                                subject.setSecrets(adapter.getData());

                                try {
                                    subject.entryptAllSecret(custed.getText().toString().trim());
                                    Log.e("backinfo", GsonUtil.getGsonInstance().toJson(subject));
                                    if (StringUtils.isEmpty(SPUtils.getInstance().getString("username", "")) || (StringUtils.isEmpty(SPUtils.getInstance().getString("username", "")) && SPUtils.getInstance().getBoolean("cloud") == false)) {
                                        harlan.paradoxie.dizzypassword.dbdomain.Secret secret = new harlan.paradoxie.dizzypassword.dbdomain.Secret();
                                        secret.setTitle(secrettitle.getText().toString().trim());
                                        secret.setUrl(url.getText().toString().trim());
                                        secret.setCreateTime(Date_U.getNowDate());
                                        secret.setId((long)-1);
                                        secret.setAccount(account.getText().toString().trim());
                                        secret.setCloud(false);
                                        secret.setUsername(SPUtils.getInstance().getString("username",""));
                                        SecretHelp.insert(secret);
                                        Long secretsid = SecretHelp.getlastid();
                                        for (int i = 0; i < adapter.getData().size(); i++) {
                                            SecretList secretList = new SecretList();
                                            secretList.setSecretId(secretsid);
                                            secretList.setName(adapter.getData().get(i).getName());
                                            secretList.setValue(adapter.getData().get(i).getValue());
                                            SecretListHelp.insert(secretList);
                                        }

                                        UpdataView updataView = new UpdataView();
                                        updataView.setView("db");
                                        EventBus.getDefault().post(updataView);
                                        Toast.makeText(AddSubject.this, "保存本地成功", Toast.LENGTH_LONG).show();
                                        finish();
                                        dialog.dismiss();
                                    } else {
                                        Sava(subject);

                                    }

                                } catch (Exception e) {
                                    Toast.makeText(AddSubject.this, "加密出错" + e.getMessage(), Toast.LENGTH_LONG).show();

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
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private String isEmptyEorre() {
        Log.e("backinfo", "数据：" + GsonUtil.getGsonInstance().toJson(adapter.getData()));
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
        dialog.show();
        Log.e("backinfo", "上传数据：" + GsonUtil.getGsonInstance().toJson(subject));
        OkGo.<String>post(AllApi.save).upJson(GsonUtil.getGsonInstance().toJson(subject)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo","添加数据："+response.body());
                try {
                    ServerDatail serverSecret = GsonUtil.fromjson(response.body(), ServerDatail.class);
                    if ("0000".equals(serverSecret.getCode())) {
                        harlan.paradoxie.dizzypassword.dbdomain.Secret secret = new harlan.paradoxie.dizzypassword.dbdomain.Secret();
                        secret.setCloud(true);
                        secret.setTitle(serverSecret.getBody().getTitle());
                        secret.setUrl(serverSecret.getBody().getUrl());
                        secret.setId(serverSecret.getBody().getId());
                        secret.setAccount(serverSecret.getBody().getAccount());
                        secret.setCreateTime(Date_U.getNowDate());
                        SecretHelp.insert(secret);
                        Long lasdid = SecretHelp.getlastid();
                        for (ServerSecret.SecretsBean bean :serverSecret.getBody().getSecrets()) {
                            SecretList secretList = new SecretList();
                            secretList.setId(bean.getId());
                            secretList.setSecretId(lasdid);
                            secretList.setName(bean.getName());
                            secretList.setValue(bean.getValue());
                            secretList.setSubjectId(bean.getSubjectId());
                            SecretListHelp.insert(secretList);
                        }
                        UpdataView updataView = new UpdataView();
                        updataView.setView("db");
                        EventBus.getDefault().post(updataView);
                        finish();
                    }
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("backinfo",e.getMessage());
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Response<String> response) {
                harlan.paradoxie.dizzypassword.dbdomain.Secret secret = new harlan.paradoxie.dizzypassword.dbdomain.Secret();
                secret.setTitle(secrettitle.getText().toString().trim());
                secret.setUrl(url.getText().toString().trim());
                secret.setAccount(account.getText().toString().trim());
                secret.setCreateTime(Date_U.getNowDate());
                secret.setCloud(false);
                secret.setId((long)-1);
                SecretHelp.insert(secret);
                Long secretid = SecretHelp.getlastid();
                for (int i = 0; i < adapter.getData().size(); i++) {
                    SecretList secretList = new SecretList();
                    secretList.setSecretId(secretid);
                    secretList.setName(adapter.getData().get(i).getName());
                    secretList.setValue(adapter.getData().get(i).getValue());
                    SecretListHelp.insert(secretList);
                }
                UpdataView updataView = new UpdataView();
                updataView.setView("db");
                EventBus.getDefault().post(updataView);
                finish();

                super.onError(response);
            }
        });
    }


}
