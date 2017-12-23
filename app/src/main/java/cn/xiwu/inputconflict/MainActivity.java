package cn.xiwu.inputconflict;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xiwu.inputconflict.adapter.MyAdapter;
import cn.xiwu.inputconflict.statusbar.StatusBarCompat;

public class MainActivity extends Activity
{

    private ImageButton mImageButton;
    private EditText mEditText;
    private TextView mSendTv;
    private ListView mListView;
    private MyAdapter mAdapter;
    private List<String> mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.colorPrimary),40);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initData();
    }

   private void initData()
    {
        //防止listview抢占焦点
        mListView.setFocusableInTouchMode(false);
        mListView.setFocusable(false);
        mDatas = new ArrayList<>();
        for (int i = 1; i <= 20; i++)
        {
            mDatas.add("Hello World!    "+i);
        }
        mAdapter = new MyAdapter(this,mDatas);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(mDatas.size()-1);
    }

    private void initView()
    {
        mImageButton = (ImageButton) findViewById(R.id.add_menu_btn);
        mEditText = (EditText) findViewById(R.id.chat_input_et);
        mSendTv = (TextView) findViewById(R.id.send_msg_tv);
        mListView = (ListView) findViewById(R.id.lv_chatting);
    }
    private void initListener()
    {
        mEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().length() > 0)
                {
                    mImageButton.setVisibility(View.GONE);
                    mSendTv.setVisibility(View.VISIBLE);
                }
                else
                {
                    mImageButton.setVisibility(View.VISIBLE);
                    mSendTv.setVisibility(View.GONE);
                }
            }
        });
        mSendTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final String contentStr = mEditText.getText().toString();
                if (TextUtils.isEmpty(contentStr))
                {
                    return;
                }
                listViewSeekMsg(contentStr,mListView.getCount() - 1);
                mEditText.setText("");
            }
        });
    }
    private void listViewSeekMsg(String content,int i)
    {
        mAdapter.setData(content);
        mListView.setSelection(i);
    }
}
