package example.com.demonavdrawer;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {


    /*
    *
    * Variables associated with Navigation Drawer.
    * */


    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    FrameLayout contentFrame;
    ActionBarDrawerToggle mDrawerToggle;
    CharSequence mDrawerTitle;
    CharSequence mTitle;
    String[] mPlanetTitles;


    /*
    *
    * IMPLEMENTING TAB LAYOUT USING VIEW PAGER
    *
    * */


    /*
    *
    * FRAGMENTPAGERADAPTER -- it keeps all the fragments in memory
    * FRAGMENTSTATEPAGERADAPTER -- it will load the fragments dynamically
    *
    * We will need an adapter to load the fragments. So we will have to make the adapter first.
    *
    * */


    ViewPager mViewPager;
    TabFragmentPagerAdapter tabFragmentPagerAdapter;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = "TMDB Demo";
        mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        contentFrame = (FrameLayout) findViewById(R.id.contentFrame);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mPlanetTitles = getResources().getStringArray(R.array.nav_list_array);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // Here we are populating the listview with the contents.
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item,
                mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListner());


        // Set up the action bar.
        // enable ActionBar app icon to behave as action to toggle nav drawer


        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // This makes the TAB VISIBLE IN THE UI
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager)findViewById(R.id.pager);
        tabFragmentPagerAdapter =
                new TabFragmentPagerAdapter(getSupportFragmentManager());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.menu,
                R.string.Open,
                R.string.Close) {

            public void onDrawerClosed(View view) {
                actionBar.setTitle("DrawerClosed");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.circle_left);
                actionBar.setTitle("Drawer Opened");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mViewPager.setAdapter(tabFragmentPagerAdapter);
        for (int i=0;i< tabFragmentPagerAdapter.getCount();i++){
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(tabFragmentPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        if (savedInstanceState == null) {
            selectTab(0,0);
        }
    }

    public void selectTab(int tabPosition,int drawerPos){

        int currentTabPosition = tabPosition;

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                Log.d("SAUNAK","on Page Selected -- position is: "+ position);
                actionBar.setSelectedNavigationItem(position);
            }
        });

        mDrawerList.setItemChecked(drawerPos, true);
        setTitle(mPlanetTitles[drawerPos]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    //Without overriding the below method the drawer icon is not updating.

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void selectItem(int position) {
        // update the main content by replacing fragments

        switch (position){
            case 3:
//                Fragment profileFragment = new Fragment_Profile();
//                FragmentManager profileFragMgr = getSupportFragmentManager();
//                profileFragMgr.beginTransaction().replace(R.id.contentFrame,profileFragment).commit();

                Intent newAct = new Intent(getApplicationContext(), Activity_Profile.class);
                startActivity(newAct);
                closeDrawer(position);
                break;
            default:
                Fragment fragment = new PlanetFragment();
                Bundle args = new Bundle();
                args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentFrame, fragment).commit();
                closeDrawer(position);
                break;
        }
    }


    public void closeDrawer(int position){
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        Log.d("SAUNAK","on Tab Selected"+tab.getPosition());
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }


    class DrawerItemClickListner implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this, "Position is: "+position, Toast.LENGTH_SHORT).show();


            switch (position){
                case 3:
                    selectItem(3);
            }

            selectTab(0,position);
        }
    }


    public static class TabFragmentPagerAdapter extends FragmentPagerAdapter{


        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return new Fragment_Tab_zero();

                default:
                    Fragment tabFragment = new TabFragments();
                    Bundle args = new Bundle();
                    args.putInt(TabFragments.ARG_TABFRAGMENT_NUMBER, position);
                    tabFragment.setArguments(args);
                    return tabFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Popular";
                case 1:
                    return "New Releases";
                case 2:
                    return "Classics";
                default:
                    return "Not Defined";
            }
        }
    }


    /*
    *
    * #####################
    * #####################
    * ######################
    *
    *
    *
    * */



    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_content, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.nav_list_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(planet);
            return rootView;
        }
    }



    public static class TabFragments extends Fragment{
        public static String ARG_TABFRAGMENT_NUMBER = "tab_number";

        public TabFragments() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            View fragView = inflater.inflate(R.layout.tab_fragment_content,container,false);
            int i = getArguments().getInt(ARG_TABFRAGMENT_NUMBER);
            String tab = getResources().getStringArray(R.array.tab_fragment_list)[i];

            int imageId = getResources().getIdentifier(tab.toLowerCase(Locale.getDefault()),
                    "drawable",getActivity().getPackageName());
            ((ImageView)fragView.findViewById(R.id.tab_fragment_image)).setImageResource(imageId);
            getActivity().setTitle(tab);
            return fragView;
        }
    }

}


