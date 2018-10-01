package com.klaudiusz_wachowiak.storingapp.util;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Component
public class ResourceUtil {

    private URL url;

    public String getContentType(String originUrlAddress) throws IOException {
        url = new URL(originUrlAddress);
        URLConnection urlConnection = url.openConnection();

        return urlConnection.getContentType();
    }

    public byte[] getContent(String originUrlAddress) throws IOException {
        url = new URL(originUrlAddress);

        return IOUtils.toByteArray(new BufferedInputStream(url.openStream()));
    }

    public boolean isConnection(String originUrlAddress) {
        try {
            url = new URL(originUrlAddress);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (httpURLConnection.getResponseCode() == 200) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }
}
