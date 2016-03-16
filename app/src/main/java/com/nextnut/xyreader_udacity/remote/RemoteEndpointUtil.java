package com.nextnut.xyreader_udacity.remote;

import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class RemoteEndpointUtil {
    private static final String TAG = "RemoteEndpointUtil";
    // These two need to be declared outside the try/catch
    // so that they can be closed in the finally block.

    private RemoteEndpointUtil() {
    }

    public static JSONArray fetchJsonArray() {
        String itemsJson = null;
        try {
            itemsJson = fetchPlainText(Config.BASE_URL);
        } catch (IOException e) {
            Log.e(TAG, "Error fetching items JSON", e);
            return null;
        }

        // Parse JSON
        try {
            JSONTokener tokener = new JSONTokener(itemsJson);
            Object val = tokener.nextValue();
            if (!(val instanceof JSONArray)) {
                throw new JSONException("Expected JSONArray");
            }
            return (JSONArray) val;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing items JSON", e);
        }

        return null;
    }

    static String fetchPlainText(URL url) throws IOException {
//        return new String(fetch(url), "UTF-8");
        return fetch(url);
    }

    static String fetch(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        InputStream in = null;
        Log.v(TAG, "Built URI " + url.toString());
        StringBuffer buffer = new StringBuffer();
        try {

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();

            }

            return buffer.toString();

        }


//        try {
//            OkHttpClient client = new OkHttpClient();
//            HttpURLConnection conn = client.open(url);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            in = conn.getInputStream();
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = in.read(buffer)) > 0) {
//                out.write(buffer, 0, bytesRead);
//            }
//            return out.toByteArray();
//
//        } finally {
//            if (in != null) {
//                in.close();
//            }
//        }

    }
}

