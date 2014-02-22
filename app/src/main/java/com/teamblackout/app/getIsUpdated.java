package com.teamblackout.app;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.teamblackout.app.Themes.GetThemeList;
import com.teamblackout.app.Themes.ThemeList;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class getIsUpdated extends AsyncTask<Integer, Void, Integer> {
    // All static variables
    static final String BASEURL = "http://jbthemes.com/teamblackedoutapps/";
    static final String APPURL = "Updates-XML-PAID/";
    Context mcontext;


    public getIsUpdated(Context context) {
        mcontext = context;
    }

    @Override
    protected void onPreExecute() {
        //this
    }

    protected Integer doInBackground(Integer... passing) {

        GetThemeList themes = new GetThemeList(mcontext);
        ArrayList<ThemeList> item = null;
        try {
            item = themes.execute().get();
            for (int i = 0; i < item.size(); i++) {
                ThemeList row = item.get(i);
                Log.d("GET", row.url);

                sendInTheRows(row.url);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void sendInTheRows(String url) {
        String BASEURL = "http://jbthemes.com/teamblackedoutapps/Updates-XML-PAID/" + url;

        Log.d("ALL THE THINGS", BASEURL);
        String cached = new NotificationCheck(mcontext).getCached(BASEURL);
        if (cached.equals("true")) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mcontext)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!");


            // not cached
            NotificationManager mNotificationManager =
                    (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(0, mBuilder.build());
        } else {
            // cached
        }

    }

    protected void onPostExecute(Void unused) {
    }
}