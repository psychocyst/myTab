package org.thecyst.mytab;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListItemsAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private ArrayList<ArrayList<Object>> list;
	
	public ListItemsAdapter (Context context, ArrayList<ArrayList<Object>> arrayList) {
		list = arrayList;
		layoutInflater = LayoutInflater.from(context);
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
				
		return convertView;
	}

	public void resetDataStore (ArrayList<ArrayList<Object>> dataStore) {
		list = dataStore;
	}
	
	static class ViewHolder {
		TextView note;
		TextView amount;
	}
	
}
