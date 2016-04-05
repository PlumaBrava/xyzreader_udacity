package com.nextnut.xyreader_udacity;


import android.content.Intent;
import android.database.Cursor;



import android.support.v4.content.Loader;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nextnut.xyreader_udacity.data.ArticleLoader;
import com.nextnut.xyreader_udacity.data.ItemsContract;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * An activity representing a single Article detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ArticleListActivity}.
 */
public class ArticleDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Cursor mCursor;
    private long mStartId;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int currentPosition;
    private ActionBar mactionBar;
    private ImageView imageView;
    private int position;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
//        setSupportActionBar(toolbar);

// Show the Up button in the action bar.
        mactionBar = getSupportActionBar();
        if (mactionBar != null) {
            mactionBar.setDisplayHomeAsUpEnabled(true);
            mactionBar.setDisplayShowTitleEnabled(true);
            Log.i("DetailActivity", "mactionBar not Null");

        }else {
            Log.i("DetailActivity", "mactionBar Null");

        }




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            Log.i("DetailActivity", "Intenet Long ARG: " + Long.toString(getIntent().getLongExtra(ArticleDetailFragment.ARG_ITEM_ID, 0)));
            Log.i("DetailActivity", "OnCreate SavedInstance null: ");
            if (getIntent() == null) {
                Log.i("DetailActivity", "getIntent Null");
            }
            if (getIntent().getData() == null) {
                Log.i("DetailActivity", "getIntent().getData() Null");
            }


            if (getIntent() != null && getIntent().getLongExtra(ArticleDetailFragment.ARG_ITEM_ID, 0) != 0) {


                mStartId = getIntent().getLongExtra(ArticleDetailFragment.ARG_ITEM_ID, 0);
                Log.i("DetailActivity", "OnCreate mStartId: " + mStartId);
//                    mSelectedItemId = mStartId;
            }

        }

            getSupportLoaderManager().initLoader(0, null, this);
// Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
//            mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

//            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                    Log.i("DetailActivity", "onPageScrollStateChanged" + state);
//                    mUpButton.animate()
//                            .alpha((state == ViewPager.SCROLL_STATE_IDLE) ? 1f : 0f)
//                            .setDuration(300);
                }

                @Override
                public void onPageSelected(int position) {
                    Log.i("DetailActivity", "onPageSelected" + position);
                    updateBar( position);

//
//
//                    mSelectedItemId = mCursor.getLong(ArticleLoader.Query._ID);
//                    updateUpButtonPosition();
                    super.onPageSelected(position);
                }
            });








            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putLong(ArticleDetailFragment.ARG_ITEM_ID, getIntent().getLongExtra(ArticleDetailFragment.ARG_ITEM_ID, 0));
//            ArticleDetailFragment fragment = new ArticleDetailFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.article_detail_container, fragment)
//                    .commit();

//                    }

//        String fotoUrl= getIntent().getStringExtra(ArticleDetailFragment.ARG_ITEM_FOTO);
       imageView = (ImageView) findViewById(R.id.backdrop);
//        PicassoCache.getPicassoInstance(getApplicationContext()).load(mCursor.getString(ArticleLoader.Query.THUMB_URL)).into(holder.mthumbnail);

//        Picasso.with(this).load("https://i.ytimg.com/vi/P9mR8V7Nn3U/hqdefault.jpg").into(imageView);
//        Picasso.with(this).load(fotoUrl).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ArticleListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            Log.i("DetailActivity jj", "setPrimaryItem: " + position);
            ArticleDetailFragment fragment = (ArticleDetailFragment) object;
            if (fragment != null) {
                Log.i("DetailActivity jj", "setPrimaryItem:  fragment != null" + position);

            } else {  Log.i("DetailActivity jj", "setPrimaryItem:  fragment = null" + position);}
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Log.i("DetailActivity jj", "getItem, position: "+ position);
            Log.i("DetailActivity jj", "getItem ItemID: "+ getId(position));
            return ArticleDetailFragment.newInstance( getId(position));
        }

        public long getId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(ArticleLoader.Query._ID);
        }

        @Override
        public int getCount() {
            Log.i("DetailActivity jj", "getCount: "+ ((mCursor != null) ? mCursor.getCount() : 0));
            return (mCursor != null) ? mCursor.getCount() : 0;
        }
//        @Override
//        public void finishUpdate (ViewGroup container){
//            Log.i("DetailActivity", "finishUpdate");
////            mactionBar.setTitle(getPageTitle(currentPosition));
//
//        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.i("DetailActivity ", "getPageTitle, position"+position);

//            switch (position) {
//                case 0:
//                    return "SECTION 1";
//                case 1:
//                    return "SECTION 2";
//                case 2:
//                    return "SECTION 3";
//            }
            return "SECTION:" + position;
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.i("DetailActivity", "On CreateLoader");
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor =  data;
        mSectionsPagerAdapter.notifyDataSetChanged();


        Log.i("DetailActivity", "On LoadFinished mStartId" +mStartId);
        // Select the start ID
        if (mStartId > 0) {
            Log.i("DetailActivity", "On LoadFinished mStartId mayor cero" );
            mCursor.moveToFirst();
            // TODO: optimize
            int i=0;
            while (!mCursor.isAfterLast()) {
                Log.i("DetailActivity", "On LoadFinished curor _ID:" +mCursor.getLong(ArticleLoader.Query._ID));
                if (mCursor.getLong(ArticleLoader.Query._ID) == mStartId) {
                    position = mCursor.getPosition();

                    currentPosition =mCursor.getPosition();
                    Log.i("DetailActivity", "On LoadFinished Position Selected" + position);
                    mViewPager.setCurrentItem(position, false);
                    //el id del adapter es distinto al del cursor...
                    if(position==0){updateBar(position);}
//                    mViewPager.setCurrentItem(position);

//                    mViewPager.postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            mViewPager.setCurrentItem(position);
//                        }
//                    }, 100);

                    break;
                }
                mCursor.moveToNext();
                Log.i("DetailActivity", "On LoadFinished sin econtrar el ID" + mStartId);
            }
//            mStartId = 0;
            Log.i("DetailActivity", "On LoadFinished salio del loop");
        }

    }
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        mSectionsPagerAdapter.notifyDataSetChanged();
        Log.i("DetailActivity", "On LoadReset");

    }


    public void updateBar(int position){
    if (mCursor != null) {
        Log.i("DetailActivity", "onPageSelected cursor not null " + position);
        mCursor.moveToPosition(position);
        mactionBar.setTitle(mCursor.getString(ArticleLoader.Query._ID) + mCursor.getString(ArticleLoader.Query.TITLE));

        Picasso.with(getApplicationContext())
                .load(mCursor.getString(ArticleLoader.Query.PHOTO_URL))
                .into(imageView);

        if (mactionBar != null) {

            Log.i("DetailActivity", "mactionBar not Null: "+mCursor.getString(ArticleLoader.Query._ID)+mCursor.getString(ArticleLoader.Query.TITLE));

        }else {
            Log.i("DetailActivity", "mactionBar Null"+mCursor.getString(ArticleLoader.Query._ID)+mCursor.getString(ArticleLoader.Query.TITLE));

        }



    }
    else {
        Log.i("DetailActivity", "onPageSelected cursor NULL " + position);
    }

    }


}
