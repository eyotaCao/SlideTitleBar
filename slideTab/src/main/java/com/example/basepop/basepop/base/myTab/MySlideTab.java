package com.example.basepop.basepop.base.myTab;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.basepop.R;
import com.example.basepop.basepop.base.utils.AnimatorUtil;
import com.example.basepop.basepop.base.utils.PxTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySlideTab extends HorizontalScrollView {
    private int mWidth,mHeight,mIndicatorMarginLeft;
    private int mTabCount=0;
    private int mTabWidth=0;
    private int mSelectIndex=0;
    private int textColor,selectTextColor;

    //指示器
    private int selectDColor,unSelectDColor,indicatorColor;
    private int indicatorHeight,indicatorWidth;
    private int inRound, marginY,paddingX, marginX;
    private int inX,inY;
    private boolean isShowViewpagerSlide=false;
    private boolean isIndicatorWidthWithTab;  //指示器宽度与内容保持一致
    private boolean autoWidth=false;  //每个tab是否是等长
    private int tabInterval;
    private int indicatorStyle;   //1:默认 2：下方显示
    public static final int STYLE_FILL=1;
    public static final int STYLE_DOWN=2;


    private float mCurrentPositionOffset;
    private int mLastScrollX;
    private Paint indicatorPaint;
    private RectF rectFIndicator;

    private MyTabAdapter<Object> mAdapter;
    private ViewPager mViewPager;


    private Context mContext;
    private final List<View> mTabViews=new ArrayList<>();
    private final HashMap<Integer,Integer> mTitleWidths=new HashMap<>();
    private int mTabView= R.layout.item_default_tab;
    private LinearLayout mBaseContainer;
    private RelativeLayout mBaseParent;

    private boolean isChangeMsg=true;  //是否显示消息点

    public MySlideTab(Context context) {
        super(context);
        init(context,null);
    }

    public MySlideTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MySlideTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        mContext=context;
        Resources resources = context.getResources();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MySlideTab);
        indicatorColor = typedArray.getColor(R.styleable.MySlideTab_indicator_color, resources.getColor(R.color.color2866FE));
        textColor = typedArray.getColor(R.styleable.MySlideTab_text_color, resources.getColor(R.color.color95A3C1));
        selectTextColor = typedArray.getColor(R.styleable.MySlideTab_selectText_color, resources.getColor(R.color.color181D40));
        indicatorStyle=typedArray.getInteger(R.styleable.MySlideTab_indicator_style,STYLE_FILL);
        indicatorHeight=typedArray.getDimensionPixelSize(R.styleable.MySlideTab_indicator_height, PxTool.dpToPx(context,4));
        isIndicatorWidthWithTab=typedArray.getBoolean(R.styleable.MySlideTab_indicator_width_auto,true);

        selectDColor= resources.getColor(R.color.color2866FE);
        unSelectDColor= resources.getColor(R.color.colorFFFFFF);
        mBaseParent=new RelativeLayout(mContext);

        mBaseContainer=new LinearLayout(mContext);
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mBaseContainer.setLayoutParams(rlp);
        initIndicator();
        setOverScrollMode(OVER_SCROLL_NEVER);
        setHorizontalScrollBarEnabled(false);
        addView(mBaseParent);
        mBaseParent.addView(mBaseContainer);
        typedArray.recycle();
        tabInterval=PxTool.dip2px(mContext,5);
        marginX =PxTool.dip2px(getContext(),2);
        marginY =PxTool.dip2px(getContext(),2);
        paddingX =PxTool.dip2px(getContext(),6);
    }




    private void initIndicator(){
        indicatorPaint=new Paint();
        indicatorPaint.setColor(indicatorColor);
        inRound=PxTool.dpToPx(getContext(),3);
        rectFIndicator=new RectF();
    }

    private void refreshIndicator(){
        indicatorWidth=mTitleWidths.get(mSelectIndex)==null?mTabWidth:mTitleWidths.get(mSelectIndex);
        if (autoWidth){
            inX= mTabViews.get(mSelectIndex).getLeft()+tabInterval;
        }else {
            inX=mSelectIndex*mTabWidth+(int)((float)(mTabWidth-indicatorWidth)/2f);
        }
        invalidate();
    }

    private boolean isInitIndicatorPosition=false;
    public void setTabTitles(int size){  //初始化视图
        if (mTabViews.size()>0){
            mTabViews.clear();
        }
        mTabCount=size;
        if (mTabWidth==0){
            mTabWidth= (int)((float)mWidth/(float) size);
        }
        for (int i=0;i<size;i++){
            View tab = LayoutInflater.from(mContext).inflate(mTabView, null);
            TextView title=tab.findViewById(R.id.item_text);
            title.setTextColor(textColor);
            ViewTreeObserver vto = title.getViewTreeObserver();
            int index=i;
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (mTitleWidths.get(index)==null){
                        mTitleWidths.put(index,title.getWidth());
                        if (mTitleWidths.size()>=mTabCount-1){
                            if (!isInitIndicatorPosition){
                                isInitIndicatorPosition=true;
                                refreshIndicator();
                            }
                        }
                    }

                }
            });
            mTabViews.add(tab);
            LinearLayout tabLl=tab.findViewById(R.id.item_ll);
            DragBadgeView badgeView=tab.findViewById(R.id.item_text_msg_num);
            badgeView.setOnDragBadgeViewListener(new DragBadgeView.OnDragBadgeViewListener() {
                @Override
                public void onDisappear(String text) {
                    postDelayed(()->{
                        refreshIndicator();
                    },200);

                }
            });
            if (mAdapter!=null){
                mAdapter.convert(mAdapter.getDatas().get(i),tab);
            }
            LinearLayout.LayoutParams llp;
            if (autoWidth){
                llp =new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                tabLl.setPadding(tabInterval,0,tabInterval,0);
            }else {
                llp =new LinearLayoutCompat.LayoutParams(mTabWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            llp.gravity= Gravity.CENTER;
            tabLl.setLayoutParams(llp);
            int i2=i;
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mViewPager!=null){

                        select(i2,false);
                    }
                }
            });

            mBaseContainer.addView(tab);
        }
        initSelect(mSelectIndex);
    }


    private boolean isFirst=true;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst){
            isFirst=false;
            setTabTitles(mTabCount);
        }

        //画指示器
        if (indicatorStyle==STYLE_FILL){
            if (isIndicatorWidthWithTab||autoWidth){
                rectFIndicator.left=inX-paddingX;
                rectFIndicator.right=inX+indicatorWidth+paddingX;
            }else {
                rectFIndicator.left=mTabWidth*mSelectIndex+marginX;
                rectFIndicator.right=mTabWidth*(mSelectIndex+1)-marginX;
            }
            rectFIndicator.top= marginY;
            rectFIndicator.bottom=mHeight- marginY;
        }else {
            if (isIndicatorWidthWithTab||autoWidth){
                rectFIndicator.left=inX;
                rectFIndicator.right=inX+indicatorWidth;
            }else {
                rectFIndicator.left=mTabWidth*mSelectIndex+marginX;
                rectFIndicator.right=mTabWidth*(mSelectIndex+1)-marginX;
            }
            rectFIndicator.top=mHeight-indicatorHeight;
            rectFIndicator.bottom=mHeight;
        }


        canvas.drawRoundRect(rectFIndicator, inRound,inRound,indicatorPaint);
    }

    private void select(int index,boolean moveViewpager){
        mSelectIndex=index;
        for (int i=0;i<mTabViews.size();i++){
            if (index==i){
                View tab=mTabViews.get(i);
                TextView title=tab.findViewById(R.id.item_text);
                title.getPaint().setFakeBoldText(true);
                title.setTextColor(selectTextColor);
            }else {
                View tab=mTabViews.get(i);
                TextView title=tab.findViewById(R.id.item_text);
                title.getPaint().setFakeBoldText(false);
                title.setTextColor(textColor);
            }
        }

        if (mViewPager!=null){
            mViewPager.setCurrentItem(index,moveViewpager);
        }


    }


    private void refreshData(int size){
        for (int i=0;i<size;i++){
            if (mAdapter!=null){
                mAdapter.convert(mAdapter.getDatas().get(i),mTabViews.get(i));
            }
        }
    }

    public void setClickAnimate(boolean isShow){
        isShowViewpagerSlide=isShow;
    }
    public void setShow(boolean isShow){  //显示消息点
        isChangeMsg=isShow;
    }

    public void setAutoWidth(boolean autoWidth) {
        this.autoWidth = autoWidth;
    }

    public int getCurrentTab(){
        return mSelectIndex;
    }

    public void setTabWidth(int width) {
        this.mTabWidth = width;
        if (mTabViews.size()>0){
            for (View view:mTabViews){
                LinearLayout.LayoutParams llp=(LinearLayout.LayoutParams) view.getLayoutParams();
                llp.width=mTabWidth;
                view.setLayoutParams(llp);
            }
        }else {
            mTabWidth=width;
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
    }

    public void setTabAdapter(MyTabAdapter adapter){
        mAdapter=adapter;
        mAdapter.setListLister(new MyTabAdapter.SetListLister() {
            @Override
            public void onSet(int s,ViewPager viewPager) {
                mTabView=mAdapter.getLayout();
                setViewPager(viewPager);
                mTabCount=s;
            }

            @Override
            public void onSet(int size) {
                refreshData(size);
            }
        });
    }

    public void setViewPager(ViewPager viewPager){
        mViewPager=viewPager;
        try {
            mViewPager.setCurrentItem(mSelectIndex,false);
        }catch (Exception e){}


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mTitleWidths.get(position+1)!=null&&mTitleWidths.get(position)!=null){
                    int width1=mTitleWidths.get(position);
                    int width2=mTitleWidths.get(position+1);
                    int mar1=(int)((float)(mTabWidth-width1)/2f)+mTabWidth*(position);
                    int mar2=mTabWidth*(position+1)+(int)((float)(mTabWidth-width2)/2f);
                    if (autoWidth){
                        inX= mTabViews.get(position).getLeft()+
                                (int)((mTabViews.get(position).getMeasuredWidth())*positionOffset)+tabInterval;

                    }else {
                        inX=mar1+(int)((mar2-mar1)*positionOffset);
                    }

                    indicatorWidth=width1+(int)((width2-width1)*positionOffset);
                }
                invalidate();
                mCurrentPositionOffset= positionOffset;
                mSelectIndex=position;
                try {
                    selectColor2();
                    scrollToCurrentTab();
                }catch (Exception e){}


            }

            @Override
            public void onPageSelected(int position) {
                mSelectIndex=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void scrollToCurrentTab() {
        if (mTabCount <= 0) {
            return;
        }
        int offset = (int) (mCurrentPositionOffset * mBaseContainer.getChildAt(mSelectIndex).getWidth());
        int newScrollX = mBaseContainer.getChildAt(mSelectIndex).getLeft() + offset;

        if (mSelectIndex > 0 || offset > 0) {
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            newScrollX += ((mTabWidth) / 2);
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            smoothScrollTo(newScrollX, 0);
        }
    }

    private void initSelect(int index){
        mSelectIndex=index;
        selectColor2();
        scrollToCurrentTab();
        if (mViewPager!=null){
            mViewPager.setCurrentItem(index,false);
        }
    }

    private boolean isChange1=true,isChange2=false;
    private void selectColor2(){  //过渡颜色
        if (mTabViews.size()==0)return;
        View tab=mTabViews.get(mSelectIndex);
        TextView title=tab.findViewById(R.id.item_text);
        title.setTextColor(AnimatorUtil.getCurrentColor(1f-mCurrentPositionOffset,textColor,selectTextColor));
        if (mCurrentPositionOffset>0.5){
            if (isChange1){
                title.getPaint().setFakeBoldText(false);
                isChange1=false;
            }
        }else {
            if (!isChange1){
                title.getPaint().setFakeBoldText(true);
                isChange1=true;
            }

        }

        if (mTabViews.size()>mSelectIndex+1){
            View tab2=mTabViews.get(mSelectIndex+1);
            if (tab2!=null){
                TextView title2=tab2.findViewById(R.id.item_text);
                title2.setTextColor(AnimatorUtil.getCurrentColor(mCurrentPositionOffset,textColor,selectTextColor));
                if (mCurrentPositionOffset>0.5){
                    if (!isChange2){
                        title2.getPaint().setFakeBoldText(true);
                        isChange2=true;
                    }

                }else {
                    if (isChange2){
                        title2.getPaint().setFakeBoldText(false);
                        isChange2=false;
                    }

                }
            }
        }
    }

    public void setIndicatorStyle(int indicatorStyle) {
        this.indicatorStyle = indicatorStyle;
        invalidate();
    }

    public int getIndicatorStyle() {
        return indicatorStyle;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mWidth=r-l;
        mHeight=b-t;
    }
    public static class CommonTabBean{
        public String title;
        public int msgNum;

        public CommonTabBean(String t,int m){
            title=t;
            msgNum=m;
        }
    }



}
