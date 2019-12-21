package com.codeitsuisse.team13.expensetracker.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Chinmay on 13/09/2015.
 */
public class ExpenseListAdapter extends BaseAdapter {

    static class ComponentHolder {
        TextView category;
        TextView amount;
        ImageView isPaid;
    }

    public ExpenseListAdapter(Context context, List<ListViewData> listviewdatas, int layoutResourceId) {
        this.context = context;
        this.listviewdatas = listviewdatas;
        this.layoutResourceId = layoutResourceId;
    }

    Context context;
    private List<ListViewData> listviewdatas;
    int layoutResourceId;

    @Override
    public int getCount() {
        return listviewdatas.size();
    }

    @Override
    public Object getItem(int position) {
        return listviewdatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ComponentHolder componentHolder =null;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResourceId,parent,false);
            componentHolder= new ComponentHolder();
            componentHolder.category = (TextView) convertView.findViewById(R.id.category);

            componentHolder.amount = (TextView) convertView.findViewById(R.id.amount);

            componentHolder.isPaid = (ImageView) convertView.findViewById(R.id.circle);
            convertView.setTag(componentHolder);

        } else {
            componentHolder = (ComponentHolder)convertView.getTag();
        }

	    if(listviewdatas.get(position).getIsPaid())
		    componentHolder.isPaid.setImageResource(R.drawable.green_circle);
	    else
	        componentHolder.isPaid.setImageResource(R.drawable.red_circle);
        componentHolder.category.setText(listviewdatas.get(position).getCategory());
        componentHolder.amount.setText(listviewdatas.get(position).getAmount());

        return convertView;
    }
}

class ListViewData {
    String category;
    String amount;
    Boolean isPaid;

    public ListViewData(String category, String amount, Boolean isPaid) {
        this.category = category;
        this.amount = amount;
        this.isPaid = isPaid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }
}