package cf.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weasel.secret.common.domain.Secret;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.adapter.AddTypeAdapter;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.widget.CustListView;

public class AddSubject extends Activity {
    AddTypeAdapter adapter;
    List<Secret> secrets = new ArrayList<>();
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.secrettitle)
    EditText secrettitle;
    @BindView(R.id.listviewinfo)
    CustListView listviewinfo;
    @BindView(R.id.url)
    EditText url;
    @BindView(R.id.ok)
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        ButterKnife.bind(this);
        Secret secret = new Secret();
        secrets.add(secret);
        adapter = new AddTypeAdapter(AddSubject.this, secrets);
        listviewinfo.setAdapter(adapter);
    }

    @OnClick({R.id.back, R.id.ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ok:
                Log.e("backinfo", GsonUtil.getGsonInstance().toJson(adapter.getData()));
                break;
        }
    }

}
