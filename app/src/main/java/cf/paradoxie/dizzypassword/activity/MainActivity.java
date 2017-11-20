package cf.paradoxie.dizzypassword.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cf.paradoxie.dizzypassword.MyApplication;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.fragment.Homefragment;
import cf.paradoxie.dizzypassword.fragment.Myfragment;
import cf.paradoxie.dizzypassword.tabhost.Tab;
import cf.paradoxie.dizzypassword.tabhost.TabFragmentHost;
import cf.paradoxie.dizzypassword.widget.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LayoutInflater layoutInflater;
    private TabFragmentHost mTabHost;
    boolean isExit = false;
    private List<Tab> mTabs = new ArrayList<Tab>(2);
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    Homefragment homefragment = new Homefragment();
    Myfragment twofragment = new Myfragment();
    private String mTextviewArray[] = {"密码列表", "我的"};
    private int mImageViewArray[] = {R.drawable.productclassification_bg, R.drawable.my_bg
    };
    CircleImageView loginimag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        loginimag= (CircleImageView) findViewById(R.id.personalimg);
        layoutInflater = LayoutInflater.from(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        //  mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mTabHost = (TabFragmentHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        initView();
        loginimag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Login.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void initView() {
        Tab tab_home = new Tab(homefragment.getClass(), R.string.home, mImageViewArray[0]);
        Tab tws = new Tab(twofragment.getClass(), R.string.My, mImageViewArray[1]);
        mTabs.add(tab_home);
        mTabs.add(tws);
        int i = -1;
        for (Tab tab : mTabs) {
            i = i + 1;
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(i));//需要一个View显示 菜单控件
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }
       // mTabHost.getTabWidget().setDividerDrawable(null);
    }

    private View buildIndicator(int index) {
        View view = null;
        view = layoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);
        return view;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        exitBy2Click();
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true;
            // 准备退出
            MyApplication.showToast("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}
