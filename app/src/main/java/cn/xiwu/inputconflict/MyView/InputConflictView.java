package cn.xiwu.inputconflict.MyView;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import cn.xiwu.inputconflict.R;
import cn.xiwu.inputconflict.utils.SPUtils;

/**
 * Created by xiwu on 2017/12/21.
 */

public class InputConflictView extends LinearLayout {
    private static final String SHARE_NAME="share_name";
    private static final String COLUMN_NAME="column_name";
    private InputMethodManager imm;
    private Context mContext;
    private EditText mEditText;//输入框
    private ListView mListView;//列表
    private View mPanelView;//跟输入法切换的面板
    private ImageButton mSwitchBtn;//切换面板和键盘的按钮
    private int mEditTextId;
    private int mListViewId;
    private int mPanelViewId;
    private int mSwitchBtnId;
    private boolean mIsKeyboardActive = false; //　输入法是否激活
    private KeyboardOnGlobalChangeListener mKeyboardListener;

    public InputConflictView(Context context) {
        super(context);
        this.mContext = context;
        init(null);
    }
    
    public InputConflictView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs);
    }


    public InputConflictView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        //监听布局变化
        getViewTreeObserver().addOnGlobalLayoutListener(new KeyboardOnGlobalChangeListener());
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        TypedArray custom = getContext().obtainStyledAttributes(attrs,
                R.styleable.InputConflictView);
        mEditTextId = custom.getResourceId(
                R.styleable.InputConflictView_editText, NO_ID);
        mListViewId = custom.getResourceId(
                R.styleable.InputConflictView_listView, NO_ID);
        mPanelViewId = custom.getResourceId(R.styleable.InputConflictView_panelView,
                NO_ID);
        mSwitchBtnId = custom.getResourceId(R.styleable.InputConflictView_switchBtn,
                NO_ID);
        custom.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
        initListener();
    }


    private void initView() {
        if (mEditTextId != NO_ID) {
            mEditText = (EditText) this.findViewById(mEditTextId);
        }
        if (mListViewId != NO_ID) {
            mListView = (ListView) findViewById(mListViewId);
        }
        if (mPanelViewId != NO_ID) {
            mPanelView = findViewById(mPanelViewId);
            int keyboardHeight = SPUtils.getInt(mContext, COLUMN_NAME);
            if(keyboardHeight>0) {
                ViewGroup.LayoutParams params = mPanelView.getLayoutParams();
                    params.height = keyboardHeight;
                    mPanelView.setLayoutParams(params);
            }
        }
        if (mSwitchBtnId != NO_ID) {
            mSwitchBtn = (ImageButton) findViewById(mSwitchBtnId);
        }
    }

    private void initListener() {

        mListView.setOnTouchListener(new View.OnTouchListener()

                                     {
                                         @Override
                                         public boolean onTouch(View v, MotionEvent event) {
                                             hideKeyboard();
                                             mPanelView.setVisibility(View.GONE);
                                             mSwitchBtn.setBackgroundResource(R.drawable.icon_more);
                                             return false;
                                         }
                                     }

        );

        mSwitchBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSwitchBtn.setSelected(!mSwitchBtn.isSelected());
                        if (mIsKeyboardActive) { // 输入法打开状态下
                            if (mSwitchBtn.isSelected()) { // 打开表情
                                mSwitchBtn.setBackgroundResource(R.drawable.icon_key);
                                // 设置为不会调整大小，以便输入弹起时布局不会改变。若不设置此属性，输入法弹起时布局会闪一下
                                ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mEditText.getApplicationWindowToken(), 0);
                                mPanelView.setVisibility(View.VISIBLE);
                            } else {
                                mSwitchBtn.setBackgroundResource(R.drawable.icon_more);
                                mPanelView.setVisibility(View.GONE);
                                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mEditText.getApplicationWindowToken(), 0);
                                ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                            }
                        } else {
                            //  输入法关闭状态下
                            if (mSwitchBtn.isSelected()) {
                                mSwitchBtn.setBackgroundResource(R.drawable.icon_key);
                                // 设置为不会调整大小，以便输入弹起时布局不会改变。若不设置此属性，输入法弹起时布局会闪一下
                                ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                                mPanelView.setVisibility(View.VISIBLE);
                            } else {
                                mSwitchBtn.setBackgroundResource(R.drawable.icon_more);
                                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                mSwitchBtn.postDelayed(new Runnable() {
                                    @Override
                                    public void run() { // 输入法弹出之后，重新调整
                                        mPanelView.setVisibility(View.GONE);
                                        ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                                    }
                                }, 250); // 延迟一段时间，等待输入法完全弹出*/
                            }
                        }
                    }
                }

        );

        mEditText.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View v) {
                                             Log.v("3699dianiji", "haha307");
                                             mEditText.setCursorVisible(true);
                                             mSwitchBtn.setBackgroundResource(R.drawable.icon_more);
                                             mEditText.postDelayed(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     mSwitchBtn.setSelected(false);
                                                     mPanelView.setVisibility(View.GONE);
                                                     // 输入法弹出之后，重新调整
                                                     ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                                             | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                                                 }
                                             }, 250); // 延迟一段时间，等待输入法完全弹出
                                         }
                                     }

        );

    }


    private class KeyboardOnGlobalChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {
        int mScreenHeight = 0;
        Rect mRect = new Rect();

        private int getScreenHeight() {
            if (mScreenHeight > 0) {
                return mScreenHeight;
            }
            mScreenHeight = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getHeight();
            return mScreenHeight;
        }

        @Override
        public void onGlobalLayout() {
            // 获取当前页面窗口的显示范围
            getWindowVisibleDisplayFrame(mRect);
            int screenHeight = getScreenHeight();
            int keyboardHeight = screenHeight - mRect.bottom; // 输入法的高度
            boolean isActive = false;
            if (Math.abs(keyboardHeight) > screenHeight / 5) {
                isActive = true; // 超过屏幕五分之一则表示弹出了输入法
            }
            mIsKeyboardActive = isActive;
            onKeyboardStateChanged(mIsKeyboardActive, keyboardHeight);
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getApplicationWindowToken(), 0);
    }

    private void onKeyboardStateChanged(boolean isActive, int keyboardHeight) {
        if (isActive) {
            mSwitchBtn.setFocusable(false);
            mSwitchBtn.setFocusableInTouchMode(false);
            mEditText.requestFocus();
            if (mSwitchBtn.isSelected()) { // 表情打开状态下
                mPanelView.setVisibility(View.GONE);
                mSwitchBtn.setSelected(false);
            }
            SPUtils.saveInt(mContext,  COLUMN_NAME, keyboardHeight);
            ViewGroup.LayoutParams params = mPanelView.getLayoutParams();
            if (!(params.height == keyboardHeight)) {
                params.height = keyboardHeight;
                mPanelView.setLayoutParams(params);
            }
        } else {
            if (mSwitchBtn.isSelected()) {
                return;
            }
        }
    }
}
