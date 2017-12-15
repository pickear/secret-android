package harlan.paradoxie.dizzypassword.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weasel.secret.common.helper.EntryptionHelper;

import java.lang.reflect.Method;
import java.util.List;

import harlan.paradoxie.dizzypassword.R;
import harlan.paradoxie.dizzypassword.util.StringUtils;


public class DetailsAdapter<T> extends BaseAdapter {
    List<T> data;
    /**
     * 上下文对象
     */
    private Activity mContext = null;
    private  String KEY;





    /**
     * @param
     */
    public DetailsAdapter(Activity ctx,List<T> data,String key) {
        mContext = ctx;
        this.data=data;
        this.KEY=key;

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.details_item, parent, false);
            item = new ViewHolder();
            item.info= (TextView) convertView.findViewById(R.id.info);
            item.type= (TextView) convertView.findViewById(R.id.type);
            convertView.setTag(item);
        } else {// 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }
        item.type.setText("密码类型："+getFieldValueByName("name", data.get(position)));
        try {
            Log.e("backinfo", "valuse" + (String) getFieldValueByName("valuse", data.get(position)));
            if(!StringUtils.isEmpty((String)getFieldValueByName("value",data.get(position)))){
                item.info.setText("密码："+EntryptionHelper.decrypt(KEY,(String)getFieldValueByName("value",data.get(position))));
            }else{
                item.info.setText("密码："+EntryptionHelper.decrypt(KEY,(String)getFieldValueByName("valuse",data.get(position))));
            }

        } catch (Exception e) {
         //   Toast.makeText(mContext,"秘钥出错",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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


        TextView type;

        TextView info;
    }
}