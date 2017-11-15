package cf.paradoxie.dizzypassword.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cf.paradoxie.dizzypassword.R;

public class Homefragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homefragment, null);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
