package com.nextnut.xyreader_udacity;




import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;



import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.nextnut.xyreader_udacity.data.ArticleLoader;
import com.nextnut.xyreader_udacity.widget.CustomImageView;
import com.nextnut.xyreader_udacity.widget.DrawInsetsFrameLayout;
import com.nextnut.xyreader_udacity.widget.ObservableScrollView;
import com.squareup.picasso.Transformation;

/**
 * A fragment representing a single Article detail screen.
 * This fragment is either contained in a {@link ArticleListActivity}
 * in two-pane mode (on tablets) or a {@link ArticleDetailActivity}
 * on handsets.
 */
public class ArticleDetailFragment  extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = ArticleDetailFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "item_id";

    private static final String STATE_SCROLL_VIEW = "state_scroll";
    private static final float PARALLAX_FACTOR = 1.25f;

    private View mRootView;

    private CustomImageView mPhotoView;
    private View mPhotoContainerView;
    private TextView mTitleTextView;
    private TextView mBylineTextView;
    private TextView mBodyTextView;
    private View mContentContainerView;
    private View mMetaBarView;
    private DrawInsetsFrameLayout mDrawInsetsFrameLayout;
    private ObservableScrollView mScrollView;

//    private boolean mIsCard;
    private boolean mhasNotImagen=true;
    private int mStatusBarFullOpacityBottom;

    private int mColorBackground;
    private int mColorTextTitle;
    private int mColorTextSubtitle;

    private Cursor mCursor;
    private long mItemId;
    private ColorDrawable mStatusBarColorDrawable;

    private FloatingActionButton mfab;

    private int mTopInset;
    private int mScrollY;
    Palette.Swatch swatch ;



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
        setHasOptionsMenu(true);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
        }
    }


    public ArticleDetailActivity getActivityCast() {
        return (ArticleDetailActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mRootView = inflater.inflate(R.layout.article_detail, container, false);

        mDrawInsetsFrameLayout = (DrawInsetsFrameLayout)
                mRootView.findViewById(R.id.draw_insets_frame_layout);


        mScrollView = (ObservableScrollView) mRootView.findViewById(R.id.scrollview);




       mPhotoView=(CustomImageView)mRootView.findViewById(R.id.article_detail_photo);
       mPhotoContainerView=(View)mRootView.findViewById(R.id.article_detail_photo_container);
       mTitleTextView=(TextView) mRootView.findViewById(R.id.article_detail_title);


        mBylineTextView=(TextView)mRootView.findViewById(R.id.article_detail_byline);
        mBodyTextView=(TextView)mRootView.findViewById(R.id.article_detail_body);
        mContentContainerView=(View)mRootView.findViewById(R.id.article_detail_content_container);
        mMetaBarView=(View)mRootView.findViewById(R.id.article_detail_meta_bar);

        mfab= (FloatingActionButton) mRootView.findViewById(R.id.article_detail_share_fab);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("Read this Article: " + mCursor.getString(ArticleLoader.Query.TITLE))
                        .getIntent(), getString(R.string.action_share)));

            }
        });



//        mIsCard=getResources().getBoolean(R.bool.article_detail_is_card);
        mStatusBarFullOpacityBottom=(int)getResources().getDimension(R.dimen.article_detail_card_top_margin);




            mColorBackground = ContextCompat.getColor(getContext(), R.color.theme_primary);
            mColorTextTitle = ContextCompat.getColor(getContext(), R.color.body_text_white);
            mColorTextSubtitle = ContextCompat.getColor(getContext(), R.color.body_text_1_inverse);




        return mRootView;
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDrawInsetsFrameLayout.setOnInsetsCallback(new DrawInsetsFrameLayout.OnInsetsCallback() {
            @Override
            public void onInsetsChanged(Rect insets) {
                mTopInset = insets.top;
            }
        });

        mScrollView.setCallbacks(new ObservableScrollView.Callbacks() {
            @Override
            public void onScrollChanged() {
                mScrollY = mScrollView.getScrollY();
                getActivityCast().onUpButtonFloorChanged(mItemId, ArticleDetailFragment.this);
                mPhotoContainerView.setTranslationY((int) (mScrollY - mScrollY / PARALLAX_FACTOR));
                updateStatusBar();
            }
        });

//        ViewCompat.setElevation(mContentContainerView, getResources().getDimension(R.dimen.cardview_default_elevation));
        mBylineTextView.setMovementMethod(new LinkMovementMethod());

        mStatusBarColorDrawable = new ColorDrawable(0);
        updateStatusBar();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("jj", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.

        getLoaderManager().initLoader(0, null, (ArticleDetailFragment.this));

    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("jj", "Create Loader" + mItemId);
        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!isAdded()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        mCursor = cursor;
        if (mCursor != null && !mCursor.moveToFirst()) {
            Log.e(TAG, "Error reading item detail cursor");
            mCursor.close();
            mCursor = null;
        }

        bindViews();
    }


    private void bindViews() {
        if (mRootView == null) {
            return;
        }

        if (mCursor != null) {
            mRootView.setAlpha(0);
            mRootView.setVisibility(View.VISIBLE);
            mRootView.animate().alpha(1);
            mTitleTextView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
            mBylineTextView.setText(Html.fromHtml(
                    getString(R.string.article_detail_byline,
                            DateUtils.getRelativeTimeSpanString(
                                    mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                                    System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                    DateUtils.FORMAT_ABBREV_ALL).toString(),
                            mCursor.getString(ArticleLoader.Query.AUTHOR))));
            if(mBodyTextView==null)
            {Log.i("jj", "mBodyTextView" + " NullBodyTextView");}
            if(mCursor.getString(ArticleLoader.Query.BODY)!=null) {
                Log.i("jj", "mBodyTextView" + mCursor.getString(ArticleLoader.Query.BODY));
                mBodyTextView.setText(Html.fromHtml(mCursor.getString(ArticleLoader.Query.BODY)));
                mBodyTextView.setText(Html.fromHtml(mCursor.getString(ArticleLoader.Query.BODY)));
            }else {
                Log.i("jj", "mBodyTextView" + "  null");
            }

            mPhotoView.setAspectRatio(mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO));
            String photoUrl = mCursor.getString(ArticleLoader.Query.PHOTO_URL);


            Log.i("Glide", "pedido ID: "+mCursor.getString(ArticleLoader.Query.TITLE));
            Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.color.photo_placeholder)
                    .listener(GlidePalette.with(photoUrl).intoCallBack(new BitmapPalette.CallBack() {
                        @Override public void onPaletteLoaded(Palette palette) {
                            Palette.Swatch swatch = palette.getVibrantSwatch();
                            if (swatch != null) {
                                mColorBackground = swatch.getRgb();
                                mColorTextTitle = swatch.getBodyTextColor();
                                mColorTextSubtitle = swatch.getTitleTextColor();

                                mMetaBarView.setBackgroundColor(mColorBackground);
                                mTitleTextView.setTextColor(mColorTextTitle);
                                mBylineTextView.setTextColor(mColorTextSubtitle);
                                Log.i("Glide", "Pallet actualizado "+mCursor.getString(ArticleLoader.Query.TITLE));
                            }
                            else{Log.i("Glide", "Pallet Null "+mCursor.getString(ArticleLoader.Query.TITLE));}
                            updateStatusBar();
                        }
                    }))
                    .into(mPhotoView);
        } else {
            mRootView.setVisibility(View.GONE);
            mTitleTextView.setText("N/A");
            mBylineTextView.setText("N/A");
            mBodyTextView.setText("N/A");
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i("jj", "onLoaderReset");
    }

    public int getUpButtonFloor() {
        if (mPhotoContainerView == null || mPhotoView.getHeight() == 0) {
            return Integer.MAX_VALUE;
        }

        // account for parallax
//        return mIsCard
//                ? (int) mPhotoContainerView.getTranslationY() + mPhotoView.getHeight() - mScrollY
//                : mPhotoView.getHeight() - mScrollY;

        return mPhotoView.getHeight() - mScrollY;

    }

    private void updateStatusBar() {
        int color = 0;
        if (mPhotoView != null && mTopInset != 0 && mScrollY > 0) {
            float f = progress(mScrollY,
                    mStatusBarFullOpacityBottom - mTopInset * 3,
                    mStatusBarFullOpacityBottom - mTopInset);
            color = Color.argb((int) (255 * f),
                    (int) (Color.red(mColorBackground) * 0.9),
                    (int) (Color.green(mColorBackground) * 0.9),
                    (int) (Color.blue(mColorBackground) * 0.9));
        }
        mStatusBarColorDrawable.setColor(color);
        mDrawInsetsFrameLayout.setInsetBackground(mStatusBarColorDrawable);
    }

    static float progress(float v, float min, float max) {
        return constrain((v - min) / (max - min), 0, 1);
    }

    static float constrain(float val, float min, float max) {
        if (val < min) {
            return min;
        } else if (val > max) {
            return max;
        } else {
            return val;
        }
    }


}
