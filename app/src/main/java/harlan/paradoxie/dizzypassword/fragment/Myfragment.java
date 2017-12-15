package harlan.paradoxie.dizzypassword.fragment;

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
import harlan.paradoxie.dizzypassword.R;
import harlan.paradoxie.dizzypassword.activity.BaseFragment;
import harlan.paradoxie.dizzypassword.activity.Login;
import harlan.paradoxie.dizzypassword.domian.UpdataView;
import harlan.paradoxie.dizzypassword.service.HeartbeatService;
import harlan.paradoxie.dizzypassword.util.SPUtils;
import harlan.paradoxie.dizzypassword.util.StringUtils;
import harlan.paradoxie.dizzypassword.widget.CircleImageView;
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
        vLauncherVoice.setOpened(SPUtils.getInstance().getBoolean("cloud", true));
        vLauncherVoice.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                SPUtils.getInstance().put("cloud", true);
                UpdataView updataView=new UpdataView();
                updataView.setView("HOME");
                EventBus.getDefault().post(updataView);
                vLauncherVoice.setOpened(true);


            }

            @Override
            public void toggleToOff(SwitchView view) {
                vLauncherVoice.setOpened(false);
                SPUtils.getInstance().put("cloud", false);
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
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 1000)
    public void exitLogin(boolean event) {
            islogin();

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


}
