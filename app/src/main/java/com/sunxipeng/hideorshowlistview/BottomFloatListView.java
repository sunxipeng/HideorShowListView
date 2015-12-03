package com.sunxipeng.hideorshowlistview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;


/**
 * �ײ�View�Զ����غ���ʧlistview(����ListView���Լ̳и��࣬��CtripBottomRefreshListView���)
 *
 * @author sunxieng
 * @Date 2015-12-3 ����22:06:15
 */
public class BottomFloatListView extends ListView implements OnScrollListener {

    public View mBottomBar;
    private int mCurrentScrollState;
    private boolean bIsMoved = false;
    private boolean bIsDown = false;
    private int mDeltaY;
    private float mMotionY;
    private int oldFirstVisibleItem = 0;
    private Handler mHandler = new Handler();
    private static final String TAG = "BottomFloatListView";


    public BottomFloatListView(Context context) {
        this(context, null);
        super.setOnScrollListener(this);
    }

    public BottomFloatListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        super.setOnScrollListener(this);
    }

    public BottomFloatListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setOnScrollListener(this);
    }


    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        showBottomViewOnBottom(visibleItemCount, totalItemCount, firstVisibleItem);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        hideBottomViewOnScrollStateChanged(view, scrollState);

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        float y = ev.getY();
        float x = ev.getX();
        Log.d("FloatListView", "onTouchEvent" + "" + x + "" + y);
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                action_down(y);
                break;
            case MotionEvent.ACTION_MOVE:
                mDeltaY = (int) (y - mMotionY);
                bIsMoved = true;
                //�ƶ���ʱ��Ҫ�Ƴ�����ʾbottomView����Ϣ
                mHandler.removeCallbacks(showBottomBarRunnable);
                //����action_down�¼�����Ϊ�е�ʱ��action_down �¼�û��ִ��
                action_down(y);
                break;
            case MotionEvent.ACTION_UP:
                bIsMoved = false;
                bIsDown = false;
                if (!bIsMoved && !bIsDown) {
                    // �����Ļ��ʲôû�������2s֮��Ҫ��ʾbottomView
                    mHandler.postDelayed(showBottomBarRunnable, 2000);
                }
                if (mDeltaY < 0) { //�»�Ӱ��
                    hideBottomBar();
                } else {  //�ϻ���ʾ
                    showBottomBar();
                }

                bIsMoved = false;
                break;

        }

        return super.onTouchEvent(ev);
    }


    private void action_down(float y) {
        mMotionY = y;
        bIsDown = true;
        Log.d(TAG, "action down execed");
        mHandler.removeCallbacks(showBottomBarRunnable);
    }


    /**
     * ����������ʱ��Ҫ����bottomView
     *
     * @param view
     * @param scrollState
     */
    private void hideBottomViewOnScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;
        if (view != null) {
            if (view.getFirstVisiblePosition() == 0 && scrollState == SCROLL_STATE_IDLE) {
                hideBottomBar();
                Log.d(TAG, "hide bottom view");
            }
        }


    }

    /**
     * ��ʾ�ײ�������
     */
    public void showBottomBar() {

        if (mBottomBar != null && mBottomBar.getVisibility() == View.GONE) {
            mBottomBar.setVisibility(View.INVISIBLE);
            Animation translateAnimation = new TranslateAnimation(mBottomBar.getLeft(), mBottomBar.getLeft(), 30, 0);
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
            mBottomBar.startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mBottomBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    /**
     * ���ظ����ײ���
     */
    private void hideBottomBar() {


        if (mBottomBar != null && mBottomBar.getVisibility() == View.VISIBLE) {
            Animation translateAnimation = new TranslateAnimation(mBottomBar.getLeft(), mBottomBar.getLeft(), 0, 30);
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
            mBottomBar.startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mBottomBar.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * �������ײ�ʱֱ����ʾbottomView
     *
     * @param visibleItemCount
     * @param totalItemCount
     * @param firstVisibleItem
     */
    private void showBottomViewOnBottom(int visibleItemCount, int totalItemCount, int firstVisibleItem) {

        Log.d(TAG, "visible bottem item count:" + "firstVisibleItem:" + firstVisibleItem + "oldFirstVisibleItem:" + oldFirstVisibleItem + mBottomBar);
        if (getLastVisiblePosition() == totalItemCount - 1 && mCurrentScrollState != SCROLL_STATE_IDLE) {
            showBottomBar();
        }
    }

    private Runnable showBottomBarRunnable = new Runnable() {

        @Override
        public void run() {
            showBottomBar();
        }

    };


    /**
     * ����Ҫ������ʾ��view����
     *
     * @param bottomBar
     */
    public void setBottomBar(ViewGroup bottomBar) {
        this.mBottomBar = bottomBar;
    }

}















