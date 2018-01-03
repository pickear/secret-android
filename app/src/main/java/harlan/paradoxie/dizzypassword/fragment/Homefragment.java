package harlan.paradoxie.dizzypassword.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yd.commonlibrary.pagestate.YdPageStateManager;
import com.yd.commonlibrary.pagestate.listener.OnErrorRetryListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import harlan.paradoxie.dizzypassword.R;
import harlan.paradoxie.dizzypassword.activity.AddSubject;
import harlan.paradoxie.dizzypassword.activity.BaseFragment;
import harlan.paradoxie.dizzypassword.activity.EditSecret;
import harlan.paradoxie.dizzypassword.activity.Login;
import harlan.paradoxie.dizzypassword.activity.Secretdetails;
import harlan.paradoxie.dizzypassword.adapter.LSwipeAdapter;
import harlan.paradoxie.dizzypassword.adapter.SwipeAdapter;
import harlan.paradoxie.dizzypassword.api.AllApi;
import harlan.paradoxie.dizzypassword.db.help.dbutlis.SecretHelp;
import harlan.paradoxie.dizzypassword.db.help.dbutlis.SecretListHelp;
import harlan.paradoxie.dizzypassword.dbdomain.Secret;
import harlan.paradoxie.dizzypassword.domian.LoginBean;
import harlan.paradoxie.dizzypassword.domian.SecretList;
import harlan.paradoxie.dizzypassword.domian.UpdataView;
import harlan.paradoxie.dizzypassword.help.GsonUtil;
import harlan.paradoxie.dizzypassword.password.PassValitationPopwindow;
import harlan.paradoxie.dizzypassword.util.ACache;
import harlan.paradoxie.dizzypassword.util.SPUtils;
import harlan.paradoxie.dizzypassword.util.StringUtils;

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
    boolean isdelete = false;

    YdPageStateManager ydPageStateManager;
    MaterialDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homefragment, null);
        ydPageStateManager = new YdPageStateManager(view, R.id.listView);
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
        //SecretHelp.deleteall();
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
                                subjectsBean.setAccount(secret.getAccount());
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
                        custed.setText("");
                        sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (custed.getText().toString().trim().length() == 8) {
                                    Intent intent = new Intent(getActivity(), EditSecret.class);
                                    if (StringUtils.isEmpty(SPUtils.getInstance().getString("username", ""))) {
                                        intent.putExtra("json", GsonUtil.getGsonInstance().toJson(adapter.getsecret(position)));
                                        intent.putExtra("location", "0");
                                        intent.putExtra("key", custed.getText().toString().trim());
                                    } else {
                                        intent.putExtra("json", GsonUtil.getGsonInstance().toJson(adapter.getsecret(position)));
                                        intent.putExtra("location", "1");
                                        intent.putExtra("key", custed.getText().toString().trim());
                                    }
                                    startActivity(intent);
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "请输入完整的8位数秘钥", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                        break;
                    case 1:
                        isdelete = true;

                        if (StringUtils.isEmpty(SPUtils.getInstance().getString("username", ""))) {
                            Secret secret = (Secret) adapter.getsecret(position);
                            SecretHelp.delete(secret.getSid());
                            adapter.delete(position);
                            if (adapter.getCount() == 0) {
                                ydPageStateManager.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata),
                                        getString(R.string.ydPageState_empty_title), "本地数据库没有数据，请添加");
                            } else {
                                ydPageStateManager.showContent();
                            }
                        } else {

                            Log.e("backinfo", GsonUtil.getGsonInstance().toJson(adapter.getsecret(position)));
                            SecretList.SubjectsBean secret = null;
                            try {
                                secret = GsonUtil.fromjson(GsonUtil.getGsonInstance().toJson(adapter.getsecret(position)), SecretList.SubjectsBean.class);
                                //  id = secret.getId();
                                Delete(secret, position);
                            } catch (Exception e) {
                                Log.e("backinfo", "解析出错" + e.getLocalizedMessage());
                                e.printStackTrace();
                            }

                        }
                        getdb();
                        break;
                }
                return false;
            }
        });

        return view;
    }
    private void getdb(){
        Log.e("backinfo","secret:"+GsonUtil.getGsonInstance().toJson(SecretHelp.querySecretAll()));
        Log.e("backinfo","secretlist:"+GsonUtil.getGsonInstance().toJson(SecretListHelp.querySecretAll()));
    }
    private void initdata() {
        if (StringUtils.isEmpty(SPUtils.getInstance().getString("username", ""))) {
            List<Secret> secrets = SecretHelp.queryall();
            Log.e("backinfo", "本地数据库数据：" + GsonUtil.getGsonInstance().toJson(SecretHelp.queryall()));
            if (secrets.size() <= 0) {
                ydPageStateManager.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata),
                        getString(R.string.ydPageState_empty_title), "本地数据库没有数据，请添加");
            } else {
                ydPageStateManager.showContent();
                adapter = new SwipeAdapter(getActivity(), secrets);
                listView.setAdapter(adapter);
                listView.setMenuCreator(creator);
            }
        } else {
            cound();
        }
    }


    private void init() {
        List<Secret> secrets = SecretHelp.querycloud();
        adapter = new SwipeAdapter(getActivity(), secrets);
        listView.setAdapter(adapter);
        listView.setMenuCreator(creator);

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
        if (tag.equals("HOME")) {
            Log.i("hemiy", "收到了tag的消息");
            initdata();
        } else if (tag.equals("db")) {
           List<Secret> secrets=SecretHelp.queryall();
            getdb();
            Log.e("backinfo","数据库所有数据："+GsonUtil.getGsonInstance().toJson(secrets));
            if (secrets.size() <= 0) {
                Log.e("backinfo", "进去空");
                ydPageStateManager.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata),
                        getString(R.string.ydPageState_empty_title), "本地数据库没有数据，请添加");
            } else {
                Log.e("backinfo", "进去不为空");
                ydPageStateManager.showContent();
                adapter = new SwipeAdapter(getActivity(),secrets);
                listView.setAdapter(adapter);
                listView.setMenuCreator(creator);
            }
        }
    }

    @Override
    public void finishlogin() {
        Log.e("backinfo", "登录完成");
      /*  if (isdelete == true) {
            Delete(id);
        } else {
            init();
        }*/

    }

    @Override
    public void onerror() {

    }

    private void cound() {
        List<Secret> secrets = SecretHelp.querycloud();
        List<com.weasel.secret.common.domain.Subject> subjects = new ArrayList<com.weasel.secret.common.domain.Subject>();
        for (int i = 0; i < secrets.size(); i++) {
            com.weasel.secret.common.domain.Subject subject = new com.weasel.secret.common.domain.Subject();
            subject.setTitle(secrets.get(i).getTitle());
            subject.setUrl(secrets.get(i).getUrl());
            if(secrets.get(i).getId()!=null){
                subject.setId(secrets.get(i).getId());
            }
            subject.setDeleted(secrets.get(i).getDeleted());
            subject.setUpdateTime(secrets.get(i).getUpdateTime());
            subject.setAccount(secrets.get(i).getAccount());
            List<com.weasel.secret.common.domain.Secret> secretList = new ArrayList<com.weasel.secret.common.domain.Secret>();
            for (int j = 0; j < secrets.get(i).getSecrets().size(); j++) {
                com.weasel.secret.common.domain.Secret secret = new com.weasel.secret.common.domain.Secret();
                secret.setName(secrets.get(i).getSecrets().get(j).getName());
                secret.setValue(secrets.get(i).getSecrets().get(j).getValue());
               /* if(secrets.get(i).getSecrets().get(j).getSubjectId()!=null){
                    secret.setSubjectId(secrets.get(i).getSecrets().get(j).getSubjectId());
                }*/
                if(secrets.get(i).getSecrets().get(j).getId()!=null){
                    secret.setId(secrets.get(i).getSecrets().get(j).getId());
                }
                secretList.add(secret);
            }
            subject.setSecrets(secretList);
            subjects.add(subject);
        }
        Log.e("backinfo", "需要同步的数据：" + GsonUtil.getGsonInstance().toJson(subjects));
        if (SPUtils.getInstance().getBoolean("cloud", false) == true) {
            ShowProgress("加载中...");
            OkGo.<String>post(AllApi.savelist).upJson(GsonUtil.getGsonInstance().toJson(subjects)).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.e("backinfo", "同步返回的数据：" + response.body());
                    if ("false".equals(response.headers().get("logined"))) {
                        Intent intent = new Intent(getActivity(), Login.class);
                        SPUtils.getInstance().remove("username");
                        EventBus.getDefault().post(false);
                        startActivity(intent);
                    } else {
                        try {
                            List<harlan.paradoxie.dizzypassword.domian.Subject> subjects1 = new ArrayList<harlan.paradoxie.dizzypassword.domian.Subject>();
                            Type type = new TypeToken<ArrayList<harlan.paradoxie.dizzypassword.domian.Subject>>() {
                            }.getType();
                            subjects1 = GsonUtil.getGsonInstance().fromJson(response.body(), type);

                            // init();
                            if (subjects1.size() <= 0) {
                                ydPageStateManager.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata),
                                        getString(R.string.ydPageState_empty_title), "云端没有数据，请添加");
                            } else {
                                ydPageStateManager.showContent();
                                SecretHelp.deleteall();
                                List<Secret> secretList = new ArrayList<Secret>();
                                for (int i = 0; i < subjects1.size(); i++) {
                                    Secret secret = new Secret();
                                    secret.setId(subjects1.get(i).getId());
                                    secret.setCloud(true);
                                    secret.setCreateTime(subjects1.get(i).getCreateTime());
                                    secret.setUrl(subjects1.get(i).getUrl());
                                    secret.setTitle(subjects1.get(i).getTitle());
                                    secret.setAccount(subjects1.get(i).getAccount());
                                    secret.setUpdateTime(subjects1.get(i).getUpdateTime());
                                    SecretHelp.insert(secret);
                                    Long lasdid=SecretHelp.getlastid();
                                    List<harlan.paradoxie.dizzypassword.dbdomain.SecretList> secretLists = new ArrayList<harlan.paradoxie.dizzypassword.dbdomain.SecretList>();
                                    for (int j = 0; j < subjects1.get(i).getSecrets().size(); j++) {
                                        harlan.paradoxie.dizzypassword.dbdomain.SecretList list = new harlan.paradoxie.dizzypassword.dbdomain.SecretList();
                                        list.setSubjectId(subjects1.get(i).getSecrets().get(j).getSubjectId());
                                        list.setId(subjects1.get(i).getSecrets().get(j).getId());
                                        list.setName(subjects1.get(i).getSecrets().get(j).getName());
                                        list.setValue(subjects1.get(i).getSecrets().get(j).getValue());
                                        list.setSecretId(lasdid);
                                        SecretListHelp.insert(list);
                                    }
                                }
                                adapter = new SwipeAdapter(getActivity(), SecretHelp.queryall());
                                listView.setAdapter(adapter);
                                listView.setMenuCreator(creator);
                                Log.e("backinfo", "本地数据库数据：" + GsonUtil.getGsonInstance().toJson(SecretHelp.queryall()));
                            }
                            Toast.makeText(getActivity(), "加载成功", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "解析出错", Toast.LENGTH_SHORT).show();
                            ydPageStateManager.showError(getResources().getDrawable(R.mipmap.nointent),
                                    "解析", "解析出错",
                                    getString(R.string.ydPageState_retry), new OnErrorRetryListener() {
                                        @Override
                                        public void onErrorRetry(View view) {
                                            cound();
                                        }
                                    });

                            e.printStackTrace();
                        }


                    }

                    HideProgress();
                }

                @Override
                public void onFinish() {
                    HideProgress();
                    super.onFinish();
                }

                @Override
                public void onError(Response<String> response) {
                    Log.e("backinfo", "出错" + response.getException());
                    List<Secret> secrets = SecretHelp.queryall();
                    Log.e("backinfo", "出错" + response.code());
                    if (secrets.size() <= 0) {
                        ydPageStateManager.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata),
                                getString(R.string.ydPageState_empty_title), "云端没有数据，请添加");
                    } else {
                        ydPageStateManager.showContent();
                    }
                    adapter = new SwipeAdapter(getActivity(), secrets);
                    listView.setAdapter(adapter);
                    listView.setMenuCreator(creator);

                    super.onError(response);
                }
            });
        } else {
            Log.e("backinfo", "没有数据要同步");
            init();
        }

    }

    private void Delete(final SecretList.SubjectsBean secret, final int mposition) {
        Log.e("backinfo", "mid:" + secret.getId());
        ShowProgress("删除中...");
        OkGo.<String>delete(AllApi.delete + "?id=" + secret.getId()).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", "删除：" + response.body());
                LoginBean loginBean = GsonUtil.getGsonInstance().fromJson(response.body(), LoginBean.class);
                if ("false".equals(response.headers().get("logined"))) {
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                } else if (loginBean.getCode().equals("0000")) {
                    if (mposition != -1) {
                        adapter.delete(mposition);
                        SecretHelp.delete(secret.getSid());
                        if (adapter.getCount() == 0) {
                            ydPageStateManager.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata),
                                    getString(R.string.ydPageState_empty_title), "该账户下没有数据，请添加");
                        } else {
                            ydPageStateManager.showContent();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), loginBean.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFinish() {
                HideProgress();
                super.onFinish();
            }

            @Override
            public void onError(Response<String> response) {
                Secret secret1 = new Secret();
                secret1.setTitle(secret.getTitle());
                secret1.setSid(secret.getSid());
                if(secret.getId()!=null){
                    secret1.setId(secret.getId());
                }
                secret1.setCloud(false);
                secret1.setDeleted(true);
                SecretHelp.update(secret1);
                adapter.delete(mposition);
                if (adapter.getCount() == 0) {
                    ydPageStateManager.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata),
                            getString(R.string.ydPageState_empty_title), "该账户下没有数据，请添加");
                } else {
                    ydPageStateManager.showContent();
                }
                Log.e("backinfo", "删除失败");
                super.onError(response);
            }

        });
    }

}
