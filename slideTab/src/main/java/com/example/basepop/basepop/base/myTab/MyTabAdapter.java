package com.example.basepop.basepop.base.myTab;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import java.util.List;

public abstract class MyTabAdapter<T> {
    private SetListLister mSetListLister;
    private int myLayout;
    private List<T> datas;
    public MyTabAdapter(int lay){
        myLayout=lay;
    }

    protected abstract void convert(T data, View baseView);

    protected  void onSelect(int position){};

    public void setList(List<T> list, ViewPager viewPager){  //初始化并绑定

        datas=list;
        if (mSetListLister!=null){
            mSetListLister.onSet(datas.size(),viewPager);
        }
    }
    public void setList(List<T> list){ //刷新数据

        datas=list;
        if (mSetListLister!=null){
            mSetListLister.onSet(datas.size());
        }
    }


    public List<T> getDatas(){
        return datas;
    }
    public int getLayout(){
        return myLayout;
    }
    public void setListLister(SetListLister setListLister){
        mSetListLister=setListLister;
    }
    public interface SetListLister{
        void onSet(int size,ViewPager viewPager);
        void onSet(int size);
   //     void onSet(int size);
    }
}