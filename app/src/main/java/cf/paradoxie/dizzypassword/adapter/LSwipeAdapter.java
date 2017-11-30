package cf.paradoxie.dizzypassword.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.db.help.dbutlis.SecretHelp;
import cf.paradoxie.dizzypassword.dbdomain.Secret;
import io.reactivex.annotations.NonNull;


public class LSwipeAdapter extends BaseAdapter {
    List<Secret> data;
    /**
     * 上下文对象
     */
    private Activity mContext = null;

    /**
     *
     */
    private int mRightWidth = 0;

    /**
     * 单击事件监听器
     */
    private IOnItemRightClickListener mListener = null;

    public interface IOnItemRightClickListener {
        void onRightClick(View v, int position);
    }
    public Secret getsecret(int position){
        return data.get(position);
    }

    /**
     * @param
     */
    public LSwipeAdapter(Activity ctx, int rightWidth, List<Secret> mdata, IOnItemRightClickListener l) {
        mContext = ctx;
        mRightWidth = rightWidth;
        mListener = l;
        data=mdata;
    }
    public void delete(int position){
        data.remove(position);
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
            item = new ViewHolder();

            item.item_right = (View)convertView.findViewById(R.id.item_right);
            item.item_left_txt = (TextView)convertView.findViewById(R.id.item_left_txt);
            item.url = (TextView)convertView.findViewById(R.id.url);
            item.item_right_txt = (TextView)convertView.findViewById(R.id.item_right_txt);
            convertView.setTag(item);
        } else {// 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }


        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        item.item_right.setLayoutParams(lp2);
        item.item_right.setBackgroundColor(Color.parseColor("#333333"));
        item.item_left_txt.setText("标题:"+data.get(position).getTitle());
        item.item_right_txt.setText("删除");
        item.url.setText("链接:"+data.get(position).getUrl());
        item.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {

                new MaterialDialog.Builder(mContext)
                        .title("删除")
                        .content("你确定要删除该条记录吗？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                SecretHelp.delete(data.get(position).getId());
                                data.remove(position);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
        return convertView;
    }

    private class ViewHolder {


        View item_right;

        TextView item_left_txt,url;

        TextView item_right_txt;
    }
}