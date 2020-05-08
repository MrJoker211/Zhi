package com.example.zhi.Fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public Context context;
    public Resources resources;
    public LayoutInflater layoutInflater;

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        this.context = context;
        this.resources = context.getResources();
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //用于缓存的东西,缓存的Frag
    private View RootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(RootView == null){
            RootView = inflater.inflate(getLayoutId(),container,false);
        }

        ViewGroup parent = (ViewGroup) RootView.getParent();

        if(parent != null){
            parent.removeView(RootView);
        }

        return RootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bindEvent();
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    //VV是View类型， id是控件的id
    protected <VV extends View> VV findView(View view,@IdRes int id){
        return view.findViewById(id);
    }
    protected <VV extends View> VV findView(@IdRes int id){
        return RootView.findViewById(id);
    }

    protected abstract void initData();

    protected abstract void bindEvent();

    protected abstract void initView(View view);

    protected abstract int getLayoutId();
}
