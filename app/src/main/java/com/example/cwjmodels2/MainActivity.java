package com.example.cwjmodels2;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.basepop.basepop.base.myTab.MyBasePagerAdapter;
import com.example.basepop.basepop.base.myTab.MyDefaultTabAdapter;
import com.example.basepop.basepop.base.myTab.MySlideTab;
import com.example.basepop.basepop.base.utils.PxTool;
import com.example.cwjmodels2.databinding.ActivityMainBinding;

import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        MyDefaultTabAdapter tabAdapter=new MyDefaultTabAdapter(getResources());
        ArrayList<Fragment> list=new ArrayList<>();
        list.add(new FirstFragment());
        list.add(new FirstFragment());
        MyBasePagerAdapter adapter = new MyBasePagerAdapter(getSupportFragmentManager(),list);
        binding.mVp.setAdapter(adapter);
        binding.mTab.setTabAdapter(tabAdapter);
        List<MySlideTab.CommonTabBean> beans=new ArrayList<>();
        beans.add(new MySlideTab.CommonTabBean("test1",0));
        beans.add(new MySlideTab.CommonTabBean("test2",0));
        tabAdapter.setList(beans,binding.mVp);

        initTab2();
        initTab3();
        initTab4();

        binding.changeStyle.setOnClickListener(v->{
            if (binding.mTab3.getIndicatorStyle()== MySlideTab.STYLE_DOWN){
                binding.mTab3.setIndicatorStyle(MySlideTab.STYLE_FILL);
            }else {
                binding.mTab3.setIndicatorStyle(MySlideTab.STYLE_DOWN);
            }

        });

    }


    private void initTab2(){
        MyDefaultTabAdapter tabAdapter=new MyDefaultTabAdapter(getResources());
        ArrayList<Fragment> list=new ArrayList<>();
        list.add(new FirstFragment());
        list.add(new FirstFragment());list.add(new FirstFragment());list.add(new FirstFragment());
        MyBasePagerAdapter adapter = new MyBasePagerAdapter(getSupportFragmentManager(),list);
        binding.mVp2.setAdapter(adapter);
        binding.mTab2.setTabAdapter(tabAdapter);
        binding.mTab2.setTabWidth(PxTool.dip2px(getApplicationContext(),150));
        List<MySlideTab.CommonTabBean> beans=new ArrayList<>();
        beans.add(new MySlideTab.CommonTabBean("test1",0));
        beans.add(new MySlideTab.CommonTabBean("test2222222",0));
        beans.add(new MySlideTab.CommonTabBean("test333333",0));
        beans.add(new MySlideTab.CommonTabBean("test4444",0));
        tabAdapter.setList(beans,binding.mVp2);
    }

    private void initTab3(){
        MyDefaultTabAdapter tabAdapter=new MyDefaultTabAdapter(getResources());
        ArrayList<Fragment> list=new ArrayList<>();
        list.add(new FirstFragment());list.add(new FirstFragment());
        list.add(new FirstFragment());list.add(new FirstFragment());list.add(new FirstFragment());list.add(new FirstFragment());
        MyBasePagerAdapter adapter = new MyBasePagerAdapter(getSupportFragmentManager(),list);
        binding.mVp3.setAdapter(adapter);
        binding.mTab3.setAutoWidth(true);
        binding.mTab3.setTabAdapter(tabAdapter);
        List<MySlideTab.CommonTabBean> beans=new ArrayList<>();
        beans.add(new MySlideTab.CommonTabBean("test1",0));
        beans.add(new MySlideTab.CommonTabBean("test2222222222222222222222",0));
        beans.add(new MySlideTab.CommonTabBean("test33333333333333333333333",0));
        beans.add(new MySlideTab.CommonTabBean("test4444444444444444",0));
        beans.add(new MySlideTab.CommonTabBean("test5555555555555",0));
        beans.add(new MySlideTab.CommonTabBean("test666",0));

        tabAdapter.setList(beans,binding.mVp3);

    }
    private void initTab4(){
        MyDefaultTabAdapter tabAdapter=new MyDefaultTabAdapter(getResources());
        ArrayList<Fragment> list=new ArrayList<>();
        list.add(new FirstFragment());list.add(new FirstFragment());
        list.add(new FirstFragment());list.add(new FirstFragment());list.add(new FirstFragment());list.add(new FirstFragment());
        MyBasePagerAdapter adapter = new MyBasePagerAdapter(getSupportFragmentManager(),list);
        binding.mVp4.setAdapter(adapter);
        binding.mTab4.setAutoWidth(true);
        binding.mTab4.setTabAdapter(tabAdapter);
        List<MySlideTab.CommonTabBean> beans=new ArrayList<>();
        beans.add(new MySlideTab.CommonTabBean("test1",0));
        beans.add(new MySlideTab.CommonTabBean("test2222222222222222222222",1));
        beans.add(new MySlideTab.CommonTabBean("test33333333333333333333333",2));
        beans.add(new MySlideTab.CommonTabBean("test4444444444444444",4));
        beans.add(new MySlideTab.CommonTabBean("test5555555555555",3));
        beans.add(new MySlideTab.CommonTabBean("test666",0));

        tabAdapter.setList(beans,binding.mVp4);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}