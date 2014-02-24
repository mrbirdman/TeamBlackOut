package com.blackout.paidupdater;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import hirondelle.date4j.DateTime;

public class XMLParser {
    OkHttpClient client = new OkHttpClient();
    Context mcontext;
    HttpResponseCache cache;

    public XMLParser(Context context) {
        mcontext = context;
    }

    public String getXmlFromUrl(String url) {
        try
        {
            URL realUrl = new URL(url);

            String result = get(realUrl);

            return result;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    public byte[] readFully(InputStream in) throws IOException {
       ByteArrayOutputStream out = new ByteArrayOutputStream();
       byte[] buffer = new byte[1024];
       for (int count; (count = in.read(buffer)) != -1; ) {
           out.write(buffer, 0, count);
       }
       return out.toByteArray();
    }

    public String get(URL realUrl) throws IOException {

        HttpURLConnection connection = client.open(realUrl);

        InputStream in = null;
        try {
            in = connection.getInputStream();
            byte[] response = readFully(in);
            return new String(response, "UTF-8");
        } finally {
            if (in != null) in.close();
        }
    }

    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);
        } catch (ParserConfigurationException e) {
            return null;
        } catch (SAXException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return doc;
    }

    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }
}