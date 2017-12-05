package cf.paradoxie.dizzypassword.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.weasel.secret.common.helper.EntryptionHelper;

import java.util.ArrayList;
import java.util.List;

import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.help.ObjectUtils;
import cf.paradoxie.dizzypassword.pickerview.Util;
import cf.paradoxie.dizzypassword.util.StringUtils;


public class LSwipeAdapter<T> extends BaseAdapter {
    List<T> data;
    ArrayList<String> typedata = new ArrayList<>();
    /**
     * 上下文对象
     */
    private Activity mContext = null;

    View rootView;
    EditText custed;
    Button cancel, sure;

    Dialog dialog;


    public T getsecret(int position) {
        return data.get(position);
    }

    String KEY;

    /**
     * @param
     */
    public LSwipeAdapter(Activity ctx, List<T> mdata, String MKEY) {
        mContext = ctx;

        KEY = MKEY;
        init(mdata);
        data = mdata;

        typedata.add("登录密码");
        typedata.add("取现密码");
        typedata.add("支付密码");
        typedata.add("自定义");
    }

    private void init(List<T> mdata) {
        for(int i=0;i<mdata.size();i++){
            String value="";
            try {
                 value=EntryptionHelper.decrypt(KEY, (String) ObjectUtils.getValueByKey(mdata.get(i), "value"));
                if(StringUtils.isEmpty(value)){
                    Toast.makeText(mContext,"秘钥错误",Toast.LENGTH_LONG).show();
                    mContext.finish();
                }


            } catch (Exception e) {
                Toast.makeText(mContext,"秘钥错误",Toast.LENGTH_LONG).show();
                mContext.finish();
                e.printStackTrace();
            }
            try {
                ObjectUtils.setProperty(mdata.get(i), "value",value);
            } catch (Exception e) {
                Toast.makeText(mContext,"初始化数据出错",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public List<T> getData(){
        return data;
    }
    public void delete(int position) {
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
    ViewHolder holder;
    @Override
    public View getView(final int mposition, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.edit_item, parent, false);
            holder = new ViewHolder();
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.valuse = (EditText) convertView.findViewById(R.id.valuse);
            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }
        final Object itemObj = data.get(mposition);
        holder.type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.alertBottomWheelOption(v, mContext, typedata, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        if (typedata.get(postion).equals("自定义")) {
                            showcustdialog(mposition);

                        } else {
                            try {
                                ObjectUtils.setProperty(data.get(mposition), "name", typedata.get(postion));
                                holder.type.setText((String)ObjectUtils.getValueByKey(itemObj, "name"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // data.get(mposition).setName(typedata.get(postion));
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        try {
            holder.type.setText((String)ObjectUtils.getValueByKey(itemObj, "name"));
            holder.valuse.setText((String)ObjectUtils.getValueByKey(itemObj, "value"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (holder.valuse.getTag() instanceof TextWatcher) {
            holder.valuse.removeTextChangedListener((TextWatcher) holder.valuse.getTag());
        }
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !"".equals(s.toString())) {
                    try {
                        ObjectUtils.setProperty(itemObj, "value", s.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        holder.valuse.addTextChangedListener(watcher);
        holder.valuse.setTag(watcher);

        return convertView;
    }

    private void showcustdialog(final int postion) {
        dialog = DialogUIUtils.showCustomAlert(mContext, rootView, Gravity.CENTER, true, false).show();
        custed = (EditText) rootView.findViewById(R.id.et_1);
        cancel = (Button) rootView.findViewById(R.id.cancel);
        sure = (Button) rootView.findViewById(R.id.sure);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(dialog);
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StringUtils.isEmpty(custed.getText().toString().trim())) {
                    Toast.makeText(mContext, "请输入自定义的密码名称", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        ObjectUtils.setProperty(data.get(postion), "name", custed.getText().toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    DialogUIUtils.dismiss(dialog);
                    notifyDataSetChanged();
                }

            }
        });
    }

    private class ViewHolder {
        EditText valuse;
        TextView type;

    }
}