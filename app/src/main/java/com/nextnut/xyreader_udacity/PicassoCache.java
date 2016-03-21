package com.nextnut.xyreader_udacity;

//import android.content.Context;
//
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.picasso.Downloader;
//import com.squareup.picasso.OkHttpDownloader;
//import com.squareup.picasso.Picasso;
//
///**
// * Created by perez.juan.jose on 16/03/2016.
// */
//public class PicassoCache {
//    /**
//     * Static Picasso Instance
//     */
//    private static Picasso picassoInstance = null;
//
//    /**
//     * PicassoCache Constructor
//     *
//     * @param context application Context
//     */
//    private PicassoCache (Context context) {
//
//        Downloader downloader   = new OkHttpDownloader(new OkHttpClient());
//
//
//        Picasso.Builder builder = new Picasso.Builder(context);
//        builder.downloader(downloader);
//
//        picassoInstance = builder.build();
//    }
//
//    /**
//     * Get Singleton Picasso Instance
//     *
//     * @param context application Context
//     * @return Picasso instance
//     */
//    public static Picasso getPicassoInstance (Context context) {
//
//        if (picassoInstance == null) {
//
//            new PicassoCache(context);
//            return picassoInstance;
//        }
//
//        return picassoInstance;
//    }
//
//
//}


import android.app.Application;

        import com.squareup.picasso.OkHttpDownloader;
        import com.squareup.picasso.Picasso;

public class PicassoCache extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(false);
        Picasso.setSingletonInstance(built);

    }
}