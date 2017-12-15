package harlan.paradoxie.dizzypassword.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dou361.dialogui.DialogUIUtils;

import harlan.paradoxie.dizzypassword.interfaces.StateType;

public abstract class BaseFragment extends Fragment implements StateType {


    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return null;
    }

    public void ShowProgress(String msg) {
        dialog = DialogUIUtils.showLoading(getActivity(), msg, true, true, false, true).show();
    }

    public void HideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }



}
