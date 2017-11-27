package cf.paradoxie.dizzypassword.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.activity.AddSubject;
import cf.paradoxie.dizzypassword.activity.LookPassWord;
import cf.paradoxie.dizzypassword.adapter.SwipeAdapter;
import cf.paradoxie.dizzypassword.api.AllApi;
import cf.paradoxie.dizzypassword.domian.SecretList;
import cf.paradoxie.dizzypassword.help.Constant;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.password.PassValitationPopwindow;
import cf.paradoxie.dizzypassword.util.ACache;
import cf.paradoxie.dizzypassword.util.StringUtils;
import cf.paradoxie.dizzypassword.widget.SwipeListView;

public class Homefragment extends Fragment {

    View view;

    Unbinder unbinder;
    @BindView(R.id.swipelistview)
    SwipeListView swipelistview;

    SwipeAdapter adapter;
    private ACache aCache;
    private byte[] gesturePassword;
    Intent intent=null;
    View footerview;
    PassValitationPopwindow passValitationPopwindow;
    Button add;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homefragment, null);
        footerview=inflater.inflate(R.layout.footer, null);
        unbinder = ButterKnife.bind(this, view);
        add= (Button) footerview.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSubject.class);
                startActivity(intent);
            }
        });
        aCache = ACache.get(getActivity());
       init();

        swipelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* gesturePassword = aCache.getAsBinary(Constant.GESTURE_PASSWORD);
                if (gesturePassword != null && gesturePassword.length > 0) {
                    intent = new Intent(getActivity(), GesturePasswordActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), CreateGestureActivity.class);
                    startActivity(intent);
                }
*/
                Log.e("backinfo","加密后的秘钥："+aCache.getAsString(Constant.PD));
                if(StringUtils.isEmpty(aCache.getAsString(Constant.PD))){
                    intent = new Intent(getActivity(), LookPassWord.class);
                    startActivity(intent);
                }else{
                    passValitationPopwindow=  new PassValitationPopwindow(getActivity(),1,view,new PassValitationPopwindow.OnInputNumberCodeCallback() {

                        @Override
                        public void onSuccess() {
                            passValitationPopwindow.dismiss();
                        }
                    });
                }


            }
        });

        swipelistview.addFooterView(footerview);
        return view;
    }

    private void init() {
        OkGo.<String>get(AllApi.query).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("backinfo", response.body());

                Log.e("backinfo", response.toString());
                SecretList serverSecret = GsonUtil.getGsonInstance().fromJson(response.body(), SecretList.class);
                adapter = new SwipeAdapter(getActivity(), swipelistview.getRightViewWidth(),serverSecret.getSubjects(),
                        new SwipeAdapter.IOnItemRightClickListener() {
                            @Override
                            public void onRightClick(View v, int position) {
                                adapter.delete(position);
                                swipelistview.hideRight();
                            }
                        });

                swipelistview.setAdapter(adapter);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
