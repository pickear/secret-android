package cf.paradoxie.dizzypassword.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.help.Constant;
import cf.paradoxie.dizzypassword.util.ACache;
import cf.paradoxie.dizzypassword.util.LockPatternUtil;
import cf.paradoxie.dizzypassword.util.StringUtils;
import cf.paradoxie.dizzypassword.widget.LockPatternView;

public class GesturePasswordActivity extends Activity {


    @Bind(R.id.messageTv)
    TextView messageTv;
    @Bind(R.id.lockPatternView)
    LockPatternView lockPatternView;
    private ACache aCache;
    private static final long DELAYTIME = 600l;
    private String gesturePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_pass);
        ButterKnife.bind(this);
        this.init();
    }

    private void init() {
        aCache = ACache.get(GesturePasswordActivity.this);
        //得到当前用户的手势密码
        gesturePassword = aCache.getAsString(Constant.GESTURE_PASSWORD);
        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if (pattern != null) {
                byte[] bytes = LockPatternUtil.patternToHash(pattern);
                Log.e("backinfo", "登录输入的手势密码：" + StringUtils.getBinaryStrFromByteArr(bytes) + "原来的密码：" + gesturePassword);
                if (StringUtils.getBinaryStrFromByteArr(bytes).equals(gesturePassword)) {
                    updateStatus(Status.CORRECT);
                } else {
                    updateStatus(Status.ERROR);
                }
              /*  if(LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                    updateStatus(Status.CORRECT);
                } else {
                    updateStatus(Status.ERROR);
                }*/
            }
        }
    };

    /**
     * 更新状态
     *
     * @param status
     */
    private void updateStatus(Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
        Toast.makeText(GesturePasswordActivity.this, "success", Toast.LENGTH_SHORT).show();
    }


    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }

        private int strId;
        private int colorId;
    }
}
