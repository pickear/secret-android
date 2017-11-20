package cf.paradoxie.dizzypassword.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.activity.CreateGestureActivity;
import cf.paradoxie.dizzypassword.activity.GesturePasswordActivity;
import cf.paradoxie.dizzypassword.adapter.SwipeAdapter;
import cf.paradoxie.dizzypassword.help.Constant;
import cf.paradoxie.dizzypassword.util.ACache;
import cf.paradoxie.dizzypassword.widget.SwipeListView;

public class Homefragment extends Fragment {

    View view;

    Unbinder unbinder;
    @BindView(R.id.swipelistview)
    SwipeListView swipelistview;
    List<String> data=new ArrayList<>();
    SwipeAdapter adapter;
    private ACache aCache;
    private byte[] gesturePassword;
    Intent intent=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homefragment, null);
        unbinder = ButterKnife.bind(this, view);
        aCache = ACache.get(getActivity());
        for(int i=0;i<20;i++){
            data.add("item"+i);
        }
       // aCache.clear();
       adapter = new SwipeAdapter(getActivity(), swipelistview.getRightViewWidth(),data,
                new SwipeAdapter.IOnItemRightClickListener() {
                    @Override
                    public void onRightClick(View v, int position) {


                       /* Toast.makeText(getActivity(), "right onclick " + position,
                                Toast.LENGTH_SHORT).show();*/
                        adapter.delete(position);
                        swipelistview.hideRight();
                    }
                });

        swipelistview.setAdapter(adapter);
        swipelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gesturePassword = aCache.getAsBinary(Constant.GESTURE_PASSWORD);
                if(gesturePassword!=null&&gesturePassword.length>0){
                    intent=new Intent(getActivity(), GesturePasswordActivity.class);
                    startActivity(intent);
                }else{
                    intent=new Intent(getActivity(), CreateGestureActivity.class);
                    startActivity(intent);
                }

            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
