package com.teamblackout.app;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.net.http.HttpResponseCache;
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
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.teamblackout.app.News.GetUpdatesList;
import com.teamblackout.app.News.News;
import com.teamblackout.app.News.NewsAdapter;
import com.teamblackout.app.Packages.ApkAdapter;
import com.teamblackout.app.Packages.App;
import com.teamblackout.app.Packages.GetAPKList;
import com.teamblackout.app.Themes.GetThemeList;
import com.teamblackout.app.Themes.ThemeAdapter;
import com.teamblackout.app.Themes.ThemeList;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        try {
            File httpCacheDir = new File(getApplicationContext().getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
                Log.i("FAILED", "HTTP response cache installation failed:" + e);
        }

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        switch (position) {
            case 0:
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ThemeFragment.newInstance(position + 1))
                    .commit();
            break;
            case 1:
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PressFragment.newInstance(position + 1))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Get a list of theme packages;
     */
    public static class ThemeFragment extends Fragment {
        private ThemeAdapter adapter;
        private ListView lv;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ThemeFragment newInstance(int sectionNumber) {
            ThemeFragment fragment = new ThemeFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ThemeFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);

            getActivity().getActionBar().setTitle("Available Theme Packages");
            getActivity().getActionBar().setIcon(R.drawable.ic_launcher);
            try {
                GetThemeList task = new GetThemeList(getActivity());

                lv = (ListView) rootView.findViewById(R.id.listView);

                adapter = new ThemeAdapter(getActivity(),
                        R.layout.list_icon_row, task.execute().get());

                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id)
                    {

                        ThemeList data = adapter.getItem(position);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, PlaceholderFragment.newInstance(data.title, data.url))
                                .addToBackStack(null)
                                .commit();
                    }});

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    /**
     * Get a list of packages, this is the "in theme view"
     */
    public static class PlaceholderFragment extends Fragment {


        private ApkAdapter adapter;
        private ListView lv;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_TITLE = "title";
        private static final String ARG_URL = "url";


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String sectionNumber, String url) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_TITLE, sectionNumber);
            args.putString(ARG_URL, url);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);

            getActivity().getActionBar().setTitle(getArguments().getString(ARG_TITLE));
            getActivity().getActionBar().setIcon(R.drawable.ic_launcher);

                try {
                    GetAPKList task = new GetAPKList(getArguments().getString(ARG_URL), getActivity());

                    lv = (ListView) rootView.findViewById(R.id.listView);

                    adapter = new ApkAdapter(getActivity(),
                            R.layout.list_icon_row, task.execute().get());

                    lv.setAdapter(adapter);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,int position, long id)
                        {

                            App data = adapter.getItem(position);

                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, APKDetails.newInstance(data.title, data.icon, data.md5, data.download, data.preview, data.description))
                                    .addToBackStack(null)
                                    .commit();
                        }});

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            return rootView;
        }
    }

    /**
     * This is where we get the package details, apk or zip (it's just not named the right thing)
     */
    public static class APKDetails extends Fragment {
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

        public static APKDetails newInstance(String title, String icon, String md5, String download, String preview, String description) {
            APKDetails fragment = new APKDetails();
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

        public APKDetails() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_details, container, false);

            //registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            title = getArguments().getString(ARG_TITLE);
            String icon = getArguments().getString(ARG_ICON);
            //String md5 = getArguments().getString(ARG_md5);
            download = getArguments().getString(ARG_download);
            // for some reason date breaks things at the moment, try later
            //String date = intent.getStringExtra("date");
            String preview = getArguments().getString(ARG_preview);
            String description = getArguments().getString(ARG_description);

            ImageView previewView = (ImageView) rootView.findViewById(R.id.previewView);

            Drawable emchart;

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d("IMAGE", "GOT ICON");
                    getActivity().getActionBar().setIcon(new BitmapDrawable(bitmap));
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

            Log.d("IMAGE", icon);
            p.with(getActivity()).load(icon).resize(250, 250).into(target);
            //p.with(getActivity()).load(preview).into(previewView);

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

                        BroadcastReceiver onComplete=new BroadcastReceiver() {
                            public void onReceive(Context ctxt, Intent intent) {

                                String[] type = splitter[3].split("\\.");
                                String path = Environment.getExternalStorageDirectory() + "/Download/" + splitter[3];
                                Log.d("path", path);

                                if (type[1].equals("apk")) {
                                    Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                                            .setDataAndType(Uri.fromFile(new File
                                                    (path)), "application/vnd.android.package-archive");
                                    startActivity(promptInstall);
                                } else if (type[1].equals("zip")) {
                                    try {
                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which){
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        //Yes button clicked
                                                        Process proc = null;
                                                        try {
                                                            proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot recovery" });
                                                            proc.waitFor();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        break;

                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        //No button clicked

                                                        break;
                                                }
                                            }
                                        };

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Do you want to reboot into recovery to install this download?").setPositiveButton("Yes", dialogClickListener)
                                                .setNegativeButton("No", dialogClickListener).show();

                                    } catch (Exception ex) {
                                    }
                                    // do things
                                } else {
                                            DisplayMetrics displayMetrics = new DisplayMetrics();
                                            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                            int height = displayMetrics.heightPixels;
                                            int width = displayMetrics.widthPixels << 1; // best wallpaper width is twice screen width

                                            // First decode with inJustDecodeBounds=true to check dimensions
                                            final BitmapFactory.Options options = new BitmapFactory.Options();
                                            options.inJustDecodeBounds = true;
                                            BitmapFactory.decodeFile(path, options);

                                            // Calculate inSampleSize
                                            options.inSampleSize = calculateInSampleSize(options, width, height);

                                            // Decode bitmap with inSampleSize set
                                            options.inJustDecodeBounds = false;
                                            Bitmap decodedSampleBitmap = BitmapFactory.decodeFile(path, options);

                                            WallpaperManager wm = WallpaperManager.getInstance(getActivity());
                                            try {
                                                wm.setBitmap(decodedSampleBitmap);
                                                Toast.makeText(getActivity(), "Background Changed", Toast.LENGTH_LONG);
                                            } catch (IOException e) {
                                            }
                                }
                            }
                        };

                        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    }
                    break;
            }
            return super.onOptionsItemSelected(item);
        }

        public static int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

    }

    /**
     * Get a list of packages, this is the "in theme view"
     */
    public static class PressFragment extends Fragment {


        private NewsAdapter adapter;
        private ListView lv;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_TITLE = "title";
        private static final String ARG_URL = "url";


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PressFragment newInstance(int sectionNumber) {
            PressFragment fragment = new PressFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_TITLE, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PressFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_news, container, false);

            getActivity().getActionBar().setTitle("Press Releases");
            getActivity().getActionBar().setIcon(R.drawable.ic_launcher);

            try {
                GetUpdatesList task = new GetUpdatesList("http://jbthemes.com/teamblackedoutapps/update_description.xml", getActivity());

                lv = (ListView) rootView.findViewById(R.id.listView);

                adapter = new NewsAdapter(getActivity(),
                        R.layout.list_header_row , task.execute().get());

                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id)
                    {

                        News data = adapter.getItem(position);

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, NewsDetails.newInstance(data.title, data.date, data.description))
                                .addToBackStack(null)
                                .commit();
                    }});

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return rootView;
        }
    }


    /**
     * This is where we get the package details, apk or zip (it's just not named the right thing)
     */
    public static class NewsDetails extends Fragment {
        String title;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_TITLE = "title";
        private static final String ARG_DATE = "date";
        private static final String ARG_description = "description";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static NewsDetails newInstance(String title, String date, String description) {
            NewsDetails fragment = new NewsDetails();
            Bundle args = new Bundle();
            args.putString(ARG_TITLE, title);
            args.putString(ARG_DATE, date);
            args.putString(ARG_description, description);
            fragment.setArguments(args);
            return fragment;
        }

        public NewsDetails() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_news_details, container, false);
            title = getArguments().getString(ARG_TITLE);
            String date = getArguments().getString(ARG_DATE);
            String description = getArguments().getString(ARG_description);
            getActivity().getActionBar().setTitle(title);



            TextView descView = (TextView) rootView.findViewById(R.id.newsView);
            descView.setText(description);

            setHasOptionsMenu(true);

            return rootView;
        }
    }
}
