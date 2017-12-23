package cn.xiwu.inputconflict.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xiwu.inputconflict.R;

/**
 * Created by xiwu on 2017/12/22.
 */

public class MyAdapter extends BaseAdapter
{

    private List<String> mDatas;
    private Context mContext;

    public MyAdapter(Context context, List<String> datas)
    {
        this.mContext = context;
        this.mDatas = datas;
    }
    public void setDataList(ArrayList<String> list)
    {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }
    public void setData(String str)
    {
        mDatas.add(str);
        notifyDataSetChanged();
    }
    @Override
    public int getCount()
    {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int i)
    {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    static class ViewHolder
    {
        TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = View.inflate(mContext, R.layout.item_chat_contact, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.send_content_tv);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(mDatas.get(position));
        return convertView;
    }
}
