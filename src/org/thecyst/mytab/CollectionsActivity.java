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
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class CollectionsActivity extends FragmentActivity implements OnClickListener {

	private static final String AKS = "AKS";
    private static Context context;
    
	String noteToAdd = "softcoded";
	String amountToAddText = "amount";
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
        

        actionBar.setTitle(bundle.getString("type"));
        collectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager(), bundle.getString("type"));
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
			checkAmount();
		} else if(view.getId() == R.id.reset_input) {
			editTextAmount.setText(null);
			editTextNote.setText(null);
		}
	}

	private void checkAmount() {
		amountToAddText = editTextAmount.getText().toString();
		if(amountToAddText.length()==0 | amountToAddText.length()>7)
			showAlertAmount();
		else if(amountToAddText.contentEquals("-"))
			showAlertAmount();
		else {
			amountToAdd = Integer.parseInt(amountToAddText);
			checkNote();
		}
	}
	
	private void checkNote() {
		noteToAdd = editTextNote.getText().toString();
        if(noteToAdd.length()==0) {
        	noteToAdd = "misc";
        	runTransaction();
        }
        else if(noteToAdd.matches("[a-zA-Z0-9 ]*") && noteToAdd.length()<17)
        	runTransaction();
        else
        	showNoteAlertName();
	}
	
	private void runTransaction() {
		ObjectFragment frag = (ObjectFragment) collectionPagerAdapter.getItem(viewPager.getCurrentItem());
		frag.setTableData();
		frag.insertInto(amountToAdd, noteToAdd);
		frag.updateView();
		editTextAmount.setText(null);
		editTextNote.setText(null);
	}
	
	private void showAlertAmount() {
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

	private void showNoteAlertName() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setTitle("enter a valid note");
		builder.setMessage("notes may not contain special characters or be greater than 16 characters")
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	@Override
	public void onBackPressed() {
		Intent upIntent = new Intent(this, MainActivity.class);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.from(this)
                    .addNextIntent(upIntent)
                    .startActivities();
            finish();
        } else {
            NavUtils.navigateUpTo(this, upIntent);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_collections_activity, menu);
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
            case R.id.action_close:
        		AlertDialog.Builder alertDialogBuilderClose = new AlertDialog.Builder(context);
    			alertDialogBuilderClose.setTitle("Close Record?");
    			alertDialogBuilderClose.setMessage("you will be unable to make any further changes");
    			alertDialogBuilderClose.setCancelable(false)
    				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						//TODO
    						
    					}
    				})
    				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						dialog.cancel();
    					}
    				});
    			AlertDialog alertDialogClose = alertDialogBuilderClose.create();
    			alertDialogClose.show();
            	return true;
            case R.id.action_delete:
        		AlertDialog.Builder alertDialogBuilderDelete = new AlertDialog.Builder(context);
        		alertDialogBuilderDelete.setTitle("Close Record?");
        		alertDialogBuilderDelete.setMessage("you will be unable to make any further changes");
        		alertDialogBuilderDelete.setCancelable(false)
    				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						//TODO
    						
    					}
    				})
    				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						dialog.cancel();
    					}
    				});
    			AlertDialog alertDialogDelete = alertDialogBuilderDelete.create();
    			alertDialogDelete.show();
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	public static class CollectionPagerAdapter extends FragmentStatePagerAdapter {
		
		private static ListItemsAdapter drawerAdapter;
		private String tabType = "type";
    	
    	public CollectionPagerAdapter(FragmentManager fm, String type) {
            super(fm);
            views = new HashMap<Integer, View>();
            tabType = type;
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
//TODO   
			return drawerAdapter.getItemName(position).replaceFirst("[a-zA-Z0-9]*_", "");
        }

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new ObjectFragment();
			String itemName = drawerAdapter.getItemName(i);
			Bundle args = new Bundle();
			args.putInt("position", i);
            args.putString("itemName", itemName);
            args.putString("type", tabType);
            fragment.setArguments(args);
            return fragment;
		}
	}
	
	public static class ObjectFragment extends Fragment {
    	
		Ledger ledger;
        Integer amount = 0;
        String tableName = "misc";
        String tableType = "type";
        Integer position = 0;
                    
		ArrayList<ArrayList<Object>> recentRecords = new ArrayList<ArrayList<Object>>();
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
//TODO			
    		holder.record_name.setText(tableName.replaceFirst("[a-zA-Z0-9]*_", ""));
    		holder.record_sum.setText(amount.toString());
    		holder.recent_records.setAdapter(adapter);
    		
    		holder.recent_records.setOnItemLongClickListener(
    				new OnItemLongClickListener() {

    					@Override
    					public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
    							long arg3) {
    						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
    						alertDialogBuilder.setTitle("Edit Record Data");
    						alertDialogBuilder.setMessage("Record Note");
    						final EditText note = new EditText(context);
    						InputFilter[] FilterArray = new InputFilter[1];
    						FilterArray[0] = new InputFilter.LengthFilter(16);
    						note.setFilters(FilterArray);
    						note.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
    						alertDialogBuilder.setView(note);
    						alertDialogBuilder.setCancelable(false)
    							.setPositiveButton("Update",new DialogInterface.OnClickListener() {
    								public void onClick(DialogInterface dialog,int id) {
    									String noteText = note.getText().toString();
    									if(noteText.contains(" ") | noteText.length()==0 | !noteText.matches("[a-zA-Z0-9]*")) {
    										Log.i(AKS, "crap");
    									} else {
    										Log.i(AKS, "no crap "+noteText);
    									}
    								}
    							})
    							.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
    								public void onClick(DialogInterface dialog,int id) {
    									dialog.cancel();
    								}
    							});
    						AlertDialog alertDialog = alertDialogBuilder.create();
    						alertDialog.show();  
    						return false;
    					}
    					
    				});
    		rootView.setTag(holder);
    		views.put(position, rootView);
        	return rootView;
        }
        
        public void setTableData() {
        	holder = new ViewHolder();
        	Bundle args = getArguments();
        	position = args.getInt("position");
//        	tableName = args.getString("itemName").replaceFirst("[a-zA-Z0-9]*_", "");
        	tableName = args.getString("itemName");
        	tableType = args.getString("type");
        	ledger = new Ledger(context, tableName, tableType);
        	amount = ledger.getAmount();
        	recentRecords = ledger.loadLastThree();
            adapter = new ListItemsAdapter(context, recentRecords);
        }
        
        public void insertInto(Integer amount, String note) {
			ledger.addRecord(amount, note);
			Log.i(AKS, ""+amount+" "+note);
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
//TODO
        	holder.record_name.setText(tableName.replaceFirst("[a-zA-Z0-9]*_", ""));
    		holder.record_sum.setText(amount.toString());
    		holder.recent_records.setAdapter(adapter);
    		rootView.requestFocus();
    	}
        
        @Override
        public void onStop () {
        	try{
        		ledger.closeDB();
        	} catch(Exception exception) {
        		Log.i(AKS, "SQL exception");
        	}
        	super.onStop();
        }
	}

	static class ViewHolder {
		TextView record_name;
		TextView record_sum;
		ListView recent_records;
	}
	
	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}

}
