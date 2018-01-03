package harlan.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import harlan.paradoxie.dizzypassword.R;
import harlan.paradoxie.dizzypassword.adapter.DetailsAdapter;
import harlan.paradoxie.dizzypassword.domian.SecretList;
import harlan.paradoxie.dizzypassword.help.GsonUtil;
import harlan.paradoxie.dizzypassword.widget.CustListView;

public class Secretdetails extends Activity {


    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.secrettitle)
    TextView secrettitle;
    @Bind(R.id.url)
    TextView url;
    @Bind(R.id.details)
    CustListView details;
    String key;
    String json;
    DetailsAdapter adapter;
    @Bind(R.id.account)
    TextView account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretdetails);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title.setText("密码详情");
        if (bundle != null) {
            key = bundle.getString("key");
            json = bundle.getString("json");
            Log.e("backinfo", "json:" + json);
            SecretList.SubjectsBean subjectsBean = GsonUtil.getGsonInstance().fromJson(json, SecretList.SubjectsBean.class);
            secrettitle.setText("标题：" + subjectsBean.getTitle());
            url.setText("链接：" + subjectsBean.getUrl());
            account.setText("账号名称："+subjectsBean.getAccount());
            adapter = new DetailsAdapter(Secretdetails.this, subjectsBean.getSecrets(), key);
            details.setAdapter(adapter);
        }
    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

        }
    }
}
