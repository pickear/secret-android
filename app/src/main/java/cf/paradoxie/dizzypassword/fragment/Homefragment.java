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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.activity.AddSubject;
import cf.paradoxie.dizzypassword.activity.Secretdetails;
import cf.paradoxie.dizzypassword.adapter.LSwipeAdapter;
import cf.paradoxie.dizzypassword.adapter.SwipeAdapter;
import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.db.help.dbutlis.SecretHelp;
import cf.paradoxie.dizzypassword.dbdomain.Secret;
import cf.paradoxie.dizzypassword.domian.SecretList;
import cf.paradoxie.dizzypassword.domian.UpdataView;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.password.PassValitationPopwindow;
import cf.paradoxie.dizzypassword.util.ACache;
import cf.paradoxie.dizzypassword.util.SPUtils;
import cf.paradoxie.dizzypassword.util.StringUtils;
import cf.paradoxie.dizzypassword.widget.SwipeListView;

public class Homefragment extends Fragment {

    View view;


    SwipeAdapter adapter;

    ImageButton add;
    @Bind(R.id.swipelistview)
    SwipeListView swipelistview;
    private ACache aCache;
    private byte[] gesturePassword;
    Intent intent = null;
    EditText custed;
    PassValitationPopwindow passValitationPopwindow;
    Button cancel,sure;

    View rootView;
    LSwipeAdapter ladapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homefragment, null);
        ButterKnife.bind(this, view);
        //   footerview = inflater.inflate(R.layout.footer, null);
        rootView = View.inflate(getActivity(), R.layout.key, null);
        add = (ImageButton) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSubject.class);
                startActivity(intent);
            }
        });
        EventBus.getDefault().register(this);
        aCache = ACache.get(getActivity());

        swipelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final MaterialDialog dialog =
                        new MaterialDialog.Builder(getActivity())
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
                        if(custed.getText().toString().trim().length()==8){
                            Intent intent=new Intent(getActivity(), Secretdetails.class);
                            intent.putExtra("key",custed.getText().toString().trim());
                            if(StringUtils.isEmpty(SPUtils.getInstance().getString("username",""))){
                                SecretList.SubjectsBean subjectsBean=new SecretList.SubjectsBean();
                                Secret secret =ladapter.getsecret(position);
                                subjectsBean.setTitle(secret.getTitle());
                                subjectsBean.setUrl(secret.getUrl());
                                List<SecretList.SubjectsBean.SecretsBean> secretsBeans=new ArrayList<SecretList.SubjectsBean.SecretsBean>();
                                for(int i=0;i<secret.getSecretLists().size();i++){
                                    SecretList.SubjectsBean.SecretsBean bean=new SecretList.SubjectsBean.SecretsBean();
                                    bean.setName(secret.getSecretLists().get(i).getName());
                                    bean.setValue(secret.getSecretLists().get(i).getValuse());
                                    secretsBeans.add(bean);
                                }
                                subjectsBean.setSecrets(secretsBeans);
                                intent.putExtra("json",GsonUtil.getGsonInstance().toJson(subjectsBean));
                            }else{
                                intent.putExtra("json",GsonUtil.getGsonInstance().toJson(adapter.getsecret(position)));
                            }

                            startActivity(intent);
                        }else{
                            Toast.makeText(getActivity(), "请输入完整的8位数秘钥", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });

             /*   if (StringUtils.isEmpty(aCache.getAsString(Constant.PD))) {
                    intent = new Intent(getActivity(), LookPassWord.class);
                    startActivity(intent);
                } else {
                    passValitationPopwindow = new PassValitationPopwindow(getActivity(), 1, view, new PassValitationPopwindow.OnInputNumberCodeCallback() {

                        @Override
                        public void onSuccess() {
                            passValitationPopwindow.dismiss();
                        }
                    });
                }*/


            }
        });
        initdata();


        return view;
    }

    private void initdata() {
        if (StringUtils.isEmpty(SPUtils.getInstance().getString("username", ""))) {
            Log.e("backinfo", "本地数据库数据：" + GsonUtil.getGsonInstance().toJson(SecretHelp.queryall()));
           ladapter = new LSwipeAdapter(getActivity(), swipelistview.getRightViewWidth(), SecretHelp.queryall(), new LSwipeAdapter.IOnItemRightClickListener() {
                @Override
                public void onRightClick(View v, int position) {

                }
            });
            swipelistview.setAdapter(ladapter);
        } else {
            init();
        }
    }

    private void init() {
        OkGo.<String>get(AllApi.query).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", response.body());
                Log.e("backinfo", response.toString());
                SecretList serverSecret = GsonUtil.getGsonInstance().fromJson(response.body(), SecretList.class);
                adapter = new SwipeAdapter(getActivity(), swipelistview.getRightViewWidth(), serverSecret.getSubjects(),
                        new SwipeAdapter.IOnItemRightClickListener() {
                            @Override
                            public void onRightClick(View v, int position) {
                                adapter.delete(position);
                                swipelistview.hideRight();
                            }
                        });

                swipelistview.setAdapter(adapter);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onError(Response<String> response) {

                super.onError(response);
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
            initdata();
        } else {

        }
    }
}
