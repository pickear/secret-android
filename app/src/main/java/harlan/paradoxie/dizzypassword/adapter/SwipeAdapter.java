package harlan.paradoxie.dizzypassword.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.List;

import harlan.paradoxie.dizzypassword.R;
import harlan.paradoxie.dizzypassword.help.StringReplaceUtil;


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

            item.item_left_txt = (TextView)convertView.findViewById(R.id.item_left_txt);
            item.createTime = (TextView)convertView.findViewById(R.id.createTime);
            item.updateTime = (TextView)convertView.findViewById(R.id.updateTime);
            convertView.setTag(item);
        } else {// 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }



        item.item_left_txt.setText("标题:" + getFieldValueByName("title", data.get(position)));
        String account=(String)getFieldValueByName("account",data.get(position));
        item.url.setText("账号名称:"+ StringReplaceUtil.userNameReplaceWithStar(account));
      //  item.createTime.setText("创建时间："+ Date_U.toLongDateString((Long) getFieldValueByName("createTime", data.get(position))));
       // item.updateTime.setText("更新时间："+Date_U.toLongDateString((Long) getFieldValueByName("updateTime", data.get(position))));
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




        TextView item_left_txt,url,createTime,updateTime;


    }
}