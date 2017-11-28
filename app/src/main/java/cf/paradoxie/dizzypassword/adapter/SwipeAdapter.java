package cf.paradoxie.dizzypassword.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.List;

import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.domian.SecretList;
import me.drakeet.materialdialog.MaterialDialog;


public class SwipeAdapter extends BaseAdapter {
    List<SecretList.SubjectsBean> data;
    /**
     * 上下文对象
     */
    private Context mContext = null;

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

    /**
     * @param
     */
    public SwipeAdapter(Context ctx, int rightWidth, List<SecretList.SubjectsBean> mdata,IOnItemRightClickListener l) {
        mContext = ctx;
        mRightWidth = rightWidth;
        mListener = l;
        data=mdata;
    }
    public void delete(int position){
        data.remove(position);
        notifyDataSetChanged();
    }
    MaterialDialog mMaterialDialog;
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
            item.item_right_txt = (TextView)convertView.findViewById(R.id.item_right_txt);
            convertView.setTag(item);
        } else {// 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }


        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        item.item_right.setLayoutParams(lp2);
        item.item_right.setBackgroundColor(Color.parseColor("#333333"));
        item.item_left_txt.setText(data.get(position).getTitle());
        item.item_right_txt.setText("删除");
        item.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                mMaterialDialog = new MaterialDialog(mContext)
                        .setTitle("删除")
                        .setMessage("你是否确定删除该密码数据")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               /* mMaterialDialog.dismiss();
                                mListener.onRightClick(v, thisPosition);*/
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });

                mMaterialDialog.show();

               /* DialogUIUtils.showMdAlert(mContext, "标题", "文本内容", new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        if (mListener != null) {
                            mListener.onRightClick(v, thisPosition);


                        }
                    }

                    @Override
                    public void onNegative() {

                    }

                }).show();*/

            }
        });
        return convertView;
    }

    private class ViewHolder {


        View item_right;

        TextView item_left_txt;

        TextView item_right_txt;
    }
}