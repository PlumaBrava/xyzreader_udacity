package com.nextnut.xyreader_udacity;




import android.app.Activity;


import android.database.Cursor;

import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;


import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextnut.xyreader_udacity.data.ArticleLoader;
import com.nextnut.xyreader_udacity.dummy.DummyContent;

/**
 * A fragment representing a single Article detail screen.
 * This fragment is either contained in a {@link ArticleListActivity}
 * in two-pane mode (on tablets) or a {@link ArticleDetailActivity}
 * on handsets.
 */
public class ArticleDetailFragment  extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_FOTO = "item_foto";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    private Cursor mCursor;
    private long mItemId;
    private CardView cardView;
    private TextView actricle_detail_card_title;
    private TextView getActricle_detail_card_descrition;
    private CollapsingToolbarLayout appBarLayout;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance( long arg_item_id) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ITEM_ID,arg_item_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItemId = getArguments().getLong(ARG_ITEM_ID);
            Log.i("jj","onCreate fragment mItemId: "+Long.toString(mItemId));

//            getSupportLoaderManager().initLoader(0, null, this);
            getLoaderManager().initLoader(0, null, (ArticleDetailFragment.this));
//            Activity activity = this.getActivity();
//             appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle("Bar Title"+Long.toString(mItemId));
//            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_detail, container, false);
        Log.i("jj", "onCreateView");
        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.article_detail)).setText("Titulo");
//        }
//        ((TextView) rootView.findViewById(R.id.article_detail)).setText("Titulo");
        cardView =(CardView) rootView.findViewById(R.id.article_detail_card);
        actricle_detail_card_title =(TextView) rootView.findViewById(R.id.article_detail_card_Title);
        getActricle_detail_card_descrition =(TextView) rootView.findViewById(R.id.article_detail_card_Descrition);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("jj", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.


//        getLoaderManager().initLoader(0, null, (ArticleDetailFragment.this));
//        getLoaderManager().initLoader(0, null,this );
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("jj", "Create Loader"+mItemId);
        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);


    }







    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("jj", "On LoadFinished"+mItemId);

        mCursor= data;
        if(mCursor!=null) {

            Log.i("jj", "On LoadFinished Fragmet cursor not nul"+mItemId);

            if (!mCursor.moveToFirst()) {

                Log.i("jj", "On LoadFinished Fragmet Cursor  No tiene un elemento"+mItemId);

            } else {

                if (appBarLayout != null) {
                    appBarLayout.setTitle(mCursor.getString(ArticleLoader.Query.TITLE));
                }

                Log.i("jj", "On LoadFinished Fragmet Curosor:  " + mCursor.toString());
                Log.i("jj", "On LoadFinished Fragmet Title: " +mItemId+":"+ mCursor.getString(ArticleLoader.Query.TITLE));
                Log.i("jj", "On LoadFinished Fragmet Title: " +mItemId+":"+ mCursor.getString(ArticleLoader.Query.TITLE));
//                actricle_detail_card_title.setText(mCursor.getString(ArticleLoader.Query.TITLE));

                actricle_detail_card_title.setText(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + " by <font color='#000000'>"
                                + mCursor.getString(ArticleLoader.Query.AUTHOR)
                                + "</font>"));
                Log.i("jj", "On LoadFinished Fragmet Autor: " + mCursor.getString(ArticleLoader.Query.AUTHOR));
                getActricle_detail_card_descrition.setText(
                        mCursor.getString(ArticleLoader.Query._ID)+Html.fromHtml(mCursor.getString(ArticleLoader.Query.BODY)));

            }
        }
        else {
            getActricle_detail_card_descrition.setText("nul cursor: "+mItemId);
            Log.i("jj", "On LoadFinished Fragmet Cursor nulo"+mItemId);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i("jj", "onLoaderReset");
    }


}
