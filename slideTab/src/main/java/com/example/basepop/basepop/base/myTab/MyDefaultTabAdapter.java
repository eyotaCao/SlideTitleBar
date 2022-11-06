package com.example.basepop.basepop.base.myTab;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.basepop.R;

public class MyDefaultTabAdapter extends MyTabAdapter<MySlideTab.CommonTabBean>{
    Resources resources;
    public MyDefaultTabAdapter(Resources resources) {
        super(R.layout.item_default_tab);
        this.resources=resources;

    }

    @Override
    protected void convert(MySlideTab.CommonTabBean data, View baseView) {
        TextView title=baseView.findViewById(R.id.item_text);
        title.setText(data.title);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.sp13));
        DragBadgeView num=baseView.findViewById(R.id.item_text_msg_num);
        num.setDragEnable(false);
        if (data.msgNum>0){

            num.setVisibility(View.VISIBLE);
            num.setText(new StringBuilder().append(data.msgNum));
        }else{
            num.setVisibility(View.GONE);
        }
    }
}
