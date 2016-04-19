package com.nextnut.xyreader_udacity;


import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.DataSetObserver;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityOptionsCompat;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

import com.nextnut.xyreader_udacity.data.ArticleLoader;

import com.nextnut.xyreader_udacity.data.UpdaterService;


import com.nextnut.xyreader_udacity.widget.CustomImageView;


/**
 * An activity representing a list of Articles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ArticleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ArticleListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        getSupportLoaderManager().initLoader(0, null, this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle("");


        View recyclerView = findViewById(R.id.article_list);
        mAdapter = new Adapter(null);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (savedInstanceState == null) {
            refresh();
        }

    }

    private void refresh() {
        startService(new Intent(this, UpdaterService.class));
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);


        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return ArticleLoader.newAllArticlesInstance(this);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapCursor(null);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Cursor mCursor;
        private DataSetObserver mDataSetObserver;
        private boolean mDataIsValid;
        private int mRowIdColumn;

        public Adapter(Cursor cursor) {
            mCursor = cursor;
        }


        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(ArticleLoader.Query._ID);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {


            if (mCursor == null) {
                Log.i("List", "cursor Nulo");
            } else{

            mCursor.moveToPosition(position);
            Log.i("List", "ID: " + Long.toString(getItemId(position)));
            holder.marticle_title.setText(mCursor.getString(ArticleLoader.Query.TITLE));
            holder.mthumbnail.setAspectRatio(mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO));
            holder.marticle_subtitle.setText(
                    DateUtils.getRelativeTimeSpanString(
                            mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString()
                            + " by "
                            + mCursor.getString(ArticleLoader.Query.AUTHOR));

            Log.i("custom", "URL:  " + mCursor.getString(ArticleLoader.Query.THUMB_URL));


            Glide.with(getBaseContext())
                    .load(mCursor.getString(ArticleLoader.Query.THUMB_URL))
                    .placeholder(R.color.photo_placeholder)
                    .listener(GlidePalette.with(mCursor.getString(ArticleLoader.Query.THUMB_URL)).intoCallBack(new BitmapPalette.CallBack() {
                        @Override
                        public void onPaletteLoaded(Palette palette) {
                            Palette.Swatch swatch = palette.getVibrantSwatch();

                            int mColorBackground;
                            int mColorTextTitle;
                            int mColorTextSubtitle;

                            mColorBackground = ContextCompat.getColor(getApplicationContext(), R.color.theme_primary);
                            mColorTextTitle = ContextCompat.getColor(getApplicationContext(), R.color.body_text_white);
                            mColorTextSubtitle = ContextCompat.getColor(getApplicationContext(), R.color.body_text_1_inverse);


                            holder.marticle_title.setBackgroundColor(mColorBackground);
                            holder.marticle_subtitle.setBackgroundColor(mColorBackground);
                            holder.marticle_title.setTextColor(mColorTextTitle);
                            holder.marticle_subtitle.setTextColor(mColorTextSubtitle);


                            if (swatch != null) {


                                mColorBackground = swatch.getRgb();
                                mColorTextTitle = swatch.getBodyTextColor();
                                mColorTextSubtitle = swatch.getTitleTextColor();

                                holder.marticle_title.setBackgroundColor(mColorBackground);
                                holder.marticle_subtitle.setBackgroundColor(mColorBackground);
                                holder.marticle_title.setTextColor(mColorTextTitle);
                                holder.marticle_subtitle.setTextColor(mColorTextSubtitle);
//                                Log.i("Glide", "Pallet actualizado " + mCursor.getString(ArticleLoader.Query.TITLE));
                            }

                        }
                    }))
                    .into(holder.mthumbnail);


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = v.getContext();
                    Intent intent = new Intent(context, ArticleDetailActivity.class);
                    intent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, getItemId(holder.getAdapterPosition()));
                    Bundle bundle = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        bundle = ActivityOptionsCompat
                                .makeSceneTransitionAnimation(ArticleListActivity.this, v.findViewById(R.id.thumbnail), v.findViewById(R.id.thumbnail).getTransitionName())
                                .toBundle();
                    }

                    context.startActivity(intent, bundle);

                }
            });

        }
        }

        @Override
        public int getItemCount() {
            int cantidad = 0;
            if (mCursor != null) {
                cantidad = mCursor.getCount();
            }
            return cantidad;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;

            public final CustomImageView mthumbnail;
            public final TextView marticle_title;
            public final TextView marticle_subtitle;
            public String fotoUrl;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                mthumbnail = (CustomImageView) view.findViewById(R.id.thumbnail);
                marticle_title = (TextView) view.findViewById(R.id.article_title);
                marticle_subtitle = (TextView) view.findViewById(R.id.article_subtitle);
            }

        }

        public Cursor swapCursor(Cursor newCursor) {
            if (newCursor == mCursor) {
                return null;
            }
            final Cursor oldCursor = mCursor;
            if (oldCursor != null && mDataSetObserver != null) {
                oldCursor.unregisterDataSetObserver(mDataSetObserver);
            }
            mCursor = newCursor;
            if (mCursor != null) {
                if (mDataSetObserver != null) {
                    mCursor.registerDataSetObserver(mDataSetObserver);
                }
                mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
                mDataIsValid = true;
                notifyDataSetChanged();
            } else {
                mRowIdColumn = -1;
                mDataIsValid = false;
                notifyDataSetChanged();
            }
            return oldCursor;
        }
    }


}
