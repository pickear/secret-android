package cf.paradoxie.dizzypassword.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.activity.AddSubject;
import cf.paradoxie.dizzypassword.activity.LookPassWord;
import cf.paradoxie.dizzypassword.adapter.LSwipeAdapter;
import cf.paradoxie.dizzypassword.adapter.SwipeAdapter;
import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.db.help.dbutlis.SecretHelp;
import cf.paradoxie.dizzypassword.domian.SecretList;
import cf.paradoxie.dizzypassword.domian.UpdataView;
import cf.paradoxie.dizzypassword.help.Constant;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.password.PassValitationPopwindow;
import cf.paradoxie.dizzypassword.util.ACache;
import cf.paradoxie.dizzypassword.util.SPUtils;
import cf.paradoxie.dizzypassword.util.StringUtils;
import cf.paradoxie.dizzypassword.widget.SwipeListView;

public class Homefragment extends Fragment {

    View view;


    SwipeAdapter adapter;

    Button add;
    @Bind(R.id.swipelistview)
    SwipeListView swipelistview;
    private ACache aCache;
    private byte[] gesturePassword;
    Intent intent = null;
    View footerview;
    PassValitationPopwindow passValitationPopwindow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homefragment, null);
        ButterKnife.bind(this, view);
        footerview = inflater.inflate(R.layout.footer, null);

        add = (Button) footerview.findViewById(R.id.add);
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("backinfo", "加密后的秘钥：" + aCache.getAsString(Constant.PD));
                if (StringUtils.isEmpty(aCache.getAsString(Constant.PD))) {
                    intent = new Intent(getActivity(), LookPassWord.class);
                    startActivity(intent);
                } else {
                    passValitationPopwindow = new PassValitationPopwindow(getActivity(), 1, view, new PassValitationPopwindow.OnInputNumberCodeCallback() {

                        @Override
                        public void onSuccess() {
                            passValitationPopwindow.dismiss();
                        }
                    });
                }


            }
        });
        initdata();
        swipelistview.addFooterView(footerview);

        return view;
    }
    private void initdata(){
        if(StringUtils.isEmpty(SPUtils.getInstance().getString("username",""))){
            Log.e("backinfo","本地数据库数据："+GsonUtil.getGsonInstance().toJson(SecretHelp.queryall()));
            LSwipeAdapter adapter=new LSwipeAdapter(getActivity(), swipelistview.getRightViewWidth(), SecretHelp.queryall(), new LSwipeAdapter.IOnItemRightClickListener() {
                @Override
                public void onRightClick(View v, int position) {

                }
            });
            swipelistview.setAdapter(adapter);
        }else{
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
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true,priority = 1000)
    public void receiveMsg(UpdataView event){
        String tag=event.getView();
        if(tag!=null&&!TextUtils.isEmpty(tag)){
            Log.i("hemiy", "收到了tag的消息");
            initdata();
        }else{

        }
    }
}
