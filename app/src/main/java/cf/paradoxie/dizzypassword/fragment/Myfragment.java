package cf.paradoxie.dizzypassword.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.activity.BaseFragment;
import cf.paradoxie.dizzypassword.activity.Login;
import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.db.help.dbutlis.SecretHelp;
import cf.paradoxie.dizzypassword.dbdomain.Secret;
import cf.paradoxie.dizzypassword.domian.UpdataView;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.service.HeartbeatService;
import cf.paradoxie.dizzypassword.util.SPUtils;
import cf.paradoxie.dizzypassword.util.StringUtils;
import cf.paradoxie.dizzypassword.widget.CircleImageView;
import ch.ielse.view.SwitchView;
import io.reactivex.annotations.NonNull;

public class Myfragment extends BaseFragment {

    View view;
    @Bind(R.id.personalimg)
    CircleImageView personalimg;
    @Bind(R.id.login)
    TextView login;

    @Bind(R.id.personalname)
    TextView personalname;
    @Bind(R.id.userinfo)
    LinearLayout userinfo;
    @Bind(R.id.Healthonsultantcly)
    LinearLayout Healthonsultantcly;
    @Bind(R.id.allsetting)
    LinearLayout allsetting;
    @Bind(R.id.exitlogin)
    Button exitlogin;
    @Bind(R.id.v_launcher_voice)
    SwitchView vLauncherVoice;
    @Bind(R.id.islogin)
    LinearLayout islogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myfragment, null);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, view);
        islogin();
        vLauncherVoice.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {

                List<Secret> secrets = SecretHelp.querycloud();
                List<com.weasel.secret.common.domain.Subject> subjects = new ArrayList<com.weasel.secret.common.domain.Subject>();
                for (int i = 0; i < secrets.size(); i++) {
                    com.weasel.secret.common.domain.Subject subject = new com.weasel.secret.common.domain.Subject();
                    subject.setTitle(secrets.get(i).getTitle());
                    subject.setUrl(secrets.get(i).getUrl());
                    List<com.weasel.secret.common.domain.Secret> secretList = new ArrayList<com.weasel.secret.common.domain.Secret>();
                    for (int j = 0; j < secrets.get(i).getSecrets().size(); j++) {
                        com.weasel.secret.common.domain.Secret secret = new com.weasel.secret.common.domain.Secret();
                        secret.setName(secrets.get(i).getSecrets().get(j).getName());
                        secret.setValue(secrets.get(i).getSecrets().get(j).getValue());
                        secretList.add(secret);
                    }
                    subject.setSecrets(secretList);
                    subjects.add(subject);
                }
                if(subjects.size()>0){
                    cound(subjects);
                }else{
                    Toast.makeText(getActivity(),"本地没有数据需要同步",Toast.LENGTH_LONG).show();
                }

                Log.e("backinfo", "需要传给云同步" + GsonUtil.getGsonInstance().toJson(subjects));

            }

            @Override
            public void toggleToOff(SwitchView view) {
                vLauncherVoice.setOpened(false);
                //  Log.e("backinfo","toggleToOff");
            }
        });
        return view;
    }

    private void islogin() {
        if (StringUtils.isEmpty(SPUtils.getInstance().getString("username", ""))) {
            exitlogin.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            userinfo.setVisibility(View.GONE);
        } else {
            finishlogin();
            // isLogin();
            //  Logined();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 1000)
    public void receiveMsg(UpdataView event) {
        String tag = event.getView();
        if (tag != null && !TextUtils.isEmpty(tag)) {
            Log.i("hemiy", "收到了tag的消息");
            islogin();
        } else {

        }
    }

    @OnClick({R.id.exitlogin, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exitlogin:
                new MaterialDialog.Builder(getActivity())
                        .title("退出")
                        .content("你确定要退出账号吗？")
                        .titleColor(Color.parseColor("#000000"))
                        .contentColor(Color.parseColor("#333333"))
                        .backgroundColor(Color.parseColor("#ffffff"))
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                SPUtils.getInstance().remove("username");
                                SPUtils.getInstance().remove("password");
                                UpdataView updataView = new UpdataView();
                                updataView.setView("HOME");
                                Intent serviceIntent = new Intent(getActivity(), HeartbeatService.class);
                                getActivity().stopService(serviceIntent);
                                EventBus.getDefault().post(updataView);
                                exitlogin.setVisibility(View.GONE);
                                islogin.setVisibility(View.GONE);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.login:
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void finishlogin() {
        exitlogin.setVisibility(View.VISIBLE);
        islogin.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
        userinfo.setVisibility(View.VISIBLE);
        personalname.setText(SPUtils.getInstance().getString("username"));

    }

    @Override
    public void onerror() {

    }

    private void cound(List<com.weasel.secret.common.domain.Subject> subjects) {
        ShowProgress("上传中...");
        OkGo.<String>post(AllApi.savelist).upJson(GsonUtil.getGsonInstance().toJson(subjects)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", "上传云服务" + response.body());
                if(response.code()==200){
                    SecretHelp.updatacloud();
                    UpdataView updataView = new UpdataView();
                    updataView.setView("HOME");
                    EventBus.getDefault().post(updataView);
                    Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                    vLauncherVoice.setOpened(true);
                }else if(response.code()==302){
                    Intent intent=new Intent(getActivity(),Login.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "同步失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFinish() {
                HideProgress();
                super.onFinish();
            }

            @Override
            public void onError(Response<String> response) {

                super.onError(response);
            }
        });
    }
}
