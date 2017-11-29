package cf.paradoxie.dizzypassword.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.widget.CircleImageView;

public class Myfragment extends Fragment {

    View view;
    @Bind(R.id.personalimg)
    CircleImageView personalimg;
    @Bind(R.id.login)
    TextView login;
    @Bind(R.id.personalID)
    TextView personalID;
    @Bind(R.id.personalname)
    TextView personalname;
    @Bind(R.id.userinfo)
    LinearLayout userinfo;
    @Bind(R.id.Healthonsultantcly)
    LinearLayout Healthonsultantcly;
    @Bind(R.id.allsetting)
    LinearLayout allsetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myfragment, null);

        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    @OnClick({R.id.personalimg, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.personalimg:
                break;
            case R.id.login:
                break;
        }
    }
}
