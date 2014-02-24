package com.blackout.paidupdater.News;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.blackout.paidupdater.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class GetUpdatesList extends AsyncTask<ArrayList<News>, Void, ArrayList<News>> {
    String mURL = null;
    boolean misUpdate;
    static final String KEY_ITEM = "Update"; // parent node
    static final String KEY_NAME = "Title";
    static final String KEY_DATE = "Date";
    static final String KEY_DESCRIPTION = "description";
    ArrayList<News> menuItems = new ArrayList<News>();
    Context mcontext;

    public GetUpdatesList(String url, Context context, boolean isServiceCheck) {
        mURL = url;
        mcontext = context;
        misUpdate = isServiceCheck;
    }

    protected ArrayList<News> doInBackground(ArrayList<News>... passing) {
        XMLParser parser = new XMLParser(mcontext);
        String xml = parser.getXmlFromUrl(mURL); // getting XML
        Document doc = parser.getDomElement(xml); // getting DOM element
        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            News datanews = new News(
                    parser.getValue(e, KEY_NAME),
                    parser.getValue(e, KEY_DATE),
                    parser.getValue(e, KEY_DESCRIPTION)
            );
            menuItems.add(datanews);
        }

        return menuItems;
    }
}
