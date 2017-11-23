package cf.paradoxie.dizzypassword.adapter;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.weasel.secret.common.domain.Secret;

import java.util.ArrayList;
import java.util.List;

import cf.paradoxie.dizzypassword.R;
import cf.paradoxie.dizzypassword.activity.AddSubject;
import cf.paradoxie.dizzypassword.help.GsonUtil;
import cf.paradoxie.dizzypassword.pickerview.Util;
import cf.paradoxie.dizzypassword.util.StringUtils;


public class AddTypeAdapter extends BaseAdapter {
    List<Secret> data;
    /**
     * 上下文对象
     */
    private AddSubject mContext = null;

    ArrayList<String> typedata=new ArrayList<>();

    View rootView;
    EditText custed;
    Button cancel,sure;

    /**
     * @param
     */
    public AddTypeAdapter(AddSubject ctx, List<Secret> mdata) {
       this.mContext = ctx;
        this.data=mdata;
        rootView = View.inflate(ctx, R.layout.custinputview, null);
        typedata.add("登录密码");
        typedata.add("取现密码");
        typedata.add("支付密码");
        typedata.add("自定义");

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
    ViewHolder item;
    @Override
    public View getView(final int mposition, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.typeinfo_item, parent, false);
            item = new ViewHolder();
            item.add= (TextView) convertView.findViewById(R.id.add);
            item.type= (TextView) convertView.findViewById(R.id.type);

            item.deletetype= (TextView) convertView.findViewById(R.id.deletetype);
            item.valuse= (EditText) convertView.findViewById(R.id.valuse);
            item.valuse.addTextChangedListener(new PASTextChanged(item));
            convertView.setTag(item);
            item.valuse.setTag(mposition);
        } else {// 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }
        if(mposition==0){
            item.add.setVisibility(View.VISIBLE);
            item.deletetype.setVisibility(View.GONE);
        }else{
            item.add.setVisibility(View.GONE);
            item.deletetype.setVisibility(View.VISIBLE);
        }

        if(data.get(mposition).getName()==null||data.get(mposition).getName().equals("")){
            item.type.setText("");

        }else{
            item.type.setText(data.get(mposition).getName());

        }
        item.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addType();
            }
        });
        item.deletetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteType(mposition);
            }
        });
        item.type.setOnClickListener(new View.OnClickListener() {
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
                        Log.e("backinfo", GsonUtil.getGsonInstance().toJson(data));

                    }
                });
            }
        });



        return convertView;
    }
    public void addType(){
        Secret secret=new Secret();
        data.add(secret);
        notifyDataSetChanged();
    }
     Dialog dialogUIUtils;
    private void showcustdialog(final int postion){
        dialogUIUtils= DialogUIUtils.showCustomAlert(mContext, rootView, Gravity.CENTER, true, false).show();
        custed= (EditText) rootView.findViewById(R.id.et_1);
        cancel= (Button) rootView.findViewById(R.id.cancel);
        sure= (Button) rootView.findViewById(R.id.sure);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.dismiss(dialogUIUtils);
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(StringUtils.isEmpty(custed.getText().toString().trim())){
                    Toast.makeText(mContext,"请输入自定义的密码名称",Toast.LENGTH_LONG).show();
                }else{
                    data.get(postion).setName(custed.getText().toString());
                    DialogUIUtils.dismiss(dialogUIUtils);
                    notifyDataSetChanged();
                }

            }
        });
    }

    public void deleteType(int position){
        data.remove(position);
        notifyDataSetChanged();

    }
    public List<Secret> getData(){
        return data;
    }
    private class ViewHolder {
        TextView add,deletetype,type;
        EditText valuse;


    }

    class PASTextChanged implements TextWatcher {
        private ViewHolder mHolder;
        public PASTextChanged(ViewHolder holder) {
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
}