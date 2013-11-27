package org.thecyst.mytab;

import java.util.ArrayList;

import org.thecyst.mytab.CollectionsActivity.CollectionPagerAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	
	private static final String AKS = "AKS";
	private final int EXPECTED_RESULT = 1;
	Context context;
	
	Summation sumLeft;
	Summation sumRight;
	Wallet wallet;
	
	ListItemsAdapter leftDrawerAdapter;
	ListItemsAdapter rightDrawerAdapter;
	ArrayList<ArrayList<Object>> leftTabs = new ArrayList<ArrayList<Object>>();
	ArrayList<ArrayList<Object>> rightTabs = new ArrayList<ArrayList<Object>>();

//-----------------------------------------------------
	
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private boolean drawerOpenLeft;
	private boolean drawerOpenRight;
	
	private ListView tabListLeft;
	private ListView tabListRight;
	
//-----------------------------------------------------
	
	AppSectionsPagerAdapter appSectionsPagerAdapter;
	ViewPager viewPager;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ActionBar actionBar;
		context = this;
		sumLeft = new Summation(context, "iou");
		sumRight = new Summation(context, "expense");
		wallet = new Wallet(context);
		
		leftTabs.clear();
		rightTabs.clear();
		leftTabs = sumLeft.loadList();
		rightTabs= sumRight.loadList();
		
		tabListLeft = (ListView) findViewById(R.id.left_drawer);
		tabListRight = (ListView) findViewById(R.id.right_drawer);
		leftDrawerAdapter = new ListItemsAdapter(context, leftTabs);
		rightDrawerAdapter = new ListItemsAdapter(context, rightTabs);
		tabListLeft.setAdapter(leftDrawerAdapter);
		tabListRight.setAdapter(rightDrawerAdapter);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		tabListLeft.setOnItemClickListener(new DrawerItemClickListener());
		tabListRight.setOnItemClickListener(new DrawerItemClickListener());
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
        	public void onDrawerClosed(View view) {
        		actionBar.setTitle("myTab");
        		invalidateOptionsMenu();
            }
        	
        	public void onDrawerOpened(View drawerView) {
        		if(drawerView.getId() == R.id.left_drawer)
        			actionBar.setTitle("IOU Tabs");
        		else if (drawerView.getId() == R.id.right_drawer)
        			actionBar.setTitle("Expense Tabs");
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        
        if (savedInstanceState == null) {
            selectItem(0);
        }
        
        appSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(appSectionsPagerAdapter);
        
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	actionBar.setSelectedNavigationItem(position);
            }
        });
        
        for (int i = 0; i < appSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(appSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        viewPager.setCurrentItem(1);
	}
	
	public void reloadRightList() {
		rightTabs.clear();
		rightTabs= sumRight.loadList();
		rightDrawerAdapter.resetDataStore(rightTabs);
		rightDrawerAdapter.notifyDataSetChanged();
	}
	
	public void reloadLeftList() {
		leftTabs.clear();
		leftTabs= sumLeft.loadList();
		leftDrawerAdapter.resetDataStore(leftTabs);
		leftDrawerAdapter.notifyDataSetChanged();
	}
	
	@Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
	
	@Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }
	
	@Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	switch (i) {
        		case 0:
                    return new IOUFragment();
	        	case 1:
        			return new WalletFragment();
        	    case 2:
                	return new ExpenseFragment();
        	    default:
                    return new WalletFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0)
            	return "IOUs";
            else if(position == 1)
            	return "Wallet";
            else if(position == 2)
            	return "Expenses";
            else
            	return "crap!";
        }
    }
	
	public static class WalletFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.wallet_fragment, container, false);
            return rootView;
        }
    }
	
	public static class IOUFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.iou_fragment, container, false);
            return rootView;
        }
    }
	
	public static class ExpenseFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.expense_fragment, container, false);
            return rootView;
        }
    }
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
	
	private void selectItem(int position) {
		if(drawerOpenLeft) {
			tabListLeft.setItemChecked(position, true);
			drawerLayout.closeDrawer(tabListLeft);
			Intent intent = new Intent(context, CollectionsActivity.class);
			intent.putExtra("position", position);
			intent.putExtra("type", "iou");
			CollectionPagerAdapter.setDrawerAdapter(leftDrawerAdapter);
			startActivityForResult(intent, EXPECTED_RESULT);
		} else if(drawerOpenRight) {
			tabListRight.setItemChecked(position, true);
			drawerLayout.closeDrawer(tabListRight);
			Intent intent = new Intent(context, CollectionsActivity.class);
			intent.putExtra("position", position);
			intent.putExtra("type", "expense");
			CollectionPagerAdapter.setDrawerAdapter(rightDrawerAdapter);
			startActivityForResult(intent, EXPECTED_RESULT);
		}
    }
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        drawerOpenLeft = drawerLayout.isDrawerOpen(tabListLeft);
        drawerOpenRight = drawerLayout.isDrawerOpen(tabListRight);
        if(drawerOpenLeft|drawerOpenRight) {
        	getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        	menu.clear();
        } else {
        	getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }
        return super.onPrepareOptionsMenu(menu);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    
    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem){
    	if(menuItem.getItemId() == R.id.Add_Exp){
    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle("Add New Expense Tab");
			alertDialogBuilder.setMessage("Tab Name");
			final EditText name = new EditText(context);
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(16);
			name.setFilters(FilterArray);
			name.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
			alertDialogBuilder.setView(name);
			alertDialogBuilder.setCancelable(false)
				.setPositiveButton("Add",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						String tableName = name.getText().toString();
						Ledger ledger = new Ledger(context, tableName, "expense");
						ledger.closeDB();
						sumRight.addRow(tableName);
						reloadRightList();
					}
				})
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();    
    	}
    	else if (menuItem.getItemId() == R.id.Add_Tab) {
    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle("Add New IOU Tab");
			alertDialogBuilder.setMessage("Tab Name");
			final EditText name = new EditText(context);
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(16);
			name.setFilters(FilterArray);
			name.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
			alertDialogBuilder.setView(name);
			alertDialogBuilder.setCancelable(false)
				.setPositiveButton("Add",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						String tableName = name.getText().toString();
						Ledger ledger = new Ledger(context, tableName, "iou");
						ledger.closeDB();
						sumLeft.addRow(tableName);
						reloadLeftList();
					}
				})
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();  
    	}
		return true;
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == EXPECTED_RESULT) {
			reloadRightList();
			reloadLeftList();
		}
	}
    
    @Override
    public void finish() {
    	sumLeft.closeDB();
    	sumRight.closeDB();
    	wallet.closeDB();
    	super.finish();
    }

}
