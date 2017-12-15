package harlan.paradoxie.dizzypassword.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dou361.dialogui.DialogUIUtils;
import com.weasel.secret.common.helper.EntryptionHelper;

import java.util.ArrayList;
import java.util.List;

import harlan.paradoxie.dizzypassword.R;
import harlan.paradoxie.dizzypassword.dbdomain.SecretList;
import harlan.paradoxie.dizzypassword.help.ObjectUtils;
import harlan.paradoxie.dizzypassword.pickerview.Util;
import harlan.paradoxie.dizzypassword.util.StringUtils;


public class LSwipeAdapter extends BaseAdapter {
    List<SecretList> data;
    ArrayList<String> typedata = new ArrayList<>();
    /**
     * 上下文对象
     */
    private Activity mContext = null;

    View rootView;
    EditText custed;
    Button cancel, sure;

    Dialog dialog;


    public SecretList getsecret(int position) {
        return data.get(position);
    }

    String KEY;

    /**
     * @param
     */
    public LSwipeAdapter(Activity ctx, List<SecretList> mdata, String MKEY) {
        mContext = ctx;
        KEY = MKEY;
        rootView = View.inflate(ctx, R.layout.custinputview, null);
        init(mdata);
        typedata.add("登录密码");
        typedata.add("取现密码");
        typedata.add("支付密码");
        typedata.add("自定义");
    }
    private void addItem(){
        SecretList secretList=new SecretList();
        data.add(secretList);
        notifyDataSetChanged();
    }
    private void remove(int position){
        data.remove(position);
        notifyDataSetChanged();
    }
    private void init(List<SecretList> mdata) {
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

    public List<SecretList> getData() {
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
            holder.add = (TextView) convertView.findViewById(R.id.add);
            holder.deletetype = (TextView) convertView.findViewById(R.id.deletetype);
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
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        holder.deletetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SecretList secretList=GsonUtil.getGsonInstance().fromJson(GsonUtil.getGsonInstance().toJson(data.get(mposition)),SecretList.class);
                //SecretListHelp.delete(secretList);
                remove(mposition);

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
        if(mposition==0){
            holder.add.setVisibility(View.VISIBLE);
            holder.deletetype.setVisibility(View.VISIBLE);
        }else{
            holder.add.setVisibility(View.GONE);
            holder.deletetype.setVisibility(View.VISIBLE);
        }
        holder.type.setText(data.get(mposition).getName());
        holder.valuse.setText(data.get(mposition).getValue());
        return convertView;
    }


    private void showcustdialog(final int postion) {
        dialog = new MaterialDialog.Builder(mContext)
                .customView(rootView, false)
                .backgroundColor(Color.parseColor("#ffffff"))
                .build();
        dialog.show();
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
                    dialog.dismiss();
                    //DialogUIUtils.dismiss(dialog);
                    notifyDataSetChanged();
                }

            }
        });
    }

    private class ViewHolder {
        EditText valuse;
        TextView type,deletetype,add;

    }
}