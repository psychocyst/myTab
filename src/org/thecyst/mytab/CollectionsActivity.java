package org.thecyst.mytab;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionsActivity extends FragmentActivity implements OnClickListener {

	private static final String AKS = "AKS";
    private static Context context;
    
	String noteToAdd = "softcoded";
	Integer amountToAdd = 13;
	EditText editTextAmount;
	EditText editTextNote;
			
	CollectionPagerAdapter collectionPagerAdapter;
	static ViewPager viewPager;
	static HashMap<Integer, View> views;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iou_collection);
        context = this;
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
                
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        
        collectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(collectionPagerAdapter);
        viewPager.setCurrentItem(bundle.getInt("position"));

        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        editTextNote = (EditText) findViewById(R.id.editTextNote);
        View buttonAddRecord = findViewById(R.id.add_record);
        buttonAddRecord.setOnClickListener(this);
        View buttonResetInput = findViewById(R.id.reset_input);
        buttonResetInput.setOnClickListener(this);
    }
	
	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.add_record) {
			try {
				amountToAdd = Integer.parseInt(editTextAmount.getText().toString());
		        noteToAdd = editTextNote.getText().toString();
		        if(noteToAdd.length()==0)
		        	noteToAdd = "misc";
		        ObjectFragment frag = (ObjectFragment) collectionPagerAdapter.getItem(viewPager.getCurrentItem());
				frag.setTableData();
				frag.insertInto(amountToAdd, noteToAdd);
				frag.updateView();
				editTextAmount.setText(null);
				editTextNote.setText(null);
			} catch(Exception exception) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Enter a valid amount")
				       .setCancelable(false)
				       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                //do things
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
		} else if(view.getId() == R.id.reset_input) {
			editTextAmount.setText(null);
			editTextNote.setText(null);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.from(this)
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	public static class CollectionPagerAdapter extends FragmentStatePagerAdapter {
		
		private static ListItemsAdapter drawerAdapter;
    	
    	public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
            views = new HashMap<Integer, View>();
        }
    	
    	public static ListItemsAdapter getDrawerAdapter() {
			return drawerAdapter;
		}

		public static void setDrawerAdapter(ListItemsAdapter drawerAdapter) {
			CollectionPagerAdapter.drawerAdapter = drawerAdapter;
		}
		
		@Override
        public int getCount() {
            return drawerAdapter.getCount();
        }
    	
		@Override
        public String getPageTitle(int position) {
            return drawerAdapter.getItemName(position);
        }

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new ObjectFragment();
			String itemName = drawerAdapter.getItemName(i);
			Bundle args = new Bundle();
			args.putInt("position", i);
            args.putString("itemName", itemName);
            fragment.setArguments(args);
            return fragment;
		}
	}
	
	public static class ObjectFragment extends Fragment {
    	
		Ledger ledger;
        Integer amount = 0;
        String tableName = "misc";
        Integer position = 0;
                    
		ArrayList<ArrayList<Object>> recentRecords = new ArrayList<ArrayList<Object>>();
//        RecentItemsAdapterLeft adapter;
        ListItemsAdapter adapter;
        ListView listView;
        View rootView;
        
        ViewHolder holder;
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	setTableData();
        	rootView = inflater.inflate(R.layout.iou_collection_object, container, false);
        	
        	holder.record_name = (TextView) rootView.findViewById(R.id.record_name);
			holder.record_sum = (TextView) rootView.findViewById(R.id.record_sum);
			holder.recent_records = (ListView) rootView.findViewById(R.id.recent_records);
			
    		holder.record_name.setText(tableName);
    		holder.record_sum.setText(amount.toString());
    		holder.recent_records.setAdapter(adapter);
    		rootView.setTag(holder);
    		views.put(position, rootView);
        	return rootView;
        }
        
        public void setTableData() {
        	holder = new ViewHolder();
        	Bundle args = getArguments();
        	position = args.getInt("position");
        	tableName = args.getString("itemName");
        	ledger = new Ledger(context, tableName);
        	amount = ledger.getAmount();
        	recentRecords = ledger.loadLastThree();
            adapter = new ListItemsAdapter(context, recentRecords);
        }
        
        public void insertInto(Integer amount, String note) {
			ledger.addRecord(amount, note);
			reloadRecentRecords();
		}
        
        public void reloadRecentRecords() {
        	amount = ledger.getAmount();
        	recentRecords.clear();
        	recentRecords = ledger.loadLastThree();
        	adapter.resetDataStore(recentRecords);
        	adapter.notifyDataSetChanged();
        }
        
        public void updateView() {
        	rootView = (View) views.get(position);
        	holder = (ViewHolder) rootView.getTag();
        	holder.record_name.setText(tableName);
    		holder.record_sum.setText(amount.toString());
    		holder.recent_records.setAdapter(adapter);
    		rootView.requestFocus();
    	}
	}

	static class ViewHolder {
		TextView record_name;
		TextView record_sum;
		ListView recent_records;
	}

}
