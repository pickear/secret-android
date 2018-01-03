package harlan.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.weasel.secret.common.domain.Secret;
import com.weasel.secret.common.domain.Subject;
import com.weasel.secret.common.helper.EntryptionHelper;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import harlan.paradoxie.dizzypassword.R;
import harlan.paradoxie.dizzypassword.adapter.LSwipeAdapter;
import harlan.paradoxie.dizzypassword.api.AllApi;
import harlan.paradoxie.dizzypassword.db.help.dbutlis.SecretHelp;
import harlan.paradoxie.dizzypassword.db.help.dbutlis.SecretListHelp;
import harlan.paradoxie.dizzypassword.dbdomain.SecretList;
import harlan.paradoxie.dizzypassword.domian.ServerSecret;
import harlan.paradoxie.dizzypassword.domian.UpdataView;
import harlan.paradoxie.dizzypassword.help.Date_U;
import harlan.paradoxie.dizzypassword.help.GsonUtil;
import harlan.paradoxie.dizzypassword.help.ObjectUtils;
import harlan.paradoxie.dizzypassword.util.SPUtils;
import harlan.paradoxie.dizzypassword.util.StringUtils;
import harlan.paradoxie.dizzypassword.widget.CustListView;

public class EditSecret extends Activity {

    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.secrettitle)
    EditText secrettitle;
    @Bind(R.id.url)
    EditText url;
    @Bind(R.id.edit_listview)
    CustListView editListview;
    String json;
    String location = "0";
    LSwipeAdapter adapter;
    String key;
    @Bind(R.id.editok)
    Button editok;
    Dialog dialog;
    harlan.paradoxie.dizzypassword.dbdomain.Secret subjectsBean;
    @Bind(R.id.account)
    EditText account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_secret);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            json = bundle.getString("json");
            location = bundle.getString("location");
            key = bundle.getString("key");
            Log.e("backinfo", "json:" + json);
            Log.e("backinfo", "location:" + location);
            Log.e("backinfo", "key:" + key);
            subjectsBean = GsonUtil.getGsonInstance().fromJson(json, harlan.paradoxie.dizzypassword.dbdomain.Secret.class);
            secrettitle.setText(subjectsBean.getTitle());
            account.setText(subjectsBean.getAccount());
            url.setText(subjectsBean.getUrl());
          /*  if(location.equals("0")){
                adapter = new LSwipeAdapter(EditSecret.this, subjectsBean.getSecretLists(), key);
            }else{*/
            adapter = new LSwipeAdapter(EditSecret.this, subjectsBean.getSecrets(), key);
            //  }

            editListview.setAdapter(adapter);
        }
    }

    @OnClick({R.id.editok, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.editok:
                String eorre = isEmptyEorre();
                if (StringUtils.isEmpty(secrettitle.getText().toString().trim())) {
                    Toast.makeText(EditSecret.this, "请输入标题", Toast.LENGTH_LONG).show();
                }else if(StringUtils.isEmpty(account.getText().toString().trim())){
                    Toast.makeText(EditSecret.this, "请输入账号名称", Toast.LENGTH_LONG).show();
                } else if (!StringUtils.isEmpty(eorre)) {
                    Toast.makeText(EditSecret.this, eorre, Toast.LENGTH_LONG).show();
                } else {
                    if (!StringUtils.isEmpty(SPUtils.getInstance().getString("username", ""))) {
                        Subject subject = new Subject();
                        subject.setTitle(secrettitle.getText().toString().trim());
                        subject.setId(subjectsBean.getId());
                        subject.setUrl(url.getText().toString().trim());
                        //   Log.e("backinfo","时间格式："+Date_U.getNowDate());
                        subject.setUpdateTime(Date_U.getNowDate());
                        subject.setAccount(account.getText().toString());
                        List<SecretList> secrets = new ArrayList<SecretList>();
                        List<Secret> secretList = new ArrayList<>();
                        Type type = new TypeToken<ArrayList<SecretList>>() {
                        }.getType();
                        secrets = GsonUtil.getGsonInstance().fromJson(GsonUtil.getGsonInstance().toJson(adapter.getData()), type);
                        for (int i = 0; i < secrets.size(); i++) {
                            Secret secret = new Secret();
                            secret.setId(secrets.get(i).getId());
                            secret.setName(secrets.get(i).getName());
                            secret.setValue(secrets.get(i).getValue());
                          /*  if(secrets.get(i).getSubjectId()!=null){
                                secret.setSubjectId(secrets.get(i).getSubjectId());
                            }*/
                            secretList.add(secret);
                        }
                        subject.setSecrets(secretList);
                        Log.e("backinfo", "未加密前的数据：" + GsonUtil.getGsonInstance().toJson(subject));
                        try {
                            Log.e("backinfo", "key:" + key);
                            subject.entryptAllSecret(key);
                            Sava(subject);
                        } catch (Exception e) {
                            Log.e("backinfo", "加密出错");
                            e.printStackTrace();
                        }
                        Log.e("backinfo", "加密后的数据：" + GsonUtil.getGsonInstance().toJson(subject));
                    } else {
                        updateDB(false);
                        UpdataView updataView = new UpdataView();
                        updataView.setView("db");
                        EventBus.getDefault().post(updataView);
                        finish();
                        Toast.makeText(EditSecret.this, "修改成功", Toast.LENGTH_LONG).show();

                    }

                }
                break;

        }
    }

    /**
     * setCloud表示是否已经同步，true表示已经同步了，false 不同步
     */
    private void updateDB(boolean setCloud) {
        harlan.paradoxie.dizzypassword.dbdomain.Secret secret = new harlan.paradoxie.dizzypassword.dbdomain.Secret();
        secret.setUrl(url.getText().toString().trim());
        if (subjectsBean.getId() != null) {
            secret.setId(subjectsBean.getId());
        }
        secret.setSid(subjectsBean.getSid());
        secret.setTitle(secrettitle.getText().toString().trim());
        secret.setAccount(account.getText().toString().trim());
        secret.setCloud(setCloud);
        secret.setUpdateTime(Date_U.getNowDate());
        SecretHelp.update(secret);
        SecretListHelp.delete(subjectsBean.getSid());
        List<SecretList> secrets;
        Type type = new TypeToken<ArrayList<SecretList>>() {
        }.getType();
        secrets = GsonUtil.getGsonInstance().fromJson(GsonUtil.getGsonInstance().toJson(adapter.getData()), type);
        // Log.e("backinfo","编辑："+GsonUtil.getGsonInstance().toJson(adapter.getData()));
        secret.setSecrets(secrets);
        Iterator var2 = secret.getSecrets().iterator();
        while (var2.hasNext()) {
            SecretList secretList = (SecretList) var2.next();
            try {
                secretList.setValue(EntryptionHelper.encrypt(key, secretList.getValue()));
                secretList.setSecretId(subjectsBean.getSid());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        SecretListHelp.insertList(secrets);
        //  SecretHelp.update(secret, secret.getSecrets());
    }

    private String isEmptyEorre() {
        try {
            Log.e("backinfo", "数据：" + GsonUtil.getGsonInstance().toJson(adapter.getData()));
            for (int i = 0; i < adapter.getData().size(); i++) {
                if (StringUtils.isEmpty((String) ObjectUtils.getValueByKey(adapter.getData().get(i), "name"))) {
                    return "请输入或自定义第" + (i + 1) + "项目的密码类型";
                } else if (StringUtils.isEmpty((String) ObjectUtils.getValueByKey(adapter.getData().get(i), "value"))) {
                    return "请输入第" + (i + 1) + "项目的密码";
                }
            }
            return "";
        } catch (Exception e) {
            return "获取数据出错";
        }

    }

    private void Sava(Subject subject) {
        dialog = DialogUIUtils.showLoading(EditSecret.this, "正在修改...", true, true, false, true).show();
        Log.e("backinfo", "上传数据：" + GsonUtil.getGsonInstance().toJson(subject));
        OkGo.<String>post(AllApi.save).upJson(GsonUtil.getGsonInstance().toJson(subject)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", "修改数据：" + response.body());
                try {
                    ServerSecret serverSecret = GsonUtil.fromjson(response.body(), ServerSecret.class);
                    if ("false".equals(response.headers().get("logined"))) {
                        Intent intent = new Intent(EditSecret.this, Login.class);
                        SPUtils.getInstance().remove("username");
                        EventBus.getDefault().post(false);
                        startActivity(intent);
                    } else {
                        Toast.makeText(EditSecret.this, "添加成功", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        harlan.paradoxie.dizzypassword.dbdomain.Secret secret = new harlan.paradoxie.dizzypassword.dbdomain.Secret();
                        secret.setUrl(serverSecret.getUrl());
                        secret.setId(serverSecret.getId());
                        secret.setSid(subjectsBean.getSid());
                        secret.setTitle(serverSecret.getTitle());
                        secret.setCloud(true);
                        secret.setAccount(serverSecret.getAccount());
                        secret.setUpdateTime(Date_U.getNowDate());
                        SecretHelp.update(secret);
                        SecretListHelp.delete(subjectsBean.getSid());
                        List<SecretList> secrets = new ArrayList<SecretList>();
                        Type type = new TypeToken<ArrayList<SecretList>>() {
                        }.getType();
                        //   secrets = GsonUtil.getGsonInstance().fromJson(GsonUtil.getGsonInstance().toJson(adapter.getData()), type);
                        Log.e("backinfo", "编辑：" + GsonUtil.getGsonInstance().toJson(secrets));
                        //  secret.setSecrets(secrets);
                        for (int i = 0; i < serverSecret.getSecrets().size(); i++) {
                            SecretList secretList = new SecretList();
                            secretList.setSubjectId(serverSecret.getSecrets().get(i).getSubjectId());
                            secretList.setValue(serverSecret.getSecrets().get(i).getValue());
                            secretList.setId(serverSecret.getSecrets().get(i).getId());
                            secretList.setName(serverSecret.getSecrets().get(i).getName());
                            secretList.setSecretId(subjectsBean.getSid());
                            secrets.add(secretList);

                        }
                        secret.setSecrets(secrets);
                        Log.e("backinfo", "插入数据库数据:" + GsonUtil.getGsonInstance().toJson(secret));
                        SecretListHelp.insertList(secrets);
                        UpdataView updataView = new UpdataView();
                        updataView.setView("db");
                        EventBus.getDefault().post(updataView);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                super.onFinish();
            }

            @Override
            public void onError(Response<String> response) {
                Log.e("backinfo", "修改出错");
                updateDB(false);
                UpdataView updataView = new UpdataView();
                updataView.setView("db");
                EventBus.getDefault().post(updataView);
                finish();
                super.onError(response);
            }
        });
    }
}
