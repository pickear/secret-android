package cf.paradoxie.dizzypassword.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weasel.secret.common.helper.EntryptionHelper;

import java.util.List;

import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.domian.SecretList;


public class DetailsAdapter extends BaseAdapter {
    List<SecretList.SubjectsBean.SecretsBean> data;
    /**
     * 上下文对象
     */
    private Activity mContext = null;
    private  String KEY;





    /**
     * @param
     */
    public DetailsAdapter(Activity ctx,List<SecretList.SubjectsBean.SecretsBean> data,String key) {
        mContext = ctx;
        this.data=data;
        this.KEY=key;

    }


    @Override
    public int getCount() {
        return data.size();
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
        item.type.setText("密码类型："+data.get(position).getName());
        try {
            item.info.setText("密码："+EntryptionHelper.decrypt(KEY,data.get(position).getValue()));
        } catch (Exception e) {

            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {


        TextView type;

        TextView info;
    }
}