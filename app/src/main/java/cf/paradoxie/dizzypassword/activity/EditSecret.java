package cf.paradoxie.dizzypassword.activity;

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
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.adapter.LSwipeAdapter;
import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.db.help.dbutlis.SecretHelp;
import cf.paradoxie.dizzypassword.dbdomain.SecretList;
import cf.paradoxie.dizzypassword.dbdomain.UpdataSecret;
import cf.paradoxie.dizzypassword.domian.ServerSecret;
import cf.paradoxie.dizzypassword.domian.UpdataView;
import cf.paradoxie.dizzypassword.help.Date_U;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.help.ObjectUtils;
import cf.paradoxie.dizzypassword.util.SPUtils;
import cf.paradoxie.dizzypassword.util.StringUtils;
import cf.paradoxie.dizzypassword.widget.CustListView;

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
    UpdataSecret subjectsBean;

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
            subjectsBean = GsonUtil.getGsonInstance().fromJson(json, UpdataSecret.class);
            secrettitle.setText(subjectsBean.getTitle());
            url.setText(subjectsBean.getUrl());
          /*  if(location.equals("0")){
                adapter = new LSwipeAdapter(EditSecret.this, subjectsBean.getSecretLists(), key);
            }else{*/
                adapter = new LSwipeAdapter(EditSecret.this, subjectsBean.getSecrets(), key);
          //  }

            editListview.setAdapter(adapter);
        }
    }

    @OnClick({R.id.editok,R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.editok:
                String eorre = isEmptyEorre();
                if (StringUtils.isEmpty(secrettitle.getText().toString().trim())) {
                    Toast.makeText(EditSecret.this, "请输入标题", Toast.LENGTH_LONG).show();
                } else if (!StringUtils.isEmpty(eorre)) {
                    Toast.makeText(EditSecret.this, eorre, Toast.LENGTH_LONG).show();
                }else {
                    if(!StringUtils.isEmpty(SPUtils.getInstance().getString("username", ""))){
                        Subject subject = new Subject();
                        subject.setTitle(secrettitle.getText().toString().trim());
                        subject.setId(subjectsBean.getId());
                        subject.setUrl(url.getText().toString().trim());
                        subject.setUpdateTime(Date_U.getNowDate());
                        Log.e("backinfo","编辑："+GsonUtil.getGsonInstance().toJson(adapter.getData()));
                        List<Secret> secrets=new ArrayList<Secret>();
                        Type type = new TypeToken<ArrayList<Secret>>() {}.getType();
                        secrets=GsonUtil.getGsonInstance().fromJson(GsonUtil.getGsonInstance().toJson(adapter.getData()), type);
                        subject.setSecrets(secrets);
                        try {
                            Log.e("backinfo","key:"+key);
                            subject.entryptAllSecret(key);
                            Log.e("backinfo", "上传数据：" + GsonUtil.getGsonInstance().toJson(subject));
                            Sava(subject);
                        } catch (Exception e) {
                            Log.e("backinfo","加密出错");
                            e.printStackTrace();
                        }
                    }else{
                        cf.paradoxie.dizzypassword.dbdomain.Secret secret=new cf.paradoxie.dizzypassword.dbdomain.Secret();
                        secret.setUrl(url.getText().toString().trim());
                        secret.setId(subjectsBean.getId());
                        secret.setTitle(secrettitle.getText().toString().trim());
                        secret.setCloud(false);
                        secret.setUpdateTime(Date_U.getNowDate());
                        List<SecretList> secrets=new ArrayList<SecretList>();
                        Type type = new TypeToken<ArrayList<SecretList>>() {}.getType();
                        secrets=GsonUtil.getGsonInstance().fromJson(GsonUtil.getGsonInstance().toJson(adapter.getData()), type);
                        Log.e("backinfo","编辑："+GsonUtil.getGsonInstance().toJson(adapter.getData()));
                        secret.setSecrets(secrets);
                        Iterator var2 = secret.getSecrets().iterator();
                        while(var2.hasNext()) {
                            SecretList secretList = (SecretList)var2.next();
                            try {
                                secretList.setValue(EntryptionHelper.encrypt(key, secretList.getValue()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        SecretHelp.update(secret, secret.getSecrets());
                        UpdataView updataView = new UpdataView();
                        updataView.setView("HOME");
                        EventBus.getDefault().post(updataView);
                        finish();
                        Toast.makeText(EditSecret.this,"修改成功",Toast.LENGTH_LONG).show();

                    }

                }
                break;

        }
    }

    private String isEmptyEorre() {
        try {
            Log.e("backinfo", "数据：" + GsonUtil.getGsonInstance().toJson(adapter.getData()));
            for (int i = 0; i < adapter.getData().size(); i++) {
                if (StringUtils.isEmpty((String)ObjectUtils.getValueByKey(adapter.getData().get(i),"name"))) {
                    return "请输入或自定义第" + (i + 1) + "项目的密码类型";
                } else if (StringUtils.isEmpty((String)ObjectUtils.getValueByKey(adapter.getData().get(i),"value"))) {
                    return "请输入第" + (i + 1) + "项目的密码";
                }
            }
            return "";
        }catch (Exception e){
            return "获取数据出错";
        }

    }
    private void Sava(Subject subject) {
        dialog = DialogUIUtils.showLoading(EditSecret.this, "正在修改...", true, true, false, true).show();
        Log.e("backinfo","上传数据："+GsonUtil.getGsonInstance().toJson(subject));
        OkGo.<String>post(AllApi.save).upJson(GsonUtil.getGsonInstance().toJson(subject)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                ServerSecret serverSecret = GsonUtil.getGsonInstance().fromJson(response.body(), ServerSecret.class);
                if("false".equals(response.headers().get("logined"))){
                    Intent intent=new Intent(EditSecret.this,Login.class);
                    SPUtils.getInstance().remove("username");
                    EventBus.getDefault().post(false);
                    startActivity(intent);
                }else{
                    Toast.makeText(EditSecret.this, "添加成功", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    UpdataView updataView = new UpdataView();
                    updataView.setView("HOME");
                    EventBus.getDefault().post(updataView);
                    finish();
                }

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                super.onFinish();
            }

            @Override
            public void onError(Response<String> response) {
                cf.paradoxie.dizzypassword.dbdomain.Secret secret=new cf.paradoxie.dizzypassword.dbdomain.Secret();
                secret.setUrl(url.getText().toString().trim());
                secret.setId(subjectsBean.getId());
                secret.setTitle(secrettitle.getText().toString().trim());
                secret.setCloud(false);
                secret.setUpdateTime(Date_U.getNowDate());
                List<SecretList> secrets=new ArrayList<SecretList>();
                Type type = new TypeToken<ArrayList<SecretList>>() {}.getType();
                secrets=GsonUtil.getGsonInstance().fromJson(GsonUtil.getGsonInstance().toJson(adapter.getData()), type);
                Log.e("backinfo","编辑："+GsonUtil.getGsonInstance().toJson(adapter.getData()));
                secret.setSecrets(secrets);
                Iterator var2 = secret.getSecrets().iterator();
                while(var2.hasNext()) {
                    SecretList secretList = (SecretList)var2.next();
                    try {
                        secretList.setValue(EntryptionHelper.encrypt(key, secretList.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                SecretHelp.update(secret, secret.getSecrets());
                UpdataView updataView = new UpdataView();
                updataView.setView("HOME");
                EventBus.getDefault().post(updataView);
                finish();
                super.onError(response);
            }
        });
    }
}
