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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.activity.BaseFragment;
import cf.paradoxie.dizzypassword.activity.Login;
import cf.paradoxie.dizzypassword.domian.UpdataView;
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

            }

            @Override
            public void toggleToOff(SwitchView view) {

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
        vLauncherVoice.setOpened(SPUtils.getInstance().getBoolean("cloud", true));
    }

    @Override
    public void onerror() {

    }
}
