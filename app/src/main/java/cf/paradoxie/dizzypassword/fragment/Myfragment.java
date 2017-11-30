package cf.paradoxie.dizzypassword.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.weasel.secret.common.domain.User;
import com.weasel.secret.common.helper.EntryptionHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.activity.Login;
import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.domian.LoginBean;
import cf.paradoxie.dizzypassword.domian.UpdataView;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.util.SPUtils;
import cf.paradoxie.dizzypassword.util.StringUtils;
import cf.paradoxie.dizzypassword.widget.CircleImageView;
import io.reactivex.annotations.NonNull;

public class Myfragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myfragment, null);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, view);
        islogin();

        return view;
    }
    private void islogin(){
        if(StringUtils.isEmpty(SPUtils.getInstance().getString("username",""))){
            exitlogin.setVisibility(View.GONE);
        }else{
            Logined();
        }
    }
    private void Logined() {
        OkGo.<String>get(AllApi.logined).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LoginBean loginBean = GsonUtil.getGsonInstance().fromJson(response.body(), LoginBean.class);
                if (loginBean.getCode().equals("0000")) {
                    exitlogin.setVisibility(View.VISIBLE);
                }else{
                    login();
                }
            }
        });
    }
    private void login() {
        User user=new User();
        user.setUsername(SPUtils.getInstance().getString("username"));
        try {
            user.setPassword(EntryptionHelper.decrypt("password", SPUtils.getInstance().getString("password")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkGo.<String>post(AllApi.login).tag(this).upJson(GsonUtil.getGsonInstance().toJson(user)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

                LoginBean loginBean = GsonUtil.getGsonInstance().fromJson(response.body(), LoginBean.class);
                if (loginBean.getCode().equals("0000")) {
                    exitlogin.setVisibility(View.VISIBLE);
                    login.setVisibility(View.GONE);
                    userinfo.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });


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
            Logined();
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
                        .backgroundColor(Color.parseColor("#ffffff"))
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            SPUtils.getInstance().remove("username");
                            SPUtils.getInstance().remove("password");
                            UpdataView updataView=new UpdataView();
                            updataView.setView("HOME");
                            EventBus.getDefault().post(updataView);
                            exitlogin.setVisibility(View.GONE);
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
                Intent intent=new Intent(getActivity(), Login.class);
                startActivity(intent);
                break;
        }
    }
}
