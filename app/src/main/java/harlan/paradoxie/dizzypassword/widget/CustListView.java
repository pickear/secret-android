package harlan.paradoxie.dizzypassword.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CustListView extends ListView {

    public CustListView(Context context) {
        super(context);
    }

    public CustListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
