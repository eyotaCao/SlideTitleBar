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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

public class MySlideTab extends HorizontalScrollView {
    private int mWidth,mHeight,mIndicatorMarginLeft,mIndicatorMarginTop;
    private int mTabCount=0;
    private int mTabWidth=0;
    private int mSelectIndex=0;
    private int textColor,selectTextColor,textSize;

    private int selectDColor,unSelectDColor,selectDBg,unSelectDBg;

    private boolean isShowViewpagerSlide=false;

    //指示器
    private int indicatorStyle;   //1:默认 2：下方显示
    private int indicatorHeight,indicatorWidth;
    private int inRound;
    private int inX,inY;
    private Paint indicatorPaint;
    private RectF rectFIndicator;

    private float mCurrentPositionOffset;
    private int mLastScrollX;


    private MyTabAdapter mAdapter;
    private ViewPager mViewPager;


    private Context mContext;
    private Resources resources;
    private final List<View> mTabViews=new ArrayList<>();
    private int mTabView= R.layout.item_default_tab;
    private LinearLayout mBaseContainer;
    private RelativeLayout mBaseParent;
    private OnselectListener onselectListener;

    private boolean isChangeMsg=false;  //是否显示消息点

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
        resources=context.getResources();
        indicatorStyle=1;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MySlideTab);
        textColor = typedArray.getColor(R.styleable.MySlideTab_myslideTab_textColor, resources.getColor(R.color.color95A3C1));
        selectTextColor = typedArray.getColor(R.styleable.MySlideTab_myslideTab_selectTextColor, resources.getColor(R.color.colorFFFFFF));
        indicatorStyle=typedArray.getInteger(R.styleable.MySlideTab_myslideTab_indicator_style,1);

        if (indicatorStyle==1){
            indicatorHeight=typedArray.getDimensionPixelSize(R.styleable.MySlideTab_myslideTab_indicator_height, PxTool.dpToPx(context,4));
        }else {
            indicatorHeight=PxTool.dpToPx(context,4);

        }
        mIndicatorMarginLeft=typedArray.getDimensionPixelSize(R.styleable.MySlideTab_myslideTab_indicator_margin, PxTool.dpToPx(context,4));
        mIndicatorMarginTop=typedArray.getDimensionPixelSize(R.styleable.MySlideTab_myslideTab_indicator_margin, PxTool.dpToPx(context,4));
        selectDColor=resources.getColor(R.color.color2866FE);
        unSelectDColor=resources.getColor(R.color.colorFFFFFF);
        selectDBg=resources.getColor(R.color.colorFFFFFF);
        unSelectDBg=resources.getColor(R.color.color95A3C1);
        mBaseParent=new RelativeLayout(mContext);
        mBaseContainer=new LinearLayout(mContext);
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mBaseContainer.setLayoutParams(rlp);
        initIndicator();
     //   setOverScrollMode(OVER_SCROLL_NEVER);
        setHorizontalScrollBarEnabled(false);
        addView(mBaseParent);
        mBaseParent.addView(mBaseContainer);

    }

    private void initIndicator(){
        indicatorPaint=new Paint();
        indicatorPaint.setColor(mContext.getResources().getColor(R.color.color2866FE));
        inRound=PxTool.dpToPx(getContext(),3);

        rectFIndicator=new RectF();

    }

    private void setIndicatorWidth(int width){
        indicatorWidth=width-mIndicatorMarginLeft*2;
        indicatorHeight=mHeight-mIndicatorMarginTop*2;
        invalidate();
    }
    private void setIndicatorPosition(int position){
        inX=position*mTabWidth+mIndicatorMarginLeft;
        invalidate();
    }

    public void setTabTitles(int size){  //初始化视图
        if (mTabViews.size()>0){
            mTabViews.clear();
        }
        mTabCount=size;


    }

    private void refreshData(int size){
        try {
            for (int i=0;i<size;i++){
                if (mAdapter!=null){
                    mAdapter.convert(mAdapter.getDatas().get(i),mTabViews.get(i));
                }
            }
        }catch (Exception ignored){}

    }

    public void setClickAnimate(boolean isShow){
        isShowViewpagerSlide=isShow;
    }
    public void setShow(boolean isShow){  //显示消息点
        isChangeMsg=isShow;
    }


    public int getCurrentTab(){
        return mSelectIndex;
    }
    public void setCurrentTab(int index){
        mSelectIndex=index;
        if (mViewPager!=null){
            select(mSelectIndex,true,true);
        }
    }

    private boolean isFirst=true;
    @Override
    protected void onDraw(Canvas canvas) {

        if (isFirst){
            isFirst=false;
            if (mTabWidth==0){
                mTabWidth= (int)((float)mWidth/(float) mTabCount)-1;
            }
          //  ToastUtils.show("tabw"+mWidth);
            setIndicatorWidth(mTabWidth);
            for (int i=0;i<mTabCount;i++){
                View tab = LayoutInflater.from(mContext).inflate(mTabView, null);
                mTabViews.add(tab);
                LinearLayout tabLl=tab.findViewById(R.id.item_ll);

                if (mAdapter!=null){
                    mAdapter.convert(mAdapter.getDatas().get(i),tab);
                }
                LinearLayout.LayoutParams llp=new LinearLayoutCompat.LayoutParams(mTabWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                llp.gravity= Gravity.CENTER;
                tabLl.setLayoutParams(llp);

                int i2=i;
                tab.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mAdapter!=null){mAdapter.onSelect(i2);}
                        if (mViewPager!=null){
                            if (isShowViewpagerSlide){
                                mViewPager.setCurrentItem(i2);
                            }else {
                                select(i2,true,true);
                            }

                        }else {
                            select(i2,true,true);
                        }
                    }
                });

                mBaseContainer.addView(tab);
            }
            select(mSelectIndex,true,true);
            setIndicatorPosition(mSelectIndex);
        }
        System.out.println("round"+inRound);
        //画指示器
        rectFIndicator.left=inX;
        rectFIndicator.top=mIndicatorMarginTop;
        rectFIndicator.right=inX+indicatorWidth;
        rectFIndicator.bottom=indicatorHeight+mIndicatorMarginTop;
        canvas.drawRoundRect(rectFIndicator, inRound,inRound,indicatorPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }



    public void setTabWidth(int width) {
        this.mTabWidth = width;
        setIndicatorWidth(mTabWidth);
        if (mTabViews.size()>0){
            for (View view:mTabViews){
                LinearLayout.LayoutParams llp=(LinearLayout.LayoutParams) view.getLayoutParams();
                llp.width=mTabWidth;
                view.setLayoutParams(llp);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mWidth=r-l;
        mHeight=b-t;
    }

    public void setIndicatorMargin(int l,int t,int r,int b){
        mIndicatorMarginLeft=l;
        mIndicatorMarginTop=t;
        invalidate();
    }

 //   public void setC



    public void setTabAdapter(MyTabAdapter adapter){
        mAdapter=adapter;
        mAdapter.setListLister(new MyTabAdapter.SetListLister() {
            @Override
            public void onSet(int s,ViewPager viewPager) {
                mTabView=mAdapter.getLayout();
                setViewPager(viewPager);
                setTabTitles(s);
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
                inX=position*mTabWidth+mIndicatorMarginLeft+(int)(mTabWidth*positionOffset);
                invalidate();
                post(()->{
                    mCurrentPositionOffset=positionOffset;
                    mSelectIndex=position;
                    selectColor2();
                    scrollToCurrentTab();
                });


            }

            @Override
            public void onPageSelected(int position) {
                if (onselectListener!=null){
                    onselectListener.onSelect(position);
                }
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
        /**当前Tab的left+当前Tab的Width乘以positionOffset*/
        int newScrollX = mBaseContainer.getChildAt(mSelectIndex).getLeft() + offset;

        if (mSelectIndex > 0 || offset > 0) {
            /**HorizontalScrollView移动到当前tab,并居中*/
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            newScrollX += ((mTabWidth) / 2);
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            /** scrollTo（int x,int y）:x,y代表的不是坐标点,而是偏移量
             *  x:表示离起始位置的x水平方向的偏移量
             *  y:表示离起始位置的y垂直方向的偏移量
             */
            smoothScrollTo(newScrollX, 0);
        }
    }

    private void select(int index,boolean setColor,boolean moveViewpager){
        mSelectIndex=index;
        for (int i=0;i<mTabViews.size();i++){
            if (index==i){
                View tab=mTabViews.get(i);
                TextView title=tab.findViewById(R.id.item_text);
                if (setColor&&isChangeMsg){
                    DragBadgeView title_msg=tab.findViewById(R.id.item_text_msg_num);
                    title_msg.setTextColor(selectDColor);
                    title_msg.setBgColor(selectDBg);
                }

                title.getPaint().setFakeBoldText(true);
                if (setColor){
                    title.setTextColor(selectTextColor);
                }
            }else {
                View tab=mTabViews.get(i);
                TextView title=tab.findViewById(R.id.item_text);
                title.getPaint().setFakeBoldText(false);
                if (setColor&&isChangeMsg){
                    DragBadgeView title_msg=tab.findViewById(R.id.item_text_msg_num);
                    title_msg.setTextColor(unSelectDColor);
                    title_msg.setBgColor(unSelectDBg);
                }
                if (setColor){
                    title.setTextColor(textColor);
                }
            }
        }

        if (mViewPager!=null&&moveViewpager){
            mViewPager.setCurrentItem(index,false);
        }
        if (onselectListener!=null){
            onselectListener.onSelect(index);
        }

    }

    private boolean isChange1=true,isChange2=false;
    private void selectColor2(){  //过渡颜色
        if (mTabViews.size()==0){
            return;
        }
        View tab=mTabViews.get(mSelectIndex);
        TextView title=tab.findViewById(R.id.item_text);
        DragBadgeView title_msg=tab.findViewById(R.id.item_text_msg_num);
        if (isChangeMsg){
            title_msg.setTextColor(AnimatorUtil.getCurrentColor(1-mCurrentPositionOffset,unSelectDColor,selectDColor));
            title_msg.setBgColor(AnimatorUtil.getCurrentColor(1-mCurrentPositionOffset,unSelectDBg,selectDBg));
        }
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
                DragBadgeView title_msg2=tab2.findViewById(R.id.item_text_msg_num);
                title2.setTextColor(AnimatorUtil.getCurrentColor(mCurrentPositionOffset,textColor,selectTextColor));
                if (isChangeMsg){
                    title_msg2.setTextColor(AnimatorUtil.getCurrentColor(mCurrentPositionOffset,unSelectDColor,selectDColor));
                    title_msg2.setBgColor(AnimatorUtil.getCurrentColor(mCurrentPositionOffset,unSelectDBg,selectDBg));
                }
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


    public static class CommonTabBean{
        public String title;
        public int msgNum;

        public CommonTabBean(String t,int m){
            title=t;
            msgNum=m;
        }
    }
    /*private static class MyHandler extends Handler {
        private WeakReference<MySlideTab> weakReference;
        public MyHandler(MySlideTab fragment) {
            weakReference = new WeakReference<>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }*/

    public void setOnselectListener(OnselectListener onselectListener){
        this.onselectListener=onselectListener;
    }
    public interface OnselectListener{
        void onSelect(int index);
    }



}
