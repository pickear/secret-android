package cf.paradoxie.dizzypassword.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.List;

import cf.paradoxie.dizzypassword.R;


public class SwipeAdapter<T> extends BaseAdapter {
    List<T> data;
    /**
     * 上下文对象
     */
    private Activity mContext = null;

    /**
     *
     */
    private int mRightWidth = 0;




    public T getsecret(int position){
        return data.get(position);
    }
    /**
     * @param
     */
    public SwipeAdapter(Activity ctx, List<T> mdata) {
        mContext = ctx;

        data=mdata;
    }
    public void delete(int position){
        data.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder item;
        final int thisPosition = position;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
            item = new ViewHolder();
            item.url = (TextView)convertView.findViewById(R.id.url);
            item.item_right = (View)convertView.findViewById(R.id.item_right);
            item.item_left_txt = (TextView)convertView.findViewById(R.id.item_left_txt);
            item.item_right_txt = (TextView)convertView.findViewById(R.id.item_right_txt);
            convertView.setTag(item);
        } else {// 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }


        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        item.item_right.setLayoutParams(lp2);
        item.item_right.setBackgroundColor(Color.parseColor("#333333"));
        item.item_left_txt.setText("标题:" + getFieldValueByName("title", data.get(position)));
        item.item_right_txt.setText("删除");
        item.url.setText("链接:"+getFieldValueByName("url",data.get(position)));

        return convertView;
    }
    /**
     * 根据属性名获取属性值
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return "";
        }
    }
    private class ViewHolder {


        View item_right;

        TextView item_left_txt,url;

        TextView item_right_txt;
    }
}