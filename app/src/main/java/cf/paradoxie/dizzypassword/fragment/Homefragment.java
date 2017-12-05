package cf.paradoxie.dizzypassword.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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
import cf.paradoxie.dizzypassword.activity.BaseFragment;
import cf.paradoxie.dizzypassword.activity.EditSecret;
import cf.paradoxie.dizzypassword.activity.Login;
import cf.paradoxie.dizzypassword.activity.Secretdetails;
import cf.paradoxie.dizzypassword.adapter.LSwipeAdapter;
import cf.paradoxie.dizzypassword.adapter.SwipeAdapter;
import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.db.help.dbutlis.SecretHelp;
import cf.paradoxie.dizzypassword.dbdomain.Secret;
import cf.paradoxie.dizzypassword.domian.LoginBean;
import cf.paradoxie.dizzypassword.domian.SecretList;
import cf.paradoxie.dizzypassword.domian.UpdataView;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.password.PassValitationPopwindow;
import cf.paradoxie.dizzypassword.util.ACache;
import cf.paradoxie.dizzypassword.util.SPUtils;
import cf.paradoxie.dizzypassword.util.StringUtils;

public class Homefragment extends BaseFragment {

    View view;


    SwipeAdapter adapter;

    ImageButton add;

    @Bind(R.id.listView)
    SwipeMenuListView listView;
    private ACache aCache;
    private byte[] gesturePassword;
    Intent intent = null;
    EditText custed;
    PassValitationPopwindow passValitationPopwindow;
    Button cancel, sure;
    boolean updata = true;
    View rootView;
    LSwipeAdapter ladapter;
    Long id;
    boolean isdelete=false;
    int mposition=-1;
    MaterialDialog dialog;
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                dialog = new MaterialDialog.Builder(getActivity())
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
                        if (custed.getText().toString().trim().length() == 8) {
                            Intent intent = new Intent(getActivity(), Secretdetails.class);
                            intent.putExtra("key", custed.getText().toString().trim());
                            if (StringUtils.isEmpty(SPUtils.getInstance().getString("username", ""))) {
                                SecretList.SubjectsBean subjectsBean = new SecretList.SubjectsBean();
                                Secret secret = (Secret) adapter.getsecret(position);
                                subjectsBean.setTitle(secret.getTitle());
                                subjectsBean.setUrl(secret.getUrl());
                                List<SecretList.SubjectsBean.SecretsBean> secretsBeans = new ArrayList<SecretList.SubjectsBean.SecretsBean>();
                                for (int i = 0; i < secret.getSecrets().size(); i++) {
                                    SecretList.SubjectsBean.SecretsBean bean = new SecretList.SubjectsBean.SecretsBean();
                                    bean.setName(secret.getSecrets().get(i).getName());
                                    bean.setValue(secret.getSecrets().get(i).getValue());
                                    secretsBeans.add(bean);
                                }
                                subjectsBean.setSecrets(secretsBeans);
                                intent.putExtra("json", GsonUtil.getGsonInstance().toJson(subjectsBean));
                            } else {
                                intent.putExtra("json", GsonUtil.getGsonInstance().toJson(adapter.getsecret(position)));
                            }
                            dialog.dismiss();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "请输入完整的8位数秘钥", Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }
        });
        initdata();
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        dialog = new MaterialDialog.Builder(getActivity())
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
                                    Intent intent = new Intent(getActivity(), EditSecret.class);
                                    if (StringUtils.isEmpty(SPUtils.getInstance().getString("username", ""))) {
                                        intent.putExtra("json", GsonUtil.getGsonInstance().toJson(adapter.getsecret(position)));
                                        intent.putExtra("location", "0");
                                        intent.putExtra("key",custed.getText().toString().trim());
                                    } else {
                                        intent.putExtra("json", GsonUtil.getGsonInstance().toJson(adapter.getsecret(position)));
                                        intent.putExtra("location", "1");
                                        intent.putExtra("key", custed.getText().toString().trim());
                                    }
                                    startActivity(intent);
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(getActivity(), "请输入完整的8位数秘钥", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                        break;
                    case 1:
                        isdelete=true;
                        mposition=position;
                        if(StringUtils.isEmpty(SPUtils.getInstance().getString("username", ""))){
                            Secret secret= (Secret) adapter.getsecret(position);
                            SecretHelp.delete(secret.getId());
                            adapter.delete(position);
                        }else{
                            ShowProgress("删除中...");
                            Log.e("backinfo", GsonUtil.getGsonInstance().toJson(adapter.getsecret(position)));
                            SecretList.SubjectsBean secret= (SecretList.SubjectsBean) adapter.getsecret(position);
                            id=secret.getId();
                            Delete(id);
                        }

                        break;
                }
                return false;
            }
        });

        return view;
    }

    private void initdata() {
        if (StringUtils.isEmpty(SPUtils.getInstance().getString("username", ""))) {
            Log.e("backinfo", "本地数据库数据：" + GsonUtil.getGsonInstance().toJson(SecretHelp.queryall()));
            adapter = new SwipeAdapter(getActivity(), SecretHelp.queryall());
            listView.setAdapter(adapter);
            listView.setMenuCreator(creator);

        } else {
            ShowProgress("加载中...");
            init();
           // isLogin();

        }
    }


    private void init() {
        OkGo.<String>get(AllApi.query).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", response.body());
                Log.e("backinfo", response.toString());
                SecretList serverSecret = GsonUtil.getGsonInstance().fromJson(response.body(), SecretList.class);
                if(response.code()==200){

                    adapter = new SwipeAdapter(getActivity(), serverSecret.getSubjects());
                    listView.setAdapter(adapter);
                    listView.setMenuCreator(creator);
                }else if(response.code() == 302){
                    Intent intent=new Intent(getActivity(),Login.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "获取数据出错", Toast.LENGTH_LONG).show();
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

    // step 1. create a MenuCreator
    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getActivity());
            // set item background
            openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                    0xCE)));
            // set item width
            openItem.setWidth(dp2px(90));
            // set item title
            openItem.setTitle("编辑");
            // set item title fontsize
            openItem.setTitleSize(16);
            // set item title font color
            openItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getActivity());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(dp2px(90));
            // set a icon
            deleteItem.setIcon(R.drawable.ic_action_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
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

    @Override
    public void finishlogin() {
        Log.e("backinfo", "登录完成");
        if(isdelete==true){
            Delete(id);
        }else{
            init();
        }

    }

    @Override
    public void onerror() {

    }

    private void Delete(Long mid){
        Log.e("backinfo","mid:"+mid);

        OkGo.<String>delete(AllApi.delete + "?id=" + mid).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", "删除：" + response.body());
                LoginBean loginBean = GsonUtil.getGsonInstance().fromJson(response.body(), LoginBean.class);
                if (loginBean.getCode().equals("0000")) {
                    if (mposition != -1) {
                        adapter.delete(mposition);
                    }
                }else if(response.code() == 302){
                    Intent intent=new Intent(getActivity(),Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), loginBean.getMessage(), Toast.LENGTH_LONG).show();
                }

                mposition = -1;
            }

            @Override
            public void onFinish() {
                HideProgress();
                super.onFinish();
            }

            @Override
            public void onError(Response<String> response) {
                mposition = -1;
                Log.e("backinfo", "删除失败");
                super.onError(response);
            }

        });
    }

}
