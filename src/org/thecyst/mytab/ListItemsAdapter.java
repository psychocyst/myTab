package org.thecyst.mytab;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListItemsAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private ArrayList<ArrayList<Object>> list;
	private Context context;
	
	public ListItemsAdapter (Context context, ArrayList<ArrayList<Object>> arrayList) {
		list = arrayList;
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	public String getItemName(int position) {
		// TODO Auto-generated method stub
		return list.get(position).get(0).toString();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		Integer textColor;
		
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_holder, null);
			holder = new ViewHolder();
			holder.note = (TextView) convertView.findViewById(R.id.list_item_name);
			holder.amount = (TextView) convertView.findViewById(R.id.list_item_amount);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.note.setText(list.get(position).get(0).toString());
		holder.amount.setText(list.get(position).get(1).toString());
		
		textColor = getTextColor((Integer) list.get(position).get(1));
		
		holder.note.setTextColor(textColor);
		holder.amount.setTextColor(textColor);

		return convertView;
	}

	private Integer getTextColor(Integer integer) {
		String color;
		if(integer > 0) {
			color = context.getString(R.string.my_green);
		} else if (integer < 0) {
			color = context.getString(R.string.my_red);
		} else {
			color = context.getString(R.string.my_grey);
		}
		return Color.parseColor(color);
	}

	public void resetDataStore (ArrayList<ArrayList<Object>> dataStore) {
		list = dataStore;
	}
	
	static class ViewHolder {
		TextView note;
		TextView amount;
	}
	
}
