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
import cf.paradoxie.dizzypassword.dbdomain.UpdataSecret;
import cf.paradoxie.dizzypassword.help.ObjectUtils;
import cf.paradoxie.dizzypassword.pickerview.Util;
import cf.paradoxie.dizzypassword.util.StringUtils;


public class LSwipeAdapter extends BaseAdapter {
    List<UpdataSecret.SecretsBean> data;
    ArrayList<String> typedata = new ArrayList<>();
    /**
     * 上下文对象
     */
    private Activity mContext = null;

    View rootView;
    EditText custed;
    Button cancel, sure;

    Dialog dialog;


    public UpdataSecret.SecretsBean getsecret(int position) {
        return data.get(position);
    }

    String KEY;

    /**
     * @param
     */
    public LSwipeAdapter(Activity ctx, List<UpdataSecret.SecretsBean> mdata, String MKEY) {
        mContext = ctx;
        KEY = MKEY;
        init(mdata);
        typedata.add("登录密码");
        typedata.add("取现密码");
        typedata.add("支付密码");
        typedata.add("自定义");
    }

    private void init(List<UpdataSecret.SecretsBean> mdata) {
        for (int i = 0; i < mdata.size(); i++) {
            String value = "";
            try {
                value = EntryptionHelper.decrypt(KEY, (String) ObjectUtils.getValueByKey(mdata.get(i), "value"));
                if (StringUtils.isEmpty(value)) {
                    Toast.makeText(mContext, "秘钥错误", Toast.LENGTH_LONG).show();
                    mContext.finish();
                }


            } catch (Exception e) {
                Toast.makeText(mContext, "秘钥错误", Toast.LENGTH_LONG).show();
                mContext.finish();
                e.printStackTrace();
            }
            try {
                ObjectUtils.setProperty(mdata.get(i), "value", value);
            } catch (Exception e) {
                Toast.makeText(mContext, "初始化数据出错", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        data = mdata;
    }

    public List<UpdataSecret.SecretsBean> getData() {
        return data;
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
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
           /* holder.valuse.addTextChangedListener(new watcher(holder));
            holder.valuse.setTag(mposition);*/
            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }
        holder.valuse.setTag(mposition);//设置editext一个标记
        holder.valuse.clearFocus();//清除焦点  不清除的话因为item复用的原因   多个Editext同时改变
        holder.type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.alertBottomWheelOption(v, mContext, typedata, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        if (typedata.get(postion).equals("自定义")) {
                            showcustdialog(mposition);
                        } else {
                            data.get(mposition).setName(typedata.get(postion));
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        final EditText tempEditText= holder.valuse;
        holder.valuse.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !"".equals(s.toString())) {
                    int position = (Integer) tempEditText.getTag();
                    data.get(position).setValue(s.toString());
                }

            }
        });
        holder.type.setText(data.get(mposition).getName());
        holder.valuse.setText(data.get(mposition).getValue());
        return convertView;
    }

    class watcher implements TextWatcher {
        private ViewHolder mHolder;

        public watcher(ViewHolder holder) {
            mHolder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !"".equals(s.toString())) {
                int position = (Integer) mHolder.valuse.getTag();
                data.get(position).setValue(s.toString());
            }
        }
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

                    data.get(postion).setName(custed.getText().toString().trim());
                    // ObjectUtils.setProperty(data.get(postion), "name", custed.getText().toString().trim());


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