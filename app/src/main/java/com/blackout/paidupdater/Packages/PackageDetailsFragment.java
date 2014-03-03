package com.blackout.paidupdater.Packages;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.blackout.paidupdater.R;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is where we get the package details, apk or zip (it's just not named the right thing)
 */
public class PackageDetailsFragment extends Fragment {
    DownloadManager downloadManager;
    Picasso p;
    String title;
    URL Download_Uri;
    String download;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_TITLE = "title";
    private static final String ARG_ICON = "icon";
    private static final String ARG_md5 = "md5";
    private static final String ARG_download = "download";
    private static final String ARG_preview = "preview";
    private static final String ARG_description = "description";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PackageDetailsFragment newInstance(String title, String icon, String md5, String download, String preview, String description) {
        PackageDetailsFragment fragment = new PackageDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_ICON, icon);
        args.putString(ARG_md5, md5);
        args.putString(ARG_download, download);
        args.putString(ARG_preview, preview);
        args.putString(ARG_description, description);

        fragment.setArguments(args);
        return fragment;
    }

    public PackageDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        //registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        title = getArguments().getString(ARG_TITLE);
        String icon = getArguments().getString(ARG_ICON);
        download = getArguments().getString(ARG_download);
        final String preview = getArguments().getString(ARG_preview);
        String description = getArguments().getString(ARG_description);

        final ImageView previewView = (ImageView) rootView.findViewById(R.id.previewView);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                getActivity().getActionBar().setIcon(new BitmapDrawable(bitmap));
                if (!preview.isEmpty()) {
                    p.with(getActivity()).load(preview).into(previewView);
                }
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
                Log.d("IMAGE", "FAILED");

            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
                Log.d("PREPARE", "PREPARELOAD");

            }
        };
        p.with(getActivity()).load(icon).resize(250, 250).into(target);

        TextView descView = (TextView) rootView.findViewById(R.id.descriptionView);
        descView.setText(description);

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getActionBar().setTitle(title);

        inflater.inflate(R.menu.download, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_download:
                try {
                    Download_Uri = new URL(download);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Download_Uri = null;
                }
                downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

                if (Download_Uri != null) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://jbthemes.com/" + Download_Uri.getPath().toString()));

                    final String[] splitter = Download_Uri.getFile().split("/");
                    downloadManager.enqueue(request
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, splitter[3])
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle(title)
 .setDescription("Downloading..")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    );
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
